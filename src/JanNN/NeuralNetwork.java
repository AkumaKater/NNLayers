package JanNN;

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
}

// Training
public void learn(double[] inputs, double[] targets){
    //double[] errors = NNUtil.ArraySubtraction(targets, Querry(inputs));
    for(int i = 0; i < layers.length; i++){
        System.out.println("Die Reihen sind "+layers[i].weights.length+"und die Spalten sind "+layers[i].weights[0].length);;
    }
    
}

// Abfragen
public double[] Querry(double[] inputs){
    for(Layer layer : layers){
        inputs = layer.CalculateOutputs(inputs);
    }
    return inputs;
}


}
