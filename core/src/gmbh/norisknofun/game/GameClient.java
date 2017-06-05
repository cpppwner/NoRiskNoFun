package gmbh.norisknofun.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import gmbh.norisknofun.game.client.Client;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.client.ClientContext;
import gmbh.norisknofun.network.Session;
import gmbh.norisknofun.network.SessionEventHandler;

/**
 * Game client handling incoming messages and processing game states.
 *
 * <p>
 *     Note: This class implements the {@link gmbh.norisknofun.network.SessionEventHandler} interface
 *     meaning all network related events are forwarded to this class and those methods run in the
 *     context of the {@link gmbh.norisknofun.network.NetworkClient} thread.
 *     Message deserialization and such are also handled in the context of the network client thread
 *     since in contrary to the game server, highly concurrent behaviour is not expected.
 * </p>
 *
 * <p>
 *     The rendering thread is only allowed to call the {@link GameClient#processPendingMessages()}
 *     method, This method processes all incoming messages and advances the client state accordingly.
 *     The {@link GameClient} class provides mechanisms to guarantee safety between incoming messages
 *     and processing them.
 * </p>
 */
public class GameClient implements SessionEventHandler {

    /**
     * Queue storing all inbound messages.
     *
     * <p>
     *     Note this must be thread safe, since it's accessed by network client thread
     *     (via the {@link Client}) and the rendering thread via {@link GameClient#processPendingMessages()}.
     * </p>
     */
    private ConcurrentLinkedQueue<Message> inboundMessageQueue;
    private final Client client;
    private final ClientContext clientContext;

    public GameClient(GameData data) {

        inboundMessageQueue = new ConcurrentLinkedQueue<>();
        client = new Client(inboundMessageQueue);
        clientContext = new ClientContext(client, data);

    }

    /**
     * Process all pending messages received via network.
     *
     * <p>
     *     The rendering thread is only allowed to call this method.
     * </p>
     */
    public void processPendingMessages() {

        for (Message message: inboundMessageQueue) {
            clientContext.delegateMessage(message);
        }
    }


    @Override
    public void newSession(Session session) {

        // called by NetworkClient
        client.newSession(session);
    }

    @Override
    public void sessionClosed(Session session) {

        // called by NetworkClient
        client.sessionClosed(session);
    }

    @Override
    public void sessionDataReceived(Session session) {

        // called by NetworkClient
        client.sessionDataReceived(session);
    }

    @Override
    public void sessionDataWritten(Session session) {

        // called by NetworkClient
        client.sessionDataWritten(session);
    }
}
