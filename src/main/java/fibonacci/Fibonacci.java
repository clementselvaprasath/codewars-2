package fibonacci;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Fibonacci {

    private static Map<BigInteger, BigInteger> memo = new HashMap<>();

    public static BigInteger fib(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        } else if (n.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        } else if (memo.containsKey(n)) {
            return memo.get(n);
        } else {
            if (n.longValue() >= 0) {
                BigInteger minus2 = BigInteger.ZERO;
                BigInteger minus1 = BigInteger.ONE;
                for (long i = 2; i <= n.longValue(); i++) {
                    BigInteger result = minus2.add(minus1);
                    minus2 = minus1;
                    minus1 = result;
                    memo.put(BigInteger.valueOf(i), result);
                }
                return minus1;
            } else {
                BigInteger plus2 = BigInteger.ONE;
                BigInteger plus1 = BigInteger.ZERO;
                for (long i = -1; i >= n.longValue(); i--) {
                    BigInteger result = plus2.subtract(plus1);
                    plus2 = plus1;
                    plus1 = result;
                    memo.put(BigInteger.valueOf(i), result);
                }
                return plus1;
            }
        }
    }
}