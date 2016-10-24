import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by MIC on 2016-10-24.
 */
public class FordFulkerson {

    private FordFulkerson() {}

    public static int getMaximumFlow(Graph<Integer, Integer> capacityGraph, int from, int to){
        Graph<Integer, Integer> flowGraph = new ListGraph<>(Integer.class, Integer.class);
        Graph<Integer, Integer> residualGraph = new ListGraph<>(Integer.class, Integer.class);
        for(Integer i : capacityGraph.getVertices()){
            for (Integer j : capacityGraph.getVertices()){
                if(!i.equals(j) && capacityGraph.existsEdge(i, j, true)){
                    flowGraph.addEdge(0, i, j, true);
                    residualGraph.addEdge(capacityGraph.getEdge(i, j), i, j, true);
                }
            }
        }
        int resultFlow = 0;
        List<Integer> path = bfsPath(residualGraph, from, to);
        while (!path.isEmpty()){
            int minflow = Integer.MAX_VALUE;
            for(int i=0; i<path.size()-1; i++){
                int flow = residualGraph.getEdge(path.get(i), path.get(i+1));
                if(flow < minflow){
                    minflow = flow;
                }
            }
            resultFlow += minflow;
            for(int i=0; i<path.size()-1; i++){
                int currentFlow = flowGraph.getEdge(path.get(i), path.get(i+1));
                flowGraph.removeEdge(path.get(i), path.get(i+1), true);
                flowGraph.addEdge(currentFlow + minflow, path.get(i), path.get(i+1), true);
                residualGraph.removeEdge(path.get(i), path.get(i+1), true);
                residualGraph.addEdge(currentFlow - minflow, path.get(i), path.get(i+1), true);
                currentFlow = flowGraph.getEdge(path.get(i+1), path.get(i));
                flowGraph.removeEdge(path.get(i+1), path.get(i), true);
                flowGraph.addEdge(currentFlow - minflow, path.get(i+1), path.get(i), true);
                residualGraph.removeEdge(path.get(i+1), path.get(i), true);
                residualGraph.addEdge(currentFlow + minflow, path.get(i+1), path.get(i), true);
            }
        }
        /*for(Integer i : flowGraph.getAdjacentVertices(from)){
            flow += flowGraph.getEdge(from, i);
        }*/
        return resultFlow;
    }

    public static List<Integer> bfsPath(Graph<Integer, Integer> graph, Integer from, Integer to){
        Queue<List<Integer>> paths = new LinkedList<>();
        List<Integer> start = new LinkedList<>();
        start.add(from);
        paths.add(start);
        while (!paths.isEmpty()){
            List<Integer> path = paths.remove();
            Integer lastNode = path.get(path.size()-1);
            if(lastNode.equals(to)){
                return path;
            }
            for(Integer i : graph.getAdjacentVertices(lastNode)){
                List<Integer> newPath = new LinkedList<>(path);
                newPath.add(i);
                paths.add(newPath);
            }
        }
        return new LinkedList<>();
    }


    private static <V> int indexOfVertex(V vertex, V[] array){
        int output = -1;
        for(int i = 0; i<array.length; i++){
            if(array[i].equals(vertex)) output = i;
        }
        return output;
    }

    private static <T> T[] getArray(Class<T> clazz, int size) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clazz, size);

        return arr;
    }

    private static <T> T[][] get2DArray(Class<T> clazz, int size1, int size2){
        T[][] temp = getArray(getArrayClass(clazz), size1);
        for(int i = 0; i < size1; i++){
            temp[i] = getArray(clazz, size2);
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
        return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
    }
}
