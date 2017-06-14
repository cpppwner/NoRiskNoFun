package gmbh.norisknofun.game.client;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.gamemessages.client.ClientConnectionRefused;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.messages.handshake.Handshake;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeAccepted;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeConstants;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeRejected;
import gmbh.norisknofun.network.Session;

/**
 * State for the client when the client needs to perform a handshake with the server.
 */
class ClientHandshakeState implements ClientState {

    /**
     * The client - or StateContext in terms of state pattern
     */
    private final Client client;

    /**
     * Constructor taking the "state context"
     */
    ClientHandshakeState(Client client) {
        this.client = client;
    }

    @Override
    public void enter() {

        sendHandshake();
    }

    /**
     * Send handshake request to the server
     */
    private void sendHandshake() {

        // build up handshake message
        Handshake handshake = new Handshake(HandshakeConstants.HANDSHAKE_MAGIC, HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION);
        try {
            byte[] data = new MessageSerializer(handshake).serialize();
            client.getSession().write(data);
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Failed to send handshake", e);
            terminateClient("Failed to send handshake: " + e.getMessage());
        }
    }

    @Override
    public void exit() {

        // intentionally left empty, since there is nothing to do here
    }

    @Override
    public void handleOutboundMessage(Message message) {

        throw new IllegalStateException("no outbound message excepted here");
    }

    @Override
    public void handleNewSession(Session newSession) {

        throw new IllegalStateException("not expecting new session during handshake");
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        client.setState(new ClientDisconnectedState(client));
    }

    @Override
    public void handleDataReceived() {

        MessageDeserializer deserializer = new MessageDeserializer(client.getMessageBuffer());
        if (deserializer.hasMessageToDeserialize()) {
            deserializeAndHandleMessage(deserializer);
        }
    }

    private void deserializeAndHandleMessage(MessageDeserializer deserializer) {

        Message message = null;
        try {
            message = deserializer.deserialize();
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Deserialize message failed", e);
            terminateClient("Deserialize message failed: " + e.getMessage());
        }

        if (message != null) {
            handleMessage(message);
        }
    }

    private void handleMessage(Message message) {

        if (message instanceof HandshakeRejected) {
            // server rejected the handshake request
            client.getSession().close();
            client.distributeInboundMessage(
                    new ClientConnectionRefused("Handshake rejected by server. Server protocol: "
                            + ((HandshakeRejected)message).getServerProtocolVersion()));
            client.setState(new ClientDisconnectedState(client));

        } else if (message instanceof HandshakeAccepted) {

            client.distributeInboundMessage(new ClientConnected());
            client.setState(new ClientConnectedState(client));

        } else {

            // huh - the server talks bogus - get us out of this server
            client.getSession().close();
            client.distributeInboundMessage(
                    new ClientConnectionRefused("Handshake sent wrong message: " + message.getType().getName()));
            client.setState(new ClientDisconnectedState(client));
        }
    }

    private void terminateClient(String reason) {

        client.getSession().terminate();
        client.distributeInboundMessage(new ClientConnectionRefused(reason));
        client.setState(new ClientDisconnectedState(client));
    }
}
