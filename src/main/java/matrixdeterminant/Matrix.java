package matrixdeterminant;

public class Matrix {

    public static int determinant(int[][] matrix) {
        if (matrix.length == 1) {
            return matrix[0][0];
        }

        int sign = 1;
        int det = 0;
        for (int i = 0; i < matrix.length; i++) {
            det = det + sign * matrix[0][i] * determinant(minor(matrix, i));
            sign *= -1;
        }
        return det;
    }

    public static int[][] minor(int[][] matrix, int pos) {
        int[][] res = new int[matrix.length - 1][matrix.length - 1];
        for (int i = 1; i < matrix.length; i++) {
            int nj = 0;
            for (int j = 0; j < matrix.length; j++) {
                if (j != pos) {
                    res[i - 1][nj] = matrix[i][j];
                    nj++;
                }
            }
        }
        return res;
    }


}
