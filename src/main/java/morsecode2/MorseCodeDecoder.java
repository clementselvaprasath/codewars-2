package morsecode2;

import java.util.HashMap;
import java.util.Map;

public class MorseCodeDecoder {

    static class MorseCode {

        static Map<String, String> MAP = new HashMap<>();

        static {
            MAP.put("·-", "A");
            MAP.put("····", "H");
            MAP.put("·", "E");
            MAP.put("··", "I");
            MAP.put("-·--","Y");
            MAP.put("·---", "J");
            MAP.put("··-","U");
            MAP.put("-··","D");

            MAP.put("···","S");
            MAP.put("···-","V");
            MAP.put("-··-","X");
            MAP.put("··---","2");

            MAP.put("--","M");

            MAP.put("-","T");

            MAP.put("--·-","Q");
            MAP.put("-·-·","C");
            MAP.put("-·-","K");

            MAP.put("-···","B");
            MAP.put("·-·","R");
            MAP.put("---","O");
            MAP.put("·--","W");
            MAP.put("-·","N");

            MAP.put("··-·","F");
            MAP.put("·--·","P");
            MAP.put("·-··","L");
            MAP.put("--··","Z");
            MAP.put("--·","G");
            MAP.put("·-·-·-",".");


        }

        private static String get(String code) {
            String s = MAP.get(code);
            return s != null ? s : "*";
        }
    }

    public static String decodeBits(String bits) {
        System.out.println(bits);
        // Ignore leading and trailing zeros
        String str = bits.substring(bits.indexOf('1'), bits.lastIndexOf('1')+1);

        // Detect samples per time unit
        int seq = 0;
        int sample = Integer.MAX_VALUE;
        int i = 0;
        char previous = str.charAt(i);
        seq++;
        i++;
        while (i < str.length()) {
            if (str.charAt(i) == '1' && previous == '1' || str.charAt(i) == '0' && previous == '0') {
                seq++;
            } else {
                sample = Math.min(sample, seq);
                seq = 1;
            }
            previous = str.charAt(i);
            i++;
        }

        System.out.println("Sample is: " + sample);

        // Start reading time units
        i = 0;
        StringBuilder morse = new StringBuilder();
        int ones = 0;
        int zeros = 0;
        while (i < str.length()) {
            // Read next unit time
            String unit = i + sample < str.length() ? str.substring(i, i + sample) : str.substring(i);
            if (unit.startsWith("1")) {
                ones++;
                if (zeros == 3) {
                    morse.append(" ");
                } else if (zeros == 7) {
                    morse.append("   ");
                }
                zeros = 0;
            } else {
                zeros++;
                if (ones == 1) {
                    morse.append("·");
                } else if (ones > 1) {
                    morse.append("-");
                }
                ones = 0;
            }
            i = i + sample;
        }
        if (ones == 1) {
            morse.append("·");
        } else if (ones > 1) {
            morse.append("-");
        }
        return morse.toString();
    }

    public static String decodeMorse(String morseCode) {
        StringBuilder sb = new StringBuilder();
        String nextCode = null;
        String tmp = morseCode.trim();
        while ((nextCode = nextMorseCode(tmp)) != null) {
            String humanReadable = MorseCode.get(nextCode);
            if (humanReadable != null) {
                sb.append(humanReadable);
            }
            tmp = tmp.substring(nextCode.length());
            if (tmp.startsWith("   ")) {
                sb.append(" ");
                tmp = tmp.substring(3);
            } else if (tmp.startsWith(" ")) {
                tmp = tmp.substring(1);
            }
        }

        return sb.toString();
    }

    public static String nextMorseCode(String morseCode) {
        if (morseCode.isEmpty()) {
            return null;
        }
        if (morseCode.indexOf(" ") != -1) {
            return morseCode.substring(0, morseCode.indexOf(" "));
        } else {
            return morseCode;
        }
    }
}