package SebNN;
public class DataPoint {
    
    double[] inputs;
    double[] expectedOutput;
    int label;
    
    public DataPoint(double[] inputs, int label, int numlaLabels)
    {
        this.inputs = inputs;
        this.label = label;
        this.expectedOutput = CreateOneHot(label, numlaLabels);
    }
    
    public static double[] CreateOneHot(int index, int num){
        double[] oneHot = new double[num];
        oneHot[index] = 1;
        return oneHot;
    }
}

