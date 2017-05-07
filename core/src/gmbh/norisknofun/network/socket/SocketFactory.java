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

    TCPServerSocket openServerSocket(int port) throws IOException;

    TCPServerSocket openServerSocket(String address, int port) throws IOException;

    TCPClientSocket openClientSocket(String address, int port) throws IOException;


}
