package gmbh.norisknofun.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

/**
 * Implementation of the session interface.
 */
public class SessionImpl implements Session {

    private static final int DEFAULT_IN_BUFFER_SIZE = 4096;

    /**
     * Session's state.
     */
    private enum SessionState {
        OPEN,
        CLOSED,
        TERMINATED
    }

    /**
     * Queue storing byte message sent via the socket to the remote site.
     */
    private final Queue<byte[]> outQueue = new LinkedList<>();
    /**
     * Queue storing messages read from the socket.
     */
    private final Queue<byte[]> inQueue = new LinkedList<>();
    /**
     * Buffer holding current message which is written to to the socket.
     */
    private ByteBuffer currentOutgoingMessage = null;
    /**
     * Sessions's current state.
     *
     * <p>
     *     By default a newly created session is open.
     *     When {@link SessionImpl#close()} or {@link SessionImpl#terminate()} is called
     *     the session's state changes accordingly.
     * </p>
     */
    private SessionState sessionState = SessionState.OPEN;

    /**
     * Synchronization object for avoiding race conditions.
     */
    private final Object syncObject = new Object();

    /**
     * Selector, since some actions require to wakeup the selector.
     */
    private final SocketSelector selector;

    /**
     * Internal constructor taking the {@link SocketSelector}.
     * @param selector Selector to notify on certain conditions.
     */
    public SessionImpl(SocketSelector selector) {
        this.selector = selector;
    }

    @Override
    public byte[] read() {

        byte[] data;
        synchronized (syncObject) {
            data = inQueue.poll();
        }

        return data;
    }

    @Override
    public void write(byte[] data) {

        synchronized (syncObject) {
            if (sessionState == SessionState.OPEN) {
                outQueue.add(data);
            }

            wakeupSelectorIfRequired();
        }
    }

    @Override
    public void close() {

        synchronized (syncObject) {
            if (isOpen()) {
                sessionState = SessionState.CLOSED;
                wakeupSelectorIfRequired();
            }
        }
    }

    @Override
    public void terminate() {

        synchronized (syncObject) {
            if (isOpen() || isClosed()) {
                sessionState = SessionState.TERMINATED;
                wakeupSelectorIfRequired();

                // terminate means don't flush any data, so kill all data immediately
                currentOutgoingMessage = null;
                outQueue.clear();
                inQueue.clear();
            }
        }
    }

    @Override
    public boolean isOpen() {

        return isSessionState(SessionState.OPEN);
    }

    @Override
    public boolean isClosed() {

        return isSessionState(SessionState.CLOSED);
    }

    @Override
    public boolean isTerminated() {

        return isSessionState(SessionState.TERMINATED);
    }

    private boolean isSessionState(SessionState expectedState) {

        boolean result;
        synchronized (syncObject) {
            result = sessionState == expectedState;
        }

        return result;
    }

    /**
     * Wakeup {@link SocketSelector} if required.
     *
     * <p>
     *     If there is new output data in, or none left, the selector must be
     *     woken up.
     * </p>
     */
    private void wakeupSelectorIfRequired() {

        if (currentOutgoingMessage != null && currentOutgoingMessage.hasRemaining()) {
            return; // still something left in current outgoing buffer
        }

        if (outQueue.size() > 1) {
            return;
        }

        selector.wakeup();
    }

    /**
     * Read data from socket and store it in the internal buffer.
     *
     * <p>
     *     This method is called from Network Server/Client to read data from the socket
     *     and store it in internal buffer.
     * </p>
     *
     * @param socket The socket from which to read data.
     * @return Number of read bytes.
     * @throws IOException If an I/O error occurred.
     */
    int doReadFromSocket(TCPClientSocket socket) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_IN_BUFFER_SIZE);
        int numBytesRead = socket.read(buffer);
        if (numBytesRead <= 0) {
            return numBytesRead; // no bytes read
        }

        byte[] bytesRead = new byte[numBytesRead];
        buffer.flip();
        buffer.get(bytesRead);

        synchronized (syncObject) {
            if (isOpen()) {
                inQueue.add(bytesRead);
            } else {
                numBytesRead = 0;
            }
        }

        return numBytesRead;
    }

    /**
     * Write data to the socket.
     *
     * <p>
     *     This method is called from Network Server/Client to write data to the socket.
     * </p>
     *
     * @param socket The socket to which to write data to.
     * @return Number of bytes written.
     * @throws IOException If an I/O error occurs.
     */
    int doWriteToSocket(TCPClientSocket socket) throws IOException {

        ByteBuffer outBuffer;
        synchronized (syncObject) {
            if (currentOutgoingMessage == null || !currentOutgoingMessage.hasRemaining()) {
                byte[] data = outQueue.poll();
                currentOutgoingMessage = ByteBuffer.wrap(data == null ? new byte[0] : data);
            }
            outBuffer = clone(currentOutgoingMessage);
        }

        int numWrittenBytes = socket.write(outBuffer);
        if (numWrittenBytes <= 0)
            return numWrittenBytes;

        synchronized (syncObject) {
            if (!isTerminated()) {
                currentOutgoingMessage = clone(outBuffer);
            }
        }

        return numWrittenBytes;
    }

    /**
     * Checks if this session has data to write.
     * @return {@code true} if there is data pending to write, {@code false} otherwise.
     */
    boolean hasDataToWrite() {

        boolean result;
        synchronized (syncObject) {

            result = ((currentOutgoingMessage != null && currentOutgoingMessage.hasRemaining())
                    || !outQueue.isEmpty());
        }

        return result;
    }

    /**
     * Duplicate the contents of a {@link ByteBuffer}.
     *
     * <p>
     *     Unlike the {@link ByteBuffer#duplicate()} method this method does a deep copy.
     * </p>
     *
     * @param buffer The source buffer to clone.
     * @return Deep clone of {@link ByteBuffer buffer}.
     */
    private static ByteBuffer clone(ByteBuffer buffer)
    {
        ByteBuffer clone = ByteBuffer.allocate(buffer.remaining());
        System.arraycopy(buffer.array(), buffer.arrayOffset() + buffer.position(), clone.array(), 0, buffer.remaining());

        return clone;
    }
}
