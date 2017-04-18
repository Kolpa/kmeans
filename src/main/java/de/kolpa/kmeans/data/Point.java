package de.kolpa.kmeans.data;

/**
 * Created by Kolpa on 18.04.2017.
 */
public class Point {
    private double x;
    private double y;

    private Cluster cluster;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow((this.getY() - other.getY()), 2) + Math.pow((this.getX() - other.getX()), 2));
    }

    public Point copy() {
        return new Point(x, y);
    }
}
