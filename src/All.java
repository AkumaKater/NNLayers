import java.io.IOException;
import java.text.DecimalFormat;

import MNISTReader.MnistDataReader;
import MNISTReader.MnistMatrix;

public class All {

    public static void main(String[] args) {

        String dataHome = "src/MNIST/archive/";// train-labels.idx1-ubyte
        try {
            boolean isEqual = false;
            MnistMatrix[] mnistMatrix1 = new MnistDataReader().readData(dataHome + "train-images.idx3-ubyte",
                    dataHome + "train-labels.idx1-ubyte");

            MnistMatrix[] mnistMatrix2 = new MnistDataReader().readData(dataHome + "t10k-images.idx3-ubyte",
                    dataHome + "t10k-labels.idx1-ubyte");

            int index = 0;

            for (MnistMatrix mm : mnistMatrix2) {
                for (MnistMatrix mm2 : mnistMatrix1) {
                    isEqual = mm2.equalsData(mm);
                }
                if (isEqual) {
                    throw new SchonVorhndenException("Im Dtaensatz vorhanden");
                }
                index++;
                System.out.print('\r');
                DecimalFormat df = new DecimalFormat("0.00");
                System.out.print(df.format((double)index / (double)mnistMatrix2.length*100.0) + "%");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

class SchonVorhndenException extends RuntimeException {
    public SchonVorhndenException(String message) {
        super(message);
    }
}
