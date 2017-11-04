package mathevaluator;

public class MathEvaluator {

    public interface Expression {
        double evaluate();
    }

    public static class Operand implements Expression {

        private String value;

        Operand(String value) {
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

        Expression left;

        Expression right;

        String operator;

        Operation(Expression left, String operator) {
            this.left = left;
            this.operator = operator;
        }

        void setRight(Expression right) {
            this.right = right;
        }

        Expression getRight() {
            return right;
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

        abstract boolean isLowPriority();

    }

    public static class Negate extends Operation {

        public Negate() {
            super(null, "-");
        }

        @Override
        public double evaluate() {
            double v = -1 * right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }

        @Override
        boolean isLowPriority() {
            return false;
        }
    }

    public static class Addition extends Operation {

        public Addition(Expression left) {
            super(left, "+");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() + right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }

        @Override
        boolean isLowPriority() {
            return true;
        }

    }

    public static class Subtraction extends Operation {

        public Subtraction(Expression left) {
            super(left, "-");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() - right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }

        @Override
        boolean isLowPriority() {
            return true;
        }

    }

    public static class Multiplication extends Operation {

        public Multiplication(Expression left) {
            super(left, "*");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() * right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }

        @Override
        boolean isLowPriority() {
            return false;
        }
    }

    public static class Division extends Operation {

        public Division(Expression left) {
            super(left, "/");
        }

        @Override
        public double evaluate() {
            double v = left.evaluate() / right.evaluate();
            System.out.println(this.toString() + " = " + v);
            return v;
        }

        @Override
        boolean isLowPriority() {
            return false;
        }

    }

    public static class ParenthesesProcessor {

        private String expression;

        ParenthesesProcessor(String e) {
            this.expression = e;
        }

        String getParentheses() {
            int open = this.expression.indexOf('(');
            int close = findClose(open);
            return open != -1 && close != -1 ? this.expression.substring(open+1, close) : null;
        }

        String replaceParentheses(double value) {
            int open = this.expression.indexOf('(');
            int close = findClose(open);
            return open != -1 && close != -1 ? this.expression.substring(0, open) + value + this.expression.substring(close+1) : this.expression;
        }

        private int findClose(int start) {
            if (start == -1) return -1;
            int inside = 0;
            for (int i = start + 1; i < expression.length(); i++) {
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

    public static class StringReader {

        private String expression;

        Character previousChar;

        private int pointer = 0;

        StringReader(String e) {
            this.expression = e;
        }

        @Override
        public String toString() {
            return this.expression;
        }

        Character nextChar() {
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

        String readFromHere() {
            return expression.substring(pointer);
        }
    }

    public double calculate(String expression) {
        String e = expression.replace(" ", "");
        Expression ex = parse(e);
        return ex.evaluate();
    }

    private boolean numberChar(Character c, boolean canBeNegative) {
        if (c == null) return false;
        return Character.isDigit(c) || c == '.' || (canBeNegative && c == '-');
    }

    private boolean newNumber(Character c, Character prev, Expression exp, boolean empty) {
        boolean canBeNegative = empty && (exp == null || (exp instanceof Operation && ((Operation)exp).right == null));
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
        return c != null && (c == '*' || c == '/');
    }

    private boolean isLowPriorityOperator(Character c) {
        return c != null && (c == '+' || c == '-');
    }

    public Expression parse(String exp) {
        System.out.println(exp);

        ParenthesesProcessor par = new ParenthesesProcessor(exp);
        String subexpression = par.getParentheses();
        if (subexpression != null) {
            return parse(par.replaceParentheses(parse(subexpression).evaluate()));
        }

        StringReader e = new StringReader(exp);
        Character curr;
        String number = "";
        Expression expression = null;
        while ((curr = e.nextChar()) != null) {
            if (newNumber(curr, e.previousChar, expression, number.isEmpty()) || sameNumber(curr, e.previousChar, expression, number.isEmpty())) {
                number += curr;
            } else if (numberEnded(curr, e.previousChar, number.isEmpty())) {
                Negate neg;
                if (number.equals("-")) {
                    neg = new Negate();
                    if (expression == null) {
                        expression = neg;
                    } else {
                        if (expression instanceof Operation) {
                            ((Operation) expression).setRight(neg);
                        }
                    }
                } else {
                    Operand operand = new Operand(number);
                    number = "";
                    if (expression == null) {
                        expression = operand;
                    } else {
                        if (expression instanceof Operation) {
                            if (isHighPriorityOperator(curr) && ((Operation)expression).isLowPriority()) {
                                ((Operation) expression).setRight(parse(operand.toString() + curr + e.readFromHere()));
                                return expression;
                            } else if (isLowPriorityOperator(curr)) {
                                ((Operation) expression).setRight(operand);
                                expression = operation(expression, curr, null);
                                continue;
                            } else {
                                ((Operation) expression).setRight(operand);
                            }
                        }
                    }
                    if (isHighPriorityOperator(curr)) {
                        expression = operation(expression, curr, null);
                    } else if (isLowPriorityOperator(curr)) {
                        expression = operation(expression, curr, null);
                    }
                }
            } else if (isHighPriorityOperator(curr)) {
                expression = operation(expression, curr, null);
            } else if (isLowPriorityOperator(curr)) {
                return operation(expression, curr, parse(e.readFromHere()));
            }
        }
        if (numberEnded(null, e.previousChar, number.isEmpty())) {
            Operand operand = new Operand(number);
            if (expression == null) {
                expression = operand;
            } else {
                if (expression instanceof Operation) {
                    Expression right = ((Operation) expression).getRight();
                    if (right == null) {
                        ((Operation) expression).setRight(operand);
                    } else if (right instanceof Operation){
                        ((Operation) right).setRight(operand);
                    }
                }
            }
        }

        return expression;
    }

    private static Expression operation(Expression op1, char operator, Expression op2) {
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
        if (op != null && op2 != null) {
            op.setRight(op2);
        }
        return op;
    }
}

