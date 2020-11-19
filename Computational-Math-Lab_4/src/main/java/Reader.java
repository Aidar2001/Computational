import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Reader {

    public static void read() {
        try {
            Scanner scanner = new Scanner(System.in);
            scanner.useLocale(new Locale("Russian"));
            boolean cycle = true;
            ArrayList<Double> lineX = new ArrayList<>(), lineY = new ArrayList<>();
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
                InputData.LINE_X.setLine(lineX);
                InputData.LINE_Y.setLine(lineY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
