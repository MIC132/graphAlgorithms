import java.lang.reflect.Array;

/**
 * Created by MIC on 2016-10-10.
 */
public class MatrixGraph<V, E> implements Graph<V, E>{
    private V[] vertices;
    private E[][] edges;

    private final Class<V> vertexClass;
    private final Class<E> edgeClass;


    public MatrixGraph(Class<V> vertexClass, Class<E> edgeClass) {
        this.vertexClass = vertexClass;
        this.edgeClass = edgeClass;
        vertices = getArray(vertexClass, 0);
        edges = get2DArray(edgeClass, 0, 0);
    }

    @Override
    public V[] getVertices() {
        return vertices;
    }

    @Override
    public Class getVertexClass() {
        return vertexClass;
    }

    @Override
    public Class getEdgeClass() {
        return edgeClass;
    }

    @Override
    public boolean addVertex(V vertex) {
        if(indexOfVertex(vertex, vertices) != -1) return false;

        V[] tempVertices = getArray(vertexClass, vertices.length + 1);

        System.arraycopy(vertices,0,tempVertices,0,vertices.length);
        tempVertices[vertices.length] = vertex;

        E[][] tempEdges = get2DArray(edgeClass, vertices.length+1, vertices.length+1);
        for(int i=0; i<vertices.length; i++){
            System.arraycopy(edges[i],0, tempEdges[i], 0, vertices.length);
        }

        edges = tempEdges;
        vertices = tempVertices;
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        int position = indexOfVertex(vertex,vertices);

        if(position == -1) return false;

        V[] tempVertices = getArray(vertexClass, vertices.length - 1);
        System.arraycopy(vertices,0,tempVertices,0,position);
        System.arraycopy(vertices,position + 1,tempVertices,position,vertices.length - (position + 1));

        E[][] tempEdges = get2DArray(edgeClass, vertices.length-1, vertices.length-1);
        for(int i=0; i<position; i++){
            System.arraycopy(edges[i],0,tempEdges[i],0,position);
            System.arraycopy(edges[i],position + 1,tempEdges[i],position,vertices.length - (position + 1));
        }
        for(int i=position; i<vertices.length - (position + 1); i++){
            System.arraycopy(edges[i],0,tempEdges[i],0,position);
            System.arraycopy(edges[i],position + 1,tempEdges[i],position,vertices.length - (position + 1));
        }

        edges = tempEdges;
        vertices = tempVertices;
        return true;
    }

    @Override
    public boolean addEdge(E edge, V from, V to, boolean directional) {
        int posFrom = indexOfVertex(from,vertices);
        int posTo = indexOfVertex(to,vertices);

        if(posFrom != -1 && posTo != -1){
            edges[posFrom][posTo] = edge;
            if(!directional){
                edges[posTo][posFrom] = edge;
            }
        }

        return true;
    }

    @Override
    public boolean removeEdge(E edge) {
        boolean out = false;
        for(int i = 0; i<vertices.length; i++){
            for (int j = 0; j<vertices.length; j++){
                if(edges[i][j].equals(edge)){
                    edges[i][j] = null;
                    out = true;
                }
            }
        }
        return out;
    }

    @Override
    public boolean removeEdge(V from, V to, boolean directional) {
        int fromPos = indexOfVertex(from, vertices);
        int toPos = indexOfVertex(to, vertices);
        if(fromPos == -1 || toPos == -1) return false;

        edges[fromPos][toPos] = null;
        if(!directional){
            edges[toPos][fromPos] = null;
        }
        return true;
    }

    @Override
    public V[] getAdjacentVertices(V vertex) {
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return getArray(vertexClass, 0);

        V[] temp = getArray(vertexClass, vertices.length);

        int j = 0;
        for(int i = 0; i<vertices.length; i++){
            if(edges[position][i] != null){
                temp[j] = vertices[i];
                j++;
            }
        }

        V[] result = getArray(vertexClass, j);
        System.arraycopy(temp,0,result,0,j);
        return result;
    }

    @Override
    public E[] getAdjacentEdges(V vertex) {
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return getArray(edgeClass, 0);

        E[] temp = getArray(edgeClass, (vertices.length*(vertices.length-3))/2+vertices.length);

        int j = 0;
        for(int i = 0; i<vertices.length; i++){
            if(edges[position][i] != null){
                temp[j] = edges[position][i];
                j++;
            }
        }

        E[] result = getArray(edgeClass, j);
        System.arraycopy(temp,0,result,0,j);
        return result;
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.length;
    }

    @Override
    public int getNumberOfEdges() {
        int counter = 0;

        for(int i = 0; i<vertices.length; i++){
            for(int j = 0; j<i; j++){
                if(edges[i][j] != null && edges[j][i] != null){
                    if(edges[i][j].equals(edges[j][i])){
                        counter += 1;
                    }else{
                        counter += 2;
                    }
                }else if(edges[i][j] != null ^ edges[j][i] !=null){
                    counter +=1;
                }
            }
        }

        return counter;
    }

    @Override
    public boolean existsEdge(V from, V to, boolean directional) {
        int fromPos = indexOfVertex(from, vertices);
        int toPos = indexOfVertex(to, vertices);
        if(fromPos == -1 || toPos == -1) return false;

        if(edges[fromPos][toPos] != null){
            return true;
        }
        if(!directional && edges[toPos][fromPos] != null){
            return true;
        }
        return false;

    }

    @Override
    public E getEdge(V from, V to) {
        int fromPos = indexOfVertex(from, vertices);
        int toPos = indexOfVertex(to, vertices);
        if(fromPos == -1 || toPos == -1) return null;
        return edges[fromPos][toPos];
    }

    private boolean existsEdge(E edge){
        boolean result = false;
        for(int i = 0; i<vertices.length; i++){
            for (int j = 0; j<vertices.length; j++){
                if(edges[i][j].equals(edge)) result = true;
            }
        }
        return result;
    }

    private int indexOfVertex(V vertex, V[] array){
        int output = -1;
        for(int i = 0; i<array.length; i++){
            if(array[i].equals(vertex)) output = i;
        }
        return output;
    }

    private <T> T[] getArray(Class<T> clazz, int size) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clazz, size);

        return arr;
    }

    private <T> T[][] get2DArray(Class<T> clazz, int size1, int size2){
        T[][] temp = getArray(getArrayClass(clazz), size1);
        for(int i = 0; i < size1; i++){
            temp[i] = getArray(clazz, size2);
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
        return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
    }
}
