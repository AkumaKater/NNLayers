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
    //Hier werden die Errors berechnet, und gespeichert
    double[][] errors = new double[layers.length][];
    errors[errors.length-1] = NNUtil.ArraySubtraction(targets, Querry(inputs));
    for(int i = layers.length-2; i >= 0; i--){
        errors[i] = NNUtil.MatrixToVektor(NNUtil.DotProdukt(layers[i+1].weights, errors[i+1]));
    }

    //Hier müssen jetzt alle PreviousOutputs gespeichert werden
    double[][] PreviousOutputs = new double[layers.length][];
    PreviousOutputs[0] = inputs;
    for(int i = 1; i < layers.length; i++){
        PreviousOutputs[i] = layers[i-1].CalculateOutputs(PreviousOutputs[i-1]);
    }

    //double[] errors = NNUtil.ArraySubtraction(targets, Querry(inputs));
    for(int i = layers.length-1; i >= 0; i--){
        // über alle Layer iterieren und die weightedOutputs und previousweightedOutputs übergeben
        //vermutlich mit dem letzten Layer anfangen
        layers[i].UpdateWeights(learnRate, errors[i], PreviousOutputs[i]);
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
