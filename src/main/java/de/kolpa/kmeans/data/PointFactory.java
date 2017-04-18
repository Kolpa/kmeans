package de.kolpa.kmeans.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kolpa on 18.04.2017.
 */
public class PointFactory {
    public static List<Point> getPointsFromFile(String path) throws IOException {
        List<Point> points = new ArrayList<>();

        FileReader fread = new FileReader(path);
        BufferedReader bread = new BufferedReader(fread);

        String line = null;

        while ((line = bread.readLine()) != null) {
            String[] vals = line.split(" ");
            points.add(new Point(Double.parseDouble(vals[0]), Double.parseDouble(vals[1])));
        }

        return points;
    }

    public static Point createRandom(int min, int max) {
        Random r = new Random();

        double x = min + (max - min) * r.nextDouble();
        double y = min + (max - min) * r.nextDouble();

        return new Point(x, y);
    }
}
