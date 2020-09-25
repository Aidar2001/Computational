package math.equation.nonlinear;

import lombok.Getter;
import utils.Adapter;
import utils.Adapter.OutputData;
import utils.NoRootsException;
import utils.VerificationException;

public class SecantMethod extends AbstractNonlinearEquationMathMethod {
    @Getter
    private static final OneFunction function = OneFunction.FUNCTION_INSTANCE;

    @Override
    public Adapter.OutputData solver(double a, double b, double accuracy) throws NoRootsException {
        double x0 = getFirstApproximationByInterval(a, b);
        double x1 = x0 == a ? x0 + 0.002 : x0 - 0.002;
        double x = 0.0;
        int i = 0;
        while (Math.abs(x1 - x0) >= accuracy || Math.abs(function.calculate(x1)) >= accuracy) {
            x = x1 - ((x1 - x0) / (function.calculate(x1) - function.calculate(x0))) * function.calculate(x1);
            x0 = x1;
            x1 = x;
            i++;
        }
        return new Adapter.OutputData(new OutputData.Coordinates(x), i);
    }


    public double getFirstApproximationByInterval(double a, double b) {
        Double firstApproximation = a;
        if (function.secondDerivative(a) * function.calculate(a) > 0) {
            firstApproximation = a;
        } else if (function.secondDerivative(b) * function.calculate(b) > 0) {
            firstApproximation = b;
        }
        return firstApproximation;
    }

    @Override
    public boolean verification(double lowLimit, double upLimit, double accuracy) throws VerificationException {
        try {
            getFirstApproximationByInterval(lowLimit, upLimit);
            return true;
        } catch (NullPointerException nupe) {
            throw new VerificationException("На заданном промежутке нет корней");
        }
    }
}
