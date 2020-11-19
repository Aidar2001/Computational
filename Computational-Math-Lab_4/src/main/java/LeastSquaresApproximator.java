import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import math.equation.nonlinear.OneFunction;
import slau.decision.GaussMethod;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class LeastSquaresApproximator {
    private Output output;
    private List<Double> X, Y;
    private double maxDegree;

    public LeastSquaresApproximator(List<Double> x, List<Double> y, double maxDegree) {
        X = x;
        Y = y;
        this.maxDegree = maxDegree;
    }

    // определение коэффицентов: a,b,c
    public static List<Number> approx(List<Double> X, List<Double> Y, double maxDegree) {
        // X
        List<List<Number>> coeffMatrix = new ArrayList<>();
        List<Number> line;
        for (int i = 0; i <= maxDegree; i++) {
            line = new ArrayList<>();
            for (int j = 0; j <= maxDegree; j++) {
                if (i == 0 && j == 0) {
                    line.add((double) X.size());
                    continue;
                }
                line.add(Utils.Summator.sumXinDegree(X, i + j));
            }
            coeffMatrix.add(line);
        }
        // b_i
        List<Number> interceptTermColumn = new ArrayList<>();
        for (int i = 0; i <= maxDegree; i++) {
            interceptTermColumn.add(Utils.Summator.sumXinDegreeY(X, Y, i));
        }
        return GaussMethod.solve(coeffMatrix, interceptTermColumn);
    }

    // note определение коэффицентов (a,b,c) + расчет среднеквадратичного отклонения(sigma) и меры отклонения(S)
    public Output approxAndCalcStatistics(Functions function) {
//        System.out.println("X| "+X+"\nY| "+ Y);
        List<Number> coeffs = approx(X, Y, maxDegree);
        if (function == Functions.LINE || function == Functions.QUADRA || function == Functions.LOG){
            Collections.reverse(coeffs);
        }
        output = new Output(function);
        output.setA(coeffs.get(0).doubleValue());
        output.setB(coeffs.get(1).doubleValue());
        if (coeffs.size() >= 3) {
            output.setC(coeffs.get(2).doubleValue());
        }
        output.setSigma(Utils.calculateSigma(X,Y,coeffs,function));
        output.setSumDeviation(Utils.Summator.sumS(X,Y,coeffs,function));
        return output;
    }

    // класс для хранения выходных данных
    @AllArgsConstructor
    @NoArgsConstructor
    public class Output {
        @Getter
        private double a, b, c, sumDeviation, sigma;
        private final List<Number> output = new ArrayList<>(Collections.nCopies(5,0.0));
        private Functions function;

        public Output(Functions function) {
            this.function = function;
        }

        public List<Number> getAsList(){return Collections.unmodifiableList(output);}

        public void setC(double c) {
            this.c = c;
            output.set(4,c);
        }

        public void setA(double a) {
            this.a = a;
            output.set(2,a);
        }

        public void setB(double b) {
            this.b = b;
            output.set(3,b);
        }

        public void setSumDeviation(double sumDeviation) {
            this.sumDeviation = sumDeviation;
            output.set(0, sumDeviation);
        }

        public void setSigma(double sigma) {
            this.sigma = sigma;
            output.set(1,sigma);
        }

        public void updateStats() {
            setSigma(Utils.calculateSigma(InputData.LINE_X.getLine(),InputData.LINE_Y.getLine(),output.subList(2,5),function));
            setSumDeviation(Utils.Summator.sumS(InputData.LINE_X.getLine(),InputData.LINE_Y.getLine(),output.subList(2,5),function));
        }
    }

    public static void main(String[] args) {
        Reader.read();
        // линенийная
        LeastSquaresApproximator approximator = new LeastSquaresApproximator(InputData.LINE_X.getLine(),InputData.LINE_Y.getLine(),1);
        Output output  = null;
        testTen(1.6854, 1.2168, 0.0, approximator, Functions.LINE);
        // полином 2-й степени
        approximator.setMaxDegree(2);
        testTen(-0.0589,2.1974, 0.3743, approximator, Functions.QUADRA);
        // экспонента
        approximator.setY(InputData.LINE_Y.getLine().stream().map(Math::log).collect(Collectors.toList()));
        approximator.setMaxDegree(1);
        testTen(2.7309,0.2346,0.0,approximator,Functions.EXPON);
        // степенная функция
        approximator.setX(InputData.LINE_X.getLine().stream().map(Math::log).collect(Collectors.toList()));
        testTen(2.5421,0.8382, 0.0, approximator,Functions.POW);
        // логарифмическая
        approximator.setY(InputData.LINE_Y.getLine());
        testTen(5.6500, 1.1989, 0.0 , approximator, Functions.LOG);
    }

    private static boolean testTen(double needA, double needB, double needC, LeastSquaresApproximator approximator, Functions line) {
        Output output = null;
        boolean success = true;
        for (int i = 0; i <= 10; i++) {
            output = approximator.approxAndCalcStatistics(line);
            double a = new BigDecimal(output.a).setScale(4, RoundingMode.HALF_EVEN).doubleValue();
            double b = new BigDecimal(output.b).setScale(4, RoundingMode.HALF_EVEN).doubleValue();
            double c = new BigDecimal(output.c).setScale(4, RoundingMode.HALF_EVEN).doubleValue();
            success &= a == needA && b == needB && c == needC;
            System.out.println("a:" + a + "\tb:" + b + "\tc:" + c);
        }
        System.out.println(line.name() + "--" + success);
        return success;
    }

}
