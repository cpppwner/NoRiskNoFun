package gmbh.norisknofun.game.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

interface Message {

    public Class<? extends Message> getType();
}
