import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilterWriter;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class WriterPrinter {
    public static void print(LeastSquaresApproximator.Output output, Functions function) {
        System.out.printf("%16s|", function.getName());
        output.getAsList().forEach(o -> {
            if (o.doubleValue() == 0.0 && output.getAsList().indexOf(o) == 4) {
                System.out.printf("%10s|", "--------");
            } else System.out.printf("%10.5f|", o.doubleValue());
        });
//        for (int i= 0; i<output.getAsList().size();i++){
//
//        }
        System.out.println();
    }

    public static void printResult(Map<Functions, LeastSquaresApproximator.Output> outputs) {
        printHeader();
        ArrayList<Double> X = InputData.LINE_X.getLine();
        ArrayList<Double> Y = InputData.LINE_Y.getLine();
        List<Functions> functions = Arrays.asList(Functions.values());
        for (int i = 0; i < InputData.LINE_X.getLine().size(); i++) {
            double x_i = X.get(i);
            System.out.printf("%5.3f|%5.2f|", x_i, Y.get(i));
            for (int j = 0; j < Functions.values().length; j++) {
                LeastSquaresApproximator.Output currentOutput = outputs.get(functions.get(j));
                if (Objects.equals(functions.get(j), Functions.QUADRA)) {
                    System.out.printf("%16f|", functions.get(j).calculate(currentOutput.getA(), currentOutput.getB(), currentOutput.getC(), x_i));
                } else {
                    System.out.printf("%16f|", functions.get(j).calculate(currentOutput.getA(), currentOutput.getB(), x_i));
                }
            }
            System.out.println();
        }
        String[] names = {"s","sigma","a","b","c"};
        for(int i = 0; i < outputs.get(Functions.LINE).getAsList().size();i++){
            System.out.printf("%11s|",names[i]);
            for(int j = 0; j < Functions.values().length; j++){
                double currVal = outputs.get(functions.get(j)).getAsList().get(i).doubleValue();
                if (currVal == 0.0 && i == 4) {
                    System.out.printf("%16s|", "--------");
                } else System.out.printf("%16f|",currVal);
            }
            System.out.println();
        }
        double min=outputs.get(Functions.LINE).getSigma();
        Functions function = null;
        for(int j = 0; j < Functions.values().length; j++){
            if (min>outputs.get(functions.get(j)).getSigma()){
                min=outputs.get(functions.get(j)).getSigma();
                function = functions.get(j);
            }
        }
        System.out.println("Наилучшее приближение у " +function.getName().replace("ая", "ой") + " функции");
    }

    public static void saveToFile(Map<Functions, LeastSquaresApproximator.Output> outputs){
        try {
            PrintStream printStream = new PrintStream(new File("D:\\OneDrive\\Документы\\Учеба\\Айдар\\ИТМО\\2 курс\\4 семестр\\Вычислительная математика\\лабы\\Git-repos\\Computational-Math-Lab_4\\out.txt"));
            System.setOut(printStream);
            printResult(outputs);
            printStream.close();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }


    public static void printHeader() {
        String[] columnsHeadersFirst = Arrays.stream(Functions.values()).map(Functions::getName).
                collect(Collectors.toList()).toArray(new String[Functions.values().length]);
        System.out.printf("%11s|", "Вид φ(x)");
        Arrays.stream(columnsHeadersFirst).forEach(o -> System.out.printf("%16.19s|", o));
        System.out.println();
        String[] columnsHeadersSecond = {"X", "Y", "F=ax+b", "F=ax^2+bx+c", "F=ae^(bx)", "F=alnx+b", "F=ax^b"};
        System.out.printf("%5s|%5s|", columnsHeadersSecond[0], columnsHeadersSecond[1]);
        Arrays.stream(columnsHeadersSecond).skip(2).forEach(o -> System.out.printf("%16.19s|", o));
        System.out.println();
    }
}
