package JanNN;

import java.util.Arrays;

public class NeuralNetwork {
    Layer[] layers;
    double learnRate;

// Initialisierung
public NeuralNetwork(double learnRate, int... layerSizes){
    layers = new Layer[layerSizes.length -1];
    for(int i = 0; i < layers.length; i++){
        layers[i] = new Layer(layerSizes[i], layerSizes[i+1]);
    }
    this.learnRate = learnRate;

    System.out.println(Arrays.deepToString(layers[0].weights));
}

// Training

// Abfragen


}
