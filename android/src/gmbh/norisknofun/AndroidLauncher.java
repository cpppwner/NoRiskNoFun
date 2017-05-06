package gmbh.norisknofun;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import gmbh.norisknofun.DiceRollTest.NoRiskNoFun_DiceRollTest;
import gmbh.norisknofun.Figures.NoRiskNoFun_DrawFigures;
import gmbh.norisknofun.GUI_Test.NoRiskNoFun_GUI_Test;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new NoRiskNoFun(), config);
	}
}
