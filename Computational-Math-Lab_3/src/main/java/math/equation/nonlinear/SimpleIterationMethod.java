package math.equation.nonlinear;

import utils.Adapter;
import utils.Adapter.*;
import utils.VerificationException;

public class SimpleIterationMethod extends AbstractNonlinearEquationMathMethod{
    private static final OneFunction function = OneFunction.FUNCTION_INSTANCE;
    @Override // note: |φ'(x)| < 1 - достаточный признак сходимости
    public boolean verification(double lowLimit, double upLimit, double accuracy) {
        boolean success = true;
        double a = lowLimit;
        while (a <= upLimit) {
            success = success && Math.abs(function.phiDerivative(a,lowLimit,upLimit)) < 1;
            a += accuracy/2;
        }
        return success;
    }

    @Override
    public OutputData solver(double lowLimit, double upLimit, double accuracy) throws VerificationException {
        if(!verification(lowLimit,upLimit,accuracy)){
            throw new VerificationException("Невыполняется достаточный признак сходимости метода.");
        }
        double x = (lowLimit + upLimit) / 2;
        double x0;
        int i=0;
        do {
            i++;
            x0 = x;
            x = function.phi(x0,lowLimit,upLimit);
        } while (Math.abs(x - x0) >= accuracy);
        return new OutputData(new OutputData.Coordinates(x),i);
    }
}
