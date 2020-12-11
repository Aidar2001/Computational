package lab4;

import lab4.io.Reader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import lab3.ui.LineChartSchedule;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Shedule {
    private static XYSeriesCollection dataset = new XYSeriesCollection();

    public static XYSeriesCollection createAllDatasets(Reader.ApproximationInputData input) {
        Arrays.asList(Functions.values()).forEach(f->createDataset(input.getLINE_X(),f));

        return dataset;
    }

    public static XYSeriesCollection createDataset(Functions function, Reader.ApproximationInputData input) {
        return createDataset(input.getLINE_X(),function);
    }

    public static XYSeriesCollection createDataset(List<Double> X, Functions function) {
        final XYSeries series1 = new XYSeries(function.getName());
        X.forEach(x -> series1.add(x, BigDecimal.valueOf(function.calculate(x))));
        dataset.addSeries(series1);
        return dataset;
    }

    private static XYSeriesCollection createInputDataset(Reader.ApproximationInputData input){
        XYSeriesCollection inputDataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("введенные точки");
        ArrayList<Double> X = (ArrayList<Double>) input.getLINE_X();
        ArrayList<Double> Y = (ArrayList<Double>) input.getLINE_Y();
        X.forEach(o-> series.add(o,Y.get(X.indexOf(o))));
        inputDataset.addSeries(series);
        return inputDataset;
    }

    public static JFreeChart createChart(XYSeriesCollection dataset, Reader.ApproximationInputData input){
        JFreeChart chart = ChartFactory.createXYLineChart("Schedule", "X", "Y", null);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(232, 232, 232));
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);

        // Определение отступа меток делений
        plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));


        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesStroke(0,new BasicStroke(2.0f));

        XYLineAndShapeRenderer inputRenderer = new XYLineAndShapeRenderer();
        inputRenderer.setSeriesLinesVisible(0,false);
        inputRenderer.setSeriesPaint(0,Color.BLACK);
        inputRenderer.setSeriesStroke(0,new BasicStroke(5));
        // Наборы данных
        plot.setDataset(0, dataset);
        plot.setDataset(1, createInputDataset(input));

        // Подключение Spline Renderer к наборам данных
        plot.setRenderer(0, renderer);
        plot.setRenderer(1,inputRenderer);
        return chart;
    }

    public static JFrame createFrame(Reader.ApproximationInputData input){
        JFrame frame = new JFrame("lab4.Shedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,500);
        JFreeChart chart = createChart(createAllDatasets(input),input);
        ChartPanel panel = LineChartSchedule.createChartPanel(chart);
        panel.setPreferredSize(new Dimension(1000,500));
        frame.setContentPane(panel);
        frame.setVisible(true);
        return frame;
    }
}
