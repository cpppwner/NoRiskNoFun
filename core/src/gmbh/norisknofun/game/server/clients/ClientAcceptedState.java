package gmbh.norisknofun.game.server.clients;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;

/**
 * State of a connected client when handshake was successfully performed.
 */
final class ClientAcceptedState implements ClientState {

    private final Client context;

    ClientAcceptedState(Client context) {

        this.context = context;
    }

    @Override
    public void enter() {

        context.subscribeToMessageBus();
    }

    @Override
    public void exit() {

        context.unsubscribeFromMessageBus();
    }

    @Override
    public void handleOutboundMessage(Message message) {

        try {
            byte[] data = new MessageSerializer(message).serialize();
            context.getSession().write(data);
        } catch (IOException | ProtocolException e) {
            Gdx.app.error(getClass().getSimpleName(), "Failed to serialize message \"" + message.getType().getSimpleName() + "\"");
            terminateClient();
        }
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

        context.getSession().terminate();
        context.setState(new ClientClosedState(context));
    }

    @Override
    public void sessionClosed() {

        context.setState(new ClientClosedState(context));
    }
}
