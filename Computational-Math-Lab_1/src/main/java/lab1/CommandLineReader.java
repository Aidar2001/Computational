package lab1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandLineReader {
    private static Map<String, Object> inputData = new HashMap<String, Object>();

    public static Map<String, Object> readCommand() {
        Scanner mainScanner = new Scanner(System.in);
        String inputStr;
        Boolean isImport = null;
        while (isImport == null) {
            System.out.print("Хотите ли вы ввести данные из файла? (да/нет)");
            inputStr = mainScanner.nextLine().trim();
            if (inputStr.equalsIgnoreCase("y") || inputStr.equalsIgnoreCase("yes") || inputStr.equalsIgnoreCase("да")) {
                isImport = true;
            } else if (inputStr.equalsIgnoreCase("n") || inputStr.equalsIgnoreCase("no") || inputStr.equalsIgnoreCase("нет")) {
                isImport = false;
            }
        }
        try {
            if (isImport) {
                //Считывание данных из файла
                boolean successRead = false;
                //mainScanner.close();
                Scanner fileScanner = null;
                outer:
                while (true) {
                    System.out.println("Введите путь к файлу с исходными данными: ");
                    String path = mainScanner.nextLine();
                    try {
                        fileScanner = new Scanner(new File(path));
                    } catch (FileNotFoundException e) {
                        System.out.println("Введен некорректный путь к файлу");
                        continue;
                    }
                    while (true) {
                        try {
                            fileScanner.useLocale(Locale.US);
                            int matrixOrder = fileScanner.nextInt();
                            inputData.put("matrixOrder", matrixOrder);
                            inputData.put("accuracy", fileScanner.nextDouble());
                            putMatrixToMap(fileScanner, matrixOrder);
                        } catch (InputMismatchException imp) {
                            fileScanner.useLocale(Locale.FRANCE);
                            continue;
                        }
                        fileScanner.close();
                        break outer;
                    }
                }
            } else {

                //Считывание n (кол-ва уравнений)
                System.out.print("Введите количество уравненийй" + (char) 27 + "[1;32m n" + (char) 27 + "[m:");
                int matrixOrder = mainScanner.nextInt();
                inputData.put("matrixOrder", matrixOrder);

                //Считывание погрешности
                System.out.print("Введите погрешность" + (char) 27 + "[1;32m \u03b5 (epsilon) " + (char) 27 + "[m:");
                inputData.put("accuracy", mainScanner.nextDouble());

                //Считывание матрицы
                System.out.print("Введите матрицу коэффициэнтов и свободных членов:");
                putMatrixToMap(mainScanner, matrixOrder);

                //Считывание максимального чила итераций M
//                System.out.print("Введите максимальное чило итераций:");
//                inputData.put("M", mainScanner.nextInt());
            }
        } catch (InputMismatchException ime) {
            System.out.println("Ошибка ввода данных. Неправильный формат данных.");

        }
        return inputData;
    }

    private static void putMatrixToMap(Scanner scanner, int matrixOrder) {
        List<List<Number>> matrix = new ArrayList<>(matrixOrder);
        Number number = null;
        for (int i = 0; i < matrixOrder; i++) {
            List<Number> numberList = new ArrayList<>();
            for (int j = 0; j < matrixOrder + 1; j++) {
                try {
                    number = scanner.nextInt();
                } catch (InputMismatchException e) {
                    try {
                        number = scanner.nextDouble();
                    } catch (InputMismatchException ex) {
                        try {
                            number = Double.parseDouble(scanner.next());
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                    }
                }
                numberList.add(j, number);
            }
            matrix.add(i, numberList);
        }
        inputData.put("matrix", matrix);
    }
}
