package pacman.controllers.examples;

import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;

public class RevampedTreePacMan extends PacmanController {

    private final int MAX_STEP;

    public RevampedTreePacMan(int max_step) { MAX_STEP = max_step; }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        return searchMove(game);
    }

    /**
     * Search for next move
     * @param game pacman.game.Game
     * @return MOVE next move
     */
    private MOVE searchMove(Game game) {
        GameInfo info = game.getPopulatedGameInfo();
        info.fixGhosts((ghost) -> new Ghost(
                ghost,
                game.getCurrentMaze().lairNodeIndex,
                -1,
                -1,
                MOVE.NEUTRAL
        ));
        Game gameClone = game.getGameFromInfo(info);

        MOVE bestMove = MOVE.NEUTRAL,
             lastMove = gameClone.getPacmanLastMoveMade();
        int bestScore = Integer.MIN_VALUE,
            pacMan = gameClone.getPacmanCurrentNodeIndex();
        int score;

        MOVE[] moves = ghostIsNear(game)
            ? game.getPossibleMoves(pacMan)
            : game.getPossibleMoves(pacMan, lastMove);

        for (MOVE move: moves) {
            score = evaluateMove(move, gameClone.copy(), 0);
            if (score > bestScore) {
                bestScore = score;
                bestMove  = move;
            }
        }
        return bestMove;
    }

    /**
     * Check if any ghost is within 20 unit
     * @param game pacman.game.Game
     * @return boolean true if there is any ghost nearby otherwise false
     */
    private boolean ghostIsNear(Game game) {
        int node, distance;
        int pacMan = game.getPacmanCurrentNodeIndex();
        for (GHOST ghost: GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                node = game.getGhostCurrentNodeIndex(ghost);
                distance = game.getShortestPathDistance(pacMan, node);
                if (distance < 20) return true;
            }
        }
        return false;
    }

    /**
     * Forward the PacMan until reach a junction or eaten
     * @param move pacman.game.Constants.MOVE
     * @param game pacman.game.Game
     */
    private void forwardPacMan(MOVE move, Game game) {
        MASController ghosts = new POCommGhosts(50);
        do {
            game.advanceGame(move, ghosts.getMove(game.copy(), 40));
        } while (!game.isJunction(game.getPacmanCurrentNodeIndex()) && !game.wasPacManEaten());
    }

    /**
     * Evaluate the score of current move
     * @param move pacman.game.Constants.MOVE
     * @param game pacman.game.Game
     * @param step tracker
     * @return int score of given move on current game
     */
    private int evaluateMove(MOVE move, Game game, int step) {
        forwardPacMan(move, game);

        if (step == MAX_STEP || game.wasPacManEaten())
            return game.getScore();

        int bestScore = Integer.MIN_VALUE;
        int pacMan = game.getPacmanCurrentNodeIndex();
        for (MOVE nextMove: game.getPossibleMoves(pacMan, game.getPacmanLastMoveMade()))
            bestScore = Math.max(
                bestScore,
                evaluateMove(nextMove, game.copy(), step + 1)
            );

        return bestScore;
    }
}
