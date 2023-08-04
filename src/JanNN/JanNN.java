package JanNN;

import java.text.DecimalFormat;
import java.util.Arrays;

import MNISTReader.MNISTPrinter;
import MNISTReader.MnistDataReader;
import MNISTReader.MnistMatrix;

public class JanNN {

    public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork(0.3, 784, 800, 10);

        double[] tempInputs = NNUtil.generateRandomWeights(1, 10)[0];
        for (int i = 0; i < tempInputs.length; i++) {
            tempInputs[i] *= 200;
        }

        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte", dataHome + "train-labels.idx1-ubyte");

        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < mnistMatrix.length; i++) {
            nn.learn(mnistMatrix[i].getInputs(), mnistMatrix[i].getTargets());
            System.out.print('\r');
            System.out.print(df.format((double)i/(double)mnistMatrix.length*100)+"%");
        }

        mnistMatrix = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        for (int numOfMNISTMatrix = 0; numOfMNISTMatrix < 10; numOfMNISTMatrix++) {
            System.out.println(Arrays.toString(nn.Querry(mnistMatrix[numOfMNISTMatrix].getInputs())));
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[numOfMNISTMatrix].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[numOfMNISTMatrix]);
        }
    }
}
