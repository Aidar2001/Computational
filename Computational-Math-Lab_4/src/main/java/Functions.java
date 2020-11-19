import lombok.Getter;

public enum Functions {
    LINE("Линейная") {
        public double calculate(double x){
            return a*x+b;
        }
    },
    QUADRA("Полиномиальная") {
        @Override
        double calculate(double x) {return a*x*x+b*x+c;}

    },
    EXPON("Экспоненциальная") {
        @Override
        double calculate(double x) {return a*Math.pow(Math.E,b*x);}

    },
    LOG("Логарифмическая") {
        @Override
        double calculate(double x) {return a*Math.log(x)+b;}

    },
    POW("Степенная") {
        @Override
        double calculate(double x) {return a*Math.pow(x,b);}

    };
    @Getter
    private String name;

    Functions(String name) {
        this.name = name;
    }

    protected double a, b, c;

    public double calculate(double a, double b, double x) {
        setABX(a, b);
        return calculate(x);
    }

    private void setABX(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double calculate(double a, double b, double c, double x) {
        this.c = c;
        return calculate(a,b,x);
    }

    abstract double calculate(double x);
}
