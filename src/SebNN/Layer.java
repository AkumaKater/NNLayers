package SebNN;
import java.util.Random;

public class Layer {

    int numNodesIn, numNodesOut;
    public double[][] costGradientW;
    public double[] costGradientB;
    double[][] weights;
    double [] biases;

    //create the layer
    public Layer(int numNodesIn, int numNodesOut){
        costGradientW = new double[numNodesIn][numNodesOut];
        weights = new double[numNodesIn][numNodesOut];
        costGradientB = new double[numNodesOut];
        biases = new double[numNodesOut];
        this.numNodesIn = numNodesIn;
        this.numNodesOut = numNodesOut;
        InitializeRandomWeights();

        weights = new double[numNodesIn][numNodesOut];
        biases = new double[numNodesOut];
    }

    // Update the weights and Biases based on the Cost gradients(gradient descent)
    public void ApplyGradients(double learnRate){
        for (int nodeOut = 0; nodeOut < numNodesOut; nodeOut++){
            biases[nodeOut] -= costGradientB[nodeOut] * learnRate;
            for (int nodeIn = 0; nodeIn < numNodesIn; nodeIn++){
                weights[nodeIn][nodeOut] -= costGradientW[nodeIn][nodeOut] * learnRate;
            }
        }
    }

    /*calculate the output of the layer
    public double[] CalculateOutputs(double[] inputs){
        double[] weightedInput = new double[numNodesOut];

        for(int nodeOut = 0; nodeOut < numNodesOut; nodeOut++){
            double weightedInput = biases[nodeOut];
            for (int nodeIn = 0; nodeIn < numNodesIn; nodeIn++){
                weightedInput += inputs[nodeIn] * weights[nodeIn][nodeOut];
            }
            activations[nodeOut] = ActivationFunction(weightedInput);
        }
        return activations;
    }*/

    double ActivationFunction(double weightedInput) {
        return Activations.Sigmoid(weightedInput);
    }

    double NodeCost(double outputActivation, double expectedOutput){
        double error = outputActivation - expectedOutput;
        return error * error;
    }

    void InitializeRandomWeights(){
        Random rng = new Random();

        for(int nodeIn = 0; nodeIn < numNodesIn; nodeIn++){
            for(int nodeOut = 0; nodeOut < numNodesOut; nodeOut++){
                double randomValue = rng.nextDouble() * 2 - 1;
                weights[nodeIn][nodeOut] = randomValue / Math.sqrt(numNodesIn);
            }
        }
    }
    
}
