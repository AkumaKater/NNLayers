package FFNetwork;

import java.text.DecimalFormat;
import java.util.Arrays;

import JanNN.NNUtil;
import MNISTReader.MnistBuffer;
import MNISTReader.MnistMatrix;

public class Main {

    public static void main(String[] args) throws Exception{
        int splitIndex = 60000; // 60.000
        int TrainingCycles = 5;
        double learnRate = 0.25;
        int BatchSize = 1;
        DecimalFormat df = new DecimalFormat("0.00");

        double[][] Matrix = NNMath.RandomDoubleArrayMatrix(3, 2);
        System.out.println(Arrays.deepToString(Matrix));


        Activation activation = Activation.geActivation();
        System.out.println("Baby"+activation.ActivationFunction(3));

        MnistBuffer mBuffer = new MnistBuffer();
        mBuffer.loadMNIST();
        mBuffer.splitData(splitIndex);

        NeuralNetwork nn = new NeuralNetwork(learnRate, 780, 300, 100, 10);

        for(int i=0; i<(splitIndex*TrainingCycles/BatchSize); i++){
            MnistMatrix[] MnistMatrixdd = mBuffer.getBatch(BatchSize);
            MnistMatrix mm = MnistMatrixdd[0];
            nn.learn(mm);
            System.out.print('\r');
            System.out.print(df.format((double) i / (splitIndex*TrainingCycles/BatchSize-1) * 100) + "%");
        }

        System.out.println("\n\n\n"+NNUtil.getAcuracy(nn, mBuffer.getTrainingsData()));
        
    }
}
