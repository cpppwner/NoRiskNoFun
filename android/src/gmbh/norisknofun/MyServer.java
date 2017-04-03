package gmbh.norisknofun;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

 class MyServer {

	private ServerSocket m_server;
	private DataDisplay m_dataDisplay;
	private boolean isStopped = false;
	private boolean error=false;

	MyServer()
	{
		try {
			m_server = new ServerSocket(2001);
		} catch (IOException e) {
			error=true;
			e.printStackTrace();
		}
	}
	void setEventListener(DataDisplay dataDisplay)
	{
		m_dataDisplay=dataDisplay;
	}
	void startListening()
	{

		Thread m_objThread = new Thread(new Runnable() {
				public void run() {
					while(!isStopped()&& !error) {
						try {

							Socket connectedSocket = m_server.accept();
							Log.d("Server:","wait");
							Message clientmessage = Message.obtain();
							ObjectInputStream ois = new ObjectInputStream(connectedSocket.getInputStream());
							clientmessage.obj = (String) ois.readObject();
							mHandler.sendMessage(clientmessage);
							ObjectOutputStream oos = new ObjectOutputStream(connectedSocket.getOutputStream());
							oos.writeObject("Server response...");
							ois.close();
							oos.close();

						} catch (Exception e) {
							Message msg3 = Message.obtain();
							msg3.obj = e.getMessage();
							mHandler.sendMessage(msg3);
						}
					}
				}
			});

			m_objThread.start();
		//}

	}
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message status) {
			m_dataDisplay.Display(status.obj.toString());
		}
	};

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop(){
		this.isStopped = true;

		try {
			this.m_server.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}
}
