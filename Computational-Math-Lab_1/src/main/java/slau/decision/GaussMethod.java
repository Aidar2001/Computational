package slau.decision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GaussMethod {
    public static List<Number> solve(List<List<Number>> coeffMatrix, List<Number> b_i) {
        for (int i = 0; i < coeffMatrix.size() - 1; i++) {
            if (coeffMatrix.get(i).get(i).doubleValue() == 0.0) {
                List<Number> numbers = coeffMatrix.get(i - 1);
                coeffMatrix.set(i - 1, coeffMatrix.get(i));
                coeffMatrix.set(i, numbers);
                double number = (double) b_i.get(i - 1);
                b_i.set(i-1,b_i.get(i));
                b_i.set(i,number);
            }
            for (int k = i + 1; k < coeffMatrix.size(); k++) {
                double c = coeffMatrix.get(k).get(i).doubleValue() / coeffMatrix.get(i).get(i).doubleValue();  // c = a_k_i / a_i_i
                coeffMatrix.get(k).set(i, 0);
                for (int j = i + 1; j < coeffMatrix.size(); j++) {
                    coeffMatrix.get(k).set(j, coeffMatrix.get(k).get(j).doubleValue() - c * coeffMatrix.get(i).get(j).doubleValue()); //  a_k_i= a_k_i - c * a_i_j
                }
                b_i.set(k, b_i.get(k).doubleValue() - c * b_i.get(i).doubleValue());            // b_k = b_k - c*b_i
            }
        }
        List<Number> x_i = new ArrayList<>(Collections.nCopies(b_i.size(), 0));
        x_i.set(b_i.size() - 1, b_i.get(b_i.size() - 1).doubleValue() / coeffMatrix.get(b_i.size() - 1).get(b_i.size() - 1).doubleValue());
        for (int i = b_i.size() - 2; i >= 0; i--) {
            double s = 0;
            for (int j = i + 1; j < coeffMatrix.size(); j++) {
                s += coeffMatrix.get(i).get(j).doubleValue() * x_i.get(j).doubleValue();
            }
            x_i.set(i, (b_i.get(i).doubleValue() - s) / coeffMatrix.get(i).get(i).doubleValue());
        }
        return x_i;
    }

    public static List<Number> solve(List<List<Number>> matrix) {
        List<List<Number>> coeffs = matrix.stream().map(o -> o.stream().limit(matrix.size()).collect(Collectors.toList())).collect(Collectors.toList());
        List<Number> b_i = matrix.stream().map(o -> o.get(o.size() - 1)).collect(Collectors.toList());
        return solve(coeffs, b_i);
    }

}
