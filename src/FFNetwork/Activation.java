package FFNetwork;

public abstract class Activation {
    public abstract double ActivationFunction(double weightedInput);
    public abstract double ActivationDerivative(double weightedInput);

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
    public double ActivationFunction(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
    public double ActivationDerivative(double weightedInput) {
        double activation = ActivationFunction(weightedInput);
        return activation * (1.0 - activation);
    }
}

class ReLu extends Activation{
    public double ActivationFunction(double weightedInput) {
        return Math.max(0, weightedInput);
    }
    public double ActivationDerivative(double weightedInput) {
        if (weightedInput <= 0)
            return 0.0;
        else
            return 1.0;
    }
}