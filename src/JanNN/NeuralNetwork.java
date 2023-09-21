package JanNN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            e.printStackTrace();
        }
    }

    // Training
    public void learn(MnistMatrix[] data) {
        for (MnistMatrix dataPoint : data) {
            UpdateAllGradients(dataPoint);
        }

        ApplyAllGradients(this.learnRate / data.length);

        ClearAllGradients();
    }

    // Abfragen
    public double[] Querry(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }

    double Cost(MnistMatrix dataPoint) {
        double[] QuerryOutputs = Querry(dataPoint.getInputs());
        double[] Targets = dataPoint.getTargets();
        double cost = 0;
        for(int i=0; i<Targets.length; i++) {
            cost = Targets[i]-QuerryOutputs[i];
        }
        return cost;
    }

    double Cost(MnistMatrix[] data) {
        double totalCost = 0;

        for (MnistMatrix dataPoint : data) {
            totalCost += Cost(dataPoint);
        }

        return totalCost / data.length;
    }

    void UpdateAllGradients(MnistMatrix dataPoint) {
        Querry(dataPoint.getInputs());

        Layer outputLayer = layers[layers.length - 1];
        double[] nodeValues = outputLayer.CalculateOutputLayerNodeValues(dataPoint.getTargets());
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
