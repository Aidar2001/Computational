package math;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class MathProcessor implements MathMethod {
    private Map<String, Object> inputData;
    private FewFunctions function; // функция
    private double eps; // введенная точность
    @Setter
    private double a; // нижний предел
    @Setter
    private double b; // верхний предел

    public MathProcessor(Map<String, Object> inputData) throws IllegalStateException{
        this.inputData = inputData;
        eps = (Double) inputData.get("accuracy");
        a = (Double) inputData.get("a");
        b = (Double) inputData.get("b");
        function = (FewFunctions) inputData.get("function");

    }

    @Override
    public void calculate() {
        int n = 2;
        double h = 0;
        double err = 0;
        double sum1, In, I2n, sum2;
        if (a>b){
            throw new IllegalStateException("Верхний предел интегрирования не может быть меньше нижнего.");
        }
        if (a == b) {
            System.out.println("Пределы интегрирования равны, результат вычисления будет равен 0 в любом случае.");
            return;
        }
        do {
            n+=2;
            h = (b - a) / n; // вычисление длины шага для n
            sum1 = 0;
            sum2 = 0;
            int currentN=0;
            for (int i = 1; i < n; i++) {
                sum1 += 4 * function.calculate(a + i * h);
                ++i;
                if(i==n){ continue;}
                sum1 += 2 * function.calculate(a + i * h);
                currentN=n;
            }
            In = (sum1 + function.calculate(a) + function.calculate(b)) * h / 3; // вычисление интеграла с количеством шагов = n
            System.out.println("In:"+In+"||"+n);
            h = (b - a) / (2 * n); // вычисление длины шага для 2n
            for (int i = 1; i < 2 * n; i++) {
                sum2 += 4 * function.calculate(a + i * h);
                ++i;
                if(i==2*n){ continue;}
                sum2 += 2 * function.calculate(a + i * h);
            }
            I2n = (sum2 + function.calculate(a) + function.calculate(b)) * h / 3;// вычисление интеграла с количеством шагов = 2n
            System.out.println("I2n:"+I2n+"||"+2*n);
        }
        while ((Math.abs(I2n - In) / 15) > eps);
        System.out.println("Значение интеграла:"+I2n+"|| "+In+"\n" +
                "Количество делений:"+2*n+"\n" +
                "Погрешность:" +String.format("%.10f",Math.abs(I2n - In) / 15));
    }

    public void setLimits(Map<String,Double> limits){
        a = limits.get("a");
        b = limits.get("b");
    }
}





