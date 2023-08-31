package JanNN;

import java.text.DecimalFormat;
import java.util.Arrays;

import MNISTReader.MNISTPrinter;
import MNISTReader.MnistDataReader;
import MNISTReader.MnistMatrix;

public class JanNN {

    public static void main(String[] args) throws Exception {
        NNLog log = NNLog.getLogger();

        String path = "/home/kater/Projekte/testlayer/NNLayers/src/JanNN/NetworksPersitance/NetwerkMitBiases.json";

        int splitIndex = 60000; // 60.000
        int TrainingCycles = 1;
        double learnRate = 0.25;
        int BatchSize = 50;
        NeuralNetwork nn = new NeuralNetwork(learnRate, 784, 300, 100, 10);

        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                dataHome + "train-labels.idx1-ubyte");
        mnistMatrix = Arrays.copyOfRange(mnistMatrix, 0, splitIndex);

        DecimalFormat df = new DecimalFormat("0.00");

        DataPoint[] Batch = new DataPoint[BatchSize];
        int index = 0;
        int mnistIndex = mnistMatrix.length - 1;
        for (int j = 0; j < (TrainingCycles * mnistMatrix.length); j++) {
            Batch[index] = new DataPoint(mnistMatrix[mnistIndex].getInputs(), mnistMatrix[mnistIndex].getTargets());
            index++;
            mnistIndex--;
            if (mnistIndex < 0) {
                mnistIndex = mnistMatrix.length - 1;
            }
            if (index == BatchSize) {
                nn.learn(Batch, learnRate);
                index = 0;
            }
            System.out.print('\r');
            System.out.print(df.format(((double) j / (double) (TrainingCycles * mnistMatrix.length)) * 100) + "%");
        }

        System.out.println();

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

        log.add(nn.toString());
        log.add(mnistMatrix.length);
        log.add(TrainingCycles);
        log.add(BatchSize);
        log.add(learnRate);
        log.add(NNUtil.getAcuracy(nn, mnistMatrix));

        mnistMatrix = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

        log.whatever();
        log.add(NNUtil.getAcuracy(nn, mnistMatrix));
        log.flush();

        //System.out.println(NNUtil.getAcuracy(nn, mnistMatrix));
        //System.out.println(NNUtil.getAcuracy(nn, mnistMatrix));
        //System.out.println(NNUtil.getAcuracy(nn, mnistMatrix));

        //log.saveNetwork(nn.layers, path);
        //nn = new NeuralNetwork(learnRate, path);
        //System.out.println(NNUtil.getAcuracy(nn, mnistMatrix));
    }
}

class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}