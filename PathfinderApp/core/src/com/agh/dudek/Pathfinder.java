package com.agh.dudek;

import java.util.List;

/**
 * Created by dudek on 10/14/16.
 */
public interface Pathfinder {
    List<Position> findPath (Position startPosition, Position endPosition, ConnectionList map);
}
