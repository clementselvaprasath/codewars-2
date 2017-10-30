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
            double v = left.evaluate() + right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
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
            double v = left.evaluate() - right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
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
            double v = left.evaluate() * right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
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
            double v = left.evaluate() / right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
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
        System.out.println(expression);
        Expression ex = parse(expression);
        return ex.evaluate();
    }

    public Expression parse(String e) {
        if (e.isEmpty()) {
            return null;
        }

        String tmp = e;

        // Resolve parentheses first
        int open = tmp.indexOf('(');
        if (open != -1) {
            int close = findClose(tmp, open);
            String parenthesis = tmp.substring(open + 1, close);
            double result = parse(parenthesis).evaluate();
            tmp = tmp.substring(0, open) + Double.toString(result) + tmp.substring(close + 1);
            return parse(tmp);
        }

        int i = 0;
        i = ignoreSpaces(tmp, i);

        String e1 = "";

        char c;
        while (i < tmp.length() && (c = tmp.charAt(i)) != '+' && (c != '-' || (c == '-' && e1.isEmpty()))) {
            e1 += c;
            i++;
        }

        i = ignoreSpaces(tmp, i);

        if (i == tmp.length()) {
            return parseSubexpression(e1);
        }

        if (tmp.charAt(i) == '+') {
            return new Addition(parseSubexpression(e1), parse(tmp.substring(i+1)));
        } else if (tmp.charAt(i) == '-') {
            // If last char is an operator, then this is a negative number
            String aux = e1.trim();
            char last = aux.charAt(aux.length()-1);
            aux = aux.substring(0, aux.length()-1);
            if (!Character.isDigit(last)) {
                String sign = "-";
                i++;
                String number = "";
                while (i < tmp.length()) {
                    c = tmp.charAt(i);
                    if (c == '-') {
                        sign = sign.equals("+") ? "-" : "+";
                    } else {
                        if (Character.isDigit(c) || c == '.') {
                            number += c;
                        } else {
                            break;
                        }
                    }
                    i++;
                }
                Operand op = new Operand(sign, number);

                if (i == tmp.length()) {
                    return operation(parseSubexpression(aux), last, op);
                }

                i = ignoreSpaces(tmp, i);

                if (i == tmp.length()) {
                    return operation(parseSubexpression(aux), last, op);
                }

                c = tmp.charAt(i);

                char operator = c;
                i++;
                return operation(operation(parseSubexpression(aux), last, op), operator, parse(tmp.substring(i+1)));
            } else {
                return new Subtraction(parseSubexpression(e1), parse(tmp.substring(i + 1)));
            }
        }

        return null;
    }

    private int ignoreSpaces(String tmp, int i) {
        while (i < tmp.length() && tmp.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

    public Expression parseSubexpression(String e) {
        if (e.isEmpty()) {
            return new Operand("+","0");
        }

        int i = 0;

        i = ignoreSpaces(e, i);

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
        Operand op1 = new Operand(sign, number);
        if (i == e.length()) {
            return op1;
        }

        i = ignoreSpaces(e, i);

        if (i == e.length()) {
            return op1;
        }

        c = e.charAt(i);

        char operator = c;
        i++;

        i = ignoreSpaces(e, i);

        sign = "+";
        if (e.charAt(i) == '-') {
            sign = "-";
            i++;
        }
        number = "";
        while (i < e.length()) {
            c = e.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                number += c;
            } else {
                break;
            }
            i++;
        }
        Operand op2 = new Operand(sign, number);

        if (i == e.length()) {
            return operation(op1, operator, op2);
        }

        i = ignoreSpaces(e, i);

        if (i == e.length()) {
            return operation(op1, operator, op2);
        }

        c = e.charAt(i);
        char operator2 = c;
        return operation(operation(op1, operator, op2), operator2, parse(e.substring(i+1)));
    }

    public static Expression operation(Expression op1, char operator, Expression op2) {
        if (operator == '*') {
            return new Multiplication(op1, op2);
        } else if (operator == '/') {
            return new Division(op1, op2);
        } else if (operator == '-') {
            return new Subtraction(op1, op2);
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