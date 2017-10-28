package moleculetoatoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class ParseMolecule {

    interface Expression {
    }

    public static class Group implements Expression {
        private Character open;
        private Character close;
        private List<Expression> subexpressions;
        private int number;

        public Group(Character open, Character close, List<Expression> subexpressions, int number) {
            this.open = open;
            this.close = close;
            this.subexpressions = subexpressions;
            this.number = number;
        }

        @Override
        public String toString() {
            String exp = "" + (open != null ? open : "");
            for (Expression e : subexpressions) {
                exp += e.toString();
            }
            exp = exp + (close != null ? close : "") + (number == 1 ? "" : number);
            return exp;
        }
    }

    public static class Element implements Expression {
        private String atom;
        private int number;

        public Element(String atom, int number) {
            this.atom = atom;
            this.number = number;
        }

        @Override
        public String toString() {
            return this.atom + (this.number == 1 ? "" : this.number);
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
    }

    public static Map<String,Integer> getAtoms(String formula) {
        System.out.println(formula);
        return read(formula, 1, new HashMap<>());
    }

    private static Map<String, Integer> read(String formula, int factor, Map<String, Integer> atoms) {
        List<Expression> expressions = findExpressions(formula);
        for (Expression expression : expressions) {
            readExpression(expression, factor, atoms);
        }
        return atoms;
    }

    public static List<Expression> findExpressions(String formula) {
        List<Expression> expressions = new ArrayList<>();
        Expression e;
        while ((e = findExpression(formula)) != null) {
            expressions.add(e);
            formula = formula.substring(e.toString().length());
        }
        return expressions;
    }

    public static Expression findExpression(String formula) {
        if (formula.isEmpty()) {
            return null;
        }
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
            int closeIndex = findClose(formula, open, close);
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
                return new Group(open, close, findExpressions(formula.substring(1, closeIndex)), Integer.parseInt(number));
            }
            throw new IllegalArgumentException("Wrong expression: " + formula);
        }

        if (Character.isAlphabetic(open) && Character.isUpperCase(open)) {
            return findElement(formula);
        }

        throw new IllegalArgumentException("Wrong expression: " + formula);
    }

    public static int findClose(String formula, char open, char close) {
        int inside = 0;
        for (int i = 1; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == open) {
                inside++;
            } else if (c == close) {
                if (inside > 0) {
                    inside--;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Element findElement(String formula) {
        String element = "";
        String number = "";
        // First find element
        int i = 0;
        char c;
        do {
            c = formula.charAt(i);
            if (Character.isAlphabetic(c)) {
                // First uppercase, others lowercase
                if ((i == 0 && Character.isUpperCase(c)) || (i > 0 && Character.isLowerCase(c))) {
                    element += c;
                } else {
                    break;
                }
            } else if (Character.isDigit(c)) {
                number += c;
            } else {
                break;
            }
            i++;
        } while (i < formula.length());
        if (element.isEmpty()) {
            return null;
        }
        if (number.isEmpty()) {
            number = "1";
        }

        return new Element(element, Integer.parseInt(number));
    }

    private static void readExpression(Expression e, int factor, Map<String, Integer> atoms) {
        if (e instanceof Element) {
            addElement(factor, atoms, (Element)e);
        } else {
            Group g = (Group)e;
            for (Expression s : g.subexpressions) {
                readExpression(s, factor * g.number, atoms);
            }
        }
    }

    private static void addElement(int factor, Map<String, Integer> atoms, Element e) {
        Integer number = atoms.get(e.atom);
        if (number == null) {
            number = 0;
        }
        atoms.put(e.atom, number + factor * e.number);
    }
}