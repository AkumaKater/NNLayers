package JanNN;

import java.util.Arrays;

public class JanNN {
    
    public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork(0.3,700, 400,300,10);

        double[] tempInputs = NNUtil.generateRandomWeights(1, 10)[0];
        for(int i = 0; i<tempInputs.length; i++){tempInputs[i]*=200;}
        
        double[] inputs = NNUtil.MatrixToVektor(NNUtil.generateRandomWeights(700, 1));
        double[] targets = NNUtil.MatrixToVektor(NNUtil.generateRandomWeights(10, 1));

        nn.learn(inputs, targets);
       

    
    }
}
