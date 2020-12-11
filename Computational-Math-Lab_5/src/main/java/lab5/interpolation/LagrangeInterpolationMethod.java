package lab5.interpolation;

import java.awt.geom.Point2D;
import java.util.List;

public class LagrangeInterpolationMethod implements InterpolationMethod {
    @Override
    public Point2D interpolate(List<Point2D> interpolationNodes, double interpolationPointX) {
        double interpolationPointY = 0;
        for (int i = 0; i < interpolationNodes.size(); i++) {
            interpolationPointY += calculateL(interpolationNodes,interpolationPointX,interpolationNodes.get(i))*interpolationNodes.get(i).getY();
        }
        return new Point2D.Double(interpolationPointX, interpolationPointY);
    }

    private double calculateL(List<Point2D> interpolationNodes, double interpolationPoint, Point2D x_i ){ // метод для вычисления l_i
        double numerator = interpolationNodes.stream().filter(o->interpolationNodes.indexOf(o)!=interpolationNodes.indexOf(x_i)).
                map(o->interpolationPoint-o.getX()).reduce((n1, n2)->n1*n2).get();
        double denominator = interpolationNodes.stream().filter(o->interpolationNodes.indexOf(o)!=interpolationNodes.indexOf(x_i)).
                map(o->x_i.getX()-o.getX()).reduce((n1, n2)->n1*n2).get();
        return numerator/denominator;
    }
}
