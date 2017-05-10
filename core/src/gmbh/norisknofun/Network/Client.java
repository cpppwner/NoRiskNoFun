package gmbh.norisknofun.Network;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by philipp on 06.04.2017.
 */

public class Client {
    public static  String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 2002;
    static PrintWriter out = null;

    public static void startCLient(String ip)
    {

        SERVER_HOSTNAME=ip;
        BufferedReader in = null;

        try {
            // Connect to Nakov Chat Server
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            display("Connected to server " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);


        } catch (Exception ioe) {
            System.err.println("Can not establish connection to " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
            ioe.printStackTrace();
            //System.exit(-1);
        }

        // Create and start Sender thread


        try {
            // Read messages from the server and print them
            String message;
            while ((message=in.readLine()) != null) {
                System.out.println("+++++++++++++++++++++++++++++++++");
                System.out.println(""+message);
                display(message);
                System.out.println("+++++++++++++++++++++++++++++++++");
            }
        } catch (IOException ioe) {
            System.err.println("Connection to server broken.");
            ioe.printStackTrace();
        }

    }
    public static void sendMessage(String message)
    {
        Sender sender = new Sender(out, message);
        sender.setDaemon(true);
        sender.start();

    }
    public static void display(final String message)
    {




    }
}

class Sender extends Thread
{
    private PrintWriter mOut;
    private String mMessage;


    public Sender(PrintWriter aOut, String message)
    {
        mOut = aOut;
        mMessage = message;

    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run()
    {
        try {



                mOut.println(mMessage);
                mOut.flush();

        } catch (Exception io) {
            // Communication is broken
        }
    }
}
