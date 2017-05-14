package gmbh.norisknofun.network.socket;

import java.io.Closeable;
import java.io.IOException;

/**
 * Selector used to handle non-blocking I/O.
 */
public interface SocketSelector extends Closeable {

    /**
     * Register a {@link TCPServerSocket} with this Selector.
     *
     * <p>
     *     Registering a {@link TCPServerSocket} means to register it only
     *     accept operations.
     * </p>
     *
     * @param serverSocket The server socket to register.
     * @throws IOException If an I/O error occurs.
     */
    void register(TCPServerSocket serverSocket) throws IOException;

    void unregister(TCPServerSocket serverSocket);

    void register(TCPClientSocket clientSocket, boolean writable) throws IOException;

    void unregister(TCPClientSocket clientSocket);

    void modify(TCPClientSocket clientSocket, boolean writable) throws IOException;

    SelectionResult select() throws IOException;

    void wakeup();
}
