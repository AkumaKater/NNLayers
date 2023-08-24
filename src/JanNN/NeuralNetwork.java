package JanNN;

public class NeuralNetwork {
    Layer[] layers;
    double learnRate;

    // Initialisierung
    public NeuralNetwork(double learnRate, int... layerSizes) {
        layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i], layerSizes[i + 1]);
        }
        this.learnRate = learnRate;
    }

    // Training
    public void learnD(double[] inputs, double[] targets) {

    }

    // Training
    public void learndeprecated(DataPoint dataPoint) {
        double[] inputs = dataPoint.getInputs();
        double[] targets = dataPoint.getExpectedOutput();

        // Hier werden die Errors berechnet, und gespeichert
        double[][] errors = new double[layers.length][];
        errors[errors.length - 1] = NNUtil.ArraySubtraction(targets, Querry(inputs));
        for (int i = layers.length - 2; i >= 0; i--) {
            errors[i] = layers[i + 1].weights.DotVektorTranspose(errors[i + 1]);
        }

        // Hier müssen jetzt alle PreviousOutputs gespeichert werden
        double[][] PreviousOutputs = new double[layers.length][];
        PreviousOutputs[0] = inputs;
        for (int i = 1; i < layers.length; i++) {
            PreviousOutputs[i] = layers[i - 1].activations;
        }

        // double[] errors = NNUtil.ArraySubtraction(targets, Querry(inputs));
        for (int i = layers.length - 1; i >= 0; i--) {
            // über alle Layer iterieren und die weightedOutputs und previousweightedOutputs
            // übergeben
            // vermutlich mit dem letzten Layer anfangen
            layers[i].UpdateWeights(learnRate, errors[i], PreviousOutputs[i]);
        }

    }

    //Training
    public void learn(DataPoint[] data, double learnrate) {
        for(DataPoint dataPoint : data){
            UpdateAllGradients(dataPoint);
        }

        ApplyAllGradients(learnrate);

        ClearAllGradients();
    }

    // Abfragen
    public double[] Querry(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }

    double Cost(DataPoint dataPoint) {
        double[] ExpectedOutput = dataPoint.getExpectedOutput();
        double[] errors = new double[ExpectedOutput.length];
        errors = NNUtil.ArraySubtraction(ExpectedOutput, Querry(dataPoint.getInputs()));
        double cost = 0;
        for (double e : errors) {
            cost += e * e;
        }
        return cost;
    }

    double Cost(DataPoint[] data) {
        double totalCost = 0;

        for (DataPoint dataPoint : data) {
            totalCost += Cost(dataPoint);
        }

        return totalCost / data.length;
    }

    void UpdateAllGradients(DataPoint dataPoint) {
        Querry(dataPoint.getInputs());

        Layer outputLayer = layers[layers.length - 1];
        double[] nodeValues = outputLayer.CalculateOutputLayerNodeValues(dataPoint.expectedOutput);
        outputLayer.UpdateGradients(nodeValues);

        for (int index = layers.length - 2; index >= 0; index--) {
            Layer hiddenLayer = layers[index];
            nodeValues = hiddenLayer.CalculateHiddenLayerNodeValues(layers[index+1], nodeValues);
            hiddenLayer.UpdateGradients(nodeValues);
        }
    }

    private void ClearAllGradients(){
        for(Layer layer : layers){
            layer.ClearGradient();
        }
    }

    private void ApplyAllGradients(double learnrate){
        for(Layer layer : layers){
            layer.ApplyGradient(learnrate);
        }
    }

    public String toString(){
        String result ="";
        for(int i = 0; i <layers.length-1; i++){
            result += " ("+layers[i].numOutputNodes+") ";
        }
        return result;
    }
}
