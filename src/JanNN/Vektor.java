package JanNN;

public class Vektor {
    double[] Vektor;
    int length;

    public Vektor(double[] Vektor) {
        this.Vektor = Vektor;
        this.length = Vektor.length;
    }

    public Vektor(int length, boolean random) {
        setVektor(length, random);
    }

    public Vektor(int length) {
        setVektor(length, false);
    }

    public void setVektor(int length, boolean random) {
        if (random) {
            this.Vektor = NNUtil.generateRandomBiasas(length);
        } else {
            this.Vektor = new double[length];
        }
        this.length = length;
    }

    public Vektor X(Vektor Vektor) {
        if (Vektor.length != length) {
            throw new VektorException("Vektoren Passen nicht zusammen!");
        }
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = this.Vektor[i] * Vektor.getValue(i);
        }
        return new Vektor(result);
    }

    public Vektor EinsMinus() {
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = 1 - Vektor[i];
        }
        return new Vektor(result);
    }

    public double getValue(int x) {
        return Vektor[x];
    }

    public void setValue(int x, double value) {
        Vektor[x] = value;
    }

    public Matrix OneByOneToMatrix(Vektor vektor) {
        double[][] matrix = new double[length][vektor.length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < vektor.length; j++) {
                matrix[i][j] = getValue(i) * vektor.getValue(j);
            }
        }
        return new Matrix(matrix);
    }

    public void MinusVektor(Vektor vektor, double learnrate) {
        if (this.length != vektor.length)
            throw new MatrixException(this.length + " / " + vektor.length);
        for (int i = 0; i < length; i++) {
            this.Vektor[i] -= vektor.getValue(i) * learnrate;
        }
    }

}

class VektorException extends RuntimeException {
    public VektorException(String message) {
        super(message);
    }
}