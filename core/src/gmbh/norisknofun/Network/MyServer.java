package gmbh.norisknofun.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

   private ServerSocket m_server;

   private boolean isStopped = false;
   private boolean error=false;
    private  Thread m_objThread;



   public MyServer()
   {
       try {
           m_server = new ServerSocket(2002);

       } catch (IOException e) {
           error=true;
           e.printStackTrace();
       }
   }

   public void startListening()
   {
        m_objThread = new Thread(new Runnable() {
           public void run() {
               // Start ServerDispatcher thread
               ServerDispatcher serverDispatcher = new ServerDispatcher();
               serverDispatcher.start();

               while (!isStopped) {
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



                   } catch (IOException ioe) {
                       ioe.printStackTrace();
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


       try {
           this.m_objThread.interrupt();
           this.m_server.close();
       } catch (IOException e) {
           throw new RuntimeException("Error closing server", e);
       }
   }
}
