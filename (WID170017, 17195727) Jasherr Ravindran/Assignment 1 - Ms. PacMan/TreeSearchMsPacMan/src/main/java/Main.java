
import examples.StarterGhostComm.Blinky;
import examples.StarterGhostComm.Inky;
import examples.StarterGhostComm.Pinky;
import examples.StarterGhostComm.Sue;
import pacman.Executor;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples.RevampedPacMan;
import pacman.controllers.examples.RevampedTreePacMan;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.*;
import pacman.game.internal.POType;

import java.util.EnumMap;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {

    public static void main(String[] args) {

        Executor executor = new Executor();

        EnumMap<GHOST, IndividualGhostController> controllers = new EnumMap<>(GHOST.class);

        controllers.put(GHOST.INKY, new Inky());
        controllers.put(GHOST.BLINKY, new Blinky());
        controllers.put(GHOST.PINKY, new Pinky());
        controllers.put(GHOST.SUE, new Sue());

       Legacy2TheReckoning ghosts = new Legacy2TheReckoning();
        
        PacmanController pacMan = new RevampedTreePacMan(5);
        executor.runGame(pacMan, ghosts, true,10);
        //executor.runExperiment(pacMan, ghosts, 10);
    }
}
