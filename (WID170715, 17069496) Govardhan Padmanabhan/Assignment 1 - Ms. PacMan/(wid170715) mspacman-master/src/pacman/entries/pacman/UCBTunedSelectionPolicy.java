package pacman.entries.pacman;

/**
 * Computes UCB and selects the children
 */

public class UCBTunedSelectionPolicy {
	
	private static final double DEFAULT_BALANCE_PARAMETER = 10000;
	private double balanceParameter;
	
	
	// default constructor
	public UCBTunedSelectionPolicy() {
		this.balanceParameter = DEFAULT_BALANCE_PARAMETER;
	}
	
	
	// select the best child after performing UCB calculations
	public MonteCarloTreeNode selectChild(MonteCarloTreeNode node) {
		MonteCarloTreeNode selectedChild = null;
		
		double max = Double.NEGATIVE_INFINITY;
		double currentUCB;
		
		for(MonteCarloTreeNode child : node.getChildren()) {
			currentUCB = getUCBValue(child);
			
			if(currentUCB > max)  {
				max = currentUCB;
				selectedChild = child;
			}
		}
		
		if(selectedChild == null)
			throw new IllegalStateException("Child cannot be selected in a leaf node!");
		
		return selectedChild;
	}
	
	
	// function to calculate the UCB value
	public double getUCBValue(MonteCarloTreeNode node) {
		return node.getAverageScore() + 1 *
                Math.sqrt(2*Math.log(node.getParent().getNumberOfVisits()) / node.getNumberOfVisits()*Math.min(0.25, node.getNumberOfVisits()));
	}
}