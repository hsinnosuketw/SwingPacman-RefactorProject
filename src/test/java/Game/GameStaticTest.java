package Game;

import org.junit.Test;
import static org.junit.Assert.*;

import Painter.Painter;

public class GameStaticTest {

    @Test
    public void testGameStateInstantiation() {
        // Now GameState requires dependencies, and should succeed when provided
        
        GameInputManager input = new GameInputManager();
        Painter painter = new Painter(input);
        
        GameState gameState = new GameState(painter);
        
        assertNotNull(gameState);
        assertNotNull(gameState.getMap());
    }
}
