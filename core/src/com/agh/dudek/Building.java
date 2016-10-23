package com.agh.dudek;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

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

    public Building(int x, int y, int width, int depth, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.depth = depth;
        this.height = height;
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

    public int getBoundX(){
        return x + width;
    }

    public int getBoundY(){
        return y + depth;
    }

    public Model getModel(){
        if (model == null){
            Random random = new Random();

            ModelBuilder builder = new ModelBuilder();
            this.model = builder.createBox(width * Map.NODE_SIZE, height * Map.NODE_SIZE, depth * Map.NODE_SIZE,
                    new Material(ColorAttribute.createDiffuse(new Color(random.nextInt()))),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);
        }
        return model;
    }
}
