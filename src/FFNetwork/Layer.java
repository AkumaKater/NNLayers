package FFNetwork;

public class Layer {

	int numInputNodes, numOutputNodes;
	double[][] weights;

    double[] inputs;
    double[] weightedInputs;
    double[] activations;

	public Layer(int numInputNodes, int numOutputNodes) {
		this.numInputNodes = numInputNodes;
		this.numOutputNodes = numOutputNodes;
		weights = NNMath.RandomDoubleArrayMatrix(numOutputNodes,numInputNodes);
	}

    public double[] CalculateOutputs(double[] inputs) {
        this.inputs = inputs;
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            weightedInputs[nodeOut] =
        }


        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            double weightedInput = 0;
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                weightedInput += inputs[nodeIn] * weights[nodeOut][nodeIn];
            }
            inputs[nodeOut] = weightedInput;
        }

        // Apply activation function
        for (int i = 0; i < activations.length; i++) {
            activations[i] = Activations.Sigmoid(this.inputs.getValue(i));
        }

        return activations;
    }
}