package gmbh.norisknofun.game.server.clients;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.messages.handshake.Handshake;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeAccepted;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeConstants;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeRejected;

/**
 * Initial state for a freshly connected client.
 */
final class ClientConnectedState extends ClientStateBase {

    /**
     * Context in terms of state pattern.
     */
    private final Client context;

    /**
     * Constructor taking the context
     * @param context Context of this state.
     */
    ClientConnectedState(Client context) {

        this.context = context;
    }

    @Override
    public void handleOutboundMessage(Message message) {

        throw new IllegalStateException("no Messages can be handled in this state");
    }

    @Override
    public void processDataReceived() {

        MessageDeserializer deserializer = new MessageDeserializer(context.getMessageBuffer());
        if (deserializer.hasMessageToDeserialize()) {
            deserializeAndHandleMessage(deserializer);
        }
    }

    @Override
    public void sessionClosed() {

        context.setState(new ClientClosedState(context));
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

        handleInboundMessage(message);
    }

    private void terminateClient() {

        context.getSession().terminate();
        context.setState(new ClientClosedState(context));
    }

    private void handleInboundMessage(Message message) {

        if (!(message instanceof Handshake) || context.getMessageBuffer().length() != 0) {
            // so either the client did not send a handshake message
            // or some more data was sent in addition
            // this violates the protocol
            terminateClient();
            return;
        }

        try {
            handleHandShakeMessage((Handshake)message);
        } catch (IOException | ProtocolException e) {
            Gdx.app.error(getClass().getSimpleName(), "Failed to handle handshake message", e);
            terminateClient();
        }
    }

    private void handleHandShakeMessage(Handshake handshake) throws IOException, ProtocolException {

        if (!handshake.getProtocolMagic().equals(HandshakeConstants.HANDSHAKE_MAGIC)) {
            // client sends bogus magic string
            terminateClient();
        } else if (!handshake.getProtocolVersion().equals(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION)) {
            rejectClient();
        } else {
            acceptClient();
        }
    }

    private void rejectClient() throws IOException, ProtocolException {

        byte[] data = new MessageSerializer(new HandshakeRejected(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION)).serialize();
        context.getSession().write(data);
        context.getSession().close();
        context.setState(new ClientClosedState(context)); // closed event received later, but set state immediately
    }

    private void acceptClient() throws IOException, ProtocolException {

        byte[] data = new MessageSerializer(new HandshakeAccepted(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION)).serialize();
        context.getSession().write(data);
        context.setState(new ClientAcceptedState(context));
    }
}
