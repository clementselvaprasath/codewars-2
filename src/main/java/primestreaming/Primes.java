package primestreaming;

import java.util.stream.IntStream;

public class Primes {

    public static IntStream stream() {
        return IntStream.iterate(2, i -> nextPrime(i));
    }

    private static Integer nextPrime(Integer i) {
        int aux = i;
        do {
            aux = aux + 1;
        } while (!isPrime(aux));
        return aux;
    }

    private static boolean isPrime(int n) {
        if (n % 2 == 0) return false;
        for (int i = 3; i*i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
