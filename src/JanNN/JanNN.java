package JanNN;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

import MNISTReader.MnistBuffer;
import MNISTReader.MnistMatrix;

public class JanNN {

    public static void main(String[] args) throws Exception {
        NNLog log = NNLog.getLogger("Performance Notes Gemischt.md");

        String path = "src/JanNN/NetworksPersitance/NetwerkMit!!!Epochen.json";
        String FileNameVisualization = "Visualization.md";

        int splitIndex = 60000; // 60.000
        int TrainingCycles = 5;
        double learnRate = 0.25;
        int BatchSize = 50;
        NeuralNetwork nn = new NeuralNetwork(learnRate, 784, 300, 100, 10);
        
        DecimalFormat df = new DecimalFormat("0.00");

        //nn = new NeuralNetwork(learnRate, path);

        MnistBuffer mBuffer = new MnistBuffer();
        mBuffer.loadMNIST();
        mBuffer.splitData(splitIndex);
        for(int i=0; i<(splitIndex*TrainingCycles/BatchSize); i++){
            MnistMatrix[] MnistMatrix = mBuffer.getBatch(BatchSize);
            nn.learn(MnistMatrix);
            System.out.print('\r');
            System.out.print(df.format((double) i / (splitIndex*TrainingCycles/BatchSize-1) * 100) + "%");
        }
        int rest = splitIndex*TrainingCycles%BatchSize;
        if(rest != 0){nn.learn(mBuffer.getBatch(rest));
        System.out.print('\r');
        System.out.print("100.00%   "+rest);}

        //nn = new NeuralNetwork(learnRate, path);

        LinkedList<Integer> wrongList = new LinkedList<Integer>();

        System.out.println();
        log.add(nn.toString());
        log.add(mBuffer.getTrainingsDataLength());
        log.add(TrainingCycles);
        log.add(BatchSize);
        log.add(learnRate);
        log.add(NNUtil.getAcuracy(nn, mBuffer.getTrainingsData()));
        log.add(NNUtil.getAcuracy(nn, mBuffer.getTestData(), wrongList));

        MnistMatrix[] wrongMatrixs = new MnistMatrix[wrongList.size()];
        for(Integer i  =0; i<wrongList.size();i++){
            wrongMatrixs[i] = mBuffer.getTestData()[wrongList.get(i)];
        }

        

        log.WipeFile(FileNameVisualization);
        log.log(FileNameVisualization,
                "| HLayersSizes |DataSize| Epochen | BatchSize | Learnrate | ACtrainingD | ACtestD |\n|----------|----------|----------|----------|----------|----------|----------|\n"
                        + log.messageToString(),
                false);
        log.log(FileNameVisualization, "# Trainings Daten\n", false);
        log.printDataPoints(Arrays.copyOfRange(mBuffer.getTrainingsData(), 1, 10), FileNameVisualization, nn); // 5 Bilder aus den Trainings Data werden ausgegeben
        log.log(FileNameVisualization, "# Test Daten\n", false);
        log.printDataPoints(Arrays.copyOfRange(mBuffer.getTestData(), 1, 10), FileNameVisualization, nn); // 5 Bilder aus den Test Data werden ausgegeben
        log.log(FileNameVisualization, "# Wrong Examples\n", false);
        log.printDataPoints(Arrays.copyOfRange(wrongMatrixs, 1, 20), FileNameVisualization, nn); // 5 Bilder aus den Test Data werden ausgegeben

        log.flush();

        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));

        // log.saveNetwork(nn.layers, path);
        // nn = new NeuralNetwork(learnRate, path);
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
    }
}

class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}