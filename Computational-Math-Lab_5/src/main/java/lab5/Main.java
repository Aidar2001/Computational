package lab5;

import lab5.io.Reader;

import java.awt.geom.Point2D;

public class Main {
    public static void main(String[] args) {
        Reader.EnteredData enteredData = Reader.readInputData();
        Point2D point = enteredData.getInterpolationMethod().interpolate(enteredData);
        System.out.println("y("+point.getX()+")"+" = "+point.getY());
    }
}
