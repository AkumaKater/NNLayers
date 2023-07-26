package JanNN;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MnistDataloader {
    private String trainingImagesFilePath;
    private String trainingLabelsFilePath;
    private String testImagesFilePath;
    private String testLabelsFilePath;

    public MnistDataloader(String trainingImagesFilePath, String trainingLabelsFilePath,
                           String testImagesFilePath, String testLabelsFilePath) {
        this.trainingImagesFilePath = trainingImagesFilePath;
        this.trainingLabelsFilePath = trainingLabelsFilePath;
        this.testImagesFilePath = testImagesFilePath;
        this.testLabelsFilePath = testLabelsFilePath;
    }

    private ArrayList<Integer> readLabels(String labelsFilePath) throws IOException {
        ArrayList<Integer> labels = new ArrayList<>();
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(labelsFilePath));
        int magicNumber = dataInputStream.readInt();
        int size = dataInputStream.readInt();

        for (int i = 0; i < size; i++) {
            labels.add(dataInputStream.readUnsignedByte());
        }

        dataInputStream.close();
        return labels;
    }

    private ArrayList<int[]> readImages(String imagesFilePath) throws IOException {
        ArrayList<int[]> images = new ArrayList<>();
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(imagesFilePath));
        int magicNumber = dataInputStream.readInt();
        int size = dataInputStream.readInt();
        int rows = dataInputStream.readInt();
        int cols = dataInputStream.readInt();

        for (int i = 0; i < size; i++) {
            int[] image = new int[rows * cols];
            for (int j = 0; j < rows * cols; j++) {
                image[j] = dataInputStream.readUnsignedByte();
            }
            images.add(image);
        }

        dataInputStream.close();
        return images;
    }

    public void load_data() throws IOException {
        ArrayList<Integer> trainingLabels = readLabels(trainingLabelsFilePath);
        ArrayList<int[]> trainingImages = readImages(trainingImagesFilePath);

        // Do something with the loaded training labels and images

        ArrayList<Integer> testLabels = readLabels(testLabelsFilePath);
        ArrayList<int[]> testImages = readImages(testImagesFilePath);

        // Do something with the loaded test labels and images
    }
}
