package gmbh.norisknofun.game.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

public interface Message {

    Class<? extends Message> getType();
}
