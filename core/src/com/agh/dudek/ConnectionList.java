package com.agh.dudek;

/**
 * Created by dudek on 10/13/16.
 */
public class ConnectionList {

    private int width;
    private int depth;
    private int height;

    private Node[][][] tab;

    public ConnectionList(int n, int m, int h){
        width = n;
        depth = m;
        height = h;

        tab = new Node[width][depth][height];
    }

    public void initialize(int[][][] map){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                for (int k = 0; k < height; k++) {
                    tab[i][j][k] = new Node(i, j, k, map[i][j][k]);
                }
            }
        }

        for (int i = 0; i < width; i++){
            for (int j = 0; j < depth; j++) {
                for (int k = 0; k < height; k++) {
                    setNeighbourhood(tab[i][j][k]);
                }
            }
        }
    }


    public Node getNode(Position position){
        return tab[position.getX()][position.getY()][position.getZ()];
    }

    private void setNeighbourhood(Node node){
        int x = node.getX();
        int y = node.getY();
        int z = node.getZ();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                for (int k = z - 1; k <= z + 1; k++) {
                    if ((i != x || j != y || k != z )&& i >= 0 && j >= 0 && k >= 0 &&
                            i < width && j < depth && k < height) {
                        node.addAdjacentNode(tab[i][j][k]);
                    }
                }
            }
        }
    }


}
