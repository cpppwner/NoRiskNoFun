package gmbh.norisknofun.game.server.clients;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.gamemessages.client.ClientDisconnected;
import gmbh.norisknofun.game.gamemessages.client.DisconnectClient;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;

/**
 * State of a connected client when handshake was successfully performed.
 */
final class ClientAcceptedState extends ClientStateBase {

    private final Client context;

    ClientAcceptedState(Client context) {

        this.context = context;
    }

    @Override
    public void enter() {

        context.subscribeToMessageBus();
        context.distributeInboundMessage(new ClientConnected());
    }

    @Override
    public void exit() {

        context.distributeInboundMessage(new ClientDisconnected());
        context.unsubscribeFromMessageBus();
    }

    @Override
    public void handleOutboundMessage(Message message) {

        if (message instanceof DisconnectClient) {
            handleDisconnect((DisconnectClient)message);
            return;
        }

        try {
            byte[] data = new MessageSerializer(message).serialize();
            context.getSession().write(data);
        } catch (IOException | ProtocolException e) {
            Gdx.app.error(getClass().getSimpleName(), "Failed to serialize message \"" + message.getType().getSimpleName() + "\"", e);
            terminateClient();
        }
    }

    private void handleDisconnect(DisconnectClient message) {

        closeClient(message.isTerminateClient());
    }

    @Override
    public void processDataReceived() {

        MessageDeserializer deserializer = new MessageDeserializer(context.getMessageBuffer());
        if (deserializer.hasMessageToDeserialize()) {
            deserializeAndHandleMessage(deserializer);
        }
    }

    private void deserializeAndHandleMessage(MessageDeserializer deserializer) {

        Message message;
        try {
            message = deserializer.deserialize();
        } catch (ProtocolException | IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Deserialize message failed", e);
            terminateClient();
            return;
        }

        context.distributeInboundMessage(message);
    }

    private void terminateClient() {
        closeClient(true);
    }

    private void closeClient(boolean terminate) {

        if (terminate) {
            context.getSession().terminate();
        } else {
            context.getSession().close();
        }
        context.setState(new ClientClosedState(context));
    }

    @Override
    public void sessionClosed() {

        context.setState(new ClientClosedState(context));
    }
}
