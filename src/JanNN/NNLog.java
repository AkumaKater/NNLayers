package JanNN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NNLog {

    static NNLog logger;
    String message = "|";
    String filePath = "Obsidian/Notes/Projektarbeit/";//../NNLayers/Obsidian/Notes/Projektarbeit/
    File file = new File(filePath + "Performance Notes with Bias3.md");
    File sideFile = new File(filePath + "SideNote.md");
    File NetworkPath;

    public static NNLog getLogger() {
        if (logger == null) {
            logger = new NNLog();
        }
        return logger;
    }

    public void log(String message) {
        try {
            FileWriter writer = new FileWriter(file, true); // Öffne im Append-Modus
            writer.write("\n" + message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }

    public void add(String message) {
        this.message += message + "|";
    }

    public void add(int message) {
        this.message += message + "|";
    }

    public void add(double message) {
        this.message += message + "|";
    }

    public void flush() {
        log(message);
        message = "|";
    }

    public void sideLog(String message) {
        try {
            FileWriter writer = new FileWriter(sideFile, true); // Öffne im Append-Modus
            writer.write("\n" + message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }

    public void saveNetwork(Layer[] layers, String path) {
        NetworkPath = new File(path);
        try {
            FileWriter writer = new FileWriter(NetworkPath, false);
            writer.write("");
            writer.close();
            writer = new FileWriter(NetworkPath, true); // Öffne im Append-Modus
            BufferedWriter bw = new BufferedWriter(writer);

            bw.write("<context>");
            bw.write(layers.length+"\n");


            for (Layer layer : layers) {
                int length = layer.weights.length;
                int width = layer.weights.width;
                int biasSize = layer.biases.length;
                bw.write("<layer>");
                bw.write(layer.numInputNodes+",");
                bw.write(layer.numOutputNodes+"\n");
                for (int i = 0; i < length; i++) {
                    bw.write("\t<row>");
                    bw.write(""+layer.weights.getValue(i, 0));
                    for (int j = 1; j < width; j++) {
                        bw.write("," + layer.weights.getValue(i, j));
                    }
                    bw.write("\n");
                }
                bw.write("<biases>");
                bw.write(""+layer.biases.getValue(0));
                for(int i=1; i<biasSize;i++){
                    bw.write("," + layer.biases.getValue(i));
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }

}
