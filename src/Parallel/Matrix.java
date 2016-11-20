package Parallel;

/**
 * Created by MIC on 2016-11-14.
 */
public class Matrix {
    double[][] array;

    public Matrix(int rows, int cols){
        array = new double[rows][cols];
    }

    public Matrix multiply(Matrix with){
        int aRows = array.length;
        int aColumns = array[0].length;
        int bRows = with.array.length;
        int bColumns = with.array[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] result = new double[aRows][bColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    result[i][j] += array[i][k] * with.array[k][j];
                }
            }
        }

        Matrix output = new Matrix(aRows, bColumns);
        output.array = result;
        return output;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i=0; i<array.length;i++){
            for(int j=0; j<array[0].length;j++){
                output.append(array[i][j]).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
