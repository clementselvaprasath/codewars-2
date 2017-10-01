package base91;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 24/9/17.
 */
public class Base91Test {

    @Test
    public void fixedTests() {
        assertEquals(Base91.encode("test"), "fPNKd");
        assertEquals(Base91.encode("Hello World!"), ">OwJh>Io0Tv!8PE");
        assertEquals(Base91.decode("fPNKd"), "test");
        assertEquals(Base91.decode(">OwJh>Io0Tv!8PE"), "Hello World!");
    }
}
