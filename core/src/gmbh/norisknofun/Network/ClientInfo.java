package gmbh.norisknofun.Network;

import java.net.Socket;

/**
 * Created by philipp on 06.04.2017.
 */
public class ClientInfo {

    public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
    //public CheckConnection mCheckConnection = null;
    public int ID;
}
