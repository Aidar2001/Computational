package lab4.io;

import lombok.Getter;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;

public class Reader implements InputDataReader {

    @Override
    public ApproximationInputData read() {
        ArrayList<Double> lineX = new ArrayList<>(),
                lineY = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(System.in);
            scanner.useLocale(new Locale("Russian"));
            boolean cycle = true;
            String[] line;
            while (cycle) {
                System.out.println("Выберите тип ввода(0 - из файла, 1 - с клавиатуры): ");
                String nextLine = scanner.nextLine();
                switch (nextLine) {
                    case ("0"): {
                        System.out.println("Введите полный путь к файлу: ");
                        String path = scanner.nextLine();
                        Scanner fileScanner = new Scanner(new File(path));
                        line = fileScanner.nextLine().replace(',','.').split(" ");
                        Arrays.asList(line).forEach(o->lineX.add(Double.parseDouble(o)));
                        line = fileScanner.nextLine().replace(',','.').split(" ");;
                        Arrays.asList(line).forEach(o->lineY.add(Double.parseDouble(o)));
                        fileScanner.close();
                        if (lineX.size() != lineY.size()) {
                            System.out.println("Количество аргументов X и значений Y должно быть одинаково.");
                            cycle = true;
                        } else {
                            cycle = false;
                        }
                        break;
                    }
                    case ("1"): {
                        boolean success = false;
                        while (!success) {
                            System.out.print("Введите значения X в строку через пробел:");
                            line = scanner.nextLine().replace(',','.').split(" ");
                            Arrays.asList(line).forEach(o->lineX.add(Double.parseDouble(o)));
                            System.out.print("Введите значения Y в строку через пробел:");
                            line = scanner.nextLine().replace(',','.').split(" ");
                            Arrays.asList(line).forEach(o->lineY.add(Double.parseDouble(o)));
                            if (lineX.size() != lineY.size()) {
                                System.out.println("Количество аргументов X и значений Y должно быть одинаково.");
                            }
                            success = true;
                        }
                        cycle = false;
                        break;
                    }
                    default: {
                        System.out.println("Wrong answer. Try again.");
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApproximationInputData input = new ApproximationInputData();
        input.LINE_X = lineX;
        input.LINE_Y = lineY;
        return input;
    }

    @Getter
    public class ApproximationInputData implements MathInput {
        private List<Double> LINE_X;
        private List<Double> LINE_Y;

        public List<Point2D> getCoordinates(){
            List<Point2D> points = new ArrayList<>(LINE_X.size());
            for(int i = 0; i<LINE_X.size(); i++){
                points.add(new Point2D.Double(LINE_X.get(i),LINE_Y.get(i)));
            }
            return points;
        }

        public int getN() {
            int n;
            if (LINE_Y.size() == LINE_X.size()) {
                n = LINE_Y.size();
            } else throw new IllegalStateException("Количество аргументов X и значений Y должно быть оюинаково.");
            return n;
        }
    }
}
