package pacman.entries.pacman;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;


public class MonteCarloTreeNode {
	private MOVE move;
	private MonteCarloTreeNode parent;
	private int numberOfVisits;
	private int scoreBonus;
	private Map<Object, MonteCarloTreeNode> children;
	private double mean;
	private boolean moveEatsPowerPill;
	private boolean moveEatsPills;
	
	
	// default constructor
	public MonteCarloTreeNode() {
		this.numberOfVisits = 0;
		this.move = MOVE.NEUTRAL;
		this.scoreBonus = 0;
		this.parent = null;
	}
	
	
	// parameterized constructor, for children nodes
	public MonteCarloTreeNode(MonteCarloTreeNode parent, MOVE move) {
		this.parent = parent;
		this.move = move;
	}
	
	
	// update the mean score and number of visits at the node
	public void updateScore(int score) {
		this.mean = (this.mean * this.numberOfVisits + score)/(this.numberOfVisits + 1);
		this.numberOfVisits += 1;
	}
	
	
	// add bonus score
	public void addScoreBonus(int bonus) {
		this.scoreBonus += bonus;
	}
	
	
	// expand the game from the current node, based on the set of moves available
	public void expand(Game game) {
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		
		this.children = new HashMap<Object, MonteCarloTreeNode>(possibleMoves.length);
		
		for(MOVE move : possibleMoves)
			this.children.put(move, new MonteCarloTreeNode(this, move));
	}
	
	
	// returns whether the node is a leaf node or not
	public boolean isLeafNode() {
		return this.children == null;
	}
	
	
	// gets the move contained in the node
	public MOVE getMove() {
		return this.move;
	}
	
	
	// gets the number of visits to this node
	public int getNumberOfVisits() {
		return this.numberOfVisits;
	}
	
	
	// gets the children of this node
	public Collection<MonteCarloTreeNode> getChildren() {
		if (this.children == null)
			return null;
		return this.children.values();
	}
	
	
	// gets the parent of this node
	public MonteCarloTreeNode getParent() {
		return this.parent;
	}
	
	
	// gets the average score of the node
	public double getAverageScore() {
		if(this.numberOfVisits > 0)
			return this.mean + this.scoreBonus;
		else
			return this.scoreBonus;
	}
	
	
	// returns whether the current move eats any power pills
	public boolean isMoveEatsPowerPill() {
		return this.moveEatsPowerPill;
	}
	
	
	// set indicator variable to true if the current move eats any power pills, false otherwise
	public void setMoveEatsPowerPills(boolean moveEatsPowerPill) {
		this.moveEatsPowerPill = moveEatsPowerPill;
	}
	
	
	// returns whether the current move eats any pills
	public boolean isMoveEatsPills() {
		return this.moveEatsPills;
	}
	
	
	// set indicator variable to true if the current move eats any pills, false otherwise
	public void setMoveEatsPills(boolean moveEatsPills) {
        this.moveEatsPills = moveEatsPills;
    }
	
	
	// set indicator variable to true if any of the children has moves that will eat pills, false otherwise
	public boolean isEatPillsInFuture() {
		if(this.children == null)
			return false;
		for(MonteCarloTreeNode child : this.children.values()) {
			if(child.isMoveEatsPills())
				return true;
		}
		
		return false;
	}
}