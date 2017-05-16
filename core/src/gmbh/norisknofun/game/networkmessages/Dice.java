package gmbh.norisknofun.game.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */
/**
 *
 * If attackedPLayer is reachable and player has chosen amount of Troops and rolled the Dice
 *
 * Client -> Server
 */
public class Dice {


    String playername;
    int amountofTroops;
    int diceResult;
}
