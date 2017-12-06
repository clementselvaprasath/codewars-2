package morsecode1;

public class MorseCodeDecoder {

    static class MorseCode {
        private static String get(String code) {
            return null;
        }
    }

    public static String decode(String morseCode) {
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