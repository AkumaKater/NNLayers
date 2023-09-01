package JanNN;

import java.util.Arrays;

import MNISTReader.MnistDataReader;
import MNISTReader.MnistMatrix;

public class DataLoader {

     MnistMatrix[] TrainingsData;
     MnistMatrix[] TestData;

     public DataLoader(int splitIndex) throws Exception{
        loadMNIST(splitIndex);
     }

    public  void loadMNIST(int splitIndex) throws Exception{
        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                dataHome + "train-labels.idx1-ubyte");
        MnistMatrix[] mnistMatrix2 = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        MnistMatrix[] combinedArray = new MnistMatrix[mnistMatrix.length + mnistMatrix2.length];
        System.arraycopy(mnistMatrix, 0, combinedArray, 0, mnistMatrix.length);
        System.arraycopy(mnistMatrix2, 0, combinedArray, mnistMatrix.length, mnistMatrix2.length);

        TrainingsData = Arrays.copyOfRange(combinedArray, 0, splitIndex);
        TestData = Arrays.copyOfRange(combinedArray, splitIndex, combinedArray.length);
    }

    public MnistMatrix[] getTrainingsData() {
        return TrainingsData;
    }

    public MnistMatrix[] getTestData() {
        return TestData;
    }

}
