import math.MathProcessor;
import read.CommandLineReader;

public class Main {
    public static void main(String[] args) {
        MathProcessor mathProcessor = new MathProcessor(CommandLineReader.read());
        while (true){
            try {
                mathProcessor.calculate();
            } catch (IllegalStateException ise){
                System.out.println(ise.getMessage());
                System.out.println("Повторите ввод");
                mathProcessor.setLimits(CommandLineReader.enterLimits(CommandLineReader.getMainScanner()));
                continue;
            }
            break;

        }
    }
}
