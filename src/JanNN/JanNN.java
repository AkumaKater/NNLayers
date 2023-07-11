package JanNN;

import java.util.Arrays;

public class JanNN {
    
    public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork(0.3,700,300,10);

        double[] tempInputs = NNUtil.generateRandomWeights(1, 700)[0];
        for(int i = 0; i<tempInputs.length; i++){tempInputs[i]*=200;}
        System.out.println(Arrays.toString(nn.Querry(tempInputs)));

        double[][] matrix1 = {{2.0, 3.0}, {4.0, 5.0}};
        double[] matrix2 = {6.0, 2.0};

        nn.learn(matrix2, matrix2);
    }
}
