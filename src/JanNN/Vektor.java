package JanNN;

public class Vektor {
    double[] Vektor;
    int length;

    public Vektor(double[] Vektor){
        this.Vektor = Vektor;
        this.length = Vektor.length;
    }

    public Vektor X(Vektor Vektor){
        if(Vektor.length!=length){throw new VektorException("Vektoren Passen nicht zusammen!");}
        double[] result = new double[length];
        for(int i=0; i<length; i++){
            result[i] = this.Vektor[i] * Vektor.getValue(i);
        }
        return new Vektor(result);
    }

    public Vektor EinsMinus(){
        double[] result = new double[length];
        for(int i=0; i<length; i++){
            result[i] = 1 - Vektor[i];
        }
        return new Vektor(result);
    }

    public double getValue(int x){
        return Vektor[x];
    }

    public Matrix OneByOneToMatrix(Vektor vektor){
        double[][] matrix = new double[length][vektor.length];
        for(int i=0; i<length; i++){
            for(int j=0; j<vektor.length;j++){
                matrix[i][j] = getValue(i) * vektor.getValue(j);
            }
        }
        return new Matrix(matrix);
    }
    
}

class VektorException extends RuntimeException {
    public VektorException(String message) {
        super(message);
    }
}