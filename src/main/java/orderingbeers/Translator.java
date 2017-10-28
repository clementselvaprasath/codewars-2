package orderingbeers;

import java.util.HashMap;
import java.util.Map;

public class Translator {

    private static final String BEER_SINGULAR = "piwo";
    private static final String BEER_GENITIVE_PLURAL = "piw";
    private static final String BEER_NOMINATIVE_PLURAL = "piwa";
    private static final String ONE = "jedno";

    private static final Map<Integer, String> numbers = new HashMap<>();

    static {
        numbers.put(1, "jeden");
        numbers.put(2, "dwa");
        numbers.put(3, "trzy");
        numbers.put(4, "cztery");
        numbers.put(5, "piec");
        numbers.put(6, "szesc");
        numbers.put(7, "siedem");
        numbers.put(8, "osiem");
        numbers.put(9, "dziewiec");
        numbers.put(10, "dziesiec");
        numbers.put(11, "jedenascie");
        numbers.put(12, "dwanascie");
        numbers.put(13, "trzynascie");
        numbers.put(14, "czternascie");
        numbers.put(15, "pietnascie");
        numbers.put(16, "szesnascie");
        numbers.put(17, "siedemnascie");
        numbers.put(18, "osiemnascie");
        numbers.put(19, "dziewietnascie");
        numbers.put(20, "dwadziescia");
        numbers.put(30, "trzydziesci");
        numbers.put(40, "czterdziesci");
        numbers.put(50, "piecdziesiat");
        numbers.put(60, "szescdziesiat");
        numbers.put(70, "siedemdziesiat");
        numbers.put(80, "osiemdziesiat");
        numbers.put(90, "dziewiecdziesiat");
    }

    private static String beers(int n) {
        if (n == 1) {
            return BEER_SINGULAR;
        } else {
            int d = n / 10;
            int endsIn = n % 10;
            if (endsIn > 1 && endsIn < 5 && d != 1) {
                return BEER_NOMINATIVE_PLURAL;
            }
        }
        return BEER_GENITIVE_PLURAL;
    }

    private static String number(int n, boolean composed) {
        if (n == 1 && !composed) return ONE;
        String number = numbers.get(n);
        if (number == null) {
            int tens = (n / 10) * 10;
            int units = n % 10;
            return number(tens, true) + " " + number(units, true);
        }
        return number;
    }

    public static String orderingBeers(int nbOfBeers) {
        if (nbOfBeers < 0 || nbOfBeers > 99) throw new IllegalArgumentException("Wrong number of beers");

        if (nbOfBeers == 0) {
            return "Woda mineralna poprosze";
        }

        String translation = number(nbOfBeers, false) + " " + beers(nbOfBeers) + " poprosze";
        return translation.substring(0, 1).toUpperCase() + translation.substring(1);
    }
}
