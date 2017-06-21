package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.gamemessages.gui.EvaluateDiceResultGui;
import gmbh.norisknofun.game.gamemessages.gui.RemoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.UpdateRegionOwnerGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.AttackResult;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceAmount;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceResult;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by Katharina on 19.05.2017.
 */

class EvaluateDiceResultState extends State {

    private final ClientContext context;
    private final GameData data;

    EvaluateDiceResultState(ClientContext context){
        this.context=context;
        this.data = context.getGameData();
    }
    @Override
    public void handleMessage(String senderId, Message message) {
        if(message.getType().equals(DiceAmount.class)){

            data.setAvailableDice(((DiceAmount)message).getAmount());
            SceneManager.getInstance().setActiveScene(SceneNames.DICE_SCENE);

        }else if(message.getType().equals(EvaluateDiceResultGui.class)){
            context.sendMessage(new DiceResult(context.getGameData().getDiceRoll()));

        }else if(message.getType().equals(AttackResult.class)){

            handleAttackResult((AttackResult)message);

        }else{
            Gdx.app.log("Client - EvaluateDiceResultState","unknown message:"+message.getClass().getSimpleName());
        }
    }

    private void handleAttackResult(AttackResult attackResult){

        updateRegions(attackResult);
        if (attackResult.getWinnerId().equals("")) { // partial win
            data.setLastError("Both lost troops, but the region\nhasn't been captured yet");
            if (data.isMyTurn()) {
                context.setState(new ChooseTargetState(context));
            } else {
                context.setState(new WaitingForNextTurnState(context));
            }
        } else if(context.getGameData().isMyTurn()){ //  I am attacker
            if(attackResult.getWinnerId().equals(context.getGameData().getMyself().getId())) {
                data.setLastError("Congratulations!  \n  you captured " + attackResult.getDefenderRegion());
            }else {
                data.setLastError("You lost!  \n  more luck next time.");
            }
            context.setState(new ChooseTargetState(context));
        } else { //  I am Defender
            if(attackResult.getWinnerId().equals(context.getGameData().getMyself().getId())) {
                data.setLastError("Congratulations!  \n  you defended " + attackResult.getDefenderRegion());
            }else {
                data.setLastError("Sorry!  \n  you lost "+attackResult.getDefenderRegion());
            }
            context.setState(new WaitingForNextTurnState(context));
        }

        // switch back to Game Scene after the attack was done
        SceneManager.getInstance().setActiveScene(SceneNames.GAME_SCENE);
    }

    /**
     * Update Troops on attacker and defender region
     * if defender lost -> also update region owner
     * @param message
     */
    private void updateRegions(AttackResult message){
        AssetMap.Region attackerRegion = data.getMapAsset().getRegion(message.getAttackerRegion());
        AssetMap.Region defenderRegion = data.getMapAsset().getRegion(message.getDefenderRegion());

        Gdx.app.log("Client Evaluate", "Troops to remove: " + attackerRegion.getName()+ ": " + (attackerRegion.getTroops() - message.getAttackerTroops()));
        Gdx.app.log("Client Evaluate", "Troops to remove: " + defenderRegion.getName()+ ": " + (defenderRegion.getTroops() - message.getDefenderTroops()));


        // if difference is negative
        // e.g. defender had 1 troop, got attacked with 3 -> difference = -2, so 2 troops need to be added
        int defenderRegionTroops = defenderRegion.getTroops() - message.getDefenderTroops();
        if (defenderRegionTroops < 0) {
            // spawn the correct amount of troops as an amount can't be set in SpawnTroopGui
            for (int i = 0; i < Math.abs(defenderRegionTroops); i++) {
                data.setGuiChanges(new SpawnTroopGui(defenderRegion.getName(), 1000));
            }
        } else  {
            data.setGuiChanges(new RemoveTroopGui(defenderRegion.getName(), defenderRegion.getTroops() - message.getDefenderTroops()));
        }
        data.setGuiChanges(new UpdateRegionOwnerGui(defenderRegion.getName(), message.getDefenderRegionOwner()));

        data.setGuiChanges(new RemoveTroopGui(attackerRegion.getName(), attackerRegion.getTroops() - message.getAttackerTroops()));

        Gdx.app.log("Client EvaluateDiceResultState", "Attacker Region: " + message.getAttackerRegion() +
                ", Troops: " + message.getAttackerTroops());
        Gdx.app.log("Client EvaluateDiceResultState", "Defender Region: " + defenderRegion.getName() +
                ", Troops: " + defenderRegion.getTroops() +
                ", Owner: " + defenderRegion.getOwner());

    }
}
