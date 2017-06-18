package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Special state class, which is a final state - in case of an error.
 */
class ErrorState extends State {

    @Override
    public void enter() {

        // switch back to main menu
        SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
    }

    @Override
    public void handleMessage(String senderId, Message message) {
        // nothing to do here, since we do not leave this state
    }
}
