package Parallel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by MIC on 2016-11-14.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
        int amount = 100;
        int numThreads = Runtime.getRuntime().availableProcessors();
        List<Matrix> matrices = MatrixParser.parseFile("matrices.txt", amount);

        long parallelStart = System.nanoTime();
        ExecutorService service = Executors.newFixedThreadPool(numThreads);
        List<Future<Matrix>> futures = new ArrayList<>();
        int perThread = (int)Math.ceil((double)amount/numThreads);
        int globalCounter = 0;
        for(int i = 0; i<numThreads;i++){
            List<Matrix> toProcess = new ArrayList<>();
            for(int j = 0; j<perThread;j++){
                if(globalCounter<matrices.size()){
                    toProcess.add(matrices.get(globalCounter));
                    globalCounter++;
                }else{
                    break;
                }
            }
            ParallelMatrixMultiplier mm = new ParallelMatrixMultiplier(toProcess);
            Future<Matrix> future = service.submit(mm);
            futures.add(future);
        }
        Matrix result = futures.get(0).get();
        for(int i = 1; i<futures.size();i++){
            result = result.multiply(futures.get(i).get());
        }
        long parallelStop = System.nanoTime();

        System.out.println(result);

        long sequentialStart = System.nanoTime();
        Matrix result2 = matrices.get(0);
        for(int i = 1; i<matrices.size();i++){
            result2 = result2.multiply(matrices.get(i));
        }
        long sequentialStop = System.nanoTime();

        System.out.println(result2);

        long parallelTime = parallelStop-parallelStart;
        long sequentialTime = sequentialStop-sequentialStart;

        System.out.println("Parallel time = "+parallelTime+" Sequential time = "+sequentialTime+" Difference (seq-par) = "+(sequentialTime-parallelTime));
        service.shutdown();
    }
}
