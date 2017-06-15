package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.StartGameClicked;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayersInGame;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * State in use, when waiting for other players.
 */
class WaitingForPlayersState extends State {

    /** Context in terms of state pattern */
    private final ClientContext context;

    WaitingForPlayersState( ClientContext context){
        this.context=context;
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(StartGame.class)) {
            startGame();
        } else if (message.getType().equals(PlayersInGame.class)) {
            context.getGameData().updateAllPlayers(((PlayersInGame)message).getAllPlayers());
        } else if (message.getType().equals(StartGameClicked.class)) {
            context.sendMessage(new StartGame(true));
        } else{
            Gdx.app.log(getClass().getSimpleName(),"unknown message " + message.getType().getName());
        }
    }

    private void startGame() {
        context.setState(new SpreadTroopsState(context));
        SceneManager.getInstance().setActiveScene(SceneNames.GAME_SCENE);
    }
}
