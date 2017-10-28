package moleculetoatoms;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testFindElement() {
        assertEquals(null, ParseMolecule.findElement("2O"));
        assertEquals(new ParseMolecule.Element("O",1), ParseMolecule.findElement("O"));
        assertEquals(new ParseMolecule.Element("H", 2), ParseMolecule.findElement("H2O"));
    }

    @Test
    public void testFindElements() {
        assertEquals(0, ParseMolecule.findElements("2O").size());
        assertTrue(ParseMolecule.findElements("O").contains(new ParseMolecule.Element("O",1)));

        List<ParseMolecule.Element> h2O = ParseMolecule.findElements("H2O");
        assertTrue(h2O.contains(new ParseMolecule.Element("H", 2)));
        assertTrue(h2O.contains(new ParseMolecule.Element("O", 1)));
    }

    @Test
    public void findExpression() {
        assertEquals(null, ParseMolecule.findExpression("(H2"));
        assertEquals(new ParseMolecule.Expression("H2O", 1), ParseMolecule.findExpression("H2O"));
        assertEquals(new ParseMolecule.Expression("H2O", 1), ParseMolecule.findExpression("(H2O)"));
        assertEquals(new ParseMolecule.Expression("OH", 2), ParseMolecule.findExpression("(OH)2"));
        assertEquals(new ParseMolecule.Expression("OH", 2), ParseMolecule.findExpression("{OH}2"));
        assertEquals(new ParseMolecule.Expression("OH", 2), ParseMolecule.findExpression("[OH]2"));
    }

}
