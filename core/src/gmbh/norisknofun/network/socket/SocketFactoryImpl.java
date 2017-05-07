package gmbh.norisknofun.network.socket;

import java.io.IOException;

/**
 * Default implementation of the socket factory.
 */
public class SocketFactoryImpl implements SocketFactory {

    @Override
    public TCPServerSocket openServerSocket(int port) throws IOException {
        return TCPServerSocketImpl.open(port);
    }

    @Override
    public TCPServerSocket openServerSocket(String address, int port) throws IOException {
        return TCPServerSocketImpl.open(address, port);
    }

    @Override
    public TCPClientSocket openClientSocket(String address, int port) throws IOException {
        return TCPClientSocketImpl.open(address, port);
    }

    @Override
    public SocketSelector openSocketSelector() throws IOException {
        return SocketSelectorImpl.open();
    }
}
