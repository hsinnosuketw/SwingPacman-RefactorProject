package Entities;

import Map.Edge;
import Media.EImage;

/**
 * Pink Ghost (Ghost2).
 */
public class PinkGhost extends Ghost {
    
    public PinkGhost(Edge location, Game.GameState gamestate) {
        super(location, EGhostType.ghost2, gamestate);
    }

    @Override
    public void resetGhostImg() {
        setImage(EImage.ghost2_right);
    }
}
