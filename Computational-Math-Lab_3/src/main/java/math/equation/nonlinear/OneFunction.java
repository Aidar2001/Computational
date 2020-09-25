package math.equation.nonlinear;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Adapter.InputData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.lang.Math.pow;

public enum OneFunction {
    FUNCTION_INSTANCE("2,3x^3+5,75x^2-7,41x-10,6") {
        private OneFunction.Function function = new OneFunction.Function();
        private OneFunction.Function firstDerivative = new OneFunction.Function();
        private OneFunction.Function secondDerivative = new OneFunction.Function();
        @Setter
        private double lowLimit,upLimit;

        {
            function.setCoefficientArray(Arrays.asList(-10.6,-7.41,5.75,2.3));
            firstDerivative.setCoefficientArray(Arrays.asList(-7.41,11.5,6.9));
            secondDerivative.setCoefficientArray(Arrays.asList(11.5,13.8));
        }

        @Override
        public double calculate(double x) {
             return function.calculate(x);
        }

        //первая производная
        @Override
        public double firstDerivative(double x) {
            return firstDerivative.calculate(x);
        }

        // вторая производная
        @Override
        public double secondDerivative(double x) {
            return secondDerivative.calculate(x);
        }

        // преобразованная функция для метода простой итерации
        @Override
        public double phi(double x) { // note: φ(x) = x+lambda*f(x) , lambda = -1/max{f'(x)}
//            return (2.3 * pow(x, 3) + 5.75 * pow(x, 2) - 10.6) / 7.41;
            return phi( x, InputData.LOW_LIMIT.getVal(), InputData.UP_LIMIT.getVal());

        }

        @Override
        public double phi(double x, double lowLimit, double upLimit) {
            return x - 1 / Math.max(firstDerivative(lowLimit), firstDerivative(upLimit)) * calculate(x);
        }

        @Override
        public double phiDerivative(double x) { // note: φ'(x) = 1 + lambda*f'(x)
//            return (6.9 * pow(x, 2) + 11.5 * x) / 7.41;
            return phiDerivative(x,InputData.LOW_LIMIT.getVal(), InputData.UP_LIMIT.getVal());
        }

        @Override
        public double phiDerivative(double x, double lowLimit, double upLimit) {
            return 1 - 1 / Math.max(firstDerivative(lowLimit), firstDerivative(upLimit)) * firstDerivative(x);
        }

        public Function getFunction() {
            return function;
        }
    };

    @Getter
    private String name;

    OneFunction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract double calculate(double x);

    //первая производная
    public abstract double firstDerivative(double x);

    // вторая производная
    public abstract double secondDerivative(double x);

    // проверка существования экстремумов на интервале
    public boolean existPointsOfExtremumOnInterval(double a, double b) {
        while (a<=b) {
            if (this.firstDerivative(a) == 0) {
                return true;
            }
            a += 0.1E-10;
//            if (a >= b) {
//                return false;
//            }
        }
        return false;
    }

    // преобразованная функция для метода простой итерации
    public abstract double phi(double x);

    public abstract double phi(double x, double lowLimit, double upLimit);

    //
    public abstract double phiDerivative(double x);

    public abstract double phiDerivative(double x, double lowLimit, double upLimit);

    public abstract Function getFunction();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Function{
        @Getter @Setter
        private List<Double> coefficientArray = new ArrayList<>();
        public double calculate(double x){
            double sum = 0;
            for(int i =0; i <= coefficientArray.size()-1; i++){
                sum += coefficientArray.get(i)*Math.pow(x,i); //note: умножение коэффицентов на Х в степени I
            }
            return sum;
        }
    }
}
