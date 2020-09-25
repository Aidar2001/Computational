package math.equation.nonlinear;

import math.AbstractMathMethod;
import utils.Adapter;
import utils.NoRootsException;
import utils.VerificationException;
import utils.Adapter.OutputData;

public abstract class AbstractNonlinearEquationMathMethod extends AbstractMathMethod {


    public static boolean isRootsOnInterval(double a, double b) {
        if (OneFunction.FUNCTION_INSTANCE.calculate(a) * OneFunction.FUNCTION_INSTANCE.calculate(b) > 0) {
            return false;
        }
        System.out.println("На введенном интервале присутствует хотя бы однин корень.");
        return true;
    }

    public OutputData solve(double lowLimit, double upLimit, double accuracy) throws NoRootsException, VerificationException {
        // если нижний и верхний пределы равны
        if (lowLimit == upLimit && OneFunction.FUNCTION_INSTANCE.calculate(lowLimit) != 0.0) {
            throw new NoRootsException();
        } else if (lowLimit == upLimit && OneFunction.FUNCTION_INSTANCE.calculate(lowLimit) == 0.0) {
            return new OutputData(new OutputData.Coordinates(0.0));
        }
        return solver(lowLimit, upLimit, accuracy);
    }

    public abstract boolean verification(double lowLimit, double upLimit, double accuracy) throws VerificationException;

    @Override
    public OutputData solveWithDefaultArgs() throws NoRootsException, VerificationException {
        return solve(Adapter.InputData.LOW_LIMIT.getVal(), Adapter.InputData.UP_LIMIT.getVal(), Adapter.InputData.ACCURACY.getVal());
    }

    public abstract OutputData solver(double lowLimit, double upLimit, double accuracy) throws NoRootsException, VerificationException;

}
