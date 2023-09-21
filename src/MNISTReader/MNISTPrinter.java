package MNISTReader;

public class MNISTPrinter {

    static String[] Helligkeit = { " ", ".", ",", ":", ";", "_", "~", "^", "\"", ";", "-", "=", "*", "+", "#", "%",
            "@" };
    static double part = 256.0 / (Helligkeit.length);

    public static String printMnistMatrix(final MnistMatrix matrix) {
        String result = "";
        result +=("label: " + matrix.getLabel() + " ");
        for (int r = 0; r < matrix.getNumberOfRows() * 2 - 7; r++) {
            result +=("-");
        }
        result +="\n";
        for (int r = 0; r < matrix.getNumberOfRows(); r++) {
            result +=("|");
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                int hell = (int) (matrix.getValue(r, c) / part);
                // if(hell>16){hell=16;}
                result +=(Helligkeit[hell]);
                result +=(Helligkeit[hell]);
            }
            result +=("|");
            result +="\n";
        }
        for (int r = -1; r < matrix.getNumberOfRows(); r++) {
            result +=("--");
        }
        return result;
    }

    public static String printMnistHTML(final MnistMatrix matrix) {
        String result = "<div>\n<pre class=\"Kasten\">";
        result +=("label: " + matrix.getLabel() + " ");
        result +="\n";
        for (int r = 0; r < matrix.getNumberOfRows(); r++) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                int hell = (int) (matrix.getValue(r, c) / part);
                // if(hell>16){hell=16;}
                result +=(Helligkeit[hell]);
                result +=(Helligkeit[hell]);
            }
            result +="\n";
        }
            result += "</pre>\n</div>";
        return result;
    }
}
