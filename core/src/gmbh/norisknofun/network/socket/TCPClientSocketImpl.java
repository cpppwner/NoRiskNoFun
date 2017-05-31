package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/**
 * Wrapper class used to wrap low level socket implementation.
 *
 * <p>
 *     Note: The whole socket implementation is using non-blocking sockets.
 * </p>
 */
class TCPClientSocketImpl implements TCPClientSocket {

    private SocketChannel socketChannel;

    /**
     * Internal constructor invoked when a {@link SocketChannel} is already opened.
     * @param socketChannel The opened and wrapped socket channel.
     */
    TCPClientSocketImpl(SocketChannel socketChannel) {

        this.socketChannel = socketChannel;
    }

    /**
     * Open client socket and connect to given address and port.
     *
     * @param address Address to connect to.
     * @param port Port to connect to.
     * @return {@link TCPClientSocket}
     * @throws IOException If an I/O error occurs.
     */
    static TCPClientSocketImpl open(String address, int port) throws IOException {

        TCPClientSocketImpl clientSocket = null;
        try {
            clientSocket = new TCPClientSocketImpl(SocketChannel.open());
            clientSocket.socketChannel.connect(new InetSocketAddress(address, port));
            clientSocket.socketChannel.configureBlocking(false);
        } catch(Exception e) {
            if (clientSocket != null) {
                clientSocket.close();
            }
            throw e;
        }

        return clientSocket;
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return socketChannel.read(buffer);
    }

    @Override
    public int write(ByteBuffer buffer) throws IOException {
        return socketChannel.write(buffer);
    }

    public SocketAddress getRemoteAddress() throws IOException {
        return socketChannel.socket().getRemoteSocketAddress();
    }

    public SocketAddress getLocalAddress() throws IOException {
        return socketChannel.socket().getLocalSocketAddress();
    }

    @Override
    public SelectableChannel getChannel() {
        return socketChannel;
    }

    @Override
    public void close() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
            socketChannel = null;
        }
    }
}
