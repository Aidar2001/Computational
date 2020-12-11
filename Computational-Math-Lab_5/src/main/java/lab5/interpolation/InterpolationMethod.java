package lab5.interpolation;

import lab5.io.Reader;

import java.awt.geom.Point2D;
import java.util.List;

public interface InterpolationMethod {
    static Point2D getXi(final List<Point2D> interpolationNodes, double interpolationPointX) {
        if (interpolationPointX >= interpolationNodes.get(0).getX() && interpolationPointX <= interpolationNodes.get(interpolationNodes.size()-1).getX()){
            return interpolationNodes.stream().filter(o -> o.getX() >= interpolationPointX).findFirst().get();
        } else throw new IllegalArgumentException("Точка интерполяции должна находиться в пределах крайних значейний таблицы узлов интерполяции");
    }

    Point2D interpolate(List<Point2D> interpolationNodes, double interpolationPoint);

   default Point2D interpolate(Reader.EnteredData inputData){
       return interpolate(inputData.getInterpolationNodes(),inputData.getInterpolationPoint());
   }
}
