import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by MIC on 2016-10-24.
 */
public class FordFulkerson {

    private FordFulkerson() {}

    public static int getMaximumFlow(Graph<Integer, Integer> capacityGraph, int from, int to){
        //Graph<Integer, Integer> flowGraph = new MatrixGraph<>(Integer.class, Integer.class);
        Graph<Integer, Integer> residualGraph = new MatrixGraph<>(Integer.class, Integer.class);
        for(Integer i : capacityGraph.getVertices()){
            for (Integer j : capacityGraph.getVertices()){
                if (!i.equals(j)) {
                    //flowGraph.addVertex(i);
                    //flowGraph.addVertex(j);
                    //flowGraph.addEdge(0, i, j, true);
                    residualGraph.addVertex(i);
                    residualGraph.addVertex(j);
                    if (capacityGraph.existsEdge(i, j, true)) {
                        residualGraph.addEdge(capacityGraph.getEdge(i, j), i, j, true);
                    }
                }
            }
        }
        int resultFlow = 0;
        List<Integer> path = dfsPath(residualGraph, from, to);
        while (!path.isEmpty()){
            int minflow = Integer.MAX_VALUE;
            for(int i=0; i<path.size()-1; i++){
                int flow = residualGraph.getEdge(path.get(i), path.get(i+1));
                if(flow < minflow){
                    minflow = flow;
                }
            }
            resultFlow += minflow;
            //System.out.println("Minflow: "+minflow);
            for(int i=0; i<path.size()-1; i++){
                //Integer currentFlow = flowGraph.getEdge(path.get(i), path.get(i+1));
                //flowGraph.removeEdge(path.get(i), path.get(i+1), true);
                //flowGraph.addEdge(currentFlow + minflow, path.get(i), path.get(i+1), true);
                Integer currentFlow = residualGraph.getEdge(path.get(i), path.get(i + 1));
                //System.out.println("Current flow forward: "+currentFlow);
                residualGraph.removeEdge(path.get(i), path.get(i+1), true);
                residualGraph.addEdge(currentFlow - minflow, path.get(i), path.get(i+1), true);
                /*currentFlow = flowGraph.getEdge(path.get(i+1), path.get(i));
                if(currentFlow != null){
                    flowGraph.removeEdge(path.get(i+1), path.get(i), true);
                    flowGraph.addEdge(currentFlow - minflow, path.get(i+1), path.get(i), true);
                }
                */
                currentFlow = residualGraph.getEdge(path.get(i + 1), path.get(i));
                //System.out.println("Current flow backward: "+currentFlow);
                if (currentFlow != null) {
                    residualGraph.removeEdge(path.get(i + 1), path.get(i), true);
                    residualGraph.addEdge(currentFlow + minflow, path.get(i + 1), path.get(i), true);
                } else {
                    residualGraph.addEdge(minflow, path.get(i + 1), path.get(i), true);
                }

            }
            path = dfsPath(residualGraph, from, to);
            //System.out.println("New path: "+path);
        }
        /*for(Integer i : flowGraph.getAdjacentVertices(from)){
            flow += flowGraph.getEdge(from, i);
        }*/
        return resultFlow;
    }

    private static List<Integer> bfsPath(Graph<Integer, Integer> graph, Integer from, Integer to) {
        Queue<List<Integer>> paths = new ArrayDeque<>();
        List<Integer> start = new ArrayList<>();
        start.add(from);
        paths.add(start);
        while (!paths.isEmpty()){
            List<Integer> path = paths.remove();
            Integer lastNode = path.get(path.size()-1);
            if(lastNode.equals(to)){
                return path;
            }
            for(Integer i : graph.getAdjacentVertices(lastNode)){
                if (!path.contains(i) && graph.getEdge(lastNode, i) > 0) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(i);
                    paths.add(newPath);
                }
            }
        }
        return new ArrayList<>();
    }

    private static List<Integer> dfsPath(Graph<Integer, Integer> graph, Integer from, Integer to) {
        return dfsPathInternal(graph, from, to, new ArrayList<>());
    }

    private static List<Integer> dfsPathInternal(Graph<Integer, Integer> graph, Integer from, Integer to, List<Integer> visited) {
        visited.add(from);
        List<Integer> result = new ArrayList<>();
        for (Integer i : graph.getAdjacentVertices(from)) {
            if (!visited.contains(i) && graph.getEdge(from, i) > 0) {
                if (i.equals(to)) {
                    result.add(from);
                    result.add(i);
                    break;
                } else {
                    List<Integer> recursionResult = dfsPathInternal(graph, i, to, visited);
                    if (!recursionResult.isEmpty()) {
                        result.add(from);
                        result.addAll(recursionResult);
                        break;
                    }
                }
            }
        }
        return result;
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
