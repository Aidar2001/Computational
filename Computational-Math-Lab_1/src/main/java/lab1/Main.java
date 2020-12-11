package lab1;

public class Main {
    public static void main(String[] args) {
        MethodGaussZeidel methodGaussZeidel = new MethodGaussZeidel(CommandLineReader.readCommand());
        methodGaussZeidel.solve();
    }
}
