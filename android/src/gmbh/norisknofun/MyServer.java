package gmbh.norisknofun;

/**
 * Created by Philipp Mödritscher on 06.04.2017.
 */

import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private ServerSocket m_server;

    private boolean isStopped = false;
    private boolean error=false;
    private  Thread m_objThread;
    private ServerDispatcher serverDispatcher;



    MyServer()
    {
        try {
            m_server = new ServerSocket(2002);

        } catch (Exception e) {
            error=true;
            System.err.println(e);
        }
    }

    void startListening()
    {
        m_objThread = new Thread(new Runnable() {
            public void run() {
                // Start ServerDispatcher thread
                serverDispatcher = new ServerDispatcher();
                serverDispatcher.start();

                while (!isStopped()) {
                    try {
                        Socket socket = m_server.accept();
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.mSocket = socket;
                        ClientListener clientListener =
                                new ClientListener(clientInfo, serverDispatcher);
                        ClientSender clientSender =
                                new ClientSender(clientInfo, serverDispatcher);
                        clientInfo.mClientListener = clientListener;
                        clientInfo.mClientSender = clientSender;
                        clientListener.start();
                        clientSender.start();
                        serverDispatcher.addClient(clientInfo);



                    } catch (Exception ioe) {
                       System.err.println("Error Connection: "+ioe);
                    }
                }

            }});

        m_objThread.start();
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        // System.out.println("Try to close Server:");

        try {
            serverDispatcher.dispatchMessage(null,NetworkMessages.SERVER_MESSAGE_CLOSED);
            serverDispatcher.stopAll();
            serverDispatcher.interrupt();
            this.m_server.close();
            this.m_objThread.interrupt();




        } catch (Exception e) {
            System.out.println("SERVER: Exception in stop():");
            e.printStackTrace();
        }
    }
}
