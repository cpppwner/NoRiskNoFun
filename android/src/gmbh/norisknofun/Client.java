
package gmbh.norisknofun;


import java.io.BufferedReader;
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
                ClientMessageHandler.handleMessage(message);
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
    }




    public void display(final String message)
    {


        System.out.println("SENDER: "+message);

    }
}

