package de.kolpa.kmeans.data;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kolpa on 18.04.2017.
 */
public class Cluster {
    private List<Point> points;
    private Point center;
    private Marker marker;
    private String name;

    public Cluster() {
        points = new ArrayList<>();
    }

    public static void clearClusters(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.clearPoints();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public void clearPoints() {
        points.clear();
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public void addToChart(XYChart chart) {
        if (points.size() < 1)
            return;

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (Point point : points) {
            x.add(point.getX());
            y.add(point.getY());
        }

        chart.addSeries(name, x, y).setMarker(marker);
    }
}
