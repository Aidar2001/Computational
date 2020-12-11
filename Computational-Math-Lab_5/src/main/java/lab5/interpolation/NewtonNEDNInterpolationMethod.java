package lab5.interpolation;

import java.awt.geom.Point2D;
import java.util.List;

public class NewtonNEDNInterpolationMethod implements InterpolationMethod {
    // note Nn(x)=f(x0)+f(x0,x1)*(x-x0)+f(x0,x1,x2)*(x-x0)*(x-x1)
    @Override
    public Point2D interpolate(List<Point2D> interpolationNodes, double interpolationPointX) {
        int index = interpolationNodes.indexOf(InterpolationMethod.getXi(interpolationNodes, interpolationPointX));
        interpolationNodes.subList(index-2,index+1);
        double result = calculateDividedDifference(interpolationNodes.subList(0,1));
        for (int i = 1; i < interpolationNodes.size(); i++) {
            result+=calculateDividedDifference(interpolationNodes.subList(0,i+1))*interpolationNodes.stream().limit(i).map(o->interpolationPointX-o.getX()).reduce((n1,n2)->n1*n2).get();
        }
        return new Point2D.Double(interpolationPointX,result);
    }

    private double calculateDividedDifference(List<Point2D> args){
        if (args.size()==1){
            return args.get(0).getY();
        } else {
            return (calculateDividedDifference(args.subList(1,args.size()))- calculateDividedDifference(args.subList(0,args.size()-1)))/
                    (args.get(args.size()-1).getX()-args.get(0).getX());
        }
    }
}
