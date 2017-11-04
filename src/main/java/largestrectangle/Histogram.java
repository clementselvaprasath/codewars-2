package largestrectangle;

import java.util.Arrays;

public class Histogram {

    private final int[] values;

    public Histogram(int... values) {
        System.out.println(values);
        this.values = values;
    }

    public long largestRect() {
        int max = 0;
        if (values != null && values.length > 0) {
            int maxRow = Arrays.stream(values).parallel().max().getAsInt();
            int nRows = values.length;
            if (maxRow > 0) {
                for (int i = maxRow; i > 0; i--) {
                    for (int j = nRows; j > 0; j--) {
                        int area = i * j;
                        if (area <= max) {
                            break;
                        } else if (fits(i, j)) {
                            max = area;
                            break;
                        }
                    }
                }
            }
        }
        return max;
    }

    private boolean fits(int w, int h) {
        int rows = 0;
        for (int i = 0; i < this.values.length; i++) {
            if (rows == 0 && values.length - i < h) {
                break;
            }
            if (this.values[i] >= w) {
                rows++;
                if (rows == h) {
                    System.out.println("" + w + " x " + h + " = ok");
                    return true;
                }
            } else {
                rows = 0;
            }
        }
        System.out.println("" + w + " x " + h + " = ko");
        return false;
    }
}