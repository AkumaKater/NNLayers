package JanNN;

import java.text.DecimalFormat;
import java.util.Arrays;

import MNISTReader.MnistMatrix;

public class JanNN {

    public static void main(String[] args) throws Exception {
        NNLog log = NNLog.getLogger("Performance Notes Gemischt.md");

        String path = "src/JanNN/NetworksPersitance/NetwerkMit50Epochen.json";
        String FileNameVisualization = "Visualization.md";

        int splitIndex = 60000; // 60.000
        int TrainingCycles = 1;
        double learnRate = 0.25;
        int BatchSize = 50;
        NeuralNetwork nn = new NeuralNetwork(learnRate, 784, 300, 100, 10);

        DataLoader dataLoader = new DataLoader(splitIndex);

        MnistMatrix[] TrainingData = dataLoader.getTrainingsData();
        MnistMatrix[] TestData = dataLoader.getTestData();

        DecimalFormat df = new DecimalFormat("0.00");

        DataPoint[] Batch = new DataPoint[BatchSize];
        int index = 0;
        int mnistIndex = TrainingData.length - 1;
        for (int j = 0; j < (TrainingCycles * TrainingData.length); j++) {
            Batch[index] = new DataPoint(TrainingData[mnistIndex].getInputs(), TrainingData[mnistIndex].getTargets());
            index++;
            mnistIndex--;
            if (mnistIndex < 0) {
                mnistIndex = TrainingData.length - 1;
            }
            if (index == BatchSize) {
                nn.learn(Batch, learnRate);
                index = 0;
            }
            System.out.print('\r');
            System.out.print(df.format(((double) j / (double) (TrainingCycles * TrainingData.length)) * 100) + "%");
        }

        System.out.println();
        log.add(nn.toString());
        log.add(TrainingData.length);
        log.add(TrainingCycles);
        log.add(BatchSize);
        log.add(learnRate);
        log.add(NNUtil.getAcuracy(nn, TrainingData));
        log.add(NNUtil.getAcuracy(nn, TestData));

        log.WipeFile(FileNameVisualization);
        log.log(FileNameVisualization, "| HLayersSizes |DataSize| Epochen | BatchSize | Learnrate | ACtrainingD | ACtestD |\n|----------|----------|----------|----------|----------|----------|----------|\n"+log.messageToString(), false);
        log.log(FileNameVisualization, "# Trainigs Daten\n", false);
        log.printDataPoints(Arrays.copyOfRange(TrainingData, 1, 10), FileNameVisualization, nn); //5 Bilder aus den Trainings Data werden ausgegeben
        log.log(FileNameVisualization, "# Test Daten\n", false);
        log.printDataPoints(Arrays.copyOfRange(TestData, 1, 10), FileNameVisualization, nn); //5 Bilder aus den Test Data werden ausgegeben

        log.flush();

        //System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        //System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        //System.out.println(NNUtil.getAcuracy(nn, TrainingData));

        //log.saveNetwork(nn.layers, path);
        //nn = new NeuralNetwork(learnRate, path);
        //System.out.println(NNUtil.getAcuracy(nn, TrainingData));
    }
}

class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}