package befunge;

import begunfe.BefungeInterpreter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BefungeInterpreterTests {

    @Test
    public void testPlane() {
        char[][] plane = new BefungeInterpreter().plane(">987v>.v\nv456<  :\n>321 ^ _@");
        assertEquals(3, plane.length);
        assertEquals(8, plane[0].length);
        assertEquals(">987v>.v", new String(plane[0]));
        assertEquals(8, plane[1].length);
        assertEquals("v456<  :", new String(plane[1]));
        assertEquals(9, plane[2].length);
        assertEquals(">321 ^ _@", new String(plane[2]));
    }

    @Test
    public void testMatrixCode() {
        char[][] plane = new BefungeInterpreter().plane(">98\nv45\n>321@");
        BefungeInterpreter.CodeMatrix matrix = new BefungeInterpreter.CodeMatrix(plane);
        assertEquals('>', matrix.inst());
        matrix.direction = BefungeInterpreter.CodeMatrix.Direction.RIGTH;
        matrix.advance();
        assertEquals('9', matrix.inst());
        matrix.advance();
        assertEquals('8', matrix.inst());
        matrix.advance();
        assertEquals('v', matrix.inst());
        matrix.direction = BefungeInterpreter.CodeMatrix.Direction.DOWN;
        matrix.advance();
        assertEquals('>', matrix.inst());
        matrix.direction = BefungeInterpreter.CodeMatrix.Direction.RIGTH;
        matrix.advance();
        assertEquals('3', matrix.inst());
        matrix.advance();
        assertEquals('2', matrix.inst());
        matrix.advance();
        assertEquals('1', matrix.inst());
        matrix.advance();
        assertEquals('@', matrix.inst());
        matrix.advance();
        assertEquals(0, matrix.inst());
    }

    @Test
    public void tests() {
        assertEquals(
                "123456789",
                new BefungeInterpreter().interpret(">987v>.v\nv456<  :\n>321 ^ _@"));

    }

    @Test
    public void tests2() {
        String interpret = new BefungeInterpreter().interpret(">25*\"!dlroW olleH\":v\n" +
                "                v:,_@\n" +
                "                >  ^");
        assertEquals(
                "Hello World!",
                interpret);

    }

}
