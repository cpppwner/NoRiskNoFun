package gmbh.norisknofun.game.gamemessages.gui;

/**
 * Created by MödritscherPhilipp on 19.06.2017.
 */


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GuiTest {
    private  final String ATTACKEREGION = "region1";
    private final String DEFENDERREGION = "region2";
    private final String PLAYER = "player1";
    private final int AMOUNT = 2;
    private final int ID = 99;

    @Test
    public void ActionDoneGui() {

        ActionDoneGui actionDoneGui = new ActionDoneGui();
    }
    @Test
    public void EndGameGui() {

        EndGameGui endGameGui = new EndGameGui();
    }

    @Test
    public void EvaluateDiceResultGui() {

        EvaluateDiceResultGui evaluateDiceResultGui = new EvaluateDiceResultGui();
    }
    @Test
    public void StartGameClicked() {
        StartGameClicked startGameClicked = new StartGameClicked();
        assertEquals(startGameClicked.getType(),StartGameClicked.class);
    }

    @Test
    public void AttackRegionGui() {

        AttackRegionGui attackRegionGui = new AttackRegionGui(ATTACKEREGION,DEFENDERREGION);
        attackRegionGui.setAttackedRegion(ATTACKEREGION);
        attackRegionGui.setOriginRegion(DEFENDERREGION);
        assertEquals(attackRegionGui.getAttackedRegion(),  ATTACKEREGION);
        assertEquals(attackRegionGui.getOriginRegion(),  DEFENDERREGION);
    }
    @Test
    public void MoveTroopGui() {

        MoveTroopGui moveTroopGui = new MoveTroopGui(ATTACKEREGION, DEFENDERREGION, ID);
        moveTroopGui.setFromRegion(ATTACKEREGION);
        moveTroopGui.setFigureId(ID);
        moveTroopGui.setToRegion(DEFENDERREGION);
        moveTroopGui.setTroopamount(AMOUNT);


        assertEquals(moveTroopGui.getFromRegion(),  ATTACKEREGION);
        assertEquals(moveTroopGui.getFigureId(),  ID);
        assertEquals(moveTroopGui.getToRegion(),  DEFENDERREGION);
        assertEquals(moveTroopGui.getTroopamount(),  AMOUNT);


    }
    @Test
    public void RemoveTroopGui() {

        RemoveTroopGui removeTroopGui = new RemoveTroopGui(ATTACKEREGION,AMOUNT);
        removeTroopGui.setTroopAmount(AMOUNT);
        removeTroopGui.setRegionName(ATTACKEREGION);
        assertEquals(removeTroopGui.getTroopAmount(),  AMOUNT);
        assertEquals(removeTroopGui.getRegionName(),  ATTACKEREGION);

    }
    @Test
    public void SpawnTroopGui() {

        SpawnTroopGui spawnTroopGui = new SpawnTroopGui(ATTACKEREGION,ID);
        spawnTroopGui.setRegionName(ATTACKEREGION);
        spawnTroopGui.setId(ID);
        spawnTroopGui.setX(AMOUNT);
        spawnTroopGui.setY(new Float(1));
        assertEquals(spawnTroopGui.getY(),1 );
        assertEquals(spawnTroopGui.getRegionName(),  ATTACKEREGION);
        assertEquals(spawnTroopGui.getId(),  ID);
        assertEquals(spawnTroopGui.getX(),  AMOUNT);


    }
    @Test
    public void StartGameGui() {

        StartGameGui startGameGui = new StartGameGui(true);
        startGameGui.setStartGame(true);
        assertEquals(startGameGui.isStartGame(),  true);

    }
    @Test
    public void ChooseTroopsAmountGui() {

        ChooseTroopsAmountGui chooseTroopsAmountGui = new ChooseTroopsAmountGui(AMOUNT);
        chooseTroopsAmountGui.setAmount(AMOUNT);
        assertEquals(chooseTroopsAmountGui.getAmount(),  AMOUNT);

    }
    @Test
    public void UpdateCurrentPlayerGui() {

        UpdateCurrentPlayerGui updateCurrentPlayerGui = new UpdateCurrentPlayerGui(PLAYER);

        assertEquals(updateCurrentPlayerGui.getCurrentPlayer(),  PLAYER);

    }
    @Test
    public void UpdateRegionOwnerGui() {

        UpdateRegionOwnerGui updateRegionOwnerGui = new UpdateRegionOwnerGui(ATTACKEREGION,PLAYER);
        updateRegionOwnerGui.setRegionName(ATTACKEREGION);
        updateRegionOwnerGui.setNewOwner(PLAYER);
        assertEquals(updateRegionOwnerGui.getRegionName(),  ATTACKEREGION);
        assertEquals(updateRegionOwnerGui.getNewOwner(),  PLAYER);

    }
}
