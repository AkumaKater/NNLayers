package JanNN;

import java.text.DecimalFormat;

public class Matrix {
    private double[][] matrix;
    public int length;
    public int width;

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        setLengthWidth();
    }

    public Matrix(int numOutputNodes, int numInputNodes) {
         setMatrix( numOutputNodes,  numInputNodes, false); 
    }
    public Matrix(int numOutputNodes, int numInputNodes, boolean random) {
        setMatrix( numOutputNodes,  numInputNodes, random); 
    }

    public void setMatrix(int numOutputNodes, int numInputNodes, boolean random) {
        if (random) {
            this.matrix = NNUtil.generateRandomWeights(numOutputNodes, numInputNodes);
            setLengthWidth();
        }else{
            this.matrix = new double[numOutputNodes][numInputNodes];
            setLengthWidth();
        }
    }

    public Matrix Dot(double[][] matrix) {
        return new Matrix(NNUtil.DotProdukt(this.matrix, matrix));
    }

    public Matrix Dot(double[] vektor) {
        return new Matrix(NNUtil.DotProdukt(this.matrix, vektor));
    }

    public Matrix Dot(Vektor vektor) {
        return new Matrix(NNUtil.DotProdukt(this.matrix, vektor.Vektor));
    }

    public double[] DotVektor(double[] vektor) {
        if (width != vektor.length)
            throw new MatrixException("DotVektor unpassende Dimensionen: " + width + " / " + vektor.length);
        return NNUtil.MatrixToVektor(new Matrix(NNUtil.DotProdukt(this.matrix, vektor)).matrix);
    }

    public double[] DotVektorTranspose(double[] vektor) {
        return NNUtil.MatrixToVektor(new Matrix(NNUtil.DotProdukt(Transpose(), vektor)).matrix);
    }

    public String DimensionsToString() {
        String result = ("Dimensionen der Matrix: " + matrix.length + "X" + matrix[0].length);
        return result;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean formatter) {
        String result = "";
        DecimalFormat df = new DecimalFormat("0.000");
        for (int j = 0; j < length; j++) {
            result += ("[");
            for (int i = 0; i < width - 1; i++) {
                if (formatter) {
                    result += (df.format(getValue(j, i)));
                } else {
                    result += (getValue(j, i));
                }
                result += (", ");
            }
            if (formatter) {
                result += (df.format(getValue(j, width - 1)));
            } else {
                result += getValue(j, width - 1);
            }
            result += ("]");
        }
        return result;
    }

    public double getValue(int row, int col) {
        return matrix[row][col];
    }

    public void setValue(int row, int col, double value) {
        matrix[row][col] = value;
    }

    public void setValues(int row, double[] values) {
        matrix[row] = values;
    }

    public void setLengthWidth() {
        this.length = matrix.length;
        this.width = matrix[0].length;
    }

    public double[][] Transpose() {
        double[][] resultMatrix = new double[width][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                resultMatrix[j][i] = matrix[i][j];
            }
        }
        return resultMatrix;
    }

    public Matrix T() {
        return new Matrix(Transpose());
    }

    public Matrix MalWert(double wert) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] *= wert;
            }
        }
        return this;
    }

    public void PlusMatrix(Matrix matrix) {
        if (this.length != matrix.length || this.width != matrix.width)
            throw new MatrixException(this.length + "X" + width + " / " + matrix.length + "X" + matrix.width);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                this.matrix[i][j] += matrix.getValue(i, j);
            }
        }
    }

    public void MinusMatrix(Matrix matrix) {
        MinusMatrix(matrix, 1.0);
    }

    public void MinusMatrix(Matrix matrix, double learnrate) {
        if (this.length != matrix.length || this.width != matrix.width)
            throw new MatrixException(this.length + "X" + width + " / " + matrix.length + "X" + matrix.width);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                this.matrix[i][j] -= matrix.getValue(i, j) * learnrate;
            }
        }
    }
}

class MatrixException extends RuntimeException {
    public MatrixException(String message) {
        super(message);
    }
}