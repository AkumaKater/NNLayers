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

        inputs = new double[numInputNodes];
        weightedInputs = new double[numOutputNodes];
        activations = new double[numOutputNodes];
	}

    public double[] CalculateOutputs(double[] inputs) {
        this.inputs = inputs;
        Activation activ = Activation.geActivation();
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            double weightedInput = 0;
            for(int nodeIn = 0; nodeIn<numInputNodes; nodeIn++){
                weightedInput += inputs[nodeIn] * weights[nodeOut][nodeIn];
            }
            weightedInputs[nodeOut] = weightedInput;
            activations[nodeOut] = activ.ActivationFunction(weightedInput);
        }
        return activations;
    }
}