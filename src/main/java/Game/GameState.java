package Game;

import Entities.EGhostType;
import Entities.EntityManager;
import Entities.Ghost;
import Entities.Pacman;
import Map.Edge;
import Map.Map;
import Settings.EParam;
import Settings.Settings;
import Entities.RedGhost;
import Settings.Settings;
import Entities.OrangeGhost;
import Entities.CyanGhost;
import Entities.PinkGhost;
import Painter.Painter;
import Game.GameThread;
import Game.GameThread;

import java.util.ArrayList;
import java.util.Random;

/**
 * Saves all the valuable stats and data about the game.
 */
public class GameState {
    private Painter painter;
    private GameThread gamethread;
    private Pacman pacman;
    private ArrayList<Ghost> ghosts = new ArrayList<>();
    private Map map;
    
    private int round = 1;
    
    /**
     * Initializes the GameState object.
     */
    /**
     * Initializes the GameState object.
     * @param painter the Painter object
     */
    public GameState(Painter painter) {
        this.painter = painter;
        
        // Initializes the Map.
        map = new Map(this);
        
        
        // Creates the Ghosts
        ArrayList<Edge> edges = map.getEdges();
        Random rand = new Random();
        
        for(int i = 1; i <= (int)Settings.get(EParam.ghost_count); i++) {
            Ghost g;
            switch(i%4) {
                case 0:
                    g = new OrangeGhost(edges.get(rand.nextInt(edges.size())), this);
                    break;
                case 1:
                    g = new RedGhost(edges.get(rand.nextInt(edges.size())), this);
                    break;
                case 2:
                    g = new PinkGhost(edges.get(rand.nextInt(edges.size())), this);
                    break;
                default: // 3
                    g = new CyanGhost(edges.get(rand.nextInt(edges.size())), this);
                    break;
            }
            ghosts.add(g);
            
            painter.registerSprite(g);
        }
    
        // Prevents Pacman to spawn at the same position as a Ghost
        boolean valid = false;
        while(!valid) {
            pacman = new Pacman(edges.get(rand.nextInt(edges.size())), this);
        
            valid = true;
            for(Ghost g : ghosts) {
                if(EntityManager.areColliding(g,pacman)) {
                    valid = false;
                    break;
                }
            }
        }
        painter.registerSprite(pacman);
    }
    
    /**
     * Takes all the MovingEntities and moves them randomly to a new Edge.
     */
    //TODO: Move method to EntityManager
    public void reshuffleEntityPositions() {
        System.out.println("Reshuffling");
        Random rand = new Random();
        ArrayList<Edge> edges = map.getEdges();
    
        Edge e;
        for (Ghost g : ghosts) {
            e = edges.get(rand.nextInt(edges.size()));
            g.setCurrEdge(e);
            g.setX(e.getFrom().getX());
            g.setY(e.getFrom().getY());
            g.resetEntity();
        }
    
        // Prevents Pacman to spawn at the same position as a Ghost
        boolean valid = false;
        while(!valid) {
            e = edges.get(rand.nextInt(edges.size()));
            pacman.setCurrEdge(e);
            pacman.setX(e.getFrom().getX());
            pacman.setY(e.getFrom().getY());
            
            valid = true;
            for(Ghost g : ghosts) {
                if(EntityManager.areColliding(g,pacman)) {
                    valid = false;
                    break;
                }
            }
        }
        pacman.resetEntity();
    }
    
    //////////////
    // Getters and Setters below
    public Map getMap(){
        return map;
    }
    
    public void removeGhost(Ghost g) {
        if (ghosts.contains(g)) ghosts.remove(g);
    }
    public void removePacman(Pacman g) {
        pacman = null;
    }
    public void setRound(int round){
        this.round = round;
        painter.updateRoundPanel(round);
    }
    
    public ArrayList<Ghost> getGhosts(){
        return ghosts;
    }
    
    public Pacman getPacman(){
        return pacman;
    }

    public int getRound() {return round; }
    
    /**
     * Makes all the Ghosts of the game vulnerable or not.
     * @param toVulnearble the new vulnerable value of the Ghosts
     */
    public void makeGhostsVulnerable(boolean toVulnearble) {
        for (Ghost g : ghosts) {
            g.setVulnerable(toVulnearble);
        }
    }
    
    public Painter getPainter() {
        return painter;
    }

    public void setGameThread(GameThread gamethread) {
        this.gamethread = gamethread;
    }

    public GameThread getGameThread() {
        return gamethread;
    }
}
