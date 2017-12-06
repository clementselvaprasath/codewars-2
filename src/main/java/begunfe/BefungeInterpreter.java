package begunfe;

import java.util.Random;
import java.util.Stack;

public class BefungeInterpreter {

    public char[][] plane(String code) {
        System.out.println(code);
        String[] lines = code.split("\n");
        char[][] result = new char[lines.length][];
        int i = 0;
        for (String line : lines) {
            result[i++] = line.toCharArray();
        }
        return result;
    }

    public static class CodeMatrix {

        public enum Direction {

            LEFT(0, -1), RIGTH(0, 1), UP(-1, 0), DOWN(1, 0);

            private int i;
            private int j;

            Direction(int i, int j) {
                this.i = i;
                this.j = j;
            }
        }

        private char[][] plane;

        private int i = 0;
        private int j = 0;

        public Direction direction;

        public CodeMatrix(char[][] plane) {
            this.plane = plane;
            this.direction = Direction.RIGTH;
        }

        public char inst() {
            return i < plane.length && j < plane[i].length ? plane[i][j] : 0;
        }

        public void set(int j, int i, char c) {
            plane[i][j] = c;
        }

        public int get(int j, int i) {
            return plane[i][j];
        }

        public void advance() {
            advance(direction);
        }

        public void advance(Direction direction) {
            j += direction.j;
            if (j < 0) {
                i--;
                j = plane[i].length - 1;
            } else if (j == plane[i].length) {
                j = 0;
                i++;
            }
            this.i += direction.i;
        }

    }

    public String interpret(String code) {
        StringBuilder out = new StringBuilder();
        CodeMatrix matrix = new CodeMatrix(plane(code));
        Stack<Integer> stack = new Stack<>();

        boolean strMode = false;
        char inst = matrix.inst();
        while (inst != 0) {
            if (strMode && inst != '"') {
                stack.push((int) inst);
            } else

            // 0-9 Push this number onto the stack.
            if (Character.isDigit(inst)) {
                stack.push(Integer.valueOf(inst+""));
            } else

            // + Addition: Pop a and b, then push a+b.
            if (inst == '+') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(a + b);
            } else

            // - Subtraction: Pop a and b, then push b-a.
            if (inst == '-') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(b - a);
            } else

            // * Multiplication: Pop a and b, then push a*b.
            if (inst == '*') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(a * b);
            } else

            // / Integer division: Pop a and b, then push b/a, rounded down. If a is zero, push zero.
            if (inst == '/') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(a != 0 ? b / a : 0);
            } else

            // % Modulo: Pop a and b, then push the b%a. If a is zero, push zero.
            if (inst == '%') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(a != 0 ? b % a : 0);
            } else

            // ! Logical NOT: Pop a value. If the value is zero, push 1; otherwise, push zero.
            if (inst == '!') {
                int a = stack.pop();
                stack.push(a == 0 ? 1 : 0);
            } else

            // ` (backtick) Greater than: Pop a and b, then push 1 if b>a, otherwise push zero.
            if (inst == '`') {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(b > a ? 1 : 0);
            } else

            // > Start moving right.
            if (inst == '>') {
                matrix.direction = CodeMatrix.Direction.RIGTH;
            } else

            // < Start moving left.
            if (inst == '<') {
                matrix.direction = CodeMatrix.Direction.LEFT;
            } else

            // ^ Start moving up.
            if (inst == '^') {
                matrix.direction = CodeMatrix.Direction.UP;
            } else

            // v Start moving down.
            if (inst == 'v') {
                matrix.direction = CodeMatrix.Direction.DOWN;
            } else

            // ? Start moving in a random cardinal direction.
            if (inst == '?') {
                matrix.direction = CodeMatrix.Direction.values()[new Random().nextInt(4)];
            } else

            // _ Pop a value; move right if value = 0, left otherwise.
            if (inst == '_') {
                int a = stack.pop();
                matrix.direction = a == 0 ? CodeMatrix.Direction.RIGTH : CodeMatrix.Direction.LEFT;
            } else

            // | Pop a value; move down if value = 0, up otherwise.
            if (inst == '|') {
                int a = stack.pop();
                matrix.direction = a == 0 ? CodeMatrix.Direction.DOWN : CodeMatrix.Direction.UP;
            } else

            // " Start string mode: push each character's ASCII value all the way up to the next ".
            if (inst == '"') {
                strMode = !strMode;
            } else

            // : Duplicate value on top of the stack. If there is nothing on top of the stack, push a 0.
            if (inst == ':') {
                if (stack.isEmpty()) {
                    stack.push(0);
                } else {
                    int a = stack.pop();
                    stack.push(a);
                    stack.push(a);
                }
            } else

            // \ Swap two values on top of the stack. If there is only one value, pretend there is an extra 0 on bottom of the stack.
            if (inst == '\\') {
                int a = stack.pop();
                int b = stack.isEmpty() ? 0 : stack.pop();
                stack.push(a);
                stack.push(b);
            } else

            // $ Pop value from the stack and discard it.
            if (inst == '$') {
                stack.pop();
            } else

            // . Pop value and output as an integer.
            if (inst == '.') {
                out.append(stack.pop());
            } else

            // , Pop value and output the ASCII character represented by the integer code that is stored in the value.
            if (inst == ',') {
                out.append((char) stack.pop().intValue());
            } else

            // # Trampoline: Skip next cell.
            if (inst == '#') {
                matrix.advance();
            } else

            // p A "put" call (a way to store a value for later use). Pop y, x and v, then change the character at the position (x,y) in the program to the character with ASCII value v.
            if (inst == 'p') {
                int y = stack.pop();
                int x = stack.pop();
                int v = stack.pop();
                matrix.set(x, y, (char) v);
            } else

            // g A "get" call (a way to retrieve data in storage). Pop y and x, then push ASCII value of the character at that position in the program.
            if (inst == 'g') {
                int y = stack.pop();
                int x = stack.pop();
                stack.push(matrix.get(x, y));
            } else

            // @ End program.
            if (inst == '@') {
                break;
            }

            // (i.e. a space) No-op. Does nothing.

            matrix.advance();
            inst = matrix.inst();
        }

        return out.toString();
    }

}