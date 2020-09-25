package math.equation.nonlinear;

import lombok.Setter;
import math.AbstractMathMethod;
import utils.Adapter;
import utils.Adapter.*;
import utils.VerificationException;
//import utils.InputData;

import java.util.Objects;

public class HalfIntervalMethod extends AbstractNonlinearEquationMathMethod {
    @Setter
    private static  OneFunction function = Adapter.getFunction();
    private static volatile HalfIntervalMethod INSTANCE;

    public static AbstractMathMethod getInstance() {
        if (!Objects.nonNull(INSTANCE)) {
            synchronized (SecantMethod.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HalfIntervalMethod();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public OutputData solver(double a, double b, double accuracy) throws VerificationException {
        if(!verification(a,b,accuracy)){
            throw new VerificationException("Корень на данном промежутке отсутствует.");
        }
        Double x = null;
        int i=0;
         do {
             i++;
            x = (a + b) / 2;
            if (function.calculate(a) * function.calculate(x)  < 0) {
                b = x;
            } else a = x;
        } while (Math.abs(a-b) >= accuracy || Math.abs(function.calculate(x)) < accuracy);
        x = (a + b) / 2;
        return new OutputData(new OutputData.Coordinates(x),i);
    }

//    public double getFirstApproximationByInterval(double a, double b) {
//        return (a + b) / 2;
//    }

    @Override
    public boolean verification(double lowLimit, double upLimit, double accuracy) {
        return isRootsOnInterval(lowLimit, upLimit);
    }

}
