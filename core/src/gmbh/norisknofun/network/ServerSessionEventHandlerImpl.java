package gmbh.norisknofun.network;


import java.util.List;

import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.statemachine.server.ServerContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 */

public class ServerSessionEventHandlerImpl implements SessionEventHandler {

    private ServerContext context;
    public ServerSessionEventHandlerImpl(ServerContext context){
        this.context=context;
    }
    @Override
    public synchronized void newSession(Session session) {
        context.getGameData().addPlayer(new Player(session));
    }

    @Override
    public synchronized void sessionClosed(Session session) {
        List<Player> players=context.getGameData().getPlayers();
        for(int i=0; i<players.size(); i++){
            if(players.get(i).getSession().equals(session)){
                players.remove(i);
            }
        }
    }

    @Override
    public void sessionDataReceived(Session session) {
        //TODO
        // No Detection if everything was recieved



        try {

            BasicMessageImpl message = convertByteArraytoMessage(getFullMessage(session));

            context.getState().handleMessage(message);
        }catch (Exception e){
            //TODO Exception
        }
    }



    @Override
    public void sessionDataWritten(Session session) {
        //..
    }
    /**
     *
     * Deserilize the byte[] to a BasicMessage
     *
     * Concatinate byte[] a with byte[] b
     *
     * @param messageArray the byte[] which you want to convert
     * @return BasicMessageImpl .
     */
    public static BasicMessageImpl convertByteArraytoMessage(byte[] messageArray) throws IOException, ClassNotFoundException{


        ByteArrayInputStream in = new ByteArrayInputStream(messageArray);
        ObjectInputStream is = new ObjectInputStream(in);
        BasicMessageImpl message =(BasicMessageImpl) is.readObject() ;

        return message;


    }

    /**
     *  Reads Data from Session and returns the full byte[] (peaces will be concat)
     *
     * @param session Session from which you want to read Data
     * @return  byte[] .
     */
    private byte[] getFullMessage(Session session) throws IOException {
        byte[] message=null;

        byte[] temp =session.read();
        byte[] length_arr = Arrays.copyOfRange(temp,0,3);
        ByteBuffer buffer = ByteBuffer.wrap(length_arr);
        int length = buffer.getInt();
        int recieved=0;

            if (length < SessionImpl.DEFAULT_IN_BUFFER_SIZE) {
                message = Arrays.copyOfRange(temp, 4, length + 4);
                return message;
            } else {
                recieved+=SessionImpl.DEFAULT_IN_BUFFER_SIZE;
                while(recieved<length){
                    temp = SessionImpl.concatArray(temp,session.read());
                    recieved+=SessionImpl.DEFAULT_IN_BUFFER_SIZE;
                }
                message = Arrays.copyOfRange(temp, 4, length + 4);
                return message;
            }


    }

}
