package lab1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GaussMethod {
    public static List<Number> solve(final List<List<Number>> coeffMatrix, final List<Number> b_i) {
        List<List<Number>> matrix = new ArrayList<>(coeffMatrix);
        List<Number> b = b_i;
        // note прямой ход
        for (int i = 0; i < coeffMatrix.size() - 1; i++) {
            if (coeffMatrix.get(i).get(i).doubleValue() == 0.0) {
                List<Number> numbers = coeffMatrix.get(i - 1);
                coeffMatrix.set(i - 1, coeffMatrix.get(i));
                coeffMatrix.set(i, numbers);
                double number = (double) b.get(i - 1);
                b.set(i - 1, b.get(i));
                b.set(i, number);
            }
            for (int k = i + 1; k < coeffMatrix.size(); k++) {
                double c = coeffMatrix.get(k).get(i).doubleValue() / coeffMatrix.get(i).get(i).doubleValue();  // c = a_k_i / a_i_i
                coeffMatrix.get(k).set(i, 0);
                for (int j = i + 1; j < coeffMatrix.size(); j++) {
                    coeffMatrix.get(k).set(j, coeffMatrix.get(k).get(j).doubleValue() - c * coeffMatrix.get(i).get(j).doubleValue()); //  a_k_i= a_k_i - c * a_i_j
                }
                b.set(k, b.get(k).doubleValue() - c * b.get(i).doubleValue());            // b_k = b_k - c*b_i
            }
        }

        // note обратный ход
        List<Number> x_i = new ArrayList<>(Collections.nCopies(b.size(), 0));
        x_i.set(b.size() - 1, b.get(b.size() - 1).doubleValue() / coeffMatrix.get(b.size() - 1).get(b.size() - 1).doubleValue());
        for (int i = b.size() - 2; i >= 0; i--) {
            double s = 0;
            for (int j = i + 1; j < coeffMatrix.size(); j++) {
                s += coeffMatrix.get(i).get(j).doubleValue() * x_i.get(j).doubleValue();
            }
            x_i.set(i, (b.get(i).doubleValue() - s) / coeffMatrix.get(i).get(i).doubleValue());
        }

        return checkRootsPlaces(coeffMatrix, b_i, x_i);
    }

    private static List<Number> checkRootsPlaces(List<List<Number>> coeffMatrix, List<Number> b_i, List<Number> x_i) { // maybe bug
        int count = factorial(x_i.size());
        int max = x_i.size() - 1;
        int shift = max;
        boolean success = false;
        while (count > 0) {
            success=true;
            for (int i = 0; i < x_i.size(); i++) {
                double s = 0;
                for (int j = 0; j < b_i.size(); j++) {
                    s += x_i.get(j).doubleValue() * coeffMatrix.get(i).get(j).doubleValue();
                }
                success &= s == b_i.get(i).doubleValue();
            }
            if(!success){
                Number number = x_i.get(shift);
                x_i.set(shift,x_i.get(shift-1));
                x_i.set(shift-1, number);
            } else break;
            count--;
            if (shift < 2) {
                shift = max;
            } else {
                shift--;
            }
        }
        return x_i;
    }

    private static int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }


    public static List<Number> solve(List<List<Number>> extendedMatrix) { // maybe bug
        List<List<Number>> coeffs = new ArrayList<>();
        List<Number> b_i = extendedMatrix.stream().map(o -> o.get(o.size() - 1)).collect(Collectors.toList());
        extendedMatrix.forEach(o -> coeffs.add(o.subList(0, o.size() - 1)));
        return solve(coeffs, b_i);
    }

}
