package MNISTReader;

import java.util.Arrays;

public class MnistBuffer {
    MnistMatrix[] MnistData;
    MnistMatrix[] TrainingsData;
    MnistMatrix[] TestData;

    int index = 0;

    public void loadMNIST() throws Exception {
        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                dataHome + "train-labels.idx1-ubyte");
        MnistMatrix[] mnistMatrix2 = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                dataHome + "t10k-labels.idx1-ubyte");

        MnistData = new MnistMatrix[mnistMatrix.length + mnistMatrix2.length];
        System.arraycopy(mnistMatrix, 0, MnistData, 0, mnistMatrix.length);
        System.arraycopy(mnistMatrix2, 0, MnistData, mnistMatrix.length, mnistMatrix2.length);
    }
    public void splitData(int splitIndex){
        TrainingsData = Arrays.copyOfRange(MnistData, 0, splitIndex);
        TestData = Arrays.copyOfRange(MnistData, splitIndex, MnistData.length);
    }

    public MnistMatrix[] getBatch(int batchSize){
        MnistMatrix[] result = new MnistMatrix[batchSize];
        for(int i=0; i<batchSize; i++){
            result[i] = TrainingsData[index+i];
            if(index+i >= TrainingsData.length){index = 0;}
        }
        index+=batchSize;
        if(index >= TrainingsData.length){index = 0;}
        return result;
    }

    public MnistMatrix[] getTrainingsData() {
        return TrainingsData;
    }

    public MnistMatrix[] getTestData() {
        return TestData;
    }

    public int getTrainingsDataLength(){
        return TrainingsData.length;
    }
}
