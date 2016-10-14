/**
 * Created by MIC on 2016-10-10.
 */
public interface Graph<E, V> {
    void addVertex(V vertex);
    void removeVertex(V vertex);
    void addEdge(E edge, V from, V to, boolean directional);
    void removeEdge(E edge);
    V[] getAdjacentVertices(V vertex);
    E[] getAdjacentEdges(V vertex);
    int getNumberOfVertices();
    int getNumberOfEdges();
    boolean existsEdge(V from, V to, boolean directional);
}
