package gmbh.norisknofun.guitest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class NoRiskNoFunGUITest extends Game implements Screen{
    private Stage menu;
    private Texture img;




    @Override
    public void show() {

        Texture imgbtn;
        TextureRegion imgbtnregion;
        TextureRegionDrawable textureRegionDrawable;
        ImageButton imageButtonCreate;
        ImageButton imageButtonJoin;

        menu = new Stage();
        Gdx.input.setInputProcessor(menu);

        img = new Texture("menu.png");
        imgbtn = new Texture(Gdx.files.internal("button_create_game_eng.png"));
        imgbtnregion= new TextureRegion(imgbtn);
        textureRegionDrawable = new TextureRegionDrawable(imgbtnregion);
        imageButtonCreate= new ImageButton(textureRegionDrawable);
        imageButtonCreate.setBounds((Gdx.graphics.getWidth()/6)-5,(Gdx.graphics.getHeight()/3),553,480);
        menu.addActor(imageButtonCreate);

        imageButtonCreate.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new CreateGameScreen());
            }
        });

        imgbtn = new Texture(Gdx.files.internal("button_join_game_eng.png"));
        imgbtnregion= new TextureRegion(imgbtn);
        textureRegionDrawable = new TextureRegionDrawable(imgbtnregion);
        imageButtonJoin= new ImageButton(textureRegionDrawable);
        imageButtonJoin.setBounds((Gdx.graphics.getWidth()/2)+10,(Gdx.graphics.getHeight()/3),553,480);
        menu.addActor(imageButtonJoin);

        imageButtonJoin.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new JoinGameScreen());
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menu.getBatch().begin();
        menu.getBatch().draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        menu.getBatch().end();
        menu.draw();
    }

    @Override
    public void create() {
        //null
    }

    @Override
    public void resize(int width, int height) {
        //null
    }

    @Override
    public void hide() {
        //null
    }

    @Override
    public void dispose () {
        menu.dispose();
        img.dispose();
    }

}