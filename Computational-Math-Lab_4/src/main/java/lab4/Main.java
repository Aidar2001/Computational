package lab4;

import lab4.io.Reader;
import lab4.io.WriterPrinter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
 public static void main(String[] args) {
  Reader.ApproximationInputData entered = new Reader().read();
  Functions function;
  TreeMap<Double, Functions> meanSquareDeviations = new TreeMap<>();
  Map<Functions, LeastSquaresApproximator.Output> outputs = new HashMap<>();

  LeastSquaresApproximator.Output output;

  //  линейная
  function = Functions.LINE;
  LeastSquaresApproximator approximator = new LeastSquaresApproximator(entered.getLINE_X(), entered.getLINE_Y(), 1);
  output = approximator.approxAndCalcStatistics(function);
  meanSquareDeviations.put(output.getSigma(), function);
  outputs.put(function, output);

  //  полином 2-й степени
  function = Functions.QUADRA;
  approximator.setMaxDegree(2);
  output = approximator.approxAndCalcStatistics(function);
  meanSquareDeviations.put(output.getSigma(), function);
  outputs.put(function, output);

  // note экспоненциальная:  phi = ae^(xb), ln(phi) = ln(a)+bx, Y=A+Bx, a=e^A, b=B
  function = Functions.EXPON;
  approximator.setY(entered.getLINE_Y().stream().map(Math::log).collect(Collectors.toList()));
  approximator.setMaxDegree(1);
  output = approximator.approxAndCalcStatistics(function);
  output.setA(Math.pow(Math.E, output.getA()));
  output.updateStats(entered);
  meanSquareDeviations.put(output.getSigma(), function);
  outputs.put(function, output);

  // note степенная: phi = ax^b, ln(phi) = ln(a)+bln(x), Y=A+BX, a=e^A, b=B,
  function = Functions.POW;
  approximator.setMaxDegree(1);
  approximator.setX(entered.getLINE_X().stream().map(Math::log).collect(Collectors.toList()));
  output = approximator.approxAndCalcStatistics(function);
  output.setA(Math.pow(Math.E, output.getA()));
  output.updateStats(entered);
  meanSquareDeviations.put(output.getSigma(), function);
  outputs.put(function, output);

  // note логарифмическая: phi = a*ln(x)+b,  X=ln(x), A=a, B=b
  function = Functions.LOG;
  approximator.setY(entered.getLINE_Y());
  output = approximator.approxAndCalcStatistics(function);
  output.updateStats(entered);
  meanSquareDeviations.put(output.getSigma(), function);
  outputs.put(function, output);

  WriterPrinter.printResult(outputs, entered);


  Shedule.createFrame(entered);

  WriterPrinter.saveToFile(outputs, entered);
 }
}
