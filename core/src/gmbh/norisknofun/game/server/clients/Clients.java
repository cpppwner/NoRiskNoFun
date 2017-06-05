package gmbh.norisknofun.game.server.clients;

import java.util.HashMap;
import java.util.Map;

import gmbh.norisknofun.network.Session;

/**
 * Simple container class for managing connected {@link Client clients}.
 */
public final class Clients {

    /**
     * Internally used container.
     */
    private final Map<Session, Client> clients = new HashMap<>();

    /**
     * Register a new client for a session.
     *
     * <p>
     *     {@link IllegalArgumentException} is thrown if either {@code session} or {@link Client} is null.
     * </p>
     *
     * @param session The {@link Session} for which a new client is registered.
     * @param client The {@link Client} to register.
     */
    public void registerNewClient(Session session, Client client) {

        if (session == null)
            throw new IllegalArgumentException("session is null");
        if (client == null)
            throw new IllegalArgumentException("client is null");

        clients.put(session, client);
    }

    /**
     * Closes all sessions and immediately notifies {@link Client clients} about session closing.
     *
     * <p>
     *     All registered clients are also cleared.
     * </p>
     */
    public void closeAll() {

        for (Map.Entry<Session, Client> entry : clients.entrySet()) {
            entry.getKey().close(); // don't wait for session closed event
            entry.getValue().sessionClosed();
        }

        clients.clear();
    }

    /**
     * Called when data was received for given session.
     *
     * @param session Session for which data was received.
     */
    public void processDataReceived(Session session) {

        Client client = clients.get(session);
        if (client != null) {
            client.processDataReceived(session.read());
        }
    }

    /**
     * Called when a session closed notification was received.
     *
     * @param session The session that got closed.
     */
    public void processSessionClosed(Session session) {

        Client client = clients.remove(session);
        if (client != null) {
            client.sessionClosed();
        }
    }

    /**
     * Get the number of registered {@link Client clients}.
     */
    public int getNumberOfRegisteredClients() {

        return clients.size();
    }
}
