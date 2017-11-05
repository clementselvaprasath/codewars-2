package linesafari;

import java.util.ArrayList;
import java.util.List;

public class Dinglemouse {

    enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Direction goTo(Point p2) {
            if (p2 == null) return null;
            if (x == p2.x) {
                if (y < p2.y) {
                    return Direction.EAST;
                } else if (y > p2.y) {
                    return Direction.WEST;
                }
            }
            if (y == p2.y) {
                if (x < p2.x) {
                    return Direction.SOUTH;
                } else if (x > p2.x) {
                    return Direction.NORTH;
                }
            }
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    static class Grid {
        char[][] grid;

        public Grid(char[][] grid) {
            this.grid = grid;
        }

        public Point start() {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 'X') {
                        return new Point(i, j);
                    }
                }
            }
            return null;
        }

        public Point end(Point start) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 'X' && (i != start.x || j != start.y)) {
                        return new Point(i, j);
                    }
                }
            }
            return null;
        }

        private void addPoint(List<Point> points, int x, int y) {
            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                points.add(new Point(x, y));
            }
        }

        List<Point> adjacent(Point po, Direction dir, boolean dirChange) {
            List<Point> p = new ArrayList<>();
            Character c = content(po);

            // Start
            if (c == 'X') {
                addPoint(p, po.x, po.y - 1);
                addPoint(p, po.x, po.y + 1);
                addPoint(p, po.x - 1, po.y);
                addPoint(p, po.x + 1, po.y);
                return p;
            }

            if (c == '-') {
                addPoint(p, po.x, po.y - 1);
                addPoint(p, po.x, po.y + 1);
            } else if (c == '|') {
                addPoint(p, po.x - 1, po.y);
                addPoint(p, po.x + 1, po.y);
            } else if (dirChange) {
                if (dir == Direction.EAST || dir == Direction.WEST) {
                    addPoint(p, po.x -  1, po.y);
                    addPoint(p, po.x + 1, po.y);
                } else if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    addPoint(p, po.x, po.y - 1);
                    addPoint(p, po.x, po.y + 1);
                }
            }
            return p;
        }


        private Character content(Point p) {
            if (p != null && p.x < grid.length && p.y < grid[0].length) {
                return grid[p.x][p.y];
            }
            return 0;
        }

        public Point next(Point p, Point prev, Direction prevDir, boolean dirChange) {
            List<Point> adjacent = adjacent(p, prevDir, dirChange);
            Point next = null;
            for (Point po : adjacent) {
                if (!po.equals(prev)) {
                    Character content = content(po);
                    Direction d = p.goTo(po);
                    if (content != null && validChar(content, d)) {
                        if (next == null) {
                            next = po;
                        } else {
                            return null;
                        }
                    }
                }
            }
            return next;
        }

        private boolean validChar(Character content, Direction d) {
            if (content == ' ') return false;
            if (content == 'X') return true;
            if (content == '+') return true;

            if (d == Direction.WEST || d == Direction.EAST) {
                return content == '-';
            } else if (d == Direction.SOUTH || d == Direction.NORTH) {
                return content == '|';
            }
            return false;
        }
    }

    static boolean line(final char[][] grid) {
        Grid g = new Grid(grid);
        Point start = g.start();
        Point end = g.end(start);
        return path(g, start, end) || path(g, end, start);
    }

    private static boolean path(Grid g, Point start, Point end) {
        if (start != null) {
            Point p = start;
            Point prev = null;
            Direction prevDir = null;
            boolean dirChange = false;
            do {
                Point aux = p;
                p = g.next(p, prev, prevDir, dirChange);
                prev = aux;
                prevDir = aux.goTo(p);
                dirChange = g.content(p) == '+';
            } while (p != null && !p.equals(end));
            if (p != null) {
                return true;
            }
        }
        return false;
    }

}