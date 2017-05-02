package gmbh.norisknofun;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends Activity  {

    private Socket clientSocket;
    private EditText etIp;
    private MyServer server;
    private Button startServerButton;
    public  Client client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etIp = (EditText) findViewById(R.id.etIp);
        etIp.setText("192.168.1.164");
        startServerButton = (Button) findViewById(R.id.btnstartserver);
    }
    public void onClickstartClient(View view)
    {

        String addr =etIp.getText().toString();
        startClient(addr);

    }
    public void onClickSendMessage(View view)
    {
        if(client!=null) {
            client.sendMessage(" Hallo");
        }
    }


    public void startClient(final String ip) {
        if(client==null) {

            client = new Client();
        }
            if(!client.isConnected()) {
                Thread m_objThread = new Thread(new Runnable() {
                    public void run() {
                        client.startCLient(ip);

                    }
                });
                m_objThread.start();

            } else {
                //already Connected
            }




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


    public void onClickstartServer(View view)
    {
        String addr =etIp.getText().toString();
        startServer(addr);

        startServerButton.setEnabled(false); //avoids to start 2 Servers


    }

    public void startServer(String ip) {


        server= new MyServer();
        server.startListening();
        startClient(ip);
    }


    public void stopServer(View view){
        if(server!=null) {
            server.stop();
        }
        startServerButton.setEnabled(true);
        client.setConnected(false);
    }

}
