package findunknowndigit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by fleax on 23/9/17.
 */
public class Runes {

    public static class Expression {

        private String number1;
        private String number2;
        private String result;

        private BiFunction<Integer, Integer, Integer> operand;

        public Expression(String exp) {
            parse(exp);
        }

        private void parse(String exp) {
            String[] sides = exp.split("=");
            this.result = sides[1];
            String op = sides[0];
            if (op.contains("*")) {
                this.operand = (o, o2) -> o * o2;
                String[] split = op.split("\\*");
                this.number1 = split[0];
                this.number2 = split[1];
            } else if (op.contains("+")) {
                this.operand = (o, o2) -> o + o2;
                String[] split = op.split("\\+");
                this.number1 = split[0];
                this.number2 = split[1];
            } else if (op.contains("--")) {  // That is a sum with positive number ^_^
                this.operand = (o, o2) -> o + o2;
                String[] split = op.split("--");
                this.number1 = split[0];
                this.number2 = split[1];
            } else if (op.contains("-")) {
                // Can be the first one char or the operand
                int i = op.lastIndexOf("-");
                this.number1 = op.substring(0, i);
                this.number2 = op.substring(i+1);
                this.operand = (o, o2) -> o - o2;
            }
        }

        private List<String> candidates() {
            List<String> result = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                String s = Integer.toString(i);
                if (!this.number1.contains(s) && !this.number2.contains(s) && !this.result.contains(s)) {
                    result.add(s);
                }
            }
            return result;
        }

        public boolean isTrueWithDigit(String digit) {
            String s1 = this.number1.replaceAll("\\?", digit);
            String s2 = this.number2.replaceAll("\\?", digit);
            String r = this.result.replaceAll("\\?", digit);

            Integer n1 = Integer.parseInt(s1);
            Integer n2 = Integer.parseInt(s2);
            Integer res = Integer.parseInt(r);

            if (n1.equals(0) && s1.length() > 1) {
                return false;
            }
            if (n2.equals(0) && s2.length() > 1) {
                return false;
            }
            if (res.equals(0) && r.length() > 1) {
                return false;
            }

            return this.operand.apply(n1, n2).equals(res);
        }
    }

    public static int solveExpression( final String expression ) {
        Expression exp = new Expression(expression);
        for (String digit : exp.candidates()) {
            if (exp.isTrueWithDigit(digit)) {
                return Integer.parseInt(digit);
            }
        }
        return -1;
    }
}
