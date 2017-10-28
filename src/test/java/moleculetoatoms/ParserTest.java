package moleculetoatoms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testFindElement() {
        assertEquals(null, ParseMolecule.findElement("pie"));
        assertEquals(null, ParseMolecule.findElement("2O"));
        assertEquals(new ParseMolecule.Element("O",1), ParseMolecule.findElement("O"));
        assertEquals(new ParseMolecule.Element("H", 2), ParseMolecule.findElement("H2O"));
        assertEquals(new ParseMolecule.Element("O", 1), ParseMolecule.findElement("OH"));
        assertEquals(new ParseMolecule.Element("Mg", 1), ParseMolecule.findElement("MgO"));
        assertEquals(new ParseMolecule.Element("Mg", 1), ParseMolecule.findElement("Mg(OH)2"));
        assertEquals(new ParseMolecule.Element("S", 1), ParseMolecule.findElement("SO3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        ParseMolecule.getAtoms("pie");
    }

    @Test
    public void test1() {
        ParseMolecule.getAtoms("{((H)2)[O]}");
    }

    @Test
    public void test2() {
        ParseMolecule.getAtoms("{[Co(NH3)4(OH)2]3Co}(SO4)3");
    }

}
