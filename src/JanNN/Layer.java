package JanNN;

import java.util.Arrays;

public class Layer {

    int numInputNodes, numOutputNodes;
    double[][] weights;

    double[] weightedOutputs;
    
    public Layer(int numInputNodes, int numOutputNodes){
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;
        this.weights = NNUtil.generateRandomWeights(numInputNodes, numOutputNodes);
    }

    public double[] CalculateOutputs(double[] inputs){
        double[] weightedOutputs = new double[numOutputNodes];
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            double weightedInput = 0;
            for(int nodeIn = 0; nodeIn < numInputNodes; nodeIn++){
                weightedInput += inputs[nodeIn] * weights[nodeIn][nodeOut];
            }
            //weightedOutputs[nodeOut] = weightedInput;
            weightedOutputs[nodeOut] = Activations.Sigmoid(weightedInput);
        }
        this.weightedOutputs = weightedOutputs;
        return weightedOutputs;
    } 

    public void UpdateWeights(double learnRate, double[] errors, double[] previousWeightedOutputs){
        double[] partialDerivative = NNUtil.VektorMultiplikation(errors, NNUtil.VektorMultiplikation(weightedOutputs, NNUtil.WertMinusVektor(1.0, weightedOutputs)));
        NNUtil.UpdateWeights(weights, learnRate, partialDerivative, previousWeightedOutputs);
    }
}             