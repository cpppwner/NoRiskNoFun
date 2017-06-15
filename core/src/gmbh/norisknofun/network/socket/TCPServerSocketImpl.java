package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Wrapper class to wrap low level socket to improve testability.
 *
 * <p>
 *     Note: the whole socket implementation is using non-blocking sockets.
 * </p>
 */
class TCPServerSocketImpl implements TCPServerSocket {

    /**
     * default timeout for the socket - infinite timeout
     */
    private static final int DEFAULT_SOCKET_TIMEOUT_IN_MS = 0;


    private final ServerSocketChannel serverSocketChannel;

    /**
     * Private constructor taking the wrapped {@link ServerSocketChannel}.
     *
     * <p>
     *     Use the static {@link TCPServerSocketImpl#open(int)} or {@link TCPServerSocketImpl#open(String, int)}
     *     methods to bind a server socket.
     * </p>
     *
     * @param serverSocketChannel The underlying {@link ServerSocketChannel}.
     */
    private TCPServerSocketImpl(ServerSocketChannel serverSocketChannel) {

        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * Open a server socket.
     *
     * @param port The port to bind to.
     * @return Wrapper around the {@link ServerSocketChannel}.
     * @throws IOException If an I/O error occurs.
     */
    static TCPServerSocket open(int port) throws IOException {
        return open(null, port);
    }

    /**
     * Open a server socket.
     *
     * @param address The address to bind to.
     * @param port The port to bind to.
     * @return Wrapper around the {@link ServerSocketChannel}.
     * @throws IOException If an I/O error occurs.
     */
    static TCPServerSocket open(String address, int port) throws IOException {
        TCPServerSocketImpl serverSocket = null;
        try {
            serverSocket = new TCPServerSocketImpl(ServerSocketChannel.open());
            serverSocket.serverSocketChannel.socket().setSoTimeout(DEFAULT_SOCKET_TIMEOUT_IN_MS);
            serverSocket.serverSocketChannel.configureBlocking(false);

            // set SO_REUSEADDR socket option & bind socket
            serverSocket.serverSocketChannel.socket().setReuseAddress(true);
            serverSocket.serverSocketChannel.socket().bind(address == null ? new InetSocketAddress(port) : new InetSocketAddress(address, port));
        } catch (Exception e) {
            if (serverSocket != null) {
                serverSocket.close();
            }
            throw e;
        }

        return serverSocket;
    }

    @Override
    public TCPClientSocketImpl accept() throws IOException {

        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel == null) {
            return null; // no connection available
        }

        socketChannel.socket().setSoTimeout(DEFAULT_SOCKET_TIMEOUT_IN_MS);
        socketChannel.configureBlocking(false);

        return new TCPClientSocketImpl(socketChannel);
    }

    @Override
    public SelectableChannel getChannel() {
        return serverSocketChannel;
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {

        return serverSocketChannel.socket().getLocalSocketAddress();
    }

    @Override
    public void close() throws IOException {

        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
        }
    }
}
