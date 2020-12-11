package lab4.io;

@FunctionalInterface
public interface InputDataReader {
     MathInput read();

     interface MathInput {
     }
}
