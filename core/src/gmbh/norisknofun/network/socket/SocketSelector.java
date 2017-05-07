package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Selector interface.
 */
interface SocketSelector extends AutoCloseable {

    SelectionKey register(SelectableChannel channel, int interrestOps) throws IOException;

    SelectionKey register(SelectableChannel channel, int interrestOps, Object o) throws IOException;
}
