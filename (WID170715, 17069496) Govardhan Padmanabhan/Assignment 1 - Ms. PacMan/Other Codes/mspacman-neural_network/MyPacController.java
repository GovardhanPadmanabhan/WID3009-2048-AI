package game.controllers.pacman.examples;


import java.util.ArrayList;
import game.controllers.pacman.PacManHijackController;
import game.core.G;
import game.core.Game;
import game.controllers.pacman.examples.NeuralNet;

public class MyPacController extends PacManHijackController{
	NeuralNet myNet = new NeuralNet();
	ArrayList<Float> inputs = new ArrayList<Float>();
	int q = 0;
	int fitness = 0;
	int curLev = 0;
	int numPPrev = 0;
	int tmpP = 0;
	boolean showDirs = false;
	
	public MyPacController(NeuralNet myNetP, boolean dirsB) {
		myNet = myNetP;
		showDirs = dirsB;
	}
	public void tick(Game game, long timeDue) {
		makeInputs(game);
		ArrayList<Float> outputs = myNet.update(inputs);
		//System.out.println(outputs);
		
		myNet.fitness = (game.getNumberPills() + game.getNumberPowerPills()) - (game.getNumActivePills() + game.getNumActivePowerPills()) + numPPrev;
		if(game.getCurLevel() > curLev) {
			numPPrev += tmpP;
			curLev = game.getCurLevel();
		}
		tmpP = (game.getNumberPills() + game.getNumberPowerPills()) - (game.getNumActivePills() + game.getNumActivePowerPills());
		
		//myNet.fitness = game.getScore();
		//if(game.getCurLevel() > 1) {
		//	myNet.fitness += game.getCurLevel() * 500;
		//}
		int highestSpot = -1;
		float highest = -1;
		int forward = game.getCurPacManDir();
		if(forward == 4) {
			forward--;
		}
		int right = 0;
		int left = 0;
		int backward = 0;
		if(forward == Game.UP) {
			right = Game.RIGHT;
			left = Game.LEFT;
			backward = Game.DOWN;
		}
		if(forward == Game.RIGHT) {
			right = Game.DOWN;
			left = Game.UP;
			backward = Game.LEFT;
		}
		if(forward == Game.LEFT) {
			right = Game.UP;
			left = Game.DOWN;
			backward = Game.RIGHT;
		}
		if(forward == Game.DOWN) {
			right = Game.LEFT;
			left = Game.RIGHT;
			backward = Game.UP;
		}
		//int[] adj = game.getPacManNeighbours();
		//Find the highest move
		for(int i = 0; i < outputs.size(); i++) {
			if(outputs.get(i) > highest) {
				highest = outputs.get(i);
				highestSpot = i;
			}
		}
		if(highestSpot == 0) {
			pacman.set(forward);
			if(showDirs) {
				System.out.println("UP");
			}
		}
		if(highestSpot == 1) {
			pacman.set(right);
			if(showDirs) {
				System.out.println("RIGHT");
			}
		}
		if(highestSpot == 2) {
			pacman.set(left);
			if(showDirs) {
				System.out.println("DOWN");
			}
		}
		if(highestSpot == 3) {
			pacman.set(backward);
			if(showDirs) {
				System.out.println("LEFT");
			}
		}
		if(highestSpot == -1) {
			System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		}
	} 
	
	/*void makeNet() {
		myNet.numInputs = 11;
		myNet.numOutputs = 4;
		myNet.numHiddenLay = 4;
		myNet.neuronsPerHidden = 15;
		myNet.createNet();
	}*/
	
	public void makeInputs(Game game) {
		inputs.clear();
		
		//closest ghost
		int ghost0Loc = game.getCurGhostLoc(0);
		int ghost1Loc = game.getCurGhostLoc(1);
		int ghost2Loc = game.getCurGhostLoc(2);
		int ghost3Loc = game.getCurGhostLoc(3);
		int closestGhost = 0;
		int dist0 = game.getPathDistance(game.getCurPacManLoc(), ghost0Loc);
		int dist1 = game.getPathDistance(game.getCurPacManLoc(), ghost1Loc);
		int dist2 = game.getPathDistance(game.getCurPacManLoc(), ghost2Loc);
		int dist3 = game.getPathDistance(game.getCurPacManLoc(), ghost3Loc);
		int distGhost = 0;
		if(dist0 >= dist1 && dist0 >= dist2 && dist0 >= dist3) {
			closestGhost = 0;
			distGhost = dist0;
		}
		if(dist1 >= dist0 && dist1 >= dist2 && dist1 >= dist3) {
			closestGhost = 1;
			distGhost = dist1;
		}
		if(dist2 >= dist1 && dist2 >= dist0 && dist2 >= dist3) {
			closestGhost = 2;
			distGhost = dist2;
		}
		if(dist3 >= dist1 && dist3 >= dist2 && dist3 >= dist0) {
			closestGhost = 3;
			distGhost = dist3;
		}
		if(distGhost <= 0) {
			inputs.add(1f);
		}
		if(distGhost > 0) {
			inputs.add(1/(float)distGhost);
		}
		
		//closest pill
		
		int distPill = 100000000;
		int[] pIndex1 = game.getPillIndicesActive();
		/*
		for(int i = 0; i < pIndex1.length; i++) {
			if(game.getPathDistance(game.getCurPacManLoc(), pIndex1[i]) < distPill) {
				distPill = game.getPathDistance(game.getCurPacManLoc(), pIndex1[i]);
			}
		}
		if(distPill <= 0) {
			inputs.add(1f);
		}
		if(distPill > 0) {
			inputs.add(1/(float)distPill);
		}
		
		//closest junction
		int distJunc = 100000000;
		int[] jIndex1 = game.getJunctionIndices();
		for(int i = 0; i < jIndex1.length; i++) {
			if(game.getPathDistance(game.getCurPacManLoc(), jIndex1[i]) < distJunc) {
				distJunc = game.getPathDistance(game.getCurPacManLoc(), jIndex1[i]);
			}
		}
		if(distJunc <= 0) {
			inputs.add(1f);
		}
		if(distJunc > 0) {
			inputs.add(1/(float)distJunc);
		}
		*/
		//closest power pill
		int distPPill = 100000000;
		int[] ppIndex1 = game.getPillIndicesActive();
		for(int i = 0; i < ppIndex1.length; i++) {
			if(game.getPathDistance(game.getCurPacManLoc(), ppIndex1[i]) < distPPill) {
				distPPill = game.getPathDistance(game.getCurPacManLoc(), ppIndex1[i]);
			}
		}		
		if(distPPill <= 0) {
			inputs.add(1f);
		}
		if(distPPill > 0) {
			inputs.add(1/(float)distPPill);
		}
		/*
		//pills collected 0 = 0%, 1 = 100%
		int numCollec = (game.getNumberPills() + game.getNumberPowerPills()) - (game.getNumActivePills() + game.getNumActivePowerPills());
		int totalNum = game.getNumberPills() + game.getNumberPowerPills();
		float percC = ((float)numCollec/(float)totalNum);
		inputs.add(percC);
		*/
		//If pacman is moving towards closest pill
		int[] targetsArray=new int[pIndex1.length+ppIndex1.length];
		for(int k=0;k<pIndex1.length;k++) {
			targetsArray[k]=pIndex1[k];
		}
		for(int k=0;k<ppIndex1.length;k++) {
			targetsArray[pIndex1.length+k]=ppIndex1[k];		
		}
		int dirPill = (game.getNextPacManDir(game.getTarget(game.getCurPacManLoc(),targetsArray,true,G.DM.PATH),true,Game.DM.PATH));	
		if(dirPill == game.getCurPacManDir()) {
			inputs.add(1f);
		}
		if(dirPill != game.getCurPacManDir()) {
			inputs.add(0f);
		}
		
		//get pacman neightbors, if its a legal move, 1, otherwise 0
		int[] legalDirs = game.getPacManNeighbours();
		int forward = game.getCurPacManDir();
		if(forward == 4) {
			forward--;
		}
		int right = 0;
		int left = 0;
		int backward = 0;
		if(forward == Game.UP) {
			right = Game.RIGHT;
			left = Game.LEFT;
			backward = Game.DOWN;
		}
		if(forward == Game.RIGHT) {
			right = Game.DOWN;
			left = Game.UP;
			backward = Game.LEFT;
		}
		if(forward == Game.LEFT) {
			right = Game.UP;
			left = Game.DOWN;
			backward = Game.RIGHT;
		}
		if(forward == Game.DOWN) {
			right = Game.LEFT;
			left = Game.RIGHT;
			backward = Game.UP;
		}
		if(legalDirs[forward] <= 0) {
			inputs.add(0f);
		}
		if(legalDirs[forward] > 0) {
			inputs.add(1f);
		}
		if(legalDirs[right] <= 0) {
			inputs.add(0f);
		}
		if(legalDirs[right] > 0) {
			inputs.add(1f);
		}
		if(legalDirs[left] <= 0) {
			inputs.add(0f);
		}
		if(legalDirs[left] > 0) {
			inputs.add(1f);
		}
		if(legalDirs[backward] <= 0) {
			inputs.add(0f);
		}
		if(legalDirs[backward] > 0) {
			inputs.add(1f);
		}
		
		//If closest ghost is edible
		if(game.isEdible(closestGhost)) {
			inputs.add(1f);
		}
		if(!game.isEdible(closestGhost)) {
			inputs.add(0f);
		}
		
	}
	
	public static void main(String[] args) {	
		//PacManSimulator.play(new MyPacController(), new GameGhosts(false));
	}
}
