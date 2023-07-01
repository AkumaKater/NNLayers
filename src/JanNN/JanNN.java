package JanNN;

import java.util.Arrays;

public class JanNN {
    
    public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork(0.3,700,899,300,3);

        double[] tempInputs = NNUtil.generateRandomWeights(1, 700)[0];
        for(int i = 0; i<tempInputs.length; i++){tempInputs[i]*=200;}
        System.out.println(Arrays.toString(nn.Querry(tempInputs)));
    }
}
