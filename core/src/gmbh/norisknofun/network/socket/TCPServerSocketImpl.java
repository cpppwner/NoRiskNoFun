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

    private TCPServerSocketImpl(ServerSocketChannel serverSocketChannel) {

        this.serverSocketChannel = serverSocketChannel;
    }

    public static TCPServerSocket open(int port) throws IOException {
        return open(null, port);
    }

    public static TCPServerSocket open(String address, int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // set SO_REUSEADDR socket option & bind socket
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(address, port));

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
    public void close() throws Exception {

        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
        }
    }
}
