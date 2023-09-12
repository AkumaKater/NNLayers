package JanNN;

public class Layer {

    int numInputNodes, numOutputNodes;
    Matrix weights;
    Matrix costGradientW;
    Vektor costGradientB;

    Vektor weightedInputs;
    Vektor biases;
    Vektor inputs;
    double[] activations;

    public Layer(int numInputNodes, int numOutputNodes) {
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = new Matrix(numOutputNodes, numInputNodes, true);
        this.costGradientW = new Matrix(numInputNodes, numOutputNodes);
        this.costGradientB = new Vektor(numOutputNodes);
        this.weightedInputs = new Vektor(numOutputNodes);
        activations = new double[numOutputNodes];
        this.biases = new Vektor(numOutputNodes, true);
    }

    private double NodeCostDerivative(double activation, double expectedOutput) {
        return 2 * (activation - expectedOutput);
    }

    public double[] CalculateOutputs(double[] inputs) {
        this.inputs = new Vektor(inputs);
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            double weightedInput = biases.getValue(nodeOut);
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                weightedInput += inputs[nodeIn] * weights.getValue(nodeOut, nodeIn);
            }
            this.weightedInputs.setValue(nodeOut, weightedInput);
        }

        // Apply activation function
        for (int i = 0; i < activations.length; i++) {
            activations[i] = Activations.Sigmoid(this.weightedInputs.getValue(i));
        }

        return activations;
    }

    public double[] CalculateOutputLayerNodeValues(double[] expectedOutputs) {
        double[] nodeValues = new double[expectedOutputs.length];
        for (int i = 0; i < nodeValues.length; i++) {
            double costDerivative = NodeCostDerivative(activations[i], expectedOutputs[i]);
            double activationDerivative = Activations.SigmoidDerivative(weightedInputs.getValue(i));
            nodeValues[i] = activationDerivative * costDerivative;
        }
        return nodeValues;
    }

    public double[] CalculateHiddenLayerNodeValues(Layer oldLayer, double[] nodeValues) {
        double[] newNodeValues = oldLayer.weights.T().DotVektor(nodeValues);

        for (int i = 0; i < weightedInputs.length; i++) {
            newNodeValues[i] *= Activations.SigmoidDerivative(weightedInputs.getValue(i));
        }
        return newNodeValues;
    }

    public void UpdateGradients(double[] nodeValues) {
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                // Evaluate the partial derivative: cost/weight of current connection 
                double derivativeCostWrtWeight = inputs.getValue(nodeIn) * nodeValues[nodeOut];
                // The costGradientW array stores these partial derivatives for each weight.
                // Note: the derivative is being added to the array here because ultimately we want 
                // to calculate the average gradient across all the data in the training batch 
                double costderivW = costGradientW.getValue(nodeIn, nodeOut) + derivativeCostWrtWeight;
                costGradientW.setValue(nodeIn, nodeOut, costderivW);
            }
            // Evaluate the partial derivative: cost/bias of the current node 
            double derivativeCostWrtBias = 1 * nodeValues [nodeOut];
            double costderivB = derivativeCostWrtBias + costGradientB.getValue(nodeOut);
            costGradientB.setValue(nodeOut, costderivB);

        }
    }

    public void ClearGradient() {
        this.costGradientW = new Matrix(numInputNodes, numOutputNodes);
        this.costGradientB = new Vektor(numOutputNodes);
    }

    public void ApplyGradient(double learnrate) {
        weights.MinusMatrix(costGradientW.T(), learnrate);
        biases.MinusVektor(costGradientB, learnrate);
    }
}