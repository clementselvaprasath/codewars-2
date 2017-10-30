package mathevaluator;

public class MathEvaluator {

    public interface Expression {
        double evaluate();
    }

    public static class Operand implements Expression {

        private String sign;

        private String value;

        public Operand(String sign, String value) {
            this.sign = sign;
            this.value = value;
        }

        @Override
        public double evaluate() {
            return (sign.equals("+") ? +1 : -1) * Double.parseDouble(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operand operand = (Operand) o;

            if (sign != null ? !sign.equals(operand.sign) : operand.sign != null) return false;
            return value != null ? value.equals(operand.value) : operand.value == null;
        }

        @Override
        public int hashCode() {
            int result = sign != null ? sign.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return sign + value;
        }
    }

    public static abstract class Operation implements Expression {

        protected Expression left;

        protected Expression right;

        public Operation(Expression left, Expression right) {
            this.left = left;
            this.right = right;
            System.out.println(this.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operation operation = (Operation) o;

            if (left != null ? !left.equals(operation.left) : operation.left != null) return false;
            return right != null ? right.equals(operation.right) : operation.right == null;
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }
    }

    public static class Addition extends Operation {

        public Addition(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public double evaluate() {
            return left.evaluate() + right.evaluate();
        }

        @Override
        public String toString() {
            if (left != null && right != null) {
                return left.toString() + " + " + right.toString();
            }
            return "+";
        }
    }

    public static class Subtraction extends Operation {

        public Subtraction(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public double evaluate() {
            return left.evaluate() - right.evaluate();
        }

        @Override
        public String toString() {
            if (left != null && right != null) {
                return left.toString() + " - " + right.toString();
            }
            return "-";
        }
    }

    public static class Multiplication extends Operation {

        public Multiplication(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public double evaluate() {
            return left.evaluate() * right.evaluate();
        }

        @Override
        public String toString() {
            if (left != null && right != null) {
                return left.toString() + " * " + right.toString();
            }
            return "*";
        }
    }

    public static class Division extends Operation {

        public Division(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public double evaluate() {
            return left.evaluate() / right.evaluate();
        }

        @Override
        public String toString() {
            if (left != null && right != null) {
                return left.toString() + " / " + right.toString();
            }
            return "/";
        }
    }

    public double calculate(String expression) {
        Expression ex = parse(expression);
        return ex.evaluate();
    }

    public Expression parse(String e) {
        String e1 = "";

        if (e.isEmpty()) {
            return null;
        }

        int i = 0;
        while (i < e.length() && e.charAt(i) == ' ') {
            i++;
        }

        Expression par;
        if (e.charAt(i) == '(') {
            int close = findClose(e, i);
            par = parse(e.substring(i+1, close));
            i = close + 1;

            while (i < e.length() && e.charAt(i) == ' ') {
                i++;
            }

            if (i == e.length()) {
                return par;
            }

            if (e.charAt(i) == '+') {
                return new Addition(par, parse(e.substring(i+1)));
            } else if (e.charAt(i) == '-') {
                return new Subtraction(par, parse(e.substring(i+1)));
            } else if (e.charAt(i) == '*') {
                return new Multiplication(par, parse(e.substring(i+1)));
            } else if (e.charAt(i) == '/') {
                return new Division(par, parse(e.substring(i+1)));
            }
        }

        char c;
        char last = ' ';
        while (i < e.length() && (c = e.charAt(i)) != '+' && (c != '-' || (c == '-' && e1.isEmpty())) && c != '(') {
            e1 += c;
            if (c != ' ') {
                last = c;
            }
            i++;
        }

        while (i < e.length() && e.charAt(i) == ' ') {
            i++;
        }

        if (i == e.length()) {
            return parseSubexpression(e1);
        }

        if (e.charAt(i) == '+') {
            return new Addition(parseSubexpression(e1), parse(e.substring(i+1)));
        } else if (e.charAt(i) == '-') {
            return new Subtraction(parseSubexpression(e1), parse(e.substring(i+1)));
        } else if (e.charAt(i) == '(') {
            return parenthesis(e1, last, e.substring(i));
        }

        return null;
    }

    public Expression parenthesis(String e1, char op, String e2) {
        String e = "";
        boolean removed = false;
        for (int i = e1.length() - 1; i >= 0; i--) {
            if (e1.charAt(i) == op && !removed) {
                removed = true;
            } else {
                e = e1.charAt(i) + e;
            }
        }

        if (op == '+') {
            return new Addition(parse(e), parse(e2));
        } else if (op == '-') {
            return new Subtraction(parse(e), parse(e2));
        } else if (op == '*') {
            return new Multiplication(parse(e), parse(e2));
        } else if (op == '/') {
            return new Division(parse(e), parse(e2));
        }

        return null;
    }

    public Expression parseSubexpression(String e) {
        System.out.println("Subexpression: " + e);
        if (e.isEmpty()) {
            return null;
        }

        int i = 0;
        while (e.charAt(i) == ' ' && i < e.length()) {
            i++;
        }

        char c;
        String sign = "+";
        if (e.charAt(i) == '-') {
            sign = "-";
            i++;
        }
        String number = "";
        while (i < e.length()) {
            c = e.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                number += c;
            } else {
                break;
            }
            i++;
        }
        Operand op = new Operand(sign, number);
        if (i == e.length()) {
            return op;
        }

        while (i < e.length() && e.charAt(i) == ' ') {
            i++;
        }

        if (i == e.length()) {
            return op;
        }

        c = e.charAt(i);
        if (c == '*') {
            return new Multiplication(op, parse(e.substring(i+1)));
        } else if (c == '/') {
            return new Division(op, parse(e.substring(i+1)));
        }

        return null;
    }

    public static int findClose(String e, int pos) {
        int inside = 0;
        for (int i = pos + 1; i < e.length(); i++) {
            char c = e.charAt(i);
            if (c == '(') {
                inside++;
            } else if (c == ')') {
                if (inside > 0) {
                    inside--;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }


}