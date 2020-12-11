package lab5.io;

import lab4.io.InputDataReader;
import lab5.interpolation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Scanner;

public class Reader implements InputDataReader {
    private static EnteredData INPUT_DATA = new Reader().read();

    public static EnteredData readInputData() {
        return INPUT_DATA;
    }

    @Override
    public EnteredData read() {
        List<Point2D> points = new lab4.io.Reader().read().getCoordinates();
        EnteredData input = new EnteredData();
        input.setInterpolationNodes(points);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите значение Х: ");
        input.setInterpolationPoint(scanner.nextDouble());
        boolean cycle = true;
        InterpolationMethod interpolationMethod = null;
            System.out.println("Выберите метод интерполяции\n" +
                    "(0 - Лагранж, 1 - интерполяционный многочлен Ньютона для равноотстоящих узлов\n" +
                    "2 - интерполяционный многочлен Ньютона для неравноотстоящих узлов): ");
            switch (scanner.next()) {
                case ("0"): {
                    interpolationMethod = new LagrangeInterpolationMethod();
                    break;
                }
                case ("1"): {
                    interpolationMethod = new NewtonNEDNInterpolationMethod();
                    break;
                }
                case ("2"): {
                    interpolationMethod = new NewtonEDNInterpolationMethod();
                    break;
                }
                default: {
                    System.out.println("Wrong answer. Try again.");
                    break;
                }
            }
        input.setInterpolationMethod(interpolationMethod);
        return input;
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public class EnteredData implements MathInput {
        {
            INPUT_DATA = this;
        }
        private InterpolationMethod interpolationMethod;
        private List<Point2D> interpolationNodes;
        private double interpolationPoint;
    }
}


