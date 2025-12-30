package Game;


import Media.Media;
import Painter.Painter;

/**
 * The main class that starts the game.
 */
public class Game {
    private GameState gamestate;
    private Painter painter;
    
    private GameThread gamethread;
    private GameInputManager gameinput;
    
    /**
     * Starts the Game.
     */
    public Game() {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        
        gameinput = new GameInputManager();
        painter = new Painter(gameinput);
    
        gamestate = new GameState(painter);
    
        gamethread = new GameThread(gamestate);
        gamestate.setGameThread(gamethread);
        
        gameinput.setGameState(gamestate);
        gameinput.setGameThread(gamethread);
        
        painter.registerMap(gamestate.getMap());
    
        Thread t = new Thread(gamethread);
        t.start();
    }
    
    public static void main(String[] args){
        new Game();
    }
}


