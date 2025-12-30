package Entities;

import Map.Edge;
import Media.EImage;

/**
 * Orange Ghost (Ghost4).
 */
public class OrangeGhost extends Ghost {
    
    public OrangeGhost(Edge location, Game.GameState gamestate) {
        super(location, EGhostType.ghost4, gamestate);
    }

    @Override
    public void resetGhostImg() {
        setImage(EImage.ghost4_right);
    }
}
