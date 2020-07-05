public class Main {
    public static void main(String[] args) {
        MathProcessor mathProcessor = new MathProcessor(CommandLineReader.readCommand());
        mathProcessor.solve();
    }
}
