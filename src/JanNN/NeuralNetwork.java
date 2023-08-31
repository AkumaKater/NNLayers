package JanNN;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

    public NeuralNetwork(double learnRate, String path) {

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
            String line = lines.remove(0);
            int NetworSize = Integer.parseInt(line.split("<context>")[1]);
            layers = new Layer[NetworSize];
            for (int i = 0; i < NetworSize; i++) {
                String[] metaString = lines.remove(0).split("<layer>")[1].split(",");
                int inputNodes = Integer.parseInt(metaString[0]);
                int OutputNodes = Integer.parseInt(metaString[1]);
                layers[i] = new Layer(inputNodes, OutputNodes);
                layers[i].biases = new Vektor(OutputNodes, false);
                for (int j = 0; j < OutputNodes; j++) {
                    String[] doubles = lines.remove(0).split("<row>")[1].split(",");
                    for (int k = 0; k < inputNodes; k++) {
                        layers[i].weights.setValue(j, k, Double.parseDouble(doubles[k]));
                    }
                }
                String[] doubles = lines.remove(0).split("<biases>")[1].split(",");
                for (int j = 0; j < OutputNodes; j++) {
                    layers[i].biases.setValue(j, Double.parseDouble(doubles[j]));
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    // Training
    public void learn(DataPoint[] data, double learnrate) {
        for (DataPoint dataPoint : data) {
            UpdateAllGradients(dataPoint);
        }

        ApplyAllGradients(learnrate / data.length);

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
            nodeValues = hiddenLayer.CalculateHiddenLayerNodeValues(layers[index + 1], nodeValues);
            hiddenLayer.UpdateGradients(nodeValues);
        }
    }

    private void ClearAllGradients() {
        for (Layer layer : layers) {
            layer.ClearGradient();
        }
    }

    private void ApplyAllGradients(double learnrate) {
        for (Layer layer : layers) {
            layer.ApplyGradient(learnrate);
        }
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < layers.length - 1; i++) {
            result += " (" + layers[i].numOutputNodes + ") ";
        }
        return result;
    }
}
