package Entities;

import Map.Edge;
import Media.EImage;

/**
 * Red Ghost (Ghost1).
 */
public class RedGhost extends Ghost {
    
    public RedGhost(Edge location, Game.GameState gamestate) {
        super(location, EGhostType.ghost1, gamestate);
    }

    @Override
    public void resetGhostImg() {
        setImage(EImage.ghost1_left);
    }
}
