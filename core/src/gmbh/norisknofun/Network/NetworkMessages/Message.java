package gmbh.norisknofun.Network.NetworkMessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

interface Message {
    public Class<? extends Message> getType();
}
