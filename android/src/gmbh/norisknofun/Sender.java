package gmbh.norisknofun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Philipp Mödritscher on 02.05.2017.
 */

class Sender extends Thread
{
    private PrintWriter mOut;
    private String mMessage;
    private Socket mSocket;
    private BufferedReader in;


    public Sender(String message, Socket socket)
    {

        mMessage = message;
        mSocket =socket;
    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run()
    {
        try {




            in = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));


            System.out.println("Try to send messsage to Server: "+mMessage);
            mOut = new PrintWriter(
                    new OutputStreamWriter(mSocket.getOutputStream()));



            mOut.println(mMessage);
            mOut.flush();






        } catch (Exception io) {
            // Communication is broken
            System.err.println("Cannot send message to Server Exception: "+io);
        }
    }
}

