package ui;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import math.equation.nonlinear.OneFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import utils.Adapter.InputData;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineChartSchedule {
    @Getter
    private static XYSeriesCollection dataset = new XYSeriesCollection();
    private static int i = 0;
    //    private static final long serialVersionUID = 1L;
    private static JFreeChart chart;
//    @Getter
//    private final ChartPanel chartPanel;

//    public LineChartShedule() {
//        chart = ChartFactory.createXYLineChart(
//                "Демонстрация Spline Renderer",
//                null,                        // x axis label
//                null,                        // y axis label
//                null,                        // data
//                PlotOrientation.VERTICAL,
//                true,                        // include legend
//                false,                       // tooltips
//                false                        // urls
//        );
//        chartPanel = new ChartPanel(chart);
//
//        chartPanel.setPreferredSize(new java.awt.Dimension(560, 480));
//        //setContentPane(chartPanel);
//    }

    public static XYSeriesCollection createDataset() {
        dataset.removeAllSeries();
        final XYSeries series1 = new XYSeries(OneFunction.FUNCTION_INSTANCE.getName());
        double length = (InputData.UP_LIMIT.getVal() - InputData.LOW_LIMIT.getVal()) * 0.1;
        double downLimitVal = InputData.LOW_LIMIT.getVal() - length;
        double upLimitVal = InputData.UP_LIMIT.getVal() + length;
        double x = downLimitVal;
        while (x < upLimitVal) {
            series1.add(x, OneFunction.FUNCTION_INSTANCE.calculate(x));
            x += 0.1;
        }
        dataset.addSeries(series1);
        return dataset;
    }

    public static ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseZoomable(true, true);
        return panel;
    }

    public static JFreeChart getChart() {
        chart = ChartFactory.createXYLineChart("Schedule", "X", "Y",
                LineChartSchedule.getDataset());
        chart.removeLegend();
        return chart;
    }

    public static JFreeChart createChart(JFreeChart chart, final XYSeriesCollection dataset) {
        chart.setBackgroundPaint(Color.white);


        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(232, 232, 232));
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);

        // Определение отступа меток делений
        plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));

        // Скрытие осевых линий и меток делений
        ValueAxis axis = plot.getDomainAxis();
        //axis.setAxisLineVisible(false);    // осевая линия

        // Настройка NumberAxis
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //  rangeAxis.setAxisLineVisible(false);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

//        if (dataset.getSeries(0).getMaxX()>)
//        plot.getRangeAxis().setRange(- 15, 10);
//        plot.getDomainAxis().setRange(-5,5);

        // Настройка XYSplineRenderer
        // Precision: the number of line segments between 2 points [default: 5]
        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0,new BasicStroke(2.0f));

        // Наборы данных
        plot.setDataset(0, dataset);

        // Подключение Spline Renderer к наборам данных
        plot.setRenderer(0, renderer);

        return chart;
    }
}