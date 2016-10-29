package com.agh.dudek;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 9/12/16.
 */
public class Map {

    public static final int NODE_SIZE = 4;

    private List<Building> buildings;
    private List<Building.Point> buildingsPoints;
    private int width;
    private int depth;
    private int height;
    private int[][][] map;
    private Model model;

    private ConnectionList connectionList;

    public Map(int width, int depth, int height){
        this.width = width;
        this.depth = depth;
        this.height = height;

        buildings = new ArrayList<>();
        buildingsPoints = new ArrayList<>();
        map = new int[width][depth][height];
    }

    public void addBuilding(Building building) {
        if (!canBeAdded(building)) return;
        buildings.add(building);
        for (int i = building.getX(); i < building.getBoundX(); i++) {
            for (int j = building.getY(); j < building.getBoundY(); j++) {
                for (int k = 0; k < building.getHeight(); k++) {
                    map[i][j][k] = building.getHeight();
                }
            }
        }
    }

    public void createGraph(){
        if (buildingsPoints.size() > 0){
            List<Building> detectedBuildings = Building.createBuildingsFromPoints(buildingsPoints);
            for (Building b: detectedBuildings){
                for (Building.Point p: b.getBuildingPoints()) {
                    for (int i =0; i < p.height; i++) {
                        map[p.x][p.y][i] = p.height;
                    }
                }
            }
            buildings.addAll(detectedBuildings);
        }

        connectionList = new ConnectionList(width, depth, height);
        connectionList.initialize(map);
    }

    public List<Position> findPath(Position startPosition, Position endPosition){
        Pathfinder pathfinder = new AStarPathfinder();
        return pathfinder.findPath(startPosition, endPosition, connectionList);
    }

    public List<Building> getBuildings(){
        return buildings;
    }


    protected Model getModel(){
        if (model == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            model = modelBuilder.createLineGrid(width, depth,
                    NODE_SIZE, NODE_SIZE,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);
        }
        return model;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++){
            for (int j = 0; j < width; j++) {
                stringBuilder.append(map[j][i][0]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int getWidth(){
        return width;
    }

    public int getDepth(){
        return depth;
    }

    public int getHeight(){
        return height;
    }


    public void addBuildingPoint(int x, int y, int height){
        buildingsPoints.add(new Building.Point(x, y, height));
    }

    private boolean canBeAdded(Building b) {
        if (b.getBoundX() > width || b.getBoundY() > depth || b.getHeight() > height) {
            return false;
        }
        for (int i = b.getX(); i < b.getBoundX(); i++) {
            for (int j = b.getY(); j < b.getBoundY(); j++) {
                if (map[i][j][0] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
