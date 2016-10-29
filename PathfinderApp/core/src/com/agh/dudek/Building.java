package com.agh.dudek;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by dudek on 9/21/16.
 */
public class Building {

    private int x;
    private int y;

    private int width;
    private int depth;
    private int height;

    private Model model;
    private List<Point> buildingPoints;

    public Building(int x, int y, int width, int depth, int height) {
        this(x, y, height);

        this.width = width;
        this.depth = depth;
    }

    private Building(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;

        width = 1;
        depth = 1;

        buildingPoints = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getBoundX() {
        return x + width;
    }

    public int getBoundY() {
        return y + depth;
    }

    protected Model getModel() {
        if (model == null) {
            Random random = new Random();

            ModelBuilder builder = new ModelBuilder();
            this.model = builder.createBox(width * Map.NODE_SIZE, height * Map.NODE_SIZE, depth * Map.NODE_SIZE,
                    new Material(ColorAttribute.createDiffuse(new Color(random.nextInt()))),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);

        }
        return model;
    }

    protected boolean shouldMergePoints(){
        return buildingPoints.size() > 0;
    }

    protected List<Point> getBuildingPoints(){
        return buildingPoints;
    }

    protected static List<Building> createBuildingsFromPoints(List<Point> pointList) {
        List<Building> detectedBuildings = new ArrayList<>();
        Collections.sort(pointList);

        //Add first building with fist point...
        Point firstPoint = pointList.get(0);
        Building firstBuilding = new Building(firstPoint.x, firstPoint.y, 1, 1, firstPoint.height);
        firstBuilding.buildingPoints.add(firstPoint);

        pointList.remove(0);
        detectedBuildings.add(firstBuilding);
        for (Point point : pointList) {
            Building belongingBuilding = point.getBelongingBuilding(detectedBuildings);
            if (belongingBuilding == null) {
                belongingBuilding = new Building(point.x, point.y, 1, 1, point.height);
                detectedBuildings.add(belongingBuilding);
            }
            belongingBuilding.buildingPoints.add(point);
        }
        return detectedBuildings;
    }

    public static class Point implements Comparable<Point> {
        private static final int X_WEIGHT = 10000;
        private static final int Y_WEIGHT = 1;
        private static final int TOLERANCE_LEVEL = 1;

        public int x;
        public int y;
        public int height;

        public Point(int x, int y, int height) {
            this.x = x;
            this.y = y;
            this.height = height;
        }

        @Override
        public int compareTo(Point o) {
            return (X_WEIGHT * x + Y_WEIGHT * y) - (X_WEIGHT * o.x + Y_WEIGHT * o.y);
        }

        private Building getBelongingBuilding(List<Building> buildings) {
            for (Building b: buildings) {
                for (Point p : b.buildingPoints) {
                    if (isNeighbor(p)) {
                        return b;
                    }
                }
            }
            return null;
        }

        private boolean isNeighbor(Point p) {
            int comparisonResult = Math.abs(p.compareTo(this));

            return ((X_WEIGHT - Y_WEIGHT - TOLERANCE_LEVEL) <= comparisonResult && comparisonResult <= (X_WEIGHT + Y_WEIGHT + TOLERANCE_LEVEL)) ||
                    Y_WEIGHT == comparisonResult;
        }
    }
}
