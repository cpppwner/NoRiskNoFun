package gmbh.norisknofun.game.protocol;

/**
 * Exception class thrown, if something is wrong with protocol.
 */
public class ProtocolException extends Exception {

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
