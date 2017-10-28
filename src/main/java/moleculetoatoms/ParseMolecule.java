package moleculetoatoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class ParseMolecule {

    public static class Expression {
        private String expression;
        private int number;

        public Expression(String expression, int number) {
            this.expression = expression;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Expression that = (Expression) o;

            if (number != that.number) return false;
            return expression != null ? expression.equals(that.expression) : that.expression == null;
        }

        @Override
        public int hashCode() {
            int result = expression != null ? expression.hashCode() : 0;
            result = 31 * result + number;
            return result;
        }

        @Override
        public String toString() {
            return "Expression{" +
                    "expression='" + expression + '\'' +
                    ", number=" + number +
                    '}';
        }
    }


    public static class Element {
        private String atom;
        private int number;

        public Element(String atom, int number) {
            this.atom = atom;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Element element = (Element) o;

            if (number != element.number) return false;
            return atom != null ? atom.equals(element.atom) : element.atom == null;
        }

        @Override
        public int hashCode() {
            int result = atom != null ? atom.hashCode() : 0;
            result = 31 * result + number;
            return result;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "atom='" + atom + '\'' +
                    ", number='" + number + '\'' +
                    '}';
        }

        public String removeFrom(String formula) {
            String toRemove = this.atom + (this.number == 1 ? "" : this.number);
            return formula.replaceFirst(toRemove, "");
        }
    }

    public static Map<String,Integer> getAtoms(String formula) {
        return read(formula, 1, new HashMap<>());
    }

    private static Map<String, Integer> read(String formula, int factor, Map<String, Integer> atoms) {
        List<Element> elements = findElements(formula);
        for (Element e : elements) {
            addElement(factor, atoms, e);
        }
        return atoms;
    }

    private static void addElement(int factor, Map<String, Integer> atoms, Element e) {
        Integer number = atoms.get(e.atom);
        if (number == null) {
            number = 0;
        }
        atoms.put(e.atom, number + factor * e.number);
    }

    public static Element findElement(String formula) {
        String element = "";
        String number = "";
        // First find element
        int i = 0;
        char c;
        while (i < formula.length() && Character.isAlphabetic(c = formula.charAt(i))) {
            element += c;
            i++;
        }
        if (element.isEmpty()) {
            return null;
        }
        // Then find element
        while (i < formula.length() && Character.isDigit(c = formula.charAt(i))) {
            number += c;
            i++;
        }
        if (number.isEmpty()) {
            number = "1";
        }

        return new Element(element, Integer.parseInt(number));
    }

    public static List<Element> findElements(String formula) {
        List<Element> elements = new ArrayList<>();

        Element e;
        while ((e = findElement(formula)) != null) {
            elements.add(e);
            formula = e.removeFrom(formula);
        }

        return elements;
    }

    public static Expression findExpression(String formula) {
        char close = 0;
        char open = formula.charAt(0);
        if (open == '(') {
            close = ')';
        } else if (open == '{') {
            close = '}';
        } else if (open == '[') {
            close = ']';
        }
        if (close != 0) {
            int closeIndex = formula.lastIndexOf(close);
            if (closeIndex != -1) {
                int i = closeIndex + 1;
                String number = "";
                char c;
                while (i < formula.length() && Character.isDigit(c = formula.charAt(i))) {
                    number += c;
                    i++;
                }
                if (number.isEmpty()) {
                    number = "1";
                }
                return new Expression(formula.substring(1, closeIndex), Integer.parseInt(number));
            }
            return null;
        }
        return new Expression(formula, 1);
    }
}