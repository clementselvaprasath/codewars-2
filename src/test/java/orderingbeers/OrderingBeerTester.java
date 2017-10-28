package orderingbeers;

import static org.junit.Assert.*;
import org.junit.Test;

public class OrderingBeerTester {

    @Test public void
    shouldReturnWodaWhenOrdering0Beer() {
        assertEquals("Shall ask for mineral water when no beers are ordered :", "Woda mineralna poprosze", Translator.orderingBeers(0));
    }

    @Test public void
    shouldReturnJednoPiwoWhenOrdering1Beer() {
        assertEquals("One is like an adjective, so should be written acording to the gender : Jedno", "Jedno piwo poprosze", Translator.orderingBeers(1));
    }

    @Test public void
    shouldReturnDwaPiwaWhenOrdering2Beers() {
        assertEquals("Dwa piwa poprosze", Translator.orderingBeers(2));
    }

    @Test public void
    shouldReturnTrzyPiwaWhenOrdering3Beers() {
        assertEquals("Trzy piwa poprosze", Translator.orderingBeers(3));
    }

    @Test public void
    shouldReturndwadziesciaOsiemPiwWhenOrdering28Beers() {
        assertEquals("Dwadziescia osiem piw poprosze", Translator.orderingBeers(28));
    }
}