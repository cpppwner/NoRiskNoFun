package gmbh.norisknofun.network.socket;

import java.nio.channels.SelectionKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of the {@link SelectionResult} interface.
 */
class SelectionResultImpl implements SelectionResult {

    /**
     * Original set of {@link SelectionKey selection keys} returned by {@link java.nio.channels.Selector#select()} operation.
     */
    private final Set<SelectionKey> selectionKeys;

    /**
     * All server sockets that are in acceptable state.
     */
    private final Map<TCPServerSocket, Boolean> acceptableSockets = new HashMap<>();
    private final Map<TCPClientSocket, Boolean> readableSockets = new HashMap<>();
    private final Map<TCPClientSocket, Boolean> writableSockets = new HashMap<>();

    SelectionResultImpl(Set<SelectionKey> selectionKeys) {

        this.selectionKeys = selectionKeys;
        initializeSelectionSets(selectionKeys);
    }

    private void initializeSelectionSets(Set<SelectionKey> selectionKeys) {
        for (SelectionKey selectionKey : selectionKeys) {
            if (selectionKey.isAcceptable()) {
                acceptableSockets.put((TCPServerSocket)selectionKey.attachment(), false);
                continue; // acceptable sockets are neither readable nor writable
            }
            if (selectionKey.isReadable()) {
                readableSockets.put((TCPClientSocket)selectionKey.attachment(), false);
            }
            if (selectionKey.isWritable()) {
                writableSockets.put((TCPClientSocket)selectionKey.attachment(), false);
            }
        }
    }

    @Override
    public Set<TCPServerSocket> getAcceptableSockets() {

        return Collections.unmodifiableSet(acceptableSockets.keySet());
    }

    @Override
    public void acceptHandled(TCPServerSocket socket) {

        if (acceptableSockets.containsKey(socket) && !acceptableSockets.get(socket)) {
            acceptableSockets.put(socket, true);
            removeSelectionKeyForAttachment(socket); // remove from selection keys, since accept was handled
        }
    }

    @Override
    public Set<TCPClientSocket> getReadableSockets() {

        return Collections.unmodifiableSet(readableSockets.keySet());
    }

    @Override
    public void readHandled(TCPClientSocket socket) {

        if (readableSockets.containsKey(socket) && !readableSockets.get(socket)) {
            readableSockets.put(socket, true);
            if (!writableSockets.containsKey(socket) || writableSockets.get(socket)) {
                removeSelectionKeyForAttachment(socket); // remove from selection keys, since read/write was handled
            }
        }
    }

    @Override
    public Set<TCPClientSocket> getWritableSockets() {

        return Collections.unmodifiableSet(writableSockets.keySet());
    }

    @Override
    public void writeHandled(TCPClientSocket socket) {

        if (writableSockets.containsKey(socket) && !writableSockets.get(socket)) {
            writableSockets.put(socket, true);
            if (!readableSockets.containsKey(socket) || readableSockets.get(socket)) {
                removeSelectionKeyForAttachment(socket); // remove from selection keys, since read/write was handled
            }
        }
    }

    private void removeSelectionKeyForAttachment(Object o) {

        for (Iterator<SelectionKey> iterator = selectionKeys.iterator(); iterator.hasNext();) {
            if (iterator.next().attachment().equals(o)) {
                iterator.remove();
                break;
            }
        }
    }
}
