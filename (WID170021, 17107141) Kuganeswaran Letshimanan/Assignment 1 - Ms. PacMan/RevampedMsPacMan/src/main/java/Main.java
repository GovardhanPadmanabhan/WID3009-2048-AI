
import examples.StarterGhostComm.Blinky;
import examples.StarterGhostComm.Inky;
import examples.StarterGhostComm.Pinky;
import examples.StarterGhostComm.Sue;
import pacman.Executor;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples. AggressiveGhosts;
import pacman.controllers.examples.RevampedPacMan;
import pacman.controllers.examples.RevampedTreePacMan;
import pacman.controllers.examples.StarterPacMan;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.*;
import pacman.game.internal.POType;

import java.util.EnumMap;

public class Main {

    public static void main(String[] args) {

    	int delay=10;
		boolean visual=true;
		int numTrials=20;
		
    	Executor executor = new Executor();

        EnumMap<GHOST, IndividualGhostController> controllers = new EnumMap<>(GHOST.class);

        controllers.put(GHOST.INKY, new Inky());
        controllers.put(GHOST.BLINKY, new Blinky());
        controllers.put(GHOST.PINKY, new Pinky());
        controllers.put(GHOST.SUE, new Sue());

        AggressiveGhosts ghosts = new AggressiveGhosts();
   
        PacmanController pacMan = new RevampedPacMan(10);
        //PacmanController pacMan = new StarterPacMan();
        
        System.out.println("REVAMPED PACMAN vs AGGRESSIVE GHOSTS");
      //  System.out.println("STARTER PACMAN vs AGGRESSIVE GHOSTS");
        //  executor.runGame(pacMan, ghosts, true,10);
        executor.runExperiment(pacMan, ghosts,numTrials);
    }
}
