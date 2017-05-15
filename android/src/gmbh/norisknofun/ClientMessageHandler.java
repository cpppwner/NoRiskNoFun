package gmbh.norisknofun;

/**
 * Created by Philipp Mödritscher on 02.05.2017.+
 */

public class ClientMessageHandler {


    public  static void handleMessage(String message) {
        // HandleMessages GameLogic ...etc

        if (message.equals(NetworkMessages.SERVER + ":" + NetworkMessages.SERVER_MESSAGE_CLOSED)) {
        //SERVER Closed
            System.out.println("Server was closed");




            //TODO: Close Game etc Close Client etc
            // remove MainActivity with Core Client

           // MainActivity.client.setConnected(false);


        }


    }

}
