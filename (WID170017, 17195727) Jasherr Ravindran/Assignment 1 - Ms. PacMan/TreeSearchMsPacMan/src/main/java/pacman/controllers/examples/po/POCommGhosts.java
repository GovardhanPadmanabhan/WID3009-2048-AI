package pacman.controllers.examples.po;

import com.fossgalaxy.object.annotations.ObjectDef;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.game.Constants.GHOST;

import java.util.EnumMap;

/**
 * Created by pwillic on 25/02/2016.
 */
public class POCommGhosts extends MASController {

    public POCommGhosts() {
        this(50);
    }

    @ObjectDef("POGC")
    public POCommGhosts(int TICK_THRESHOLD) {
        super(true, new EnumMap<GHOST, IndividualGhostController>(GHOST.class));
        controllers.put(GHOST.BLINKY, new POCommGhost(GHOST.BLINKY, TICK_THRESHOLD));
        controllers.put(GHOST.INKY, new POCommGhost(GHOST.INKY, TICK_THRESHOLD));
        controllers.put(GHOST.PINKY, new POCommGhost(GHOST.PINKY, TICK_THRESHOLD));
        controllers.put(GHOST.SUE, new POCommGhost(GHOST.SUE, TICK_THRESHOLD));
    }

    @Override
    public String getName() {
        return "POGC";
    }
}

