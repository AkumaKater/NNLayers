import JanNN.Matrix;

public class All {

    
    public static void main(String[] args) {

        double[][] pix = {{1,2,3}, {3,2,1}};
        Matrix matrix = new Matrix(pix);
        Matrix trans = matrix.T();



        System.out.println(matrix.toString());
        System.out.println(trans.toString());
        System.out.println(trans.T().toString());
    }
}
