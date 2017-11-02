package mathevaluator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MathEvaluatorTest {

    @Test public void testLiteral() {
        assertEquals(123d, new MathEvaluator().calculate("123"), 0.01);
    }

    @Test public void testAddition() {
        assertEquals(2d, new MathEvaluator().calculate("1+1"), 0.01);
    }

    @Test public void testSubtraction() {
        assertEquals(0d, new MathEvaluator().calculate("1 - 1"), 0.01);
    }

    @Test public void testMultiplication() {
        assertEquals(12d, new MathEvaluator().calculate("3* 4"), 0.01);
    }

    @Test public void testDivision() {
        assertEquals(3d, new MathEvaluator().calculate("9 /3"), 0.01);
    }

    @Test public void testNegative() {
        assertEquals(-123d, new MathEvaluator().calculate("-123"), 0.01);
    }

    @Test public void testFailed1() {
        assertEquals(-12d, new MathEvaluator().calculate("12*-1"), 0.01);
    }

    @Test public void testThreeOps() {
        assertEquals(3d, new MathEvaluator().calculate("2 / 2 * 3"), 0.01);
    }

    @Test public void testExpression() {
        assertEquals(21.25, new MathEvaluator().calculate("2 /2+3 * 4.75- -6"), 0.01);
    }

    @Test public void testSimple() {
        assertEquals(1476d, new MathEvaluator().calculate("12* 123"), 0.01);
    }

    @Test public void testFailed3() {
        assertEquals(5, new MathEvaluator().calculate("(((((5)))))"), 0.01);
    }

    @Test public void testFailed6() {
        assertEquals(5, new MathEvaluator().calculate("-(-5)"), 0.01);
    }

    @Test public void testFailed7() {
        assertEquals(8, new MathEvaluator().calculate("4(2)"), 0.01);
    }

    @Test public void testFailed2() {
        assertEquals(492, new MathEvaluator().calculate("12 * 123 / -(-5 + 2)"), 0.01);
    }












    @Test public void testFailed4() {
        assertEquals(1, new MathEvaluator().calculate("(123.45*(678.90 / (-2.5+ 11.5)-(((80 -(19))) *33.25)) / 20) - (123.45*(678.90 / (-2.5+ 11.5)-(((80 -(19))) *33.25)) / 20) + (13 - 2)/ -(-11)"), 0.01);
    }

    @Test public void testFailed4_3() {
        assertEquals(2, new MathEvaluator().calculate("1 - 1 + 2"), 0.01);
    }

    @Test public void testFailed4_4() {
        assertEquals(13, new MathEvaluator().calculate("1 + 3 * 4"), 0.01);
    }

    @Test public void testFailed4_1() {
        assertEquals(-1952.8166667d, new MathEvaluator().calculate("678.90/(-2.5+11.5)-(((80-(19)))*33.25)"), 0.01);
    }

    @Test public void testFailed4_2() {
        assertEquals(2028.25d, new MathEvaluator().calculate("(80-19)*33.25"), 0.01);
    }




    @Test public void testFailed5() {
        assertEquals(-12042.76, new MathEvaluator().calculate("123.45*(678.90 / (-2.5+ 11.5)-(80 -19) *33.25) / 20 + 11"), 0.01);
    }




    @Test public void testComplex() {
        assertEquals(7.732, new MathEvaluator().calculate("2 / (2 + 3) * 4.33 - -6"), 0.01);
    }
}