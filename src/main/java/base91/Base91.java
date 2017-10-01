package base91;

/**
 * Created by alex on 24/9/17.
 */
public class Base91 {

    public static String encode(String data) {
        byte[] b = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            b[i] = (byte) data.charAt(i);
        }
        return new String(b);
    }

    public static String decode(String data) {
        return new String(); // do it!
    }

}
