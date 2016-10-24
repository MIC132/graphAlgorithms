import java.lang.reflect.Array;
import java.util.function.Function;

/**
 * Created by MIC on 2016-10-17.
 */
public class WarshalFloyd {

    private WarshalFloyd() {}

    public static <V, E> Result<V> getDistTable(Graph<V,E> graph, Function<E, Long> costFunction){
        int size = graph.getNumberOfVertices();
        V[] vertices = graph.getVertices();
        long[][] d = new long[size][size];
        V[][] prev = (V[][]) get2DArray(graph.getEdgeClass(), size, size);
        for(int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                if(i == j){
                    d[i][j] = 0;
                }else{
                    E edge = graph.getEdge(vertices[i], vertices[j]);
                    if(edge == null){
                        d[i][j] = Long.MAX_VALUE;
                        prev[i][j] = null;
                    }else{
                        d[i][j] = costFunction.apply(edge);
                        prev[i][j] = vertices[i];
                    }
                }
            }
        }

        for(int u = 0; u<size ;u++){
            for (int v1 = 0; v1<size; v1++){
                for(int v2 = 0; v2<size; v2++){
                    if(d[v1][v2] == Long.MAX_VALUE || (d[v1][v2] > d[v1][u] + d[u][v2])){
                        if(d[v1][u] != Long.MAX_VALUE && d[u][v2] != Long.MAX_VALUE){
                            d[v1][v2] = d[v1][u] + d[u][v2];
                            prev[v1][v2] = prev[u][v2];
                        }

                    }
                }
            }
        }

        return new Result<V>(d, prev);
    }

    public static <V, E> long getShortestDistance(Graph<V, E> graph, Function<E, Long> costFunction, V from, V to){
        Result<V> result = getDistTable(graph, costFunction);
        int indexFrom = indexOfVertex(from, graph.getVertices());
        int indexTo = indexOfVertex(to, graph.getVertices());
        return result.distances[indexFrom][indexTo];
    }

    public static class Result<V>{
        final long[][] distances;
        final V[][] prev;

        public Result(long[][] distances, V[][] prev) {
            this.distances = distances;
            this.prev = prev;
        }
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
