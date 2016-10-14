package com.agh.dudek;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 9/12/16.
 */
public class Map {

    public static final int NODE_SIZE = 4;

    private List<Building> buildings;
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
        map = new int[width][depth][height];

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createLineGrid(width, depth,
                NODE_SIZE, NODE_SIZE,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);
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
        connectionList = new ConnectionList(width, depth, height);
        connectionList.initialize(map);
    }

    public List<Position> findPath(Position startPosition, Position endPosition){
        Pathfinder pathfinder = new AStarPathfinder();
        List<Position> path = pathfinder.findPath(startPosition, endPosition, connectionList);

        for (Position p: path){
            System.out.println("(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ")");
        }

        return path;
    }

    public List<Building> getBuildings(){
        return buildings;
    }


    public Model getModel(){
        return model;
    }

    public int getWidth(){
        return width;
    }

    public int getDepth(){
        return depth;
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
