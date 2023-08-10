package JanNN;
public class Activations {
    
    static double StepActivation(double weightedInput) {
        return (weightedInput > 0) ? 1 : 0;
    }

    static double Sigmoid(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
    static double SigmoidDerivative(double weightedInput){
        double activation = Sigmoid(weightedInput);
        return activation * (1.0 - activation);
    }

    static double HyperbolicTangent(double weightedInput) {
        double e2w = EXP(2 * weightedInput);
        return (e2w - 1) / (e2w + 1);
    }

    static double SiLU(double weightedInput) {
        return weightedInput / (1 + EXP(-weightedInput));
    }

    static double ReLU(double weightedInput) {
        return Math.max(0, weightedInput);
    }

    static double EXP(double expo){
        return Math.exp(expo);
    }

}
