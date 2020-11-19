import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static double calculateSigma(List<Double> X, List<Double> Y, List<Number> coeffs, Functions function) {
        return Math.sqrt(Summator.sumS(X, Y, coeffs, function) / X.size());
    }

    public static void main(String[] args) {
        Reader.read();
        ArrayList<Number> coeffs = new ArrayList<>(Arrays.asList(2.7309, 0.2346));
        System.out.println(Summator.sumS(InputData.LINE_X.getLine(), InputData.LINE_Y.getLine(), coeffs, Functions.EXPON));
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
//                case EXPON:
//                    s = X.stream()
//                            .map(o -> {
//                                double result = Math.pow(function.calculate(coeffs.get(0).doubleValue(), coeffs.get(1).doubleValue(), o) - Y.get(X.indexOf(o)), 2);
//                                System.out.println("(phi(x"+X.indexOf(o)+")-y"+X.indexOf(o)+")^2="+
//                                        function.calculate(coeffs.get(0).doubleValue(), coeffs.get(1).doubleValue(), o)+"-"+Y.get(X.indexOf(o))+"="+result);
//                                return result;
//                            })    // note (phi(x)-y)^2
//                            .reduce(Double::sum).get();
//                    break;
                default:
                    s = X.stream()
                            .map(o -> Math.pow(function.calculate(coeffs.get(0).doubleValue(), coeffs.get(1).doubleValue(), o) - Y.get(X.indexOf(o)), 2))    // note (phi(x)-y)^2
                            .reduce(Double::sum).get();
            }
//            System.out.println(s);
            return s;
        }
    }
}
