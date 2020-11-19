import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public enum InputData {
    LINE_X,
    LINE_Y;
    @Getter @Setter
    private ArrayList<Double> line;

    public static int getN() {
        int n;
        if (InputData.LINE_Y.getLine().size() == InputData.LINE_X.getLine().size()) {
            n = InputData.LINE_Y.getLine().size();
        } else throw new IllegalStateException("Количество аргументов X и значений Y должно быть оюинаково.");
        return n;
    }
}
