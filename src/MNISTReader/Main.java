package MNISTReader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String dataHome = "src/MNIST/archive/";//train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome+"train-images.idx3-ubyte", dataHome+"train-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[mnistMatrix.length - 1]);
        mnistMatrix = new MnistDataReader().readData(dataHome+"t10k-images.idx3-ubyte", dataHome+"t10k-labels.idx1-ubyte");


        for(int i = 0; i< 20;i++){
            printMnistMatrix(mnistMatrix[i]);
        }

        System.out.println("mnistMatrix.length: "+mnistMatrix.length);
    }

    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println(MNISTPrinter.printMnistMatrix(matrix));
    }
}