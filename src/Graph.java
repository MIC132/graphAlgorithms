/**
 * Created by MIC on 2016-10-10.
 */
public interface Graph<V, E> {
    V[] getVertices();
    Class getVertexClass();
    Class getEdgeClass();
    boolean addVertex(V vertex);
    boolean removeVertex(V vertex);
    boolean addEdge(E edge, V from, V to, boolean directional);
    boolean removeEdge(V from, V to, boolean directional);
    boolean removeEdge(E edge);
    E getEdge(V from, V to);
    V[] getAdjacentVertices(V vertex);
    E[] getAdjacentEdges(V vertex);
    int getNumberOfVertices();
    int getNumberOfEdges();
    boolean existsEdge(V from, V to, boolean directional);
}
