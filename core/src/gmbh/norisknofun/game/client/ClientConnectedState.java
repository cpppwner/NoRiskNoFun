package gmbh.norisknofun.game.client;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.network.Session;

/**
 * This is the state in control when client is connected.
 */
class ClientConnectedState implements ClientState {

    private final Client client;

    ClientConnectedState(Client client) {

        this.client = client;
    }

    @Override
    public void enter() {

        // nothing to do here
    }

    @Override
    public void exit() {

        // nothing to do here
    }

    @Override
    public void handleOutboundMessage(Message message) {

        try {
            byte[] data = new MessageSerializer(message).serialize();
            client.getSesssion().write(data);
        } catch (IOException | ProtocolException e) {
            Gdx.app.error(getClass().getSimpleName(), "Handling outbound message failed", e);
            client.getSesssion().terminate(); // terminate the session
        }
    }

    @Override
    public void handleNewSession(Session newSession) {

        throw new IllegalStateException("new session event not expected");
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        // make state transition to closed state
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

        try {
            Message message = deserializer.deserialize();
            client.distributeInboundMessage(message);
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Deserialize message failed", e);
            client.getSesssion().terminate();
        }
    }
}
