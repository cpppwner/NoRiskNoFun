package gmbh.norisknofun.game;

import com.badlogic.gdx.Gdx;

import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.clients.Client;
import gmbh.norisknofun.game.server.clients.Clients;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;
import gmbh.norisknofun.game.server.networking.NewSessionEvent;
import gmbh.norisknofun.game.server.networking.SessionClosedEvent;
import gmbh.norisknofun.game.server.networking.SessionDataReceivedEvent;
import gmbh.norisknofun.game.server.networking.SessionEvent;
import gmbh.norisknofun.game.server.networking.SessionEventListener;
import gmbh.norisknofun.game.statemachine.server.ServerContext;

/**
 * Game server for handling game logic.
 */
public class GameServer {

    private static final long SESSION_EVENT_POLL_TIMEOUT = 1;
    private static final TimeUnit SESSION_EVENT_POLL_TIME_UNIT = TimeUnit.SECONDS;

    private final Thread gameServerThread;
    private final SessionEventListener eventListener;
    private final MessageBus messageBus;
    private final Clients clients;
    private final ServerContext serverContext;

    // TODO make server side GameData class to not confuse it with client side data.
    public GameServer(GameDataServer data, SessionEventListener eventListener) {

        gameServerThread = createGameServerThread();
        this.eventListener = eventListener;
        messageBus = new MessageBusImpl();
        clients = new Clients();
        serverContext = new ServerContext(data, messageBus);
    }

    private Thread createGameServerThread() {

        Thread result = new Thread(new Runnable() {
            @Override
            public void run() {
                serve();
            }
        });
        result.setName(getClass().getSimpleName());

        return result;
    }

    public synchronized boolean start() {

        if (isRunning()) {
            Gdx.app.error(getClass().getSimpleName(), "Server already running");
            return false;
        }

        gameServerThread.start();

        return true;
    }

    private void serve() {

        while(!Thread.interrupted()) {

            try {
                SessionEvent event = eventListener.pollSessionEvent(SESSION_EVENT_POLL_TIMEOUT, SESSION_EVENT_POLL_TIME_UNIT);
                event.process(new SessionEventProcessor(clients, messageBus));
            } catch (InterruptedException e) {
                Gdx.app.error(getClass().getSimpleName(), "polling was interrupted", e);
                break;
            }
        }

        clients.closeAll();
    }

    public synchronized boolean isRunning() {

        return gameServerThread.isAlive();
    }

    public synchronized void stop() throws InterruptedException {

        if (!isRunning())
            return;

        gameServerThread.interrupt();
        gameServerThread.join();
    }

    private static final class SessionEventProcessor implements gmbh.norisknofun.game.server.networking.SessionEventProcessor {

        private final Clients clients;
        private final MessageBus messageBus;

        SessionEventProcessor(Clients clients, MessageBus messageBus) {

            this.clients = clients;
            this.messageBus = messageBus;
        }

        @Override
        public void process(NewSessionEvent event) {

            clients.registerNewClient(event.getSession(), new Client(event.getSession(), messageBus));
        }

        @Override
        public void process(SessionClosedEvent event) {

            clients.processSessionClosed(event.getSession());
        }

        @Override
        public void process(SessionDataReceivedEvent event) {

            clients.processDataReceived(event.getSession());
        }
    }
}
