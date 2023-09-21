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
        weights = NNMath.RandomDoubleArrayMatrix(numOutputNodes, numInputNodes);

        inputs = new double[numInputNodes];
        weightedInputs = new double[numOutputNodes];
        activations = new double[numOutputNodes];
    }

    public double[] CalculateOutputs(double[] inputs) {
        this.inputs = inputs;
        Activation activ = Activation.geActivation();
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            double weightedInput = 0;
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                weightedInput += inputs[nodeIn] * weights[nodeOut][nodeIn];
            }
            weightedInputs[nodeOut] = weightedInput;
            activations[nodeOut] = activ.ActivationFunction(weightedInput);
        }
        return activations;
    }

    private double NodeCostDerivative(double activation, double expectedOutput) {
        return 2 * (activation - expectedOutput);
    }

    public double[] CalculateOutputLayerNodeValues(double[] expectedOutputs) {
        double[] nodeValues = new double[expectedOutputs.length];
        for (int i = 0; i < nodeValues.length; i++) {
            double costDerivative = NodeCostDerivative(activations[i], expectedOutputs[i]);
            double activationDerivative = Activation.geActivation().ActivationDerivative(weightedInputs[i]);
            nodeValues[i] = activationDerivative * costDerivative;
        }
        return nodeValues;
    }
}