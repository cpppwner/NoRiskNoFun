package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;

/**
 * Socket Selector class.
 */
class SocketSelectorImpl implements SocketSelector {

    private final Selector selector;

    /**
     * Private constructor taking the {@link Selector} to wrap.
     *
     * <p>
     *     Open a selector by calling {@link SocketSelectorImpl#open()}.
     * </p>
     *
     * @param selector The wrapped {@link Selector}.
     */
    private SocketSelectorImpl(Selector selector) {

        this.selector = selector;
    }

    /**
     * Open the {@link Selector} and return wrapper.
     *
     * @return Wrapper around the {@link Selector}.
     * @throws IOException If an I/O error occurs.
     */
    static SocketSelectorImpl open() throws IOException {
        return new SocketSelectorImpl(Selector.open());
    }

    @Override
    public void register(TCPServerSocket serverSocket) throws IOException {

        SelectionKey key = serverSocket.getChannel().register(selector, SelectionKey.OP_ACCEPT);
        key.attach(serverSocket);
    }

    @Override
    public void unregister(TCPServerSocket serverSocket) {

        unregister(serverSocket.getChannel());
    }

    @Override
    public void register(TCPClientSocket clientSocket, boolean writable) throws IOException {

        int interestOps = SelectionKey.OP_READ;
        if (writable) {
            interestOps |= SelectionKey.OP_WRITE;
        }

        SelectionKey key = clientSocket.getChannel().register(selector, interestOps);
        key.attach(clientSocket);
    }

    @Override
    public void unregister(TCPClientSocket clientSocket) {

        unregister(clientSocket.getChannel());
    }

    @Override
    public void modify(TCPClientSocket clientSocket, boolean writable) throws IOException {

        SelectionKey key = clientSocket.getChannel().keyFor(selector);
        if (key == null)
            return; // not registered with this selector

        int interestOps = key.interestOps();
        if (writable) {
            interestOps |= SelectionKey.OP_WRITE;
        } else {
            interestOps &= ~SelectionKey.OP_WRITE;
        }

        key.interestOps(interestOps);
    }

    @Override
    public SelectionResult select() throws IOException {

        int result = selector.select();
        if (result == 0) {
            return new SelectionResultImpl(Collections.emptySet());
        }

        return new SelectionResultImpl(selector.selectedKeys());
    }

    @Override
    public void wakeup() {

        selector.wakeup();
    }

    @Override
    public void close() throws IOException {
        selector.close();
    }

    private void unregister(SelectableChannel channel) {

        SelectionKey key = channel.keyFor(selector);
        if (key != null) {
            // not registered with this selector => null returned
            key.cancel();
        }
    }
}
