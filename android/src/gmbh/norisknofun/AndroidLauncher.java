package gmbh.norisknofun;


import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Log.d("ANDROID_Launcher","initialize");
		initialize(new NoRiskNoFun(getIpAddress()), config);
	}

	private String getIpAddress (){

		WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		@SuppressWarnings("deprecation")
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		Log.d("ANDROID_Launcher","IP:"+ip);
		return ip;
	}
}
