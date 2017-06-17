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
class ClientHandshakeState extends ClientStateBase {


    /**
     * Constructor taking the "state context"
     */
    ClientHandshakeState(Client client) {
        super(client);
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
            write(data);
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Failed to send handshake", e);
            terminateClient("Failed to send handshake: " + e.getMessage());
        }
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        distributeInboundMessage(new ClientConnectionRefused("connection closed by server"));
        setNextState(new ClientDisconnectedState(getClient()));
    }

    @Override
    public void handleDataReceived() {

        MessageDeserializer deserializer = new MessageDeserializer(getClient().getMessageBuffer());
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
            closeClient("Handshake rejected by server. Server protocol: "
                    + ((HandshakeRejected)message).getServerProtocolVersion());
        } else if (message instanceof HandshakeAccepted) {

            distributeInboundMessage(new ClientConnected());
            setNextState(new ClientConnectedState(getClient()));

        } else {

            // huh - the server talks bogus - get us out of this server
            closeClient("Handshake sent wrong message: " + message.getType().getName());
        }
    }

    private void terminateClient(String reason) {

        distributeInboundMessage(new ClientConnectionRefused(reason));
        terminateSession();
        setNextState(new ClientDisconnectedState(getClient()));
    }

    private void closeClient(String reason) {

        distributeInboundMessage(new ClientConnectionRefused(reason));
        closeSession();
        setNextState(new ClientDisconnectedState(getClient()));
    }


}
