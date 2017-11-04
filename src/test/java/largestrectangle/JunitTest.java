package largestrectangle;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class JunitTest {

    @Test
    public void testSmallExamplesNormalCases() {
        System.out.println("Example Test Cases");
        System.out.println("Normal small examples");
        assertEquals(16, new Histogram(3, 5, 12, 4, 10).largestRect());
        assertEquals(12, new Histogram(6, 2, 5, 4, 5, 1, 6).largestRect());
        assertEquals(36, new Histogram(9, 7, 5, 4, 2, 5, 6, 7, 7, 5, 7, 6, 4, 4, 3, 2).largestRect());
    }

    @Test
    public void testSmallExamplesCornerCases() {
        System.out.println("Example Test Cases");
        System.out.println("Corner Cases");
        assertEquals(0, new Histogram().largestRect());
        assertEquals(0, new Histogram(0).largestRect());
        assertEquals(0, new Histogram(0, 0, 0).largestRect());
        assertEquals(3, new Histogram(1, 1, 1).largestRect());
        assertEquals(4, new Histogram(1, 2, 3).largestRect());
        assertEquals(4, new Histogram(3, 2, 1).largestRect());
    }

    @Test
    public void testLarge() {
        Random r = new Random();
        int[] values = new int[100000];
        for (int i = 0; i < 100000; i++) {
            values[i] = (r.nextInt(1000));
        }
        new Histogram(values).largestRect();
    }
}