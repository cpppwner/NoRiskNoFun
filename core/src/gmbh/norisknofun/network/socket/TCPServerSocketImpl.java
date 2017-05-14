package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            // set SO_REUSEADDR socket option & bind socket
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(address, port));
        } catch (IOException e) {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
            throw e;
        }

        return new TCPServerSocketImpl(serverSocketChannel);
    }

    @Override
    public TCPClientSocketImpl accept() throws IOException {

        if (serverSocketChannel == null)
            throw new IllegalStateException("channel not opened");

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        return new TCPClientSocketImpl(socketChannel);
    }

    @Override
    public SelectableChannel getChannel() {
        return serverSocketChannel;
    }

    @Override
    public void close() throws IOException {

        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
        }
    }
}
