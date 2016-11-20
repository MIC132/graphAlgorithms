package Parallel;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by MIC on 2016-11-14.
 */
public class ParallelMatrixMultiplier implements Callable<Matrix> {

    final List<Matrix> matrices;

    public ParallelMatrixMultiplier(List<Matrix> matrices){
        this.matrices = matrices;
    }

    @Override
    public Matrix call() throws Exception {
        Matrix result = matrices.get(0);

        for(int i = 1; i<matrices.size();i++){
            result = result.multiply(matrices.get(i));
        }

        return result;
    }
}
