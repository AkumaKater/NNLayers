package JanNN;

public class Layer {

    int numInputNodes, numOutputNodes;
    double[][] weights;
    
    public Layer(int numInputNodes, int numOutputNodes){
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = NNUtil.generateRandomWeights(numInputNodes, numOutputNodes);
    }
}
