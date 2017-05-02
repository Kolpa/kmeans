package de.kolpa.kmeans;

import de.kolpa.kmeans.data.Cluster;
import de.kolpa.kmeans.data.Point;
import de.kolpa.kmeans.data.PointFactory;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kolpa on 18.04.2017.
 */
public class Main {
    private static int MAX_CLUSTERS = 4;
    private static final int MAX_POS = 3;
    private static final int MIN_POS = -3;

    private static List<Point> points;
    private static List<Cluster> clusters;

    private static XYChart chart;

    private static void init(String pointfile) throws IOException {
        points = PointFactory.getPointsFromFile(pointfile);

        clusters = new ArrayList<>();

        for (int i = 0; i < MAX_CLUSTERS; i++) {
            Cluster cluster = new Cluster();

            cluster.setMarker(SeriesMarkers.SQUARE);
            cluster.setName("Cluster: " + (i + 1));
            cluster.setCenter(PointFactory.createRandom(MIN_POS, MAX_POS));

            clusters.add(cluster);
        }
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

        double distance = 0.0;
        Cluster closeCluster = clusters.get(0);

        for (Point point : points) {
            min = max;

            for (Cluster cluster : clusters) {
                distance = point.distance(cluster.getCenter());
                if (distance < min) {
                    min = distance;
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
        }
    }

    private static void doRun(String pointfile, JPanel panel) {
        try {
            init(pointfile);
            calculate();

            List<String> todel = new ArrayList<>();

            todel.addAll(chart.getSeriesMap().keySet());

            for (String del : todel)
                chart.removeSeries(del);

            for (Cluster cluster : clusters)
                cluster.addToChart(chart);

            panel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        chart = new XYChartBuilder().build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);

        SwingUtilities.invokeLater(() -> {

            // Create and set up the window.
            JFrame frame = new JFrame("K Means Algorithm");

            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // chart
            JPanel chartPanel = new XChartPanel<>(chart);
            frame.add(chartPanel, BorderLayout.CENTER);

            // load frame
            JPanel loadPanel = new JPanel(new BorderLayout());
            frame.add(loadPanel, BorderLayout.SOUTH);

            JPanel filePanel = new JPanel(new BorderLayout());
            loadPanel.add(filePanel, BorderLayout.NORTH);

            JTextField loadPath = new JTextField("C:\\Users\\Kolya\\Downloads\\data.txt");
            filePanel.add(loadPath, BorderLayout.CENTER);

            // label
            JButton load = new JButton("Load File");
            filePanel.add(load, BorderLayout.EAST);

            JSlider slider = new JSlider(1, 10, 4);
            slider.setMajorTickSpacing(1);
            slider.setPaintLabels(true);
            loadPanel.add(slider, BorderLayout.CENTER);

            load.addActionListener((l) -> {
                MAX_CLUSTERS = slider.getValue();
                doRun(loadPath.getText(), chartPanel);
            });

            // Display the window.
            frame.pack();
            frame.setVisible(true);
        });
    }
}
