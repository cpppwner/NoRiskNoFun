package gmbh.norisknofun.network.socket;

import java.nio.ByteBuffer;
import java.util.Set;

/**
 * Interface specifying the result of a {@link SocketSelector#select()} operation..
 */
public interface SelectionResult {

    /**
     * Get all {@link TCPServerSocket server sockets} that are in accepting state.
     *
     * <p>
     *     After performing the {@link TCPServerSocket#accept()} operation, call the
     *     {@link SelectionResult#acceptHandled(TCPServerSocket)} method.
     * </p>
     */
    Set<TCPServerSocket> getAcceptableSockets();

    /**
     * Method that shall be called, when an accept was handled.
     *
     * <p>
     *     This method must be called, in order to remove this pending event.
     * </p>
     */
    void acceptHandled(TCPServerSocket socket);

    /**
     * Get all {@link TCPClientSocket client sockets} that can be read from.
     *
     * <p>
     *     After performing the {@link TCPClientSocket#read(ByteBuffer)} ()} operation, call the
     *     {@link SelectionResult#readHandled(TCPClientSocket)} method.
     * </p>
     */
    Set<TCPClientSocket> getReadableSockets();

    /**
     * Method that shall be called, when a read operation was handled.
     *
     * <p>
     *     This method must be called, in order to remove this pending event.
     * </p>
     */
    void readHandled(TCPClientSocket socket);

    /**
     * Get all {@link TCPClientSocket client sockets} that can be written to.
     *
     * <p>
     *     After performing the {@link TCPClientSocket#write(ByteBuffer)} ()} operation, call the
     *     {@link SelectionResult#writeHandled(TCPClientSocket)} method.
     * </p>
     */
    Set<TCPClientSocket> getWritableSockets();

    /**
     * Method that shall be called, when a write operation was handled.
     *
     * <p>
     *     This method must be called, in order to remove this pending event.
     * </p>
     */
    void writeHandled(TCPClientSocket socket);
}
