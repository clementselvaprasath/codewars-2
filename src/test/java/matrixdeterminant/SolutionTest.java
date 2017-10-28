package matrixdeterminant;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SolutionTest {

    @Test
    public void sampleTests1() {
        assertEquals("Determinant of a 1 x 1 matrix yields the value of the one element", 1, Matrix.determinant(new int[][] {{1}}));
    }

    @Test
    public void sampleTests2() {
        assertEquals("Determinant of a 1 x 1 matrix yields the value of the one element", -1, Matrix.determinant(new int[][] {{1, 3}, {2,5}}));
    }

    @Test
    public void sampleTests3() {
        assertEquals("Determinant of a 1 x 1 matrix yields the value of the one element", -20, Matrix.determinant(new int[][] {{2,5,3}, {1,-2,-1}, {1, 3, 4}}));
    }

}