package lab4;

import java.util.List;

public class Utils {
    public static double calculateSigma(List<Double> X, List<Double> Y, List<Number> coeffs, Functions function) {
        return Math.sqrt(Summator.sumS(X, Y, coeffs, function) / X.size());
    }

    public static class Summator {
        public static double sum(List<Double> X) {
            return X.stream().reduce(Double::sum).get();
        }

        public static double sumXinDegree(List<Double> X, double degree) {
            return X.stream().map(o -> Math.pow(o, degree)).reduce(Double::sum).get();
        }

        public static double sumXY(List<Double> X, List<Double> Y) {
            return X.stream().map(o -> o * Y.get(X.indexOf(o))).reduce(Double::sum).get();
        }

        public static double sumXinDegreeY(List<Double> X, List<Double> Y, double degree) {
            return X.stream().map(o -> Math.pow(o, degree) * Y.get(X.indexOf(o))).reduce(Double::sum).get();
        }

        public static double sumS(List<Double> X, List<Double> Y, List<Number> coeffs, Functions function) {
            double s;
            switch (function) {
                case QUADRA:
                    s = X.stream()
                            .map(o -> Math.pow(function.calculate(coeffs.get(0).doubleValue(),
                                    coeffs.get(1).doubleValue(), coeffs.get(2).doubleValue(), o) - Y.get(X.indexOf(o)), 2))    // note (phi(x)-y)^2, phi - полиномиальная 2-ой степени
                            .reduce(Double::sum).get();
                    break;
                default:
                    s = X.stream()
                            .map(o -> Math.pow(function.calculate(coeffs.get(0).doubleValue(), coeffs.get(1).doubleValue(), o) - Y.get(X.indexOf(o)), 2))    // note (phi(x)-y)^2
                            .reduce(Double::sum).get();
            }
            return s;
        }
    }
}
