package JanNN;

public class Layer {

    int numInputNodes, numOutputNodes;
    Matrix weights;
    Matrix costGradientW;

    Vektor inputs;
    Vektor biases;
    Vektor previouseActivations;
    double[] activations;

    public Layer(int numInputNodes, int numOutputNodes) {
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = new Matrix(numOutputNodes, numInputNodes);
        this.costGradientW = new Matrix(numInputNodes, numOutputNodes);
        this.inputs = new Vektor(numOutputNodes);
        activations = new double[numOutputNodes];
        this.biases = new Vektor(numOutputNodes);
    }

    // ToDo: Überprüfen
    private double NodeCostDerivative(double activation, double expectedOutput) {
        return 2 * (activation - expectedOutput);
    }

    public double[] CalculateOutputs2(double[] inputs) {
        this.inputs = new Vektor(weights.DotVektor(inputs));
        activations = new double[this.inputs.length];
        for (int i = 0; i < activations.length; i++) {
            activations[i] = Activations.Sigmoid(this.inputs.getValue(i));
        }
        return activations;
    }

    public void UpdateWeights(double learnRate, double[] errors, double[] previousWeightedOutputs) {
        Vektor error = new Vektor(errors);
        Vektor prevOut = new Vektor(previousWeightedOutputs);
        Vektor outpus = new Vektor(activations);

        Vektor partDeriv = error.X(outpus).X(outpus.EinsMinus());

        Matrix Update = partDeriv.OneByOneToMatrix(prevOut);

        weights.PlusMatrix(Update.MalWert(learnRate));
    }

    public double[] CalculateOutputLayerNodeValues(double[] expectedOutputs) {
        double[] nodeValues = new double[expectedOutputs.length];
        for (int i = 0; i < nodeValues.length; i++) {
            double costDerivative = NodeCostDerivative(activations[i], expectedOutputs[i]);
            double activationDerivative = Activations.SigmoidDerivative(inputs.getValue(i));
            nodeValues[i] = activationDerivative * costDerivative;
        }
        return nodeValues;
    }

    public double[] CalculateOutputs(double[] inputs) {
        previouseActivations = new Vektor(inputs);
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            double weightedInput = biases.getValue(nodeOut);
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                weightedInput += inputs[nodeIn] * weights.getValue(nodeOut, nodeIn);
            }
            this.inputs.setValue(nodeOut, weightedInput);
        }

        // Apply activation function
        for (int i = 0; i < activations.length; i++) {
            activations[i] = Activations.Sigmoid(this.inputs.getValue(i));
        }

        return activations;
    }

    public double[] CalculateHiddenLayerNodeValues(Layer oldLayer, double[] nodeValues) {
        double[] newNodeValues = oldLayer.weights.T().DotVektor(nodeValues);

        for (int i = 0; i < inputs.length; i++) {
            newNodeValues[i] *= Activations.SigmoidDerivative(inputs.getValue(i));
        }
        return newNodeValues;
    }

    public void UpdateGradients(double[] nodeValues) {
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                // Evaluate the partial derivative: cost/weight of current connection 
                double derivativeCostWrtWeight = previouseActivations.getValue(nodeIn) * nodeValues[nodeOut];
                // The costGradientW array stores these partial derivatives for each weight.
                // Note: the derivative is being added to the array here because ultimately we
                // want // to calculate the average gradient across all the data in the training batch 
                double costderiv = costGradientW.getValue(nodeIn, nodeOut) + derivativeCostWrtWeight;
                costGradientW.setValue(nodeIn, nodeOut, costderiv);
            }
            // Evaluate the partial derivative: cost/bias of the current node double
            //derivativeCostWrtBias = 1 nodeValues [nodeOut];
            //costGradientW[nodeOut] += derivativeCostWrtBias;

        }
    }

    public void ClearGradient() {
        this.costGradientW = new Matrix(numInputNodes, numOutputNodes);
    }

    public void ApplyGradient(double learnrate) {
        weights.MinusMatrix(costGradientW.T(), learnrate);
    }
}