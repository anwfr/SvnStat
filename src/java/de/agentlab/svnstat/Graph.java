/*
 * Copyright © 2006 Juergen Lind (jli@agentlab.de), 2014 Joe Egan (J0e3gan@gmail.com).
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 */

package de.agentlab.svnstat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.List;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelPosition;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelRenderer;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.encoders.JPEGEncoder;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.AxisTypeProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.ClusteredBarChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PieChart2DProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.StackedBarChartProperties;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.properties.util.ChartStroke;
import org.jCharts.types.ChartType;
import org.jCharts.types.PieLabelType;


public class Graph {

    public static List paints = new AutoList();

    public void stackedBarChart(
        int width,
        int height,
        String xAxisTitle,
        String yAxisTitle,
        String[] xAxisLabels,
        String title,
        String[] legendLabels,
        double[][] data,
        String filename) throws Exception {

        LegendProperties legendProperties = new LegendProperties();
        ChartProperties chartProperties = new ChartProperties();
        AxisProperties axisProperties = new AxisProperties(false);

        ChartFont axisScaleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 10), Color.black);
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
        axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

        ChartFont axisTitleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 12), Color.black);
        axisProperties.getXAxisProperties().setAxisTitleChartFont(axisTitleFont);
        axisProperties.getYAxisProperties().setAxisTitleChartFont(axisTitleFont);

        ChartFont titleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 14), Color.black);
        chartProperties.setTitleFont(titleFont);

        this.showGrid(axisProperties);

        ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false, false, true, -1);
        valueLabelRenderer.setValueLabelPosition(ValueLabelPosition.ON_TOP);
        valueLabelRenderer.useVerticalLabels(false);

        StackedBarChartProperties stackedBarChartProperties = new StackedBarChartProperties();
        stackedBarChartProperties.setShowOutlinesFlag(false);

        IAxisDataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        dataSeries.addIAxisPlotDataSet(
            new AxisChartDataSet(
                data,
                legendLabels,
                this.getPaints(legendLabels.length),
                ChartType.BAR_STACKED,
                stackedBarChartProperties));

        AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, width, height);
        JPEGEncoder.encode(axisChart, 1.0f, new FileOutputStream(filename));

    }

    public void lineChart(
        int width,
        int height,
        String xAxisTitle,
        String yAxisTitle,
        String[] xAxisLabels,
        String title,
        String[] legendLabels,
        double[][] data,
        long yAxisMinValue,
        long yAxisIncrement,
        String filename) throws Exception {

        LegendProperties legendProperties = new LegendProperties();
        ChartProperties chartProperties = new ChartProperties();
        AxisProperties axisProperties = new AxisProperties(false);

        ChartFont axisScaleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 10), Color.black);
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
        axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

        ChartFont axisTitleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 12), Color.black);
        axisProperties.getXAxisProperties().setAxisTitleChartFont(axisTitleFont);
        axisProperties.getYAxisProperties().setAxisTitleChartFont(axisTitleFont);

        // If yAxisIncrement <= 0, then org.jCharts.properties.DataAxisProperties.setUserDefinedScale will throw a
        // org.jCharts.properties.PropertyException ("The Axis Increment can not be a negative value or zero.").
        if (yAxisMinValue != -1 && yAxisIncrement > 0) {
            DataAxisProperties dataAxisProperties = (DataAxisProperties) axisProperties.getYAxisProperties();
            dataAxisProperties.setUserDefinedScale(yAxisMinValue, yAxisIncrement);
        }

        ChartFont titleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 14), Color.black);
        chartProperties.setTitleFont(titleFont);

        this.showGrid(axisProperties);

        ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false, false, true, -1);
        valueLabelRenderer.setValueLabelPosition(ValueLabelPosition.ON_TOP);
        valueLabelRenderer.useVerticalLabels(false);

        Stroke[] strokes = { LineChartProperties.DEFAULT_LINE_STROKE };
        Shape[] shapes = { PointChartProperties.SHAPE_DIAMOND };
        LineChartProperties lineChartProperties = new LineChartProperties(strokes, shapes);

        IAxisDataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        Paint[] color = new Paint[] { Color.BLUE };
        dataSeries.addIAxisPlotDataSet(
            new AxisChartDataSet(data, legendLabels, color, ChartType.LINE, lineChartProperties));

        AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, width, height);
        JPEGEncoder.encode(axisChart, 1.0f, new FileOutputStream(filename));

    }

    public void pieChart(
        int width, int height, String[] labels, String title, double[] data, String filename) throws Exception {

        LegendProperties legendProperties = new LegendProperties();
        ChartProperties chartProperties = new ChartProperties();
        AxisProperties axisProperties = new AxisProperties(false);

        ChartFont axisScaleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 10), Color.black);
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
        axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

        ChartFont axisTitleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 12), Color.black);
        axisProperties.getXAxisProperties().setAxisTitleChartFont(axisTitleFont);
        axisProperties.getYAxisProperties().setAxisTitleChartFont(axisTitleFont);

        ChartFont titleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 14), Color.black);
        chartProperties.setTitleFont(titleFont);

        this.showGrid(axisProperties);

        ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false, false, true, -1);
        valueLabelRenderer.setValueLabelPosition(ValueLabelPosition.ON_TOP);
        valueLabelRenderer.useVerticalLabels(false);

        PieChart2DProperties pieChart2DProperties = new PieChart2DProperties();
        pieChart2DProperties.setPieLabelType(PieLabelType.VALUE_LABELS);

        PieChartDataSet pieChartDataSet =
            new PieChartDataSet(title, data, labels, this.getPaints(labels.length), pieChart2DProperties);

        PieChart2D pieChart2D = new PieChart2D(pieChartDataSet, legendProperties, chartProperties, width, height);

        JPEGEncoder.encode(pieChart2D, 1.0f, new FileOutputStream(filename));

    }

    public void verticalBarChart(
        int width,
        int height,
        String xAxisTitle,
        String yAxisTitle,
        String[] xAxisLabels,
        String title,
        String[] legendLabels,
        double[][] data,
        String filename) throws Exception {

        LegendProperties legendProperties = new LegendProperties();
        ChartProperties chartProperties = new ChartProperties();
        ClusteredBarChartProperties clusteredBarChartProperties = new ClusteredBarChartProperties();

        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        AxisChartDataSet axisChartDataSet =
            new AxisChartDataSet(
                data,
                legendLabels,
                getPaints(legendLabels.length),
                ChartType.BAR_CLUSTERED,
                clusteredBarChartProperties);

        dataSeries.addIAxisPlotDataSet(axisChartDataSet);

        AxisProperties axisProperties = new AxisProperties(true);

        ChartFont axisScaleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 10), Color.black);
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
        axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

        ChartFont axisTitleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 12), Color.black);
        axisProperties.getXAxisProperties().setAxisTitleChartFont(axisTitleFont);
        axisProperties.getYAxisProperties().setAxisTitleChartFont(axisTitleFont);

        ChartFont titleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 14), Color.black);
        chartProperties.setTitleFont(titleFont);

        this.showGrid(axisProperties);

        AxisChart axisChart =
            new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, width, height);

        JPEGEncoder.encode(axisChart, 1.0f, new FileOutputStream(filename));

    }

    private void showGrid(AxisProperties axisProperties) {
        ChartStroke stroke =
            new ChartStroke(
                new BasicStroke(
                    0.5f,
                    0,
                    0,
                    1f,
                    new float[] { 0.5f, 0.5f, 1f, 0.5f },
                    0.5f),
                Color.BLACK);
        axisProperties.getXAxisProperties().setGridLineChartStroke(stroke);
        axisProperties.getXAxisProperties().setShowGridLines(AxisTypeProperties.GRID_LINES_ONLY_WITH_LABELS);
        axisProperties.getYAxisProperties().setGridLineChartStroke(stroke);
        axisProperties.getYAxisProperties().setShowGridLines(AxisTypeProperties.GRID_LINES_ONLY_WITH_LABELS);
    }

    private Paint[] getPaints(int count) {
        Paint[] result = new Paint[count];
        for (int i = 0; i < count; i++) {
            result[i] = (Paint) paints.get(i);
        }
        return result;
    }

    public static void parseConfig() {
        for (Enumeration<String> e = Config.getKeys(); e.hasMoreElements();) {
            String key = e.nextElement();
            if (key.startsWith("Color")) {
                String indexStr = key.substring(key.indexOf(".") + 1);
                paints.set(Integer.parseInt(indexStr), Color.decode(Config.getProperty(key)));
            }
        }
    }

}
