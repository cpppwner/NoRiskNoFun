package gmbh.norisknofun.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by philipp on 06.04.2017.
 */
public class ClientListener extends  Thread{

    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private BufferedReader mIn;

    public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
            throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run()
    {
        try {
            while (!isInterrupted()) {
                String message = mIn.readLine();
                //System.out.println("+++++++++ "+message+" ++++++++");
                if (message == null){
                    break;
                }else if(message.equals(/*NetworkMessages.CLIENT_MESSAGE_Here*/ "DOES NOT BUILD")){
                    // CLient XY response
                   //mClientInfo.mCheckConnection.isAlive=true;

                }else{
                    mServerDispatcher.dispatchMessage(mClientInfo, message);
                }
            }
        } catch (Exception ex) {
            // Problem reading from socket (communication is broken)
            System.err.println("ERROR: "+ex);
        }

        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
}
