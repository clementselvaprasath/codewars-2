package mathevaluator;

public class MathEvaluator {

    public interface Expression {
        double evaluate();
    }

    public static class Operand implements Expression {

        private String value;

        public Operand(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operand operand = (Operand) o;

            return value != null ? value.equals(operand.value) : operand.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public double evaluate() {
            return Double.parseDouble(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static abstract class Operation implements Expression {

        protected Expression left;

        protected Expression right;

        protected String operator;

        public Operation(Expression left, String operator) {
            this.left = left;
            this.operator = operator;
        }

        public Operation(Expression left, Expression right, String operator) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        public void setRight(Expression right) {
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operation operation = (Operation) o;

            if (left != null ? !left.equals(operation.left) : operation.left != null) return false;
            if (right != null ? !right.equals(operation.right) : operation.right != null) return false;
            return operator != null ? operator.equals(operation.operator) : operation.operator == null;
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            result = 31 * result + (operator != null ? operator.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return (left != null ? left.toString() : "") + operator + (right != null ? right.toString() : "");
        }
    }

    public static class Negate extends Operation {

        public Negate() {
            super(null, "-");
        }

        @Override
        public String toString() {
            return "-(" + right.toString() + ")";
        }

        @Override
        public double evaluate() {
            double v = -1 * right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }
    }


    public static class Addition extends Operation {

        public Addition(Expression left) {
            super(left, "+");
        }

        public Addition(Expression left, Expression right) {
            super(left, right, "+");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() + right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }
    }

    public static class Subtraction extends Operation {

        public Subtraction(Expression left) {
            super(left, "-");
        }

        public Subtraction(Expression left, Expression right) {
            super(left, right, "-");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() - right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }
    }

    public static class Multiplication extends Operation {

        public Multiplication(Expression left) {
            super(left, "*");
        }

        public Multiplication(Expression left, Expression right) {
            super(left, right, "*");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() * right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }
    }

    public static class Division extends Operation {

        public Division(Expression left) {
            super(left, "/");
        }

        public Division(Expression left, Expression right) {
            super(left, right, "/");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() / right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }
    }

    public static class StringReader {

        private String expression;

        private Character previousChar;

        private int pointer = 0;

        public StringReader(String e) {
            this.expression = e;
        }

        @Override
        public String toString() {
            return this.expression;
        }

        public Character nextChar() {
            if (expression.length() <= pointer) {
                previousChar = expression.charAt(expression.length() - 1);
                return null;
            }
            if (pointer == 0) {
                previousChar = null;
            } else {
                previousChar = expression.charAt(pointer - 1);
            }
            return expression.charAt(pointer++);
        }

        public Character previousChar() {
            return previousChar;
        }

        public StringReader readFromHere() {
            return new StringReader(expression.substring(pointer));
        }

        public StringReader readSubexpression() {
            int close = findClose();
            StringReader stringReader = new StringReader(expression.substring(pointer, close));
            pointer = close + 1;
            return stringReader;
        }

        private int findClose() {
            int inside = 0;
            for (int i = pointer; i < expression.length(); i++) {
                char c = expression.charAt(i);
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

    public double calculate(String expression) {
        String e = expression.replace(" ", "");
        System.out.println(e);
        Expression ex = parse(new StringReader(e));
        return ex.evaluate();
    }

    private boolean numberChar(Character c, boolean canBeNegative) {
        if (c == null) return false;
        return Character.isDigit(c) || c == '.' || (canBeNegative && c == '-');
    }

    private boolean newNumber(Character c, Character prev, Expression exp, boolean empty) {
        boolean canBeNegative = empty && (exp == null || exp instanceof Operation);
        return numberChar(c, canBeNegative) && !numberChar(prev, false);
    }

    private boolean sameNumber(Character c, Character prev, Expression exp, boolean empty) {
        boolean canBeNegative = exp == null && empty;
        return numberChar(c, canBeNegative) && numberChar(prev, canBeNegative);
    }

    private boolean numberEnded(Character c, Character prev, boolean empty) {
        boolean canBeNegative = !empty;
        return !numberChar(c, false) && numberChar(prev, canBeNegative);
    }

    private boolean isHighPriorityOperator(Character c) {
        return c != null && c == '*' || c == '/';
    }

    private boolean isLowPriorityOperator(Character c) {
        return c != null && c == '+' || c == '-';
    }

    private boolean isParentheses(Character c) {
        return c != null && c == '(';
    }

    private Expression addNumber(String number, Expression expression) {
        Operand operand = new Operand(number);
        if (expression == null) {
            return operand;
        } else {
            if (expression instanceof Operation) {
                ((Operation) expression).setRight(operand);
            }
            return expression;
        }
    }

    public Expression parse(StringReader e) {
        Character curr;
        String number = "";
        Expression expression = null;
        while ((curr = e.nextChar()) != null) {
            if (newNumber(curr, e.previousChar(), expression, number.isEmpty()) || sameNumber(curr, e.previousChar(), expression, number.isEmpty())) {
                number += curr;
            } else if (numberEnded(curr, e.previousChar(), number.isEmpty())) {
                Negate neg = null;
                if (number.equals("-")) {
                    neg = new Negate();
                } else {
                    expression = addNumber(number, expression);
                }
                number = "";
                if (isHighPriorityOperator(curr)) {
                    expression = operation(expression, curr, null);
                } else if (isLowPriorityOperator(curr)) {
                    return operation(expression, curr, parse(e.readFromHere()));
                } else if (isParentheses(curr)) {
                    if (neg != null) {
                        neg.setRight(parse(e.readSubexpression()));
                        if (expression == null) {
                            expression = neg;
                        } else {
                            if (expression instanceof Operation) {
                                ((Operation) expression).setRight(neg);
                            }
                        }
                    } else {
                        expression = new Multiplication(expression, parse(e.readSubexpression()));
                    }
                }
            } else if (isParentheses(curr)) {
                if (expression == null) {
                    return parse(e.readSubexpression());
                } else {
                    if (expression instanceof Operation) {
                        ((Operation) expression).setRight(parse(e.readSubexpression()));
                    }
                }
            } else if (isHighPriorityOperator(curr)) {
                expression = operation(expression, curr, null);
            } else if (isLowPriorityOperator(curr)) {
                return operation(expression, curr, parse(e.readFromHere()));
            }
        }
        if (numberEnded(null, e.previousChar(), number.isEmpty())) {
            expression = addNumber(number, expression);
        }

        return expression;
    }

    public static Expression operation(Expression op1, char operator, Expression op2) {
        Operation op = null;
        if (operator == '+') {
            op = new Addition(op1);
        } else if (operator == '*') {
            op = new Multiplication(op1);
        } else if (operator == '/') {
            op = new Division(op1);
        } else if (operator == '-') {
            op = new Subtraction(op1);
        }
        if (op2 != null) {
            op.setRight(op2);
        }
        return op;
    }


}

