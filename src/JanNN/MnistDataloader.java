package JanNN;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MnistDataloader {
    private String trainingImagesFilepath;
    private String trainingLabelsFilepath;
    private String testImagesFilepath;
    private String testLabelsFilepath;

    public MnistDataloader(String trainingImagesFilepath, String trainingLabelsFilepath,
                           String testImagesFilepath, String testLabelsFilepath) {
        this.trainingImagesFilepath = trainingImagesFilepath;
        this.trainingLabelsFilepath = trainingLabelsFilepath;
        this.testImagesFilepath = testImagesFilepath;
        this.testLabelsFilepath = testLabelsFilepath;
    }

    public int[][] readImages(String imagesFilepath) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(imagesFilepath));
        int magicNumber = dataInputStream.readInt();
        int numberOfImages = dataInputStream.readInt();
        int numberOfRows = dataInputStream.readInt();
        int numberOfColumns = dataInputStream.readInt();

        int[][] images = new int[numberOfImages][numberOfRows * numberOfColumns];

        for (int i = 0; i < numberOfImages; i++) {
            for (int j = 0; j < numberOfRows * numberOfColumns; j++) {
                images[i][j] = dataInputStream.readUnsignedByte();
            }
        }

        dataInputStream.close();
        return images;
    }

    public int[] readLabels(String labelsFilepath) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(labelsFilepath));
        int magicNumber = dataInputStream.readInt();
        int numberOfLabels = dataInputStream.readInt();

        int[] labels = new int[numberOfLabels];

        for (int i = 0; i < numberOfLabels; i++) {
            labels[i] = dataInputStream.readUnsignedByte();
        }

        dataInputStream.close();
        return labels;
    }

    public int[][] loadTrainingData() throws IOException {
        return readImages(trainingImagesFilepath);
    }

    public int[] loadTrainingLabels() throws IOException {
        return readLabels(trainingLabelsFilepath);
    }

    public int[][] loadTestData() throws IOException {
        return readImages(testImagesFilepath);
    }

    public int[] loadTestLabels() throws IOException {
        return readLabels(testLabelsFilepath);
    }

    // Add other methods if needed
}
