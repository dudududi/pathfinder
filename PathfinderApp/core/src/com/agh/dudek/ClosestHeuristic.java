package com.agh.dudek;

/**
 * Created by dudek on 10/14/16.
 */
public class ClosestHeuristic implements Heuristic {
    @Override
    public double getCost(ConnectionList map, Node currentNode, Node targetNode) {
        float dx = targetNode.getX() - currentNode.getX();
        float dy = targetNode.getY() - currentNode.getY();
        float dz = targetNode.getZ() - currentNode.getZ();

        return Math.sqrt(dx*dx + dy*dy + dz* dz);
    }
}
