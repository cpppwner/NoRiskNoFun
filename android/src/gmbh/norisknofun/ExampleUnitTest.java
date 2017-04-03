package gmbh.norisknofun;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before

    //https://google.github.io/android-testing-support-library/docs/espresso/ für UI Tests
    //http://www.vogella.com/tutorials/AndroidTesting/article.html



    @Test
    public void testServerStart() throws Exception {
        MyServer server;
        server= new MyServer();
        //server.setEventListener(this);
        server.startListening();





    }

}