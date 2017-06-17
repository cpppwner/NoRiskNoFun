package gmbh.norisknofun.game.client;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.gamemessages.client.DisconnectClient;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.network.Session;

/**
 * This is the state in control when client is connected.
 */
class ClientConnectedState extends ClientStateBase {

    ClientConnectedState(Client client) {

        super(client);
    }

    @Override
    public void handleOutboundMessage(Message message) {

        if (message instanceof DisconnectClient) {
            handleDisconnect(((DisconnectClient)message).isTerminateClient());
            return;
        }

        try {
            byte[] data = new MessageSerializer(message).serialize();
            write(data);
        } catch (IOException | ProtocolException e) {
            Gdx.app.error(getClass().getSimpleName(), "Handling outbound message failed", e);
            handleDisconnect(true);
        }
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        // make state transition to closed state
        setNextState(new ClientDisconnectedState(getClient()));
    }

    @Override
    public void handleDataReceived() {

        System.out.println("ClientConnectedState: Received Data");
        MessageDeserializer deserializer = new MessageDeserializer(getClient().getMessageBuffer());
        while (deserializer.hasMessageToDeserialize()) {
            deserializeAndHandleMessage(deserializer);
        }
    }

    private void deserializeAndHandleMessage(MessageDeserializer deserializer) {

        try {
            Message message = deserializer.deserialize();
            System.out.println("Deserialized: " + message.getClass().getName());
            distributeInboundMessage(message);
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Deserialize message failed", e);
            handleDisconnect(true);
        }
    }

    private void handleDisconnect(boolean terminateClient) {

        if (terminateClient) {
            terminateSession();
        } else {
            closeSession();
        }

        setNextState(new ClientDisconnectedState(getClient()));
    }
}
