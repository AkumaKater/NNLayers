package JanNN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import MNISTReader.MNISTPrinter;
import MNISTReader.MnistDataReader;
import MNISTReader.MnistMatrix;

public class JanNN {

    /*public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork(0.02, 784, 120, 10);
        int TrainingCycles = 30;
        System.out.println(nn.layers.length + " Layers wer created");

        for (Layer layer : nn.layers) {
            System.out.println("weights: " + layer.weights.length + "X" + layer.weights.width);
        }

        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                dataHome + "train-labels.idx1-ubyte");

        nn.learn(new DataPoint(mnistMatrix[1].getInputs(), mnistMatrix[1].getTargets()));

        boolean Incomplete = false;
        if (Incomplete) {
            throw new MyCustomException("Methode vorzeitig abgebrochen");
        }

        int numOfMNISTMatrix = 5;
        System.out.println("int numOfMNISTMatrix = 3;");
        NNUtil.ArrayToString(nn.Querry(mnistMatrix[numOfMNISTMatrix].getInputs()));
        nn.learn(new DataPoint(mnistMatrix[1].getInputs(), mnistMatrix[1].getTargets()));
        NNUtil.ArrayToString(nn.Querry(mnistMatrix[numOfMNISTMatrix].getInputs()));

        DecimalFormat df = new DecimalFormat("0.00");

        
        for (int j = 0; j < TrainingCycles; j++) {
            for (int i = 0; i < mnistMatrix.length; i++) {
                nn.learn(new DataPoint(mnistMatrix[i].getInputs(), mnistMatrix[i].getTargets()));
                System.out.print('\r');
                System.out.print(df.format((double)(j*mnistMatrix.length+i) / (double)(TrainingCycles*mnistMatrix.length) * 100) + "%");
            }
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int index = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if(index == label){
                correct ++;
            }
        }
        acuracy = (double)correct / (double)iterations * 100;

        mnistMatrix = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

         iterations = mnistMatrix.length;
        System.out.println("Acuracy on Training Data: "+acuracy+"%");
         correct = 0;
         acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int index = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if(index == label){
                correct ++;
            }
        }
        acuracy = (double)correct / (double)iterations * 100;
        System.out.println("Acuracy on Test Data: "+acuracy+"%");
    }*/

    public static void main(String[] args) throws Exception {
        int TrainingCycles = 20;
        double learnRate = 0.02;
        int BatchSize = 32;
        NeuralNetwork nn = new NeuralNetwork(learnRate, 784, 300, 300, 10);

        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                dataHome + "train-labels.idx1-ubyte");

        DecimalFormat df = new DecimalFormat("0.00");

        DataPoint[] Batch = new DataPoint[BatchSize];
        int index = 0;
        int mnistIndex = mnistMatrix.length-1;
        for (int j = 0; j < (TrainingCycles*mnistMatrix.length); j++) {
            Batch[index] = new DataPoint(mnistMatrix[mnistIndex].getInputs(), mnistMatrix[mnistIndex].getTargets());
            index++;
            mnistIndex--;
            if(mnistIndex<0){
                mnistIndex = mnistMatrix.length-1;
            }
            if(index == BatchSize){
                nn.learn(Batch, learnRate);
                index = 0;
            }
            System.out.print('\r');
            System.out.print(df.format(((double)j/(double)(TrainingCycles*mnistMatrix.length)) * 100) + "%");
        }
        
        System.out.println();

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int Hindex = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if(Hindex == label){
                correct ++;
            }
        }
        acuracy = (double)correct / (double)iterations * 100;

        mnistMatrix = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        for (int i = 5; i < 10; i++) {
            NNUtil.ArrayToString(nn.Querry(mnistMatrix[i].getInputs()), false);
            NNUtil.printHighestLabel(nn.Querry(mnistMatrix[i].getInputs()));
            MNISTPrinter.printMnistMatrix(mnistMatrix[i]);
        }

         iterations = mnistMatrix.length;
        System.out.println("Acuracy on Training Data: "+acuracy+"%");
         correct = 0;
         acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int Hindex = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if(index == label){
                correct ++;
            }
        }
        acuracy = (double)correct / (double)iterations * 100;
        System.out.println("Acuracy on Test Data: "+acuracy+"%");
    }
}

class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}