package FFNetwork;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        double[][] Matrix = NNMath.RandomDoubleArrayMatrix(3, 2);
        System.out.println(Arrays.deepToString(Matrix));


        Activation activation = Activation.geActivation();
        System.out.println("Baby"+activation.ActivationFunction(3));

        NeuralNetwork nn = new NeuralNetwork(2.5, 780, 300, 100, 10);
        System.out.println(Arrays.toString(nn.Querry(NNMath.RandomDoubleArray(780))));
    }
}
