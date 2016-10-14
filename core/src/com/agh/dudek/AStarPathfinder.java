package com.agh.dudek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/13/16.
 */
public class AStarPathfinder implements Pathfinder {

    public List<Position> findPath (Position startPosition, Position endPosition, ConnectionList map) {
        Node start = map.getNode(startPosition);
        Node end = map.getNode(endPosition);

        List<Node> closed = new ArrayList<>();
        List<Node> opened = new ArrayList<>();

        //Add the start node to opened list
        opened.add(start);

        Node current = opened.get(0);
        while (current != end) {
            //Looking for the smallest F value in opened list
            for (Node node : opened) {
                if (node == opened.get(0) || node.getF() <= current.getF()) {
                    current = node;
                }
            }
            //remove current point from opened list
            opened.remove(current);
            current.setOpened(false);

            //add current point to closed list
            closed.add(current);
            current.setClosed(true);

            //Getting list of adjacent nodes
            List<Node> adjacentNodes = current.getAdjacentNodes();
            for (Node child : adjacentNodes) {
                if (child.isClosed() || !child.isWalkable()) { //TODO !!!!!
                    continue;
                }
                if (child.isOpened()) {
                    if (child.getG() > child.getG(current)) {

                        // Change its parent and g score
                        child.setParent(current);
                        child.computeScores(end);
                    }
                } else {
                    //Add it to the opened list with current point as parent
                    opened.add(child);
                    child.setOpened(true);

                    // Compute it's g, h and f score
                    child.setParent(current);
                    child.computeScores(end);
                }
            }
        }

        //reset
        for (Node node: opened){
            node.setOpened(false);
        }

        for (Node node: closed) {
            node.setClosed(false);
        }

        List<Position> path = new ArrayList<>();
        while (current.hasParent() && current != start){
            path.add(current.getPosition());
            current = current.getParent();
        }
        return path;
    }
}
