package MNISTReader;

public class MNISTPrinter {

    static String[] Helligkeit = {" ", ".", ",", ":", ";", "_", "~", "^", "\"",";", "-", "=", "*", "+", "#", "%", "@"};
    static double part = 256.0/(Helligkeit.length);
    
    public static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.print("label: " + matrix.getLabel()+" ");
        for (int r = 0; r < matrix.getNumberOfRows()*2-7; r++ ) {System.out.print("-");}
        System.out.println();
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            System.out.print("|");
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                int hell = (int)(matrix.getValue(r, c)/part);
                //if(hell>16){hell=16;}
                System.out.print(Helligkeit[hell]);
                System.out.print(Helligkeit[hell]);
            }
            System.out.print("|");
            System.out.println();
        }
        for (int r = -1; r < matrix.getNumberOfRows(); r++ ) {System.out.print("--");}
        System.out.println();
        System.out.println();
    }
}
