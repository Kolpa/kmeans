package de.kolpa.kmeans;

import de.kolpa.kmeans.data.Cluster;
import de.kolpa.kmeans.data.Point;
import de.kolpa.kmeans.data.PointFactory;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kolpa on 18.04.2017.
 */
public class Main {
    private static final int MAX_CLUSTERS = 4;
    private static final int MAX_POS = 3;
    private static final int MIN_POS = -3;

    private static List<Point> points;
    private static List<Cluster> clusters;

    private static XYChart chart;
    private static SwingWrapper<XYChart> sw;

    private static void init() throws IOException {
        points = PointFactory.getPointsFromFile("C:\\Users\\Kolpa\\Downloads\\data.txt");

        clusters = new ArrayList<>();

        for (int i = 0; i < MAX_CLUSTERS; i++) {
            Cluster cluster = new Cluster();

            cluster.setMarker(SeriesMarkers.SQUARE);
            cluster.setName("Cluster: " + (i + 1));
            cluster.setCenter(PointFactory.createRandom(MIN_POS, MAX_POS));

            clusters.add(cluster);
        }

        List<Double> xcenters = new ArrayList<>();
        List<Double> ycenters = new ArrayList<>();


        for (Cluster cluster : clusters) {
            xcenters.add(cluster.getCenter().getX());
            ycenters.add(cluster.getCenter().getY());
        }

        chart.addSeries("Centers", xcenters, ycenters).setMarker(SeriesMarkers.TRIANGLE_UP);
    }

    private static List<Point> getClusterCenters() {
        List<Point> centers = new ArrayList<>();

        for (Cluster cluster : clusters) {
            centers.add(cluster.getCenter().copy());
        }

        return centers;
    }

    private static void setClusters() {
        double max = Double.MAX_VALUE;
        double min;

        double distace = 0.0;
        Cluster closeCluster = clusters.get(0);

        for (Point point : points) {
            min = max;

            for (Cluster cluster : clusters) {
                distace = point.distance(cluster.getCenter());
                if (distace < min) {
                    min = distace;
                    closeCluster = cluster;
                }
            }

            point.setCluster(closeCluster);
            closeCluster.addPoint(point);
        }
    }

    private static void calcClusterCenters() {
        for (Cluster cluster : clusters) {
            double x = 0, y = 0;

            int pct = cluster.getPoints().size();

            for (Point point : cluster.getPoints()) {
                x += point.getX();
                y += point.getY();
            }

            if (pct > 0) {
                double xn = x / pct;
                double yn = y / pct;
                cluster.getCenter().setX(xn);
                cluster.getCenter().setY(yn);
            }
        }
    }

    private static void calculate() {
        boolean done = false;
        int iters = 0;

        while (!done) {
            Cluster.clearClusters(clusters);

            List<Point> centersOld = getClusterCenters();

            setClusters();

            calcClusterCenters();

            iters++;

            List<Point> centersNew = getClusterCenters();

            double dist = 0;

            for (int i = 0; i < centersOld.size(); i++) {
                dist += centersOld.get(i).distance(centersNew.get(i));
            }

            System.out.println("Iteration: " + iters);
            System.out.println("Distance: " + dist);

            done = dist == 0;

            List<Double> xcenters = new ArrayList<>();
            List<Double> ycenters = new ArrayList<>();


            for (Cluster cluster : clusters) {
                xcenters.add(cluster.getCenter().getX());
                ycenters.add(cluster.getCenter().getY());

                if (cluster.getPoints().size() > 0)
                    cluster.updateChart(chart);
            }

            chart.updateXYSeries("Centers", xcenters, ycenters, null);
            sw.repaintChart();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            chart = new XYChartBuilder().build();

            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);

            sw = new SwingWrapper<>(chart);
            sw.displayChart();

            init();

            calculate();

            sw.repaintChart();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
