package gmbh.norisknofun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class NoRiskNoFun_DiceRollTest extends Game {

    // make batch and font public, so they can be used throughout the game
    public SpriteBatch batch;
    public BitmapFont font;

    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;


    @Override
    public void create() {

        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        SCREEN_WIDTH = Gdx.graphics.getWidth();

        // generate a bitmap font from a true type font
        // Gdx.files.internal points to asset folder
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Top_Secret.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = (int)(SCREEN_WIDTH*0.05);
        font = generator.generateFont(fontParameter);
        font.setColor(Color.WHITE);

        batch = new SpriteBatch();

        // change screen to dice roll
        this.setScreen(new DiceRollTest(this));
    }

    @Override
    public void render() {
        // IMPORTANT! otherwise the screen set in create() won't render
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }


}