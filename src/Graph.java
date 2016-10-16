/**
 * Created by MIC on 2016-10-10.
 */
public interface Graph<V, E> {
    boolean addVertex(V vertex);
    boolean removeVertex(V vertex);
    boolean addEdge(E edge, V from, V to, boolean directional);
    boolean removeEdge(E edge);
    V[] getAdjacentVertices(V vertex);
    E[] getAdjacentEdges(V vertex);
    int getNumberOfVertices();
    int getNumberOfEdges();
    boolean existsEdge(V from, V to, boolean directional);
}
