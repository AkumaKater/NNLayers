package JanNN;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

import MNISTReader.MNISTHTML;
import MNISTReader.MnistBuffer;
import MNISTReader.MnistMatrix;

public class JanNN {

    public static void main(String[] args) throws Exception {
        NNLog log = NNLog.getLogger("Performance Notes Gemischt.md");

        String path = "src/JanNN/NetworksPersitance/NetwerkMit!!!Epochen.json";
        String FileNameVisualization = "Visualization.md";

        int splitIndex = 6000; // 60.000
        int TrainingCycles = 1;
        double learnRate = 0.25;
        int BatchSize = 50;
        NeuralNetwork nn = new NeuralNetwork(learnRate, 784, 80, 10);
        
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
        String accTrain = getAcuracy(nn, mBuffer.getTrainingsData());
        String accTest = getAcuracy(nn, mBuffer.getTestData(), wrongList);
        log.add(accTrain);
        log.add(accTest);

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

        FileNameVisualization = "Visualization.html";
        log.WipeHTMLFile(FileNameVisualization);

        log.log(FileNameVisualization,new MNISTHTML()
        .h1("Visualisierung der Netzwerk Ergebnisse")
        .setHeader(accTrain, accTest)
        .h2("Trainings Daten")
        .getHtml(), false);
        log.printHTML(Arrays.copyOfRange(mBuffer.getTrainingsData(), 1, 10), FileNameVisualization, nn); // 5 Bilder aus den Trainings Data werden ausgegeben
        log.log(FileNameVisualization, "<H2>Test Daten</H2>\n", false);
        log.printHTML(Arrays.copyOfRange(mBuffer.getTestData(), 1, 10), FileNameVisualization, nn); // 5 Bilder aus den Test Data werden ausgegeben
        log.log(FileNameVisualization, "<H2>Wrong Examples</H2>\n", false);
        log.printHTML(Arrays.copyOfRange(wrongMatrixs, 1, 20), FileNameVisualization, nn); // 5 Bilder aus den Test Data werden ausgegeben

        log.flush();

        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));

        // log.saveNetwork(nn.layers, path);
        // nn = new NeuralNetwork(learnRate, path);
        // System.out.println(NNUtil.getAcuracy(nn, TrainingData));
    }

    public static String getAcuracy(NeuralNetwork nn, MnistMatrix[] mnistMatrix, LinkedList<Integer> wrongList) {
        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            double[] querry = nn.Querry(mnistMatrix[i].getInputs());
            int Hindex = NNUtil.getHighestIndex(querry);
            int label = mnistMatrix[i].getLabel();
            if (Hindex == label) {
                correct++;
            }else{
                wrongList.add(i);
            }
        }
        acuracy = (double) correct / (double) iterations * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(acuracy) + "%";
    }

    public static String getAcuracy(NeuralNetwork nn, MnistMatrix[] mnistMatrix) {
        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int Hindex = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if (Hindex == label) {
                correct++;
            }
        }
        acuracy = (double) correct / (double) iterations * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(acuracy) + "%";
    }
}

class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}