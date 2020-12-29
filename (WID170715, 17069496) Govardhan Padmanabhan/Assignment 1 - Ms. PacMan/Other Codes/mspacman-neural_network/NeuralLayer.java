package game.controllers.pacman.examples;

import java.util.ArrayList;
import game.controllers.pacman.examples.Node;

public class NeuralLayer {
	int numNeurons;
	ArrayList<Node> nodeList = new ArrayList<Node>();
	
	//constructor
	public NeuralLayer(int numN, int inputsPerNeuron) {
		numNeurons = numN;
		for(int i = 0; i < numNeurons; ++i) {
			nodeList.add(new Node(inputsPerNeuron));
		}
	}
}
