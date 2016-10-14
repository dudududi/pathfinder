package com.agh.dudek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/13/16.
 */
public class ConnectionList {

    private int width;
    private int depth;
    private int height;
    private int size;

    private List<List<List<Node>>> tab;
    private List<List<Node>> arr;

    public ConnectionList(int n, int m, int h){
        width = n;
        depth = m;
        height = h;

        size = width * depth * height;

        tab = new ArrayList<>(width);
        for (int i = 0; i < n; i++){
            List<List<Node>> depthNodes = new ArrayList<>(depth);
            tab.add(depthNodes);
            for (int j = 0; j < depth; j++){
                List<Node> heightNodes = new ArrayList<>(height);
                depthNodes.add(heightNodes);
                for (int k = 0; k < height; k++){
                    heightNodes.add(null);
                }
            }
        }

        arr = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            arr.add(new ArrayList<Node>());
        }
    }

    public void initialize(int[][][] map){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                for (int k = 0; k < height; k++) {
                    int l = i * depth + j + k * width * depth;

                    for (int a = i - 1; a <= i + 1; a++) {
                        for (int b = j - 1; b <= j + 1; b++) {
                            for (int c = k - 1; c <= k + 1; c++) {
                                if (a != i || b != j || c != k) {
                                    if ((a >= 0 && b >= 0 && c >= 0) && (a < width && b < depth && c < height)) {
                                        if (tab.get(a).get(b).get(c) == null) {
                                            Node node = new Node(a, b, c, map[a][b][c]);

                                            tab.get(a).get(b).add(c, node);
                                            arr.get(l).add(tab.get(a).get(b).get(c));
                                        } else {
                                            arr.get(l).add(tab.get(a).get(b).get(c));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public List<Node> getAdjacentNodes(int x, int y, int z){
        return arr.get(depth*x + y + z*depth*width);
    }

    public Node getNode(int x, int y, int z){
        return tab.get(x).get(y).get(z);
    }
}
