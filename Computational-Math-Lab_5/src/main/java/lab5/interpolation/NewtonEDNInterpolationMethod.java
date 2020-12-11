package lab5.interpolation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewtonEDNInterpolationMethod implements InterpolationMethod{
    double h;
    Point2D p_i, pi_minus_1;
    @Override
    public Point2D interpolate(List<Point2D> interpolationNodes, double interpolationPointsX) {
        if(!checkSegmentsForEquality(interpolationNodes)){ // проверка что введенны точки равноудалены
            throw new IllegalArgumentException("Узлы интерполяции должны быть равноудалены друг от друга.");
        }
        // определение в какой половине отрезка расположена точка интерполяции для выбора расчетной формулы
        p_i = InterpolationMethod.getXi(interpolationNodes, interpolationPointsX);
        pi_minus_1 = interpolationNodes.get(interpolationNodes.indexOf(p_i)-1);
        h = interpolationNodes.get(1).getX() - interpolationNodes.get(0).getX();
        if(interpolationNodes.get(interpolationNodes.size()/2).getX() > interpolationPointsX){
            return new Point2D.Double(interpolationPointsX,firstFormulaToInterpolateForward(interpolationNodes,interpolationPointsX));
        } else { return new Point2D.Double(interpolationPointsX,secondFormulaToInterpolateBackward(interpolationNodes, interpolationPointsX));}
    }

    private boolean checkSegmentsForEquality(List<Point2D> interpolationNodes) {
        long count = interpolationNodes.stream().skip(1).
                map(o -> o.getX() - interpolationNodes.get(interpolationNodes.indexOf(o) - 1).getX()).filter(o -> o == h).count();
        boolean b = count == interpolationNodes.size();
        return b;
    }

    private double firstFormulaToInterpolateForward(List<Point2D> interpolationNodes, double interpolationPointX){
        double t = (interpolationPointX - interpolationNodes.get(interpolationNodes.indexOf(pi_minus_1)).getX())/h;
        double result = pi_minus_1.getY();
        double multiply=1;
        for (int i = 1; i < interpolationNodes.size()-interpolationNodes.indexOf(pi_minus_1); i++) {
            multiply *= (t-i+1);
            double deltaY = calcDeltaY(interpolationNodes.subList(interpolationNodes.indexOf(pi_minus_1), interpolationNodes.indexOf(pi_minus_1) + i + 1));
            result += (multiply*deltaY)/factorial(i);
        }
        return result;
    }

    private double secondFormulaToInterpolateBackward(List<Point2D> interpolationNodes, double interpolationPointX){
        double t = (interpolationPointX - interpolationNodes.get(interpolationNodes.size()-1).getX())/h;
        double result = interpolationNodes.get(interpolationNodes.size()-1).getY();
        double multiply=1;
        for (int i = 1; i < interpolationNodes.size(); i++) {
            multiply *= (t+i-1);
            double deltaY = calcDeltaY(interpolationNodes.subList(interpolationNodes.size()-i-1, interpolationNodes.size()));
            result += (multiply*deltaY)/factorial(i);
        }
        return result;
    }

    private double calcDeltaY(List<Point2D> points){
        if (points.size()==2){
            return points.get(points.size()-1).getY()-points.get(0).getY();
        } else {
           return calcDeltaY(points.subList(1,points.size()))-calcDeltaY(points.subList(0,points.size()-1));
        }
    }

    private static int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        List<Point2D> points = new ArrayList<>(Arrays.asList(new Point2D.Double(0.5,1.532),new Point2D.Double(0.55,2.5356),
                new Point2D.Double(0.6,3.5406), new Point2D.Double(0.65,4.5462), new Point2D.Double(0.7,5.5504),
                new Point2D.Double(0.75,6.5559), new Point2D.Double(0.8,7.5594)));
        System.out.println(new NewtonEDNInterpolationMethod().interpolate(points,0.761));
    }
}
