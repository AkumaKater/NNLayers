package MNISTReader;

public class MnistMatrix {

    private int [][] data;

    private int nRows;
    private int nCols;

    private int label;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new int[nRows][nCols];
    }

    public int getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, int value) {
        data[row][col] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nCols;
    }

    public double[] getInputs(){
        double[] inputs = new double[data.length*data[0].length];
        for(int i = 0; i<data.length;i++){
            for(int j=0; j<data[0].length;j++){
                int position = (i*data.length)+(j);
                inputs[position] = (double)data[i][j];
            }
        }
        return inputs;
    }

    public double[] getTargets(){
        double[] targets = new double[10];
        targets[label] = 1.0;
        return targets;
    }

    public boolean equalsData(MnistMatrix other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (this.nRows != other.nRows || this.nCols != other.nCols) {
            return false;
        }

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (this.data[i][j] != other.data[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

}