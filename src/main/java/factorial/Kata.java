package factorial;

import java.math.BigInteger;

public class Kata
{

    private static final BigInteger ONE = new BigInteger("1");

    public static String Factorial(int n) {
        if (n < 0) {
            return null;
        }
        if (n == 0) {
            return "1";
        }
        return fact(new BigInteger(Integer.toString(n))).toString();
    }

    public static BigInteger fact(BigInteger n) {
        if (n.intValue() == 1) {
            return n;
        } else {
            return n.multiply(fact(n.subtract(ONE)));
        }
    }

}