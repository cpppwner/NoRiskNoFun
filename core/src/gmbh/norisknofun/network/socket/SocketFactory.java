package gmbh.norisknofun.network.socket;

import java.io.IOException;

/**
 * Socket factory interface.
 *
 * <p>
 *     Use it for easier testability.
 * </p>
 */
public interface SocketFactory {

    /**
     * Open {@link TCPServerSocket} and bind to given port.
     *
     * @param port The port to bind to.
     * @return An interface wrapping a {@link java.nio.channels.ServerSocketChannel}.
     * @throws IOException If an I/O error occurs.
     */
    TCPServerSocket openServerSocket(int port) throws IOException;

    /**
     * Open {@link TCPServerSocket} and bind to given address and port.
     *
     * @param address The address to bind to.
     * @param port The port to bind to.
     * @return An interface wrapping a {@link java.nio.channels.ServerSocketChannel}.
     * @throws IOException If an I/O error occurs.
     */
    TCPServerSocket openServerSocket(String address, int port) throws IOException;

    /**
     * Establish a client connection to a given address and port.
     *
     * <p>
     *     The connect is done blocking and after completion the {@link java.nio.channels.SocketChannel}
     *     is set to non blocking mode.
     * </p>
     *
     * @param address The address to connect to.
     * @param port The port to connect to.
     * @return An interface wrapping a {@link java.nio.channels.SocketChannel}.
     * @throws IOException If an I/O error occurs.
     */
    TCPClientSocket openClientSocket(String address, int port) throws IOException;

    /**
     * Open a socket selector used for non-blocking I/O.
     *
     * @return An interface wrapping the {@link java.nio.channels.Selector}.
     * @throws IOException If an I/O error occurs.
     */
    SocketSelector openSocketSelector() throws IOException;
}
