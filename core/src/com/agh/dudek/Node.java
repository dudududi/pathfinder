package com.agh.dudek;

/**
 * Created by dudek on 10/13/16.
 */
public class Node {

    private int f;
    private int g;
    private int h;

    private int walkable;

    private boolean closed;
    private boolean opened;

    private Node parent;
    private Position position;

    public Node(int a, int b, int c, int w){
        this.walkable = w;

        position = new Position(a, b, c);
    }

    public int getX(){
        return position.getX();
    }

    public int getY(){
        return position.getY();
    }

    public int getZ(){
        return position.getZ();
    }

    public int getF(){
        return f;
    }

    public int getG(){
        return g;
    }

    public int getH(){
        return h;
    }

    public Node getParent(){
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public int getG(Node node){
        if (h == node.h){
            return node.g + ((position.getX() == node.getX() || position.getY() == node.getY()) ? 10 : 14);
        } else if (h > node.h) {
            if (position.getX() == node.getX() && position.getY() == node.getY()){
                return node.g + 30;
            }
            else return node.g + ((position.getX() == node.getX() || position.getY() == node.getY()) ? 32 : 34);
        } else {
            if (position.getX() == node.getX() && position.getY() == node.getY()){
                return node.g + 2;
            }
            else return node.g + ((position.getX() == node.getX() || position.getY() == node.getY()) ? 7 : 9);
        }
    }

    public int getH(Node node){
        return (Math.abs(node.getX() - position.getX()) + Math.abs(node.getY() - position.getY()) + Math.abs(node.getZ() - position.getZ())) * 10;
    }

    public void computeScores(Node end){
        g = getG(parent);
        h = getH(end);
        f = g + h;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setClosed(boolean isClosed){
        closed = isClosed;
    }

    public void setOpened(boolean isOpened){
        opened = isOpened;
    }

    public boolean isClosed(){
        return closed;
    }

    public boolean isOpened(){
        return opened;
    }

    public Position getPosition(){
        return position;
    }

    public boolean isWalkable(){
        return walkable == 0;
    }
}
