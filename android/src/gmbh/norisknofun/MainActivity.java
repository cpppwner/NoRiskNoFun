package gmbh.norisknofun;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;import at.werkstatt.philipp.networktest.R;

public class MainActivity extends Activity implements DataDisplay {

    private Socket clientSocket;
    private EditText etIp;
    private MyServer server;
    private Button startServerButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etIp = (EditText) findViewById(R.id.etIp);
        etIp.setText("127.0.0.1");
        startServerButton = (Button) findViewById(R.id.btnstartserver);
    }
    public void startClient(View view)
    {
         Thread  m_objThreadClient=new Thread(new Runnable() {
            public void run()
            {
                try
                {


                    //Create ClientSocket and sends/recieves Stuff

                    InetAddress addr = InetAddress.getByName(etIp.getText().toString());
                    clientSocket= new Socket(addr,2001);
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject("CLient says Hello!");
                    Message serverMessage= Message.obtain();
                    ObjectInputStream ois =new ObjectInputStream(clientSocket.getInputStream());
                    serverMessage.obj = (String)ois.readObject();

                    mHandler.sendMessage(serverMessage);
                    oos.close();
                    ois.close();
                    clientSocket.close();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        m_objThreadClient.start();

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            messageDisplay(msg.obj.toString());
        }
    };
    public void messageDisplay(String servermessage) {


        Toast.makeText(getBaseContext(),"Client: "+servermessage,Toast.LENGTH_SHORT).show();

    }


    public void startServer(View view)
    {

        startServerButton.setEnabled(false); //avoids to start 2 Servers
        server= new MyServer();
        server.setEventListener(this);
        server.startListening();



    }
    public void Display(String message)
    {
        Toast.makeText(getBaseContext(),"Server: "+message,Toast.LENGTH_SHORT).show();
    }
    public void stopServer(View view){
        server.stop();
        startServerButton.setEnabled(true);
    }

}
