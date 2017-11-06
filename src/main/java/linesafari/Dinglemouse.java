package linesafari;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dinglemouse {

    enum Direction {
        NORTH('|'), SOUTH('|'), EAST('-'), WEST('-');
        private char character;

        Direction(char character) {
            this.character = character;
        }

        public char character() {
            return character;
        }
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

        Point direction(Direction dir) {
            if (dir == null) return null;
            if (dir == Direction.SOUTH) {
                return south();
            } else if (dir == Direction.NORTH) {
                return north();
            } else if (dir == Direction.WEST) {
                return west();
            } else {
                return east();
            }
        }

        Point south() {
            return new Point(x + 1, y);
        }

        Point north() {
            return new Point(x - 1, y);
        }

        Point west() {
            return new Point(x, y - 1);
        }

        Point east() {
            return new Point(x, y + 1);
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

        public int numberOfCrosses() {
            int n = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 'X') {
                        n++;
                    }
                }
            }
            return n;
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

        private void addPoint(Set<Point> visited, List<Point> points, Point p) {
            if (!visited.contains(p) && p.x >= 0 && p.x < grid.length && p.y >= 0 && p.y < grid[0].length) {
                points.add(p);
            }
        }

        List<Point> adjacent(Point po, Direction dir, boolean dirChange, Set<Point> visited) {
            List<Point> p = new ArrayList<>();
            Character c = content(po);

            // Start
            if (c == 'X') {
                addPoint(visited, p, po.west());
                addPoint(visited, p, po.east());
                addPoint(visited, p, po.north());
                addPoint(visited, p, po.south());
                return p;
            }

            if (c == '-') {
                addPoint(visited, p, po.west());
                addPoint(visited, p, po.east());
            } else if (c == '|') {
                addPoint(visited, p, po.north());
                addPoint(visited, p, po.south());
            } else if (dirChange) {
                // If dir continues given an error
                Character c2 = content(po.direction(dir));
                if (c2 == dir.character()) {
                    return null;
                }

                if (dir == Direction.EAST || dir == Direction.WEST) {
                    addPoint(visited, p, po.north());
                    addPoint(visited, p, po.south());
                } else if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    addPoint(visited, p, po.west());
                    addPoint(visited, p, po.east());
                }
            }
            return p;
        }


        private Character content(Point p) {
            if (p != null && p.x >= 0 && p.x < grid.length && p.y >= 0 && p.y < grid[0].length) {
                return grid[p.x][p.y];
            }
            return 0;
        }

        public Point next(Point p, Point prev, Direction prevDir, boolean dirChange, Set<Point> visited) {
            List<Point> adjacent = adjacent(p, prevDir, dirChange, visited);
            Point next = null;
            if (adjacent != null) {
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

        public boolean noMorePoints(Set<Point> visited) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] != ' ' && !visited.contains(new Point(i, j))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    static boolean line(final char[][] grid) {
        Grid g = new Grid(grid);
        if (g.numberOfCrosses() != 2) {
            return false;
        }
        Point start = g.start();
        Point end = g.end(start);
        return path(g, start, end) || path(g, end, start);
    }

    private static boolean path(Grid g, Point start, Point end) {
        if (start != null) {
            Set<Point> visited = new HashSet<>();
            Point p = start;
            Point prev = null;
            Direction prevDir = null;
            boolean dirChange = false;
            do {
                Point aux = p;
                visited.add(p);
                p = g.next(p, prev, prevDir, dirChange, visited);
                prev = aux;
                prevDir = aux.goTo(p);
                dirChange = g.content(p) == '+';
            } while (p != null && !p.equals(end));
            if (p != null) {
                visited.add(p);
                // Check no more non-empty points
                if (g.noMorePoints(visited)) {
                    return true;
                }
            }
        }
        return false;
    }

}