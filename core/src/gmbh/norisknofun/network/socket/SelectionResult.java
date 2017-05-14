package gmbh.norisknofun.network.socket;

import java.util.Set;

/**
 * Created by cpppwner on 11.05.17.
 */

public interface SelectionResult {

    Set<TCPServerSocket> getAcceptableSockets();

    void acceptHandled(TCPServerSocket socket);

    Set<TCPClientSocket> getReadableSockets();

    void readHandled(TCPClientSocket socket);

    Set<TCPClientSocket> getWritableSockets();

    void writeHandled(TCPClientSocket socket);
}
