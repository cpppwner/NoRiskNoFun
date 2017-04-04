package gmbh.norisknofun;

import android.os.Looper;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    MainActivity activity;
    @Before
    public void init(){
        Looper.prepare();
        activity = new MainActivity();


    }

    //https://google.github.io/android-testing-support-library/docs/espresso/ für UI Tests
    //http://www.vogella.com/tutorials/AndroidTesting/article.html


    /*
    @Test
    public void testServerStart() throws Exception {

        activity.startServer();
        String ip = "127.0.01";
    }
    */
    @Test
    public void testServerStartandConnect() throws Exception {

        activity.startServer();
        String ip = "127.0.01";
        activity.startClient(ip);


    }



}