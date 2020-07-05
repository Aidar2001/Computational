package math;


import lombok.Getter;

public enum FewFunctions {
    FUNCTION1("y=|x|") {
        @Override
        public double calculate(double x) {
            return Math.abs(x);
        }
    },
    FUNCTION2("y=x^2") {
        @Override
        public double calculate(double x) {
            return Math.pow(x, 2);
        }
    },
    FUNCTION3("y=1/x") {
        @Override
        public double calculate(double x) {
            return 1 / x;
        }
    },
    FUNCTION4("y=sqrt(x)") {
        @Override
        public double calculate(double x) {
            return Math.sqrt(x);
        }
    },
    FUNCTION5("y=2*sin(x)") {
        @Override
        public double calculate(double x) {
            return 2 * Math.sin(x);
        }
    };

    @Getter
    private String name;

    FewFunctions(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

     abstract double  calculate(double x);
}
