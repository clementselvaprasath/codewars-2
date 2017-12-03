package brainfuck;

import java.util.*;

public class BrainLuck {

    private static class Memory {

        private byte[] data;

        private int pointer;

        Memory() {
            data = new byte[1024];
            pointer = 0;
        }

        void incrementPointer() {
            pointer++;
        }

        void decrementPointer() {
            pointer--;
        }

        public void incrementValue() {
            data[pointer]++;
        }

        public void decrementValue() {
            data[pointer]--;
            if (data[pointer] == -1) {
                data[pointer] = (byte)255;
            }
        }

        public byte value() {
            return data[pointer];
        }

        public void value(byte read) {
            data[pointer] = read;
        }
    }

    private static class Input {

        private String input;

        private int pointer;

        Input(String input) {
            this.input = input;
            this.pointer = 0;
        }

        char read() {
            char c = input.charAt(pointer);
            pointer++;
            return c;
        }
    }

    private Memory data;

    private Input input;

    private String code;

    private int pointer;

    public BrainLuck(String code) {
        this.data = new Memory();
        this.code = code;
        this.pointer = 0;
    }

    public String process(String input) {

        this.input = new Input(input);
        StringBuilder output = new StringBuilder();
        do {
            char inst = this.code.charAt(pointer);
            this.pointer = processInstruction(inst, this.input, output);
        } while (this.pointer < this.code.length());
        return output.toString();
    }

    private int findStart(String code, int end) {
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

    private int findEnd(String code, int start) {
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


    private int processInstruction(char inst, Input input, StringBuilder output) {
        if (inst == '>') {
            this.data.incrementPointer();
        } else if (inst == '<') {
            this.data.decrementPointer();
        } else if (inst == '+') {
            this.data.incrementValue();
        } else if (inst == '-') {
            this.data.decrementValue();
        } else if (inst == '.') {
            byte value = this.data.value();
            output.append((char)value);
        } else if (inst == ',') {
            this.data.value((byte) input.read());
        } else if (inst == '[') {
            if (this.data.value() == 0) {
                return findEnd(this.code, this.pointer) + 1;
            }
        } else if (inst == ']') {
            if (this.data.value() != 0) {
                return findStart(this.code, this.pointer) + 1;
            }
        }
        return this.pointer + 1;
    }
}