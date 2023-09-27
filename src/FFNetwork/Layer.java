package FFNetwork;

public class Layer {

    int numInputNodes, numOutputNodes;
    double[][] weights;
    //--> Steigung der Cost Funktion im Bezug auf das Gewicht W
    double[][] CostSteigungW;

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

    private double CostAbleitung(double activation, double expectedOutput) {
        return 2 * (activation - expectedOutput);
    }

    public double[] CalculateOutputLayerNodeValues(double[] expectedOutputs) {
        double[] nodeValues = new double[expectedOutputs.length];
        for (int i = 0; i < nodeValues.length; i++) {
            double costDerivative = CostAbleitung(activations[i], expectedOutputs[i]);
            double activationAbleitung = Activation.geActivation().ActivationAbleitung(weightedInputs[i]);
            nodeValues[i] = activationAbleitung * costDerivative;
        }
        return nodeValues;
    }

    public double[] CalculateHiddenLayerNodeValues(Layer oldLayer, double[] nodeValues) {
        double[] newNodeValues = new double[numOutputNodes];
        for(int i=0; i < numOutputNodes; i++){
            for(int j=0; j < nodeValues.length; j++){
                newNodeValues[i] += nodeValues[j]*oldLayer.weights[i][j];
            } 
        }

        for (int i = 0; i < weightedInputs.length; i++) {
            newNodeValues[i] *= Activation.geActivation().ActivationAbleitung(weightedInputs[i]);
        }
        return newNodeValues;
    }

    public void UpdateGradients(double[] nodeValues) {
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                double derivativeCostWrtWeight = inputs[nodeIn] * nodeValues[nodeOut];
                CostSteigungW[nodeIn][nodeOut] += derivativeCostWrtWeight;
            }
        }
    }
}