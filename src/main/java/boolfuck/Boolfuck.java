package boolfuck;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Boolfuck {

    private static class Memory {

        private boolean[] data;

        private int pointer;

        Memory() {
            data = new boolean[30000];
            pointer = 15000;
        }

        void incrementPointer() {
            pointer++;
        }

        void decrementPointer() {
            pointer--;
        }

        public void changeValue() {
            data[pointer] = data[pointer] ? false : true;
        }

        public boolean value() {
            return data[pointer];
        }

        public void value(boolean read) {
            data[pointer] = read;
        }
    }

    private static class Output {

        private String currentByte;

        private List<Byte> data;

        Output() {
            this.currentByte = "";
            this.data = new ArrayList<>();
        }

        void append(boolean bit) {
            this.currentByte = (bit ? "1" : "0") + this.currentByte;
            if (this.currentByte.length() == 8) {
                byte value = (byte) Integer.parseInt(this.currentByte, 2);
                this.data.add(value);
                this.currentByte = "";
            }
        }

        String data() {
            if (this.currentByte.length() > 0) {
                do {
                    this.currentByte = "0" + this.currentByte;
                } while (this.currentByte.length() < 8);
                byte value = (byte) Integer.parseInt(this.currentByte, 2);
                this.data.add(value);
            }

            byte[] result = new byte[this.data.size()];
            for (int i = 0; i < this.data.size(); i++) {
                result[i] = this.data.get(i);
            }
            try {
                return new String(result, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class Input {

        private String input;

        private int pointer;

        Input(String input) {
            this.input = "";
            for (int i = 0; i < input.length(); i++) {
                String s = Integer.toBinaryString(input.charAt(i));
                System.out.println("Char: " + s);
                while (s.length() < 8) {
                    s = "0" + s;
                }
                String littleEndian = new StringBuilder(s).reverse().toString();
                this.input += littleEndian;
            }
            this.pointer = 0;
        }

        boolean read() {
            if (pointer >= input.length()) {
                return false;
            }
            char c = input.charAt(pointer);
            pointer++;
            return (c == '1');
        }
    }

    public static String interpret (String code, String input) {
        System.out.println("Code: '" + code + "'");
        Memory data = new Memory();
        int pointer = 0;

        Input in = new Input(input);
        Output out = new Output();
        if (!code.isEmpty()) {
            do {
                char inst = code.charAt(pointer);
                pointer = processInstruction(inst, in, out, data, code, pointer);
            } while (pointer < code.length());
        }
        return out.data();
    }

    private static int findStart(String code, int end) {
        int inside = 0;
        for (int i = end - 1; i >= 0; i--) {
            char c = code.charAt(i);
            if (c == ']') {
                inside++;
            } else if (c == '[') {
                if (inside > 0) {
                    inside--;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findEnd(String code, int start) {
        int inside = 0;
        for (int i = start + 1; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '[') {
                inside++;
            } else if (c == ']') {
                if (inside > 0) {
                    inside--;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }


    private static int processInstruction(char inst, Input input, Output output, Memory data, String code, int pointer) {
        if (inst == '>') {
            data.incrementPointer();
        } else if (inst == '<') {
            data.decrementPointer();
        } else if (inst == '+') {
            data.changeValue();
        } else if (inst == ';') {
            output.append(data.value());
        } else if (inst == ',') {
            data.value(input.read());
        } else if (inst == '[') {
            if (!data.value()) {
                return findEnd(code, pointer) + 1;
            }
        } else if (inst == ']') {
            if (data.value()) {
                return findStart(code, pointer) + 1;
            }
        }
        return pointer + 1;
    }
}