package com.agh.dudek;

/**
 * Created by dudek on 10/13/16.
 */
public class Position {

    private int x;
    private int y;
    private int z;

    public Position(){
        this(0, 0, 0);
    }

    public Position(int x, int y, int z){
        set(x, y, z);
    }

    public void set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }
}
