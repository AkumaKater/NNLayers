package JanNN;

public class Layer {

    int numInputNodes, numOutputNodes;
    double[][] weights;
    
    public Layer(int numInputNodes, int numOutputNodes){
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = NNUtil.generateRandomWeights(numInputNodes, numOutputNodes);
    }

    public double[] CalculateOutputs(double[] inputs){
        //ToDO

        //inputs X weights

        double[] weightedInputs = new double[numOutputNodes];
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            double weightedInput = 0;
            for(int nodeIn = 0; nodeIn < numInputNodes; nodeIn++){
                weightedInput += inputs[nodeIn] * weights[nodeIn][nodeOut];
            }
            weightedInputs[nodeOut] = Activations.Sigmoid(weightedInput);
        }
        return weightedInputs;
    } 
}