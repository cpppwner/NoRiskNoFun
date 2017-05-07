package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Socket Selector class.
 */
public class SocketSelector implements AutoCloseable {

    private final Selector selector;

    private SocketSelector(Selector selector) {

        this.selector = selector;
    }

    public static SocketSelector open() throws IOException {
        return new SocketSelector(Selector.open());
    }

    Selector getSelector() {
        return selector;
    }

    @Override
    public void close() throws Exception {
        selector.close();
    }
}
