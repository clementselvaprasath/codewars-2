package linesafari;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExampleTests {

    // "Good" examples from the Kata description.

    private static char[][] makeGrid(String[] lines) {
        char[][] result = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = lines[i].charAt(j);
            }
        }
        return result;
    }

    @Test
    public void exGood1() {
        final char grid[][] = makeGrid(new String[] {
                "           ",
                "X---------X",
                "           ",
                "           "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood2() {
        final char grid[][] = makeGrid(new String[] {
                "     ",
                "  X  ",
                "  |  ",
                "  |  ",
                "  X  "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood3() {
        final char grid[][] = makeGrid(new String[] {
                "                    ",
                "     +--------+     ",
                "  X--+        +--+  ",
                "                 |  ",
                "                 X  ",
                "                    "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood4() {
        final char grid[][] = makeGrid(new String[] {
                "                     ",
                "    +-------------+  ",
                "    |             |  ",
                " X--+      X------+  ",
                "                     "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood5() {
        final char grid[][] = makeGrid(new String[] {
                "                      ",
                "   +-------+          ",
                "   |      +++---+     ",
                "X--+      +-+   X      "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    // "Bad" examples from the Kata description.

    @Test
    public void exBad1() {
        final char grid[][] = makeGrid(new String[] {
                "X-----|----X"
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad2() {
        final char grid[][] = makeGrid(new String[] {
                " X  ",
                " |  ",
                " +  ",
                " X  "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad3() {
        final char grid[][] = makeGrid(new String[] {
                "   |--------+    ",
                "X---        ---+ ",
                "               | ",
                "               X "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad4() {
        final char grid[][] = makeGrid(new String[] {
                "              ",
                "   +------    ",
                "   |          ",
                "X--+      X   ",
                "              "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad5() {
        final char grid[][] = makeGrid(new String[] {
                "      +------+",
                "      |      |",
                "X-----+------+",
                "      |       ",
                "      X       ",
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exFailed1() {
        final char grid[][] = makeGrid(new String[] {
                "X-----+",
                "      |",
                "X-----+",
                "      |",
                "------+"
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exFailed2() {
        final char grid[][] = makeGrid(new String[] {
                "      X  ",
                "X+++  +-+",
                " +++--+ |",
                "      +-+"
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exFailed3() {
        final char grid[][] = makeGrid(new String[] {
                "X+++ ",
                " +++X"
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exFailed4() {
        final char grid[][] = makeGrid(new String[] {
                "  X-----+",
                "        |",
                "  X-----+",
                "        |",
                "  ------+",
                "    X    ",
                "    |   |",
                "+   |  -+-",
                "    |   | ",
                "    X    "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exFailed5() {
        final char grid[][] = makeGrid(new String[] {
                "    X    ",
                "    |   |",
                "+   |  -+-",
                "    |   | ",
                "    X    "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

}
