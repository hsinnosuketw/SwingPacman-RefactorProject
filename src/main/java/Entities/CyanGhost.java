package Entities;

import Map.Edge;
import Media.EImage;

/**
 * Cyan Ghost (Ghost3).
 */
public class CyanGhost extends Ghost {
    
    public CyanGhost(Edge location, Game.GameState gamestate) {
        super(location, EGhostType.ghost3, gamestate);
    }

    @Override
    public void resetGhostImg() {
        setImage(EImage.ghost3_right);
    }
}
