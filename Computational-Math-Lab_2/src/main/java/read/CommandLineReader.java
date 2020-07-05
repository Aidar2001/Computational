package read;

import lombok.Getter;
import math.FewFunctions;

import java.util.*;

public class CommandLineReader {
    private static Map<String, Object> inputData = new HashMap<String, Object>();
    private static ArrayList<FewFunctions> fewFunctions = new ArrayList<>(Arrays.asList(FewFunctions.values()));
    @Getter
    private static Scanner mainScanner;

    public static Map<String, Object> read() {
        mainScanner = new Scanner(System.in);
        mainScanner.useLocale(Locale.US);
        while (true) {
            System.out.println("Выберите функцию для вычисления интергала:");
            fewFunctions.forEach(o -> System.out.println((fewFunctions.indexOf(o) + 1) + ")" + o));
            try {
                inputData.put("function", fewFunctions.get(mainScanner.nextInt() - 1));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Введен номер не соответствующий ни одной функции из списка");
                continue;
            }
            while (true){
                try {
                    inputData.putAll(enterLimits(mainScanner));
                    System.out.println("Введите точность:");
                    inputData.put("accuracy", mainScanner.nextDouble());
                } catch (InputMismatchException e){
                    System.out.println("Пожалуйста используйте точку в качестве разделителя для ввода десятичных дробей");
                    continue;
                }
                break;
            }
            break;
        }
        return inputData;
    }

    public static Map<String,Double> enterLimits(Scanner mainScanner) {
        Map<String,Double> limits = new HashMap<>();
        System.out.println("Введите нижний предел интегрирования:");
        double a = mainScanner.nextDouble();
        limits.put("a", a);
        System.out.println("Введите верхний предел интегрирования:");
        double b = mainScanner.nextDouble();
        limits.put("b", b);
        return limits;
    }
}
