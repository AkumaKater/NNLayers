package FFNetwork;

import MNISTReader.MnistMatrix;

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

    // Abfragen
    public double[] Querry(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }

    //Den Fehler berechnen mit der Cost Funktion
    double Cost(MnistMatrix dataPoint) {
        double[] QuerryOutputs = Querry(dataPoint.getInputs());
        double[] Targets = dataPoint.getTargets();
        double cost = 0;
        for(int i=0; i<Targets.length; i++) {
            double error = Targets[i]-QuerryOutputs[i];
            cost += error*error;
        }
        return cost;
    }
}