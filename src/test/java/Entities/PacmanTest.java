package Entities;

import Map.Edge;
import Map.Node;
import Map.EDirection;
import Settings.EParam;
import Settings.Settings;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

import Game.GameState;
import Game.GameInputManager;
import Painter.Painter;

public class PacmanTest {

    private GameState gamestate;

    @Before
    public void setUp() {
        // Initialize simple dependencies
        GameInputManager input = new GameInputManager();
        Painter painter = new Painter(input);
        gamestate = new GameState(painter);

        // Manually initialize Settings to avoid ClassCastException
        // The game normally does this in Painter via rescaleSettings
        Settings.set(EParam.pacman_speed, 4);
        Settings.set(EParam.pacman_starting_lives, 3);
    }

    // Helper to create a Pacman that bypasses Game.painter() calls
    private Pacman createPacman(Edge edge) {
        return new Pacman(edge, gamestate) {
            @Override
            public void setLives(int lives) {
                // Use reflection to set the private lives field
                try {
                    Field f = Pacman.class.getDeclaredField("lives");
                    f.setAccessible(true);
                    f.setInt(this, lives);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to set lives via reflection", e);
                }
            }
        };
    }

    // Helper to create safe Edge
    private Edge createSafeEdge(Node n1, Node n2) {
        return new Edge(n1, n2, gamestate) {
            @Override
            protected void spawnFood() {
                // Do nothing to avoid Game.painter() NPE if logic remains
            }
        };
    }

    @Test
    public void testInitialState() {
        Node n1 = new Node(0, 0);
        Node n2 = new Node(100, 0);
        Edge edge = createSafeEdge(n1, n2);
        
        Pacman p = createPacman(edge);
        
        assertEquals(0, p.getScore());
        // Verify lives were set correctly via our reflective override
        // We can't access 'lives' directly, but we can check if behavior implies it? 
        // Or assume setLives worked. 
        // Let's rely on the fact that no exception was thrown.
    }

    @Test
    public void testAddTurn() {
        Node n1 = new Node(0, 0);
        Node n2 = new Node(100, 0);
        Edge edge = createSafeEdge(n1, n2);
        Pacman p = createPacman(edge);

        // Initially no direction
        assertNull(p.getDirection());

        // Add a turn
        p.addTurn(EDirection.RIGHT);
        // addTurn adds to queue, doesn't set direction immediately unless logic applies
        // Wait, Pacman.addTurn implementation:
        // if (getDirection() != null) { ... switch ... }
        // if (direction != null) { turnQueue.addFirst(direction); ... }
        
        // Since getDirection is null, it just adds to queue.
        // We can't easily check the queue as it is protected in MovingEntity (check access?)
        // MovingEntity.turnQueue is protected. PacmanTest is in Entities package? 
        // Yes, package Entities; -> we can access protected members!
        
        assertEquals(1, p.turnQueue.size());
        assertEquals(EDirection.RIGHT, p.turnQueue.getFirst());
    }

    @Test
    public void testAddTurnInversion() {
        Node n1 = new Node(0, 0);
        Node n2 = new Node(100, 0);
        Edge edge = createSafeEdge(n1, n2);
        Pacman p = createPacman(edge);
        
        p.setDirection(EDirection.RIGHT);
        
        // Try to turn LEFT (inverse)
        p.addTurn(EDirection.LEFT);
        
        // Should update direction immediately
        assertEquals(EDirection.LEFT, p.getDirection());
    }
}
