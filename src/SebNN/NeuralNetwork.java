package SebNN;
public class NeuralNetwork {
    Layer[] layers;

    public NeuralNetwork(int... layerSizes){
        layers = new Layer[layerSizes.length -1];
        for(int i = 0; i < layers.length;i++){
            layers[i] = new Layer(layerSizes[i], layerSizes[i+1]);
        }
    }

    //run the input values through the network to calculate the output values
    double[] CalculateOutputs(double[] inputs){
        for(Layer layer : layers){
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }

    int Classify(double[] inputs){
        double[] outputs = CalculateOutputs(inputs);
        int maxIndex = 0;
        for(int i = 0; i < outputs.length; i++){
            if(outputs[i] > outputs[maxIndex])
                maxIndex=i;
        }
        return maxIndex;
    }

    double Cost(DataPoint dataPoint){
        double[] outputs = CalculateOutputs(dataPoint.inputs);
        Layer outputLayer = layers[layers.length - 1];
        double cost = 0;

        for (int nodeOut = 0; nodeOut < outputs.length; nodeOut++){
            cost += outputLayer.NodeCost(outputs[nodeOut], dataPoint.expectedOutput[nodeOut]);
        }
        return cost;
    }

    // a Cost function
    double Cost(DataPoint[] data){
        double totalCost = 0;

        for (DataPoint dataPoint : data){
            totalCost += Cost(dataPoint);
        }

        return totalCost / data.length;
    }

    public void Learn(DataPoint[] trainingData, double learnRate){
        final double h = 0.0001;
        double originalCost = Cost(trainingData);

        for(Layer layer : layers){
            for(int nodeIn = 0; nodeIn < layer.numNodesIn; nodeIn++){
                for(int nodeOut = 0; nodeOut < layer.numNodesOut; nodeOut++){
                    layer.weights[nodeIn][nodeOut] += h;
                    double deltaCost = Cost(trainingData) - originalCost;
                    layer.weights[nodeIn][nodeOut] -= h;
                    layer.costGradientW[nodeIn][nodeOut] = deltaCost/h;
                }
            }

            //Calculate the cost gradient for the current biases
            for(int biasIndex = 0; biasIndex < layer.biases.length; biasIndex++){
                layer.biases[biasIndex] += h;
                double deltaCost = Cost(trainingData) - originalCost;
                layer.biases[biasIndex] -= h;
                layer.costGradientB[biasIndex] = deltaCost/h;
            }
        }
    }
}
