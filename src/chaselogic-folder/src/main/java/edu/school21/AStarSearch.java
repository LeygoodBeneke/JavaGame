package edu.school21;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarSearch {
    public static class Pair {
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair pair = (Pair) obj;
            return first == pair.first && second == pair.second;
        }
    }

    public static class Tuple {
        double value;
        int i;
        int j;

        public Tuple(double value, int i, int j) {
            this.value = value;
            this.i = i;
            this.j = j;
        }
    }

    public static class Node {
        public Pair parent;
        public double distance;

        public Node(Pair parent, double distance) {
            this.parent = parent;
            this.distance = distance;
        }

        public Node() {
            this.parent = new Pair(-1, -1);
            this.distance = Double.MAX_VALUE;
        }

    }

    private static boolean isValid(Pair point, int size) {
        return point.first >= 0 && point.first < size && point.second >= 0 && point.second < size;

    }

    private static boolean isUnblocked(Pair point, int[][] map, int size) {
        return isValid(point, size) && map[point.first][point.second] == 0;
    }

    private static boolean isDestination(Pair point, Pair dest) {
        return point.equals(dest);
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static List<Pair> tracePath(Node[][] nodeDetails, Pair dest) {
        int row = dest.first;
        int col = dest.second;
        List<Pair> path = new ArrayList<>();
        while (!(nodeDetails[row][col].parent.first == row && nodeDetails[row][col].parent.second == col)) {
            path.add(new Pair(row, col));
            int temp_row = nodeDetails[row][col].parent.first;
            int temp_col = nodeDetails[row][col].parent.second;
            row = temp_row;
            col = temp_col;
        }
        path.add(new Pair(row, col));
        return path;
    }

    public List<Pair> buildPathList(int[][] map, int size, Pair start, Pair dest) {
        if (!isValid(start, size))
            return null;
        if (!isValid(dest, size))
            return null;
        if (!isUnblocked(start, map, size))
            return null;
        if (isDestination(start, dest))
            return null;
        boolean[][] closedList = new boolean[size][size];
        Node[][] nodeDetails = new Node[size][size];
        int i = start.first;
        int j = start.second;
        nodeDetails[i][j] = new Node(new Pair(i, j), 0.0);
        PriorityQueue<Tuple> openList = new PriorityQueue<>((o1, o2) -> (int) Math.round(o1.value - o2.value));
        openList.add(new Tuple(0.0, i, j));
        while (!openList.isEmpty()) {
            Tuple p = openList.peek();
            i = p.i;
            j = p.j;
            openList.poll();
            closedList[i][j] = true;
            for (int addX = -1; addX <= 1; addX++) {
                for (int addY = -1; addY <= 1; addY++) {
                    if (addX != 0 && addY != 0)
                        continue;
                    Pair neighbour = new Pair(i + addX, j + addY);
                    if (isValid(neighbour, size)) {
                        if (nodeDetails[neighbour.first] == null) {
                            nodeDetails[neighbour.first] = new Node[size];
                        }
                        if (nodeDetails[neighbour.first][neighbour.second] == null) {
                            nodeDetails[neighbour.first][neighbour.second] = new Node();
                        }
                        if (isDestination(neighbour, dest)) {
                            nodeDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                            return tracePath(nodeDetails, dest);
                        } else if (!closedList[neighbour.first][neighbour.second]
                                && isUnblocked(neighbour, map, size)) {
                            double fNew = distance(neighbour.first, neighbour.second, dest.first, dest.second);
                            if (nodeDetails[neighbour.first][neighbour.second].distance > fNew) {
                                openList.add(new Tuple(fNew, neighbour.first, neighbour.second));
                                nodeDetails[neighbour.first][neighbour.second].distance = fNew;
                                nodeDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
