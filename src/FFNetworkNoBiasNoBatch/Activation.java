package FFNetworkNoBiasNoBatch;

public abstract class Activation {
    public abstract double ActivationFunction(double weightedInput);
    public abstract double ActivationAbleitung(double weightedInput);

    static Activation activation = new Sigmoid();

    public static Activation geActivation(){
        return activation;
    }
    public static void setActivation(String Activation){
        switch (Activation) {
            case "Sigmoid":
                activation = new Sigmoid();
                break;
            case "ReLu":
                activation = new ReLu();
                break;
            default:
                activation = new Sigmoid();
                break;
        }
    }
}

class Sigmoid extends Activation{
    //Die Sigmoid Funktion
    public double ActivationFunction(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
    //Die Ableitung der Sigmoid Funktion
    public double ActivationAbleitung(double weightedInput) {
        double activation = ActivationFunction(weightedInput);
        return activation * (1.0 - activation);
    }
}

class ReLu extends Activation{
    public double ActivationFunction(double weightedInput) {
        return Math.max(0, weightedInput);
    }
    public double ActivationAbleitung(double weightedInput) {
        if (weightedInput <= 0)
            return 0.0;
        else
            return 1.0;
    }
}