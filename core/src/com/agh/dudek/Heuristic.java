package com.agh.dudek;

/**
 * Created by dudek on 10/14/16.
 */
public interface Heuristic {
    double getCost(ConnectionList map, Node currentNode, Node targetNode);
}
