package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


import java.util.Arrays;
import java.util.EnumMap;

import static java.lang.Integer.max;


public final class MinimaxPacman extends Controller<MOVE> {
    private final Controller<EnumMap<GHOST, MOVE>> ghostController;
    private final int maxDepth;
    private static final MOVE[] moves;

    static {
        moves = new MOVE[]{MOVE.UP, MOVE.RIGHT, MOVE.DOWN, MOVE.LEFT};
    }

    public MinimaxPacman(Controller<EnumMap<GHOST, MOVE>> ghostController, int maxDepth) {
        this.ghostController = ghostController;
        this.maxDepth = maxDepth;
    }


    private int evaluateScore(Game game) {
        int score = game.getScore();
        int distanceToNearestGhost = Integer.MAX_VALUE;
        int nearestGhostEdibleTime = 0;
        GHOST[] ghosts = GHOST.values();

        for (GHOST ghost : ghosts) {
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            int ghostDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ghostIndex);
            int ghostEdibleTime = game.getGhostEdibleTime(ghost);
            if (ghostDistance < distanceToNearestGhost) {
                distanceToNearestGhost = ghostDistance;
                nearestGhostEdibleTime = ghostEdibleTime;
            }
        }

        if (distanceToNearestGhost < 20 && nearestGhostEdibleTime == 0)
            return score - distanceToNearestGhost;
        else
            return score + distanceToNearestGhost;
    }

    private int minimax(MOVE move, int depth, Game game) {
        if (game.gameOver() || depth == this.maxDepth)
            return this.evaluateScore(game);

        if (move == null) {
            int maxScore = Integer.MIN_VALUE;

            for (MOVE c_move : moves) {
                int currentScore = this.minimax(c_move, depth + 1, game);
                maxScore = max(currentScore, maxScore);
            }

            return maxScore;
        } else {
            Game copy = game.copy();
            for (int i = 0; i < 10; i++)
                copy.advanceGame(move, this.ghostController.getMove(copy, -1));


            return this.minimax(null, depth, copy);
        }
    }

    public MOVE getMove(Game game, long timeDue) {
        int[] scores = new int[moves.length];
        for (int i = 0; i < moves.length; i++) {
            scores[i] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < moves.length; ++i) {
            Game copy = game.copy();

            for (int j = 0; j < 10; j++) {
                copy.advanceGame(moves[i], this.ghostController.getMove(copy, -1));
            }

            if (copy.getPacmanCurrentNodeIndex() != game.getPacmanCurrentNodeIndex()) {
                scores[i] = this.minimax((MOVE) null, 0, copy);
            }
        }

        int maxIndex = 0;
        int maxScore = scores[0];
        for(int i = 0; i < scores.length; i++)
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxIndex = i;
            }

        return moves[maxIndex];
    }
}
