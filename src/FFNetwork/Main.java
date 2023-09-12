package FFNetwork;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        double[][] Matrix = NNMath.RandomDoubleArrayMatrix(3, 2);
        System.out.println(Arrays.deepToString(Matrix));


        Activation activation = new ReLU();
        System.out.println("Baby"+activation.ActivationFunction(3));

        NeuralNetwork nn = new NeuralNetwork(2.5, 10, 20, 5);
        nn.Querry(NNMath.RandomDoubleArray(10));
    }
}
