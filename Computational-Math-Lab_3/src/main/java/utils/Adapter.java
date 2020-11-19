package utils;

import lombok.*;
import math.AbstractMathMethod;
import math.MathMethod;
import math.equation.nonlinear.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Adapter {
    private static final Map<Double, Double> intervalsIsolation = new TreeMap<>();       //note: интервалы изоляции: пары <a,b>
    private static volatile Adapter singleAdapter;
    @Getter
    private static OneFunction function = OneFunction.FUNCTION_INSTANCE;
    //    @Getter @Setter
//    private static List<InputData> data = Arrays.asList(InputData.values());
    @Getter
    @Setter
    private AccessMathMethods mathMethod;

    {
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
//        HashSet<OutputData> roots = new HashSet<>();
        double lowLimit = InputData.LOW_LIMIT.getVal();
        double upLimit = InputData.UP_LIMIT.getVal();
        double accuracy = InputData.ACCURACY.getVal();
//        intervalsIsolation.keySet().stream().filter(a -> (isPointOnInterval(lowLimit, upLimit, a) ||
//                isPointOnInterval(lowLimit, upLimit, intervalsIsolation.get(a))))
//                .collect(Collectors.toList()).forEach(o -> {
//            try {
//                if (isPointOnInterval(lowLimit, upLimit, o) && !isPointOnInterval(lowLimit, upLimit, intervalsIsolation.get(o))) {  // левый конец интервала изоляции корня
//                    roots.add(method.solve(o, upLimit, accuracy));                                                 // входит в введенный интервал но не правый
//                } else if (!isPointOnInterval(lowLimit, upLimit, o) && isPointOnInterval(lowLimit, upLimit, intervalsIsolation.get(o))) { // правый конец интервала изоляции корня
//                    roots.add(method.solve(lowLimit, intervalsIsolation.get(o), accuracy));                              // входит в введенный интервал но не левый
//                } else roots.add(method.solve(o, intervalsIsolation.get(o), accuracy));
//            } catch (NoRootsException | NullPointerException noRoots) {
//                roots.add(null);
//            } catch (VerificationException verificationException) {
//                System.out.println(verificationException.getMessage());
//            }
//        });
        OutputData outputData = null;
        try {
            outputData = method.solve(lowLimit, upLimit, accuracy);
            System.out.println("Ответ: "+outputData);
        } catch (NoRootsException | NullPointerException | VerificationException noRoots) {
            System.out.println(noRoots.getMessage());
//            roots.add(null);
        }

//        roots.stream().filter(Objects::nonNull).sorted().collect(Collectors.toList()).forEach(System.out::println);
    }

    private boolean isPointOnInterval(double a, double b, double point) {
        return Math.abs(point - a) + Math.abs(point - b) == b - a;
    }

//    private void findRoots(double root, AbstractNonlinearEquationMathMethod method, double a, double b) {
//        while (AbstractNonlinearEquationMathMethod.isRootsOnInterval(a, root) && method.solve() || AbstractNonlinearEquationMathMethod.isRootsOnInterval(root, b)) {
//            if (AbstractNonlinearEquationMathMethod.isRootsOnInterval(a, root))
//                root = method.solve(a, root, InputData.ACCURACY.getVal());
//            if (AbstractNonlinearEquationMathMethod.isRootsOnInterval(root, b))
//                method.solve(root, b, InputData.ACCURACY.getVal());
//        }
//    }

    private void solver() {

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
        private volatile MathMethod INSTANCE;

        AccessMathMethods(Class<? extends AbstractNonlinearEquationMathMethod> methodsClazz, String name) {
            this.methodsClazz = methodsClazz;
            this.name = name;
        }

        public AbstractNonlinearEquationMathMethod getInstance() {
            return getInstance(methodsClazz);
        }

        public <T extends AbstractMathMethod> T getInstance(Class<T> clazz) {
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

        ;


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
