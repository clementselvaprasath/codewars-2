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

        char c;
        while (i < e.length() && (c = e.charAt(i)) != '+' && (c != '-' || (c == '-' && e1.isEmpty()))) {
            e1 += c;
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


}