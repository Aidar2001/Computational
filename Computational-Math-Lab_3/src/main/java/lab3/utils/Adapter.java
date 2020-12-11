package lab3.utils;

import lab3.math.equation.nonlinear.*;
import lombok.*;
import lab2.math.MathMethod;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Adapter {
    private static final Map<Double, Double> intervalsIsolation = new TreeMap<>();       //note: интервалы изоляции: пары <a,b>
    private static volatile Adapter singleAdapter;
    @Getter
    private static OneFunction function = OneFunction.FUNCTION_INSTANCE;
    @Getter
    @Setter
    private AccessMathMethods mathMethod;

    static {
        intervalsIsolation.put(-3.5, -3.0);
        intervalsIsolation.put(-1.5, -0.5);
        intervalsIsolation.put(1.0, 2.0);
    }

    public static Adapter getInstance() {
        if (!Objects.nonNull(singleAdapter)) {
            synchronized (Adapter.class) {
                if (singleAdapter == null) {
                    singleAdapter = new Adapter();
                }
            }
        }
        return singleAdapter;
    }

    public void solve() {
        AbstractNonlinearEquationMathMethod method = mathMethod.getInstance();
        printEnteredValues();
        double lowLimit = InputData.LOW_LIMIT.getVal();
        double upLimit = InputData.UP_LIMIT.getVal();
        double accuracy = InputData.ACCURACY.getVal();
        OutputData outputData = null;
        try {
            outputData = method.solve(lowLimit, upLimit, accuracy);
            System.out.println("Ответ: "+outputData);
        } catch (NoRootsException | NullPointerException | VerificationException noRoots) {
            System.out.println(noRoots.getMessage());
        }
    }

    private boolean isPointOnInterval(double a, double b, double point) {
        return Math.abs(point - a) + Math.abs(point - b) == b - a;
    }

    // печать результата
    private void printEnteredValues() {
        System.out.println("============" + mathMethod.getName() + "============");
        System.out.println("Введенные значения: ");
        ;
        System.out.println("Точность; " + InputData.ACCURACY.getVal());
        System.out.println("Нижний предел: " + InputData.LOW_LIMIT.getVal());
        System.out.println("Верхний предел: " + InputData.UP_LIMIT.getVal());
    }

    public enum AccessMathMethods {
        HALF_INTERVAL_METHOD(HalfIntervalMethod.class, "Метод половинного деления"),
        SECANT_METHOD(SecantMethod.class, "Метод секущих"),
        SIMPLE_ITERATION_METHOD(SimpleIterationMethod.class, "Метод простой итерации");
        private Class<? extends AbstractNonlinearEquationMathMethod> methodsClazz;
        @Getter
        private String name;
        private volatile lab3.MathMethod INSTANCE;

        AccessMathMethods(Class<? extends AbstractNonlinearEquationMathMethod> methodsClazz, String name) {
            this.methodsClazz = methodsClazz;
            this.name = name;
        }

        public AbstractNonlinearEquationMathMethod getInstance() {
            return getInstance(methodsClazz);
        }

        public <T extends lab3.MathMethod> T getInstance(Class<T> clazz) {
            if (!Objects.nonNull(INSTANCE)) {
                synchronized (clazz) {
                    if (INSTANCE == null) {
                        try {
                            INSTANCE = clazz.newInstance();
                        } catch (IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return (T) INSTANCE;
        }
    }

    public enum InputData {
        ACCURACY("accuracy"),
        LOW_LIMIT("lowLimit"),
        UP_LIMIT("upLimit");
        //        @Getter @NonNull
        private final String keyInInputDataMap;
        @Getter
        private double val;

        InputData(String keyInInputDataMap) {
            this.keyInInputDataMap = keyInInputDataMap;
        }

        public void setVal(double value) { //
            this.val = value;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class OutputData implements Comparable<OutputData> {
        @Getter
        @Setter
        private Coordinates coordinates;
        @Getter
        @Setter
        private int countItr;

        public OutputData(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public int compareTo(OutputData o) {
            return o.getCoordinates().getX() > this.coordinates.X ? -1 : o.getCoordinates().getY() == this.coordinates.Y ? 0 : 1;
        }

        @Data
        @AllArgsConstructor
        public static class Coordinates {
            private double X, Y;

            public Coordinates(final double x) {
                X = x;
                Y = getY(x);
            }

            public double getY(double X) {
                return OneFunction.FUNCTION_INSTANCE.calculate(X);
            }
        }

        @Override
        public String toString() {
            return "OutputData{" +
                    "coordinates=" + coordinates +
                    ", countItr=" + countItr +
                    '}';
        }
    }

}
