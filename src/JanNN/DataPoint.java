package JanNN;

public class DataPoint {
    double[] inputs;
    double[] expectedOutput;

    public DataPoint(double[] inputs, double[] expectedOutput){
        this.inputs = inputs;
        this.expectedOutput = expectedOutput;
    }

    public double[] getExpectedOutput() {
        return expectedOutput;
    }

    public double[] getInputs() {
        return inputs;
    }
    
}
