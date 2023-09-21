package JanNN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import MNISTReader.MNISTPrinter;
import MNISTReader.MnistMatrix;

public class NNLog {

    static NNLog logger;
    String message = "|";
    String filePath = "Obsidian/Notes/NN Ausgaben/";
    String fileName;
    File file;
    File sideFile = new File(filePath + "SideNote.md");
    File NetworkPath;
    boolean append = true;

    public NNLog(){}

    public NNLog(String fileName){
        this.fileName = fileName;
        file = new File(filePath + fileName);
    }

    public static NNLog getLogger(){
        return getLogger("Obsidian/Notes/Projektarbeit/");
    }

    public static NNLog getLogger(String fileName) {
        if (logger == null) {
            logger = new NNLog(fileName);
        }
        return logger;
    }

    public void log(String message) {
        try {
            FileWriter writer = new FileWriter(file, append); // Öffne im Append-Modus
            writer.write("\n" + message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }

    public void log(String FileName, String message, boolean SystemOut) {
        File file = new File(filePath + FileName);
        if(SystemOut){
            System.out.println(message);
        }
        try {
            FileWriter writer = new FileWriter(file, append); // Öffne im Append-Modus
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

    public String messageToString() {
        return message;
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
            bw.write(layers.length + "\n");

            for (Layer layer : layers) {
                int length = layer.weights.length;
                int width = layer.weights.width;
                int biasSize = layer.biases.length;
                bw.write("<layer>");
                bw.write(layer.numInputNodes + ",");
                bw.write(layer.numOutputNodes + "\n");
                for (int i = 0; i < length; i++) {
                    bw.write("\t<row>");
                    bw.write("" + layer.weights.getValue(i, 0));
                    for (int j = 1; j < width; j++) {
                        bw.write("," + layer.weights.getValue(i, j));
                    }
                    bw.write("\n");
                }
                bw.write("<biases>");
                bw.write("" + layer.biases.getValue(0));
                for (int i = 1; i < biasSize; i++) {
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

    private void setAppend(boolean append){
        this.append = append;
    }
    public void WipeFile(String fileName){
        setAppend(false);
        log(fileName, "", false);
        setAppend(true);
    }

    public void WipeHTMLFile(String fileName){
        setAppend(false);
        log(fileName, header(), false);
        setAppend(true);
    }

    public void printDataPoints(MnistMatrix[] PrintData, String fileName, NeuralNetwork nn) {
        for (MnistMatrix matrix : PrintData) {
            int Label = matrix.getLabel();
            double[] querry = nn.Querry(matrix.getInputs());
            int correctPrediction = NNUtil.CorrectLabel(querry, Label);

            if(correctPrediction == Label){
                log(fileName, "<H2 style=\"color:green;\">Prediction "+correctPrediction+"</H2>"+"\n", false);
            }else{
                log(fileName, "<H2 style=\"color:red;\">Prediction "+correctPrediction+"</H2>"+"\n", false);
            }

            //log(fileName, "### Label "+matrix.getLabel()+"\n", false);
            log(fileName, "|0|1|2|3|4|5|6|7|8|9|\n|-|-|-|-|-|-|-|-|-|-|", false);
            log(fileName, NNUtil.ArrayToString(querry, true)+"\n", true);
            log(fileName, NNUtil.HighestLabelToString(querry), true);
            log(fileName,"\n```",false);
            log(fileName, MNISTPrinter.printMnistMatrix(matrix), true);
            log(fileName,"```\n\n",false);
        }
    }

    public void printHTML(MnistMatrix[] PrintData, String fileName, NeuralNetwork nn) {
        for (MnistMatrix matrix : PrintData) {
            int Label = matrix.getLabel();
            double[] querry = nn.Querry(matrix.getInputs());
            int correctPrediction = NNUtil.CorrectLabel(querry, Label);

            if(correctPrediction == Label){
                log(fileName, "<H2 style=\"color:green;\">Prediction "+correctPrediction+"</H2>"+"\n", false);
            }else{
                log(fileName, "<H2 style=\"color:red;\">Prediction "+correctPrediction+"</H2>"+"\n", false);
            }

            log(fileName, tableHTML(), false);
            log(fileName, ArrayToStringHTML(querry, true)+"\n", true);
            log(fileName, NNUtil.HighestLabelToString(querry), true);
            log(fileName,"\n",false);
            log(fileName, MNISTPrinter.printMnistHTML(matrix), true);
            log(fileName,"\n\n",false);
        }
    }

    private String tableHTML(){
        return "<table>\n" + //
                "\t\t\t\t<tr>\n" + //
                "\t\t\t\t\t<td>0</td>\n" + //
                "\t\t\t\t\t<td>1</td>\n" + //
                "\t\t\t\t\t<td>2</td>\n" + //
                "\t\t\t\t\t<td>3</td>\n" + //
                "\t\t\t\t\t<td>4</td>\n" + //
                "\t\t\t\t\t<td>5</td>\n" + //
                "\t\t\t\t\t<td>6</td>\n" + //
                "\t\t\t\t\t<td>7</td>\n" + //
                "\t\t\t\t\t<td>8</td>\n" + //
                "\t\t\t\t\t<td>9</td>\n" + //
                "\t\t\t\t</tr>";
    }

    private String header(){
        return "<head>\n" + //
                "\t<title>Visualization</title>\n" + //
                "\t<meta charset=\"UTF-8\">\n" + //
                "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Lato\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" + //
                "\t<style>\n" + //
                "\t\tbody {\n" + //
                "\t\t\tfont-family: \"Lato\", sans-serif;\n" + //
                "\t\t\tcolor: #85b1b0;\n" + //
                "\t\t\tbackground-color: #231a10;\n" + //
                "\t\t}\n" + //
                "\n" + //
                "\t\ttable,\n" + //
                "\t\ttr,\n" + //
                "\t\ttd {\n" + //
                "\t\t\tborder: 1px solid;\n" + //
                "\t\t\tborder-collapse: collapse;\n" + //
                "\t\t\tmargin: 5px;\n" + //
                "\t\t\tpadding: 5px;\n" + //
                "\t\t}\n" + //
                "\n" + //
                "\t\t.Kasten {\n" + //
                "\t\t\tborder: 1px solid;\n" + //
                "\t\t\tmargin: 5px;\n" + //
                "\t\t\tpadding: 5px;\n" + //
                "\t\t\tdisplay: inline-block;\n" + //
                "\n" + //
                "\t\t}\n" + //
                "\n" + //
                "\t\ttd {\n" + //
                "\t\t\ttext-align: center;\n" + //
                "\t\t}\nh1 {\n" + //
                "\t\t\tcolor: #e5395a;\n" + //
                "\t\t}" + //
                "\t</style>\n" + //
                "</head>";
    }

    public static String ArrayToStringHTML(double[] arr, boolean formatter) {
        String result = "<tr>\n" + "<td>";
        DecimalFormat df = new DecimalFormat("0.0");
        for (int i = 0; i < arr.length - 1; i++) {
            if (formatter)
                result += (df.format(arr[i]*100))+"%";
            else
                System.out.print(arr[i]);
            result += ("</td><td>");
        }
        if (formatter) {
            result += (df.format(arr[arr.length - 1]*100))+"%";
        } else {
            result += (arr[arr.length - 1]);
        }
        result += ("</td></tr></table>");
        return result;
    }

}
