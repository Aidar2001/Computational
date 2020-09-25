package math;

import utils.Adapter;
import utils.NoRootsException;
import utils.VerificationException;

public interface MathMethod {
    Adapter.OutputData solveWithDefaultArgs() throws NoRootsException, VerificationException;

}
