package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Socket Selector class.
 */
public class SocketSelectorImpl implements SocketSelector {

    private final Selector selector;

    private SocketSelectorImpl(Selector selector) {

        this.selector = selector;
    }

    public static SocketSelectorImpl open() throws IOException {
        return new SocketSelectorImpl(Selector.open());
    }

    public SelectionKey register(SelectableChannel channel, int interrestOps) throws IOException {

        return channel.register(selector, interrestOps);
    }

    public SelectionKey register(SelectableChannel channel, int interrestOps, Object o) throws IOException {

        return channel.register(selector, interrestOps, o);
    }

    @Override
    public void close() throws Exception {
        selector.close();
    }
}
