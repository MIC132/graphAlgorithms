package Parallel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by MIC on 2016-11-14.
 */
public class MatrixParser {


    public static List<Matrix> parseFile(String filename, int length) throws FileNotFoundException {
        List<Matrix> result = new ArrayList<>();

        Scanner s = new Scanner(new FileInputStream(filename));

        while(result.size() < length && s.hasNextLine()){
            List<String> matrixLines = new ArrayList<>();
            String line = s.nextLine();
            while(!line.isEmpty()){
                matrixLines.add(line);
                if(s.hasNextLine()){
                    line = s.nextLine();
                }else{
                    break;
                }

            }
            int rows = matrixLines.size();
            int cols = matrixLines.get(0).split(";").length;

            Matrix m = new Matrix(rows,cols);
            for(int i=0; i<matrixLines.size();i++){
                String row = matrixLines.get(i);
                String[] col = row.split(";");
                for(int j=0;j<col.length;j++){
                    m.array[i][j] = Double.parseDouble(col[j]);
                }
            }
            result.add(m);
        }
        return result;
    }
}
