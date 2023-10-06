package MNISTReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

//import FFNetworkNoBiasNoBatch.*;

import FFNetwork.*;

public class MNISTHTML {
    String html;
    String log;
    LinkedList<String> endTags = new LinkedList<String>();

    public static void main(String[] args) {
        MNISTHTML mn = new MNISTHTML();
        System.out.println(mn.html);
    }

    public MNISTHTML() {
        setHtml();
    }

    public MNISTHTML setHeader(String accTrain, String accTest) {
        ConfigLoader conf = ConfigLoader.getConfig();
        String[] erg = { "HLayersSizes", "DataSize", "Epochen", "BatchSize", "Learnrate", "ACtrainingD", "ACtestD" };
        String[] con = { Arrays.toString(conf.getLayers()), "" + conf.getSplitIndex(), "" + conf.getTrainingCycles(),
                "" + conf.getBatchSize(), "" + conf.getLearnRate(), accTrain, accTest };
        tag("table").tr(erg).tr(con).end()
                .tag("a", "href=\"#Trainings Daten\"")
                .text("Trainings Daten")
                .end()
                .tag("a", "href=\"#Test Daten\"")
                .text("Test Daten")
                .end();

        return this;
    }

    public MNISTHTML log(String accTrain, String accTest) {
        ConfigLoader conf = ConfigLoader.getConfig();
        File f = new File(conf.getValue("PerformanceNotes"));
        System.out.println(f.getAbsolutePath());
        if (f.exists() && !f.isDirectory()) {
            html = "";
        } else {
            String[] erg = { "HLayersSizes", "DataSize", "Epochen", "BatchSize", "Learnrate", "ACtrainingD",
                    "ACtestD" };
            tag("table").tr(erg);
        }
        String[] con = { Arrays.toString(conf.getLayers()), "" + conf.getSplitIndex(),
                "" + conf.getTrainingCycles(),
                "" + conf.getBatchSize(), "" + conf.getLearnRate(), accTrain, accTest };
        tr(con);

        try {
            FileWriter writer = new FileWriter(f, true);
            writer.write(getHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }

        return this;
    }

    public MNISTHTML tag(String tag) {
        html += "<" + tag + ">";
        endTags.add(tag);
        return this;
    }

    public MNISTHTML tag(String tag, String mod) {
        html += "<" + tag + " " + mod + ">";
        endTags.add(tag);
        return this;
    }

    public MNISTHTML end() {
        html += "</" + endTags.removeLast() + ">";
        return this;
    }

    public MNISTHTML tr(String[] erg) {
        tag("tr");
        for (int i = 0; i < erg.length; i++) {
            tag("td");
            html += erg[i];
            end();
        }
        end();
        return this;
    }

    public MNISTHTML tr(double[] erg, boolean formatter) {
        tag("tr");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < erg.length; i++) {
            tag("td");
            html += df.format(erg[i] * 100) + "%";
            end();
        }
        end();
        return this;
    }

    public MNISTHTML tr(double[] erg) {
        tag("tr");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < erg.length; i++) {
            tag("td");
            html += df.format(erg[i]) + "%";
            end();
        }
        end();
        return this;
    }

    public MNISTHTML h1(String title) {
        tag("h1");
        html += title;
        end();
        return this;
    }

    public MNISTHTML h2(String title) {
        tag("a", "name=\"" + title + "\"").end()
                .tag("h2");
        html += title;
        end();
        return this;
    }

    public MNISTHTML h3(String title, boolean correct) {
        if (correct) {
            html += "<H3 style=\"color:green;\">";
            html += "Predciction " + title;
            html += "</H3>";
        } else {
            html += "<H3 style=\"color:red;\">";
            html += "Predciction " + title;
            html += "</H3>";
        }
        return this;
    }

    public String getHtml() {
        return html;
    }

    public void writeHTML() {
        File file = new File(ConfigLoader.getConfig().getValue("Visualization"));
        try {
            System.out.println(file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write(getHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }

    public MNISTHTML preTex(String text) {
        tag("pre");
        html += text;
        end();
        return this;
    }

    public MNISTHTML text(String text) {
        html += text;
        return this;
    }

    public MNISTHTML printMatrix(MnistMatrix matrix, NeuralNetwork nn) {
        String[] s = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        double[] querry = nn.Querry(matrix.getInputs());
        int Label = matrix.getLabel();
        int correctPrediction = CorrectLabel(querry, Label);
        boolean correct = (Label == correctPrediction);
        tag("div")
                .h3("" + correctPrediction, correct)
                .tag("table")
                .tr(s)
                .tr(querry, true).end()
                .preTex(MNISTPrinter.printMnistHTML(matrix))
                .end();
        return this;
    }

    public MNISTHTML printMatrix(MnistMatrix[] arr, NeuralNetwork nn) {
        for (MnistMatrix mm : arr) {
            printMatrix(mm, nn);
        }
        return this;
    }

    public void setHtml() {
        this.html = "<head>\n" + //
                "\t<title>" + ConfigLoader.getConfig().getValue("PerformanceNotes") + "</title>\n" + //
                "\t<meta charset=\"UTF-8\">\n" + //
                "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Lato\">\n" + //
                "\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n"
                + //
                "\t<style>\n" + //
                "a{margin: 5px;}" + //
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
                "\t\t}\n" + //
                "h1 {\n" + //
                "\t\t\tcolor: #e5395a;\n" + //
                "\t\t}\t</style>\n" + //
                "</head>";
    }

    public static int CorrectLabel(double[] numbers, int label) {
        int maxNumberIndex = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > numbers[maxNumberIndex]) {
                maxNumberIndex = i;
            }
        }
        return maxNumberIndex;
    }
}
