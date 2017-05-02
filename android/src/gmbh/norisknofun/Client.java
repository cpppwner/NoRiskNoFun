
package gmbh.norisknofun;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by philipp on 06.04.2017.+
 */

 class Client {
    static  String SERVER_HOSTNAME = "localhost";
    private static final int SERVER_PORT = 2002;

    private  static Socket socket = null;

    private static boolean connected = false;
    public static String CLIENT_ID;

    public Client() {
        CLIENT_ID ="???";
    }

    public void startCLient(String ip)
    {
        //System.out.println("startCLient:");
        SERVER_HOSTNAME=ip;
        BufferedReader in = null;



        try {

            //System.out.println("Try to Connect:");
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));




            display("Connected to server " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
            connected = true;

        } catch (Exception e) {
            System.err.println("Can not establish connection to " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT+" Exception: "+e);
            connected = false;


        }


        System.out.println("Check Message start:");
        try {
            // Read messages from the server and print them
            String message;
            while ((message=in.readLine()) != null && this.isConnected()) {
                System.out.println("+++++++++++++++++++++++++++++++++");
                display(message);
                handleMessage(message);
                System.out.println("+++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            System.err.println("Connection to server broken.");
            connected = false;
            e.printStackTrace();
        }

    }
    public  void sendMessage(String message)
    {
        if(this.isConnected()) {
            Sender sender = new Sender(message, socket);
            sender.setDaemon(true);
            sender.start();
        }else{
            System.err.println("Client isn´t connected");
        }

    }

    public boolean isConnected(){
        return this.connected;
    }

    public void setConnected( boolean connected){
         this.connected=connected;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void display(final String message)
    {


        System.out.println("CLIENT: "+message);

    }

    public  void handleMessage(String message) {
        // HandleMessages GameLogic ...etc

        if (message.equals(NetworkMessages.SERVER + ":" + NetworkMessages.SERVER_MESSAGE_CLOSED)) {
            //SERVER Closed
            System.out.println("Server was closed");




            //TODO: Close Game etc Close Client etc


            setConnected(false);


        }else if(message.equals(NetworkMessages.SERVER + ":" + NetworkMessages.SERVER_MESSAGE_Here)){
          // display(message);
           this.sendMessage(NetworkMessages.CLIENT_MESSAGE_Here);
        }


    }
}

