package gmbh.norisknofun.scene.game.figure;

import com.badlogic.gdx.graphics.Texture;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import gmbh.norisknofun.assets.impl.map.AssetMap;
import gmbh.norisknofun.scene.game.figures.Figure;

public class FigureTests {

    @Test
    public void creatingFigureWithEmptyConstructorWorks() {
        Figure figure = new Figure();

        assertNotNull(figure);
    }

    @Test
    public void creatingFigureWithArgumentsWorksCorrectly() {
        Figure figure = new Figure(1, 2, 3, 4);

        assertNotNull(figure);
    }

    @Test
    public void valuesGetCorrectlySetInConstructor() {
        Figure figure = new Figure(1, 2, 3, 4);

        assertEquals(1, (int) figure.getX());
        assertEquals(2, (int) figure.getY());
        assertEquals(3, (int) figure.getWidth());
        assertEquals(4, (int) figure.getHeight());
    }

    @Ignore
    @Test
    public void setHighlightedSetsVariableCorrectly() {
        Mockito.mock(Texture.class);

        Figure figure = new Figure();
        figure.setHighlighted(true);

        assertEquals(true, figure.isHighlighted());
    }

    @Test
    public void newFigureHasFirstMoveSet() {
        Figure figure = new Figure();

        assertEquals(true, figure.isFirstMove());
    }

    @Test
    public void newFigureIsNotHighlighted() {
        Figure figure = new Figure();

        assertEquals(false, figure.isHighlighted());
    }

    @Test
    public void setFirstMoveUpdatesFirstMove() {
        Figure figure = new Figure();

        figure.setFirstMove(false);

        assertEquals(false, figure.isFirstMove());
    }

    @Test
    public void getCurrentRegionReturnsCorrectRegion() {
        AssetMap.Region region = Mockito.mock(AssetMap.Region.class);
        Figure figure = new Figure();

        figure.setCurrentRegion(region);
        assertSame(region, figure.getCurrentRegion());
    }

    @Test
    public void newFigureHasNullRegion() {
        Figure figure = new Figure();

        assertNull(figure.getCurrentRegion());
    }

}
