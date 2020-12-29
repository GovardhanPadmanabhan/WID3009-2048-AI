package game.controllers.pacman.examples;

import java.util.ArrayList;
import game.controllers.pacman.examples.NeuralLayer;

public class NeuralNet {
	public int bias = -1;
	public int numInputs;
	public int numOutputs;
	public int numHiddenLay;
	public int neuronsPerHidden;
	public int fitness = 0;
	public ArrayList<NeuralLayer> neuralLayers = new ArrayList<NeuralLayer>();
	
	public void createNet() {
		if(numHiddenLay > 0) {
			//first hidden
			neuralLayers.add(new NeuralLayer(neuronsPerHidden, numInputs));
			for(int i=0; i < numHiddenLay-1; ++i) {
				neuralLayers.add(new NeuralLayer(neuronsPerHidden, neuronsPerHidden));
			}
			//output layer
			neuralLayers.add(new NeuralLayer(numOutputs, neuronsPerHidden));
		}
		else {
			neuralLayers.add(new NeuralLayer(numOutputs, numInputs));
		}
	}
	
	ArrayList<Float> getWeights(){
		ArrayList<Float> weights = new ArrayList<Float>();
		for(int i = 0; i < numHiddenLay+1; i++) {
			for(int j = 0; j < neuralLayers.get(i).numNeurons; ++j) {
				for(int k = 0; k < neuralLayers.get(i).nodeList.get(j).inputs; ++k) {
					weights.add(neuralLayers.get(i).nodeList.get(j).weights.get(k));
				}
			}
		}
		return weights;
	}
	
	void putWeights(ArrayList<Float> weights) {
		int weightI = 0;
		for(int i = 0; i < numHiddenLay+1; i++) {
			for(int j = 0; j < neuralLayers.get(i).numNeurons; ++j) {
				for(int k = 0; k < neuralLayers.get(i).nodeList.get(j).inputs; ++k) {
					neuralLayers.get(i).nodeList.get(j).weights.set(k, weights.get(weightI));
					weightI++;
				}
			}
		}
		return;
	}
	
	int getNumWeights() {
		int weights = 0;
		for(int i = 0; i < numHiddenLay + 1; i++) {
			for(int j = 0; j < neuralLayers.get(i).numNeurons; ++j) {
				for(int k = 0; k < neuralLayers.get(i).nodeList.get(j).inputs; ++k) {
					weights++;
				}
			}
		}
		return weights;
	}
	
	ArrayList<Float> update(ArrayList<Float> inputs){
		ArrayList<Float> outputs = new ArrayList<Float>();
		int weightI = 0;
		//check for correct number of inputs
		if(inputs.size() != numInputs) {
			//return empty array list
			System.out.println(inputs.size());
			System.out.println("AHHHHHH");
			return outputs;
		}
		for(int i = 0; i < numHiddenLay + 1; ++i) {
			if(i > 0) {
				inputs.addAll(outputs);
			}
			outputs.clear();
			weightI = 0;
			for(int j = 0; j < neuralLayers.get(i).numNeurons; ++j) {
				float netinput = 0;
				int numInputsA = neuralLayers.get(i).nodeList.get(j).inputs;
				for(int k = 0; k<numInputsA - 1; ++k) {
					netinput += neuralLayers.get(i).nodeList.get(j).weights.get(k) * inputs.get(weightI++);
				}
				//add the bias (might need to change later)
				netinput += neuralLayers.get(i).nodeList.get(j).weights.get(numInputsA-1) * bias;
				outputs.add((float) (1 / (1 + Math.exp(-netinput/0.5f))));
				weightI = 0;
			}
		}
		return outputs;
	}
}
