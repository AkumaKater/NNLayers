package JanNN;

public class Layer {

    int numInputNodes, numOutputNodes;
    Matrix weights;

    double[] weightedOutputs;

    public Layer(int numInputNodes, int numOutputNodes) {
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = new Matrix(numOutputNodes, numInputNodes);
    }

    public double[] CalculateOutputs(double[] inputs) {
        weightedOutputs = weights.DotVektor(inputs);
        for (int i = 0; i < weightedOutputs.length; i++) {
            weightedOutputs[i] = Activations.Sigmoid(weightedOutputs[i]);
        }
        return weightedOutputs;
    }

    public void UpdateWeights(double learnRate, double[] errors, double[] previousWeightedOutputs) {
        Vektor error = new Vektor(errors);
        Vektor prevOut = new Vektor(previousWeightedOutputs);
        Vektor outpus = new Vektor(weightedOutputs);

        Vektor partDeriv = error.X(outpus).X(outpus.EinsMinus());

        Matrix Update = partDeriv.OneByOneToMatrix(prevOut);

        weights.PlusMatrix(Update.MalWert(learnRate));
    }
}