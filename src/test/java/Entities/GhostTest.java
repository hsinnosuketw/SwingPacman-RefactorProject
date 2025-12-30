package Entities;

import Map.Edge;
import Map.Node;
import Settings.EParam;
import Settings.Settings;
import Media.EImage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import Game.GameState;
import Game.GameInputManager;
import Painter.Painter;

public class GhostTest {

    private GameState gamestate;

    @Before
    public void setUp() {
        // Initialize simple dependencies
        GameInputManager input = new GameInputManager();
        Painter painter = new Painter(input);
        gamestate = new GameState(painter);
        // Initialize Settings for Ghost dependencies
        Settings.set(EParam.ghost_speed, 3);
        Settings.set(EParam.ghost_vuln_speed, 2);
        Settings.set(EParam.ghost_vuln_val, 200);
        
        // Ghost.resetGhostImg accesses Media.getImg? No, just EImage enum usually, 
        // but let's check Ghost.java: setImage(EImage.ghost1_left) -> Sprite.setImage -> safe.
        // However, Sprite constructor calls getInitialAnimationFrame -> Media.getLegAnimation.
        // We assume Media is static safe or already initialized.
    }

    // Helper to create safe Edge
    private Edge createSafeEdge(Node n1, Node n2) {
        return new Edge(n1, n2, gamestate) {
            @Override
            protected void spawnFood() {
                // Do nothing to avoid Game.painter() NPE
            }
        };
    }

    @Test
    public void testGhostType() {
        Node n1 = new Node(0, 0);
        Node n2 = new Node(100, 0);
        Edge edge = createSafeEdge(n1, n2);
        
        RedGhost g = new RedGhost(edge, gamestate);
        
        assertEquals(EGhostType.ghost1, g.getType());
    }

    @Test
    public void testVulnerability() {
        Node n1 = new Node(0, 0);
        Node n2 = new Node(100, 0);
        Edge edge = createSafeEdge(n1, n2);
        RedGhost g = new RedGhost(edge, gamestate);
        
        // Initial state (vulnerability is static in Ghost class! shared across instances)
        // See Ghost.java: private static boolean vulnerable = false;
        // logic should probably set it to false explicitly to be sure
        g.setVulnerable(false);
        assertFalse(g.isVulnerable());
        
        g.setVulnerable(true);
        assertTrue(g.isVulnerable());
        
        // Check if speed changed?
        // Ghost.setVulnerable: 
        // if(vulnerable) setSpeed(ghost_vuln_speed) ...
        
        // We can check speed if there is getSpeed() (MovingEntity has it)
        assertEquals(2, g.getSpeed()); // 2 comes from setUp
        
        g.setVulnerable(false);
        assertEquals(3, g.getSpeed()); // 3 comes from setUp
    }
}
