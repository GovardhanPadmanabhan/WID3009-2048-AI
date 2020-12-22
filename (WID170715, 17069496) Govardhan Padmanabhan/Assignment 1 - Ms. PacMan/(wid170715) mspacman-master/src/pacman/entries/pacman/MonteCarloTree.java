package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Ghost;
import pacman.controllers.examples.StarterPacMan;
import pacman.controllers.examples.Legacy2TheReckoning;

import java.util.*;

public class MonteCarloTree {
	private Game game;
	@SuppressWarnings("rawtypes")
	private Controller ghosts;
	private Stack<Game> gameStates;
	private MonteCarloTreeNode rootNode;
	public static Random random = new Random();
	private Set<Integer> activePowerPills;
	
	
	// parameterized constructor to build an MCT based on the given game state
	public MonteCarloTree(Game game) {
		this.game = game;
		this.gameStates = new Stack<Game>();
		this.rootNode = new MonteCarloTreeNode();
		this.activePowerPills = new HashSet<Integer>();
		updateActivePowerPills(game.getActivePowerPillsIndices());
		
		this.ghosts = new Legacy2TheReckoning();
	}
	
	
	private void updateActivePowerPills(int[] indices) {
		for(int index : indices)
			activePowerPills.add(index);
	}
	
	
	public Game pushGameState() {
		gameStates.push(game);
		return game.copy();
	}
	
	
	public void popGameState() {
		game = gameStates.pop();
	}
	
	
	public Game getGameState() {
		return game;
	}
	
	
	public void setRootNode(MonteCarloTreeNode node) {
		rootNode = node;
	}
	
	
	// perform a Monte Carlo simulation from the current game state
	public void simulate() {
		List<MonteCarloTreeNode> visitedNodes = new ArrayList<MonteCarloTreeNode>();
		int lives = this.game.getPacmanNumberOfLivesRemaining();
		pushGameState();
		
		try {
			MonteCarloTreeNode node = rootNode;
			visitedNodes.add(node);
			
			while(!node.isLeafNode()) {
				node = new UCBTunedSelectionPolicy().selectChild(node);
				
				if(node == null)
					return;
				
				visitedNodes.add(node);
				
				game.advanceGame(node.getMove(), (EnumMap<GHOST, MOVE>) ghosts.getMove(game, 0));
				
				int[] indices = game.getActivePowerPillsIndices();
				if(indices.length != activePowerPills.size())
					updateActivePowerPills(indices);
				
				while(!game.isJunction(game.getPacmanCurrentNodeIndex()))
					game.advanceGame(WID170715_MCTS.nonJunctionSim(game), (EnumMap<GHOST, MOVE>) ghosts.getMove(game.copy(), 10));
				
				MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
				game.advanceGame(possibleMoves[random.nextInt(possibleMoves.length)], (EnumMap<GHOST, MOVE>) ghosts.getMove(game.copy(), 10));
			}
			
			if(node.getNumberOfVisits() >= 20 || node == rootNode) {
				node.expand(game);
				
				for (MonteCarloTreeNode child : node.getChildren()) {
					
					int powerPillCount = game.getNumberOfActivePowerPills();
					int pillCount = game.getNumberOfActivePills();
					int level = game.getCurrentLevel();
					MOVE move = child.getMove();
					
					pushGameState();
					
					game.advanceGame(move, (EnumMap<GHOST, MOVE>) ghosts.getMove(game, 0));
					
					int[] indices = game.getActivePowerPillsIndices();
					if(indices.length != activePowerPills.size())
						updateActivePowerPills(indices);
					
					while(!game.isJunction(game.getPacmanCurrentNodeIndex()))
						game.advanceGame(WID170715_MCTS.nonJunctionSim(game), (EnumMap<GHOST, MOVE>) ghosts.getMove(game.copy(), 10));
					
					MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
					game.advanceGame(possibleMoves[random.nextInt(possibleMoves.length)], (EnumMap<GHOST, MOVE>) ghosts.getMove(game.copy(), 10));
					
					if(game.getNumberOfActivePowerPills() < powerPillCount)
						child.setMoveEatsPowerPills(true);
					
					if(game.getNumberOfActivePills() < pillCount)
						child.setMoveEatsPills(true);
					
					int score = 0;
					
					if(game.getCurrentLevel() > level)
						score += 10000;
					
					score += runSimulation(visitedNodes, lives);
					child.updateScore(score);
					
					popGameState();
				}
				
				node = new UCBTunedSelectionPolicy().selectChild(node);
				if(node == null)
					return;
				
				visitedNodes.add(node);
				
				game.advanceGame(node.getMove(), (EnumMap<GHOST, MOVE>) ghosts.getMove(game, 0));
				
				int[] indices = game.getActivePowerPillsIndices();
				if(indices.length < activePowerPills.size())
					updateActivePowerPills(indices);
			}
			runSimulation(visitedNodes, lives);
			
		} finally {
			popGameState();
		}
	}
	
	
	// runs simulation for a node, and update its score based on the performance
	private int runSimulation(List<MonteCarloTreeNode> visitedNodes, int lives) {
		int score = 0;
		
		// death penalty
//		if(game.getPacmanNumberOfLivesRemaining() < lives) {
//			score -= 10000;
//		}
		
		score += exhaust();
		
		for(MonteCarloTreeNode n : visitedNodes)
			n.updateScore(score);
		
		return score;
	}
	
	
	//get the collection of children for the current game state
	public Collection<MonteCarloTreeNode> getPacManChildren() {
		Collection<MonteCarloTreeNode> children;
		children = rootNode.getChildren();
		
		if(children != null)
			return children;
		else
			return new ArrayList<MonteCarloTreeNode>();
	}
	
	
	// return the best scored node in the MCT
	public MonteCarloTreeNode bestNode() {
		double currentScore, max = Double.NEGATIVE_INFINITY;
		MonteCarloTreeNode bestNode = null;
		MonteCarloTreeNode searchNode;
		
		searchNode = rootNode;
		
		if(searchNode.getChildren() != null) {
			for(MonteCarloTreeNode node : searchNode.getChildren()) {
				currentScore = node.getAverageScore();
				
				if(currentScore > max) {
					max = currentScore;
					bestNode = node;
				}
			}
		}
		
		return bestNode;
	}
	
	
	// plays a game using a random pacman/ghost model till the end of the game
	private int exhaust() {
		int level = game.getCurrentLevel();
		int i=0;
		
		while (i++ < 10000 && !game.gameOver() && game.getCurrentLevel() == level) {
			game.advanceGame(new StarterPacMan().getMove(game, 0), (EnumMap<GHOST, MOVE>) ghosts.getMove(game, 0));
		}
		
		int score = game.getScore();
		
		return score;
	}
}