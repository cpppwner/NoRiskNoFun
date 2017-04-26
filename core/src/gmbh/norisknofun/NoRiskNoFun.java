package gmbh.norisknofun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import gmbh.norisknofun.Figures.Artillery;
import gmbh.norisknofun.Figures.Cavalry;
import gmbh.norisknofun.Figures.Figure;
import gmbh.norisknofun.Figures.Infantry;

public class NoRiskNoFun extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Stage stage;
    private int counter=0;


    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
        addFigurestoStage();
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){

                stage.getActors().get(counter).addAction(Actions.moveTo(x-50,y-50,1));
                counter++;
                if(counter==stage.getActors().size) counter=0;
                return true;
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose () {
        stage.dispose();
    }

    private void addFigurestoStage(){
        Figure figure;
        for(int i=0; i<3; i++){
            figure=new Infantry(200,200,100,100);
            stage.addActor(figure);

            figure=new Cavalry(200,200,100,100);
            stage.addActor(figure);

            figure=new Artillery(200,200,100,100);
            stage.addActor(figure);
        }


    }
}