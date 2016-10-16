import java.lang.reflect.Array;

/**
 * Created by MIC on 2016-10-15.
 */
public class ListGraph<V, E> implements Graph<V, E> {
    V[] vertices;
    ListNode[] adjacencyList;

    private final Class<V> vertexClass;
    private final Class<E> edgeClass;


    public ListGraph(Class<V> vertexClass, Class<E> edgeClass) {
        this.vertexClass = vertexClass;
        this.edgeClass = edgeClass;
        vertices = getArray(vertexClass, 0);
        adjacencyList = getArray(ListNode.class, 0);
    }

    @Override
    public boolean addVertex(V vertex) {
        if(indexOfVertex(vertex, vertices) != -1) return false;

        V[] tempVertices = getArray(vertexClass, vertices.length + 1);
        System.arraycopy(vertices,0,tempVertices,0,vertices.length);
        tempVertices[vertices.length] = vertex;

        ListNode[] tempAdjacencyList =  getArray(ListNode.class, adjacencyList.length + 1);
        System.arraycopy(adjacencyList,0,tempAdjacencyList,0,adjacencyList.length);

        vertices = tempVertices;
        adjacencyList = tempAdjacencyList;

        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        int position = indexOfVertex(vertex,vertices);
        if(position == -1) return false;

        listDeleteVertex(vertex, adjacencyList[position]);

        V[] tempVertices = getArray(vertexClass, vertices.length - 1);
        System.arraycopy(vertices,0,tempVertices,0,position);
        System.arraycopy(vertices,position + 1,tempVertices,position,vertices.length - (position + 1));

        ListNode[] tempAdjacencyList =  getArray(ListNode.class, adjacencyList.length - 1);
        System.arraycopy(adjacencyList,0,tempAdjacencyList,0,position);
        System.arraycopy(adjacencyList,position + 1,tempAdjacencyList,position,adjacencyList.length - (position + 1));

        vertices = tempVertices;
        adjacencyList = tempAdjacencyList;

        return true;
    }

    @Override
    public boolean addEdge(E edge, V from, V to, boolean directional) {
        if(existsEdge(from, to, directional)) return false;

        addEdge(edge, from, to);
        if(!directional) addEdge(edge, to, from);

        return true;
    }

    private boolean addEdge(E edge, V from, V to){
        int position = indexOfVertex(from,vertices);
        if(position == -1) return false;

        ListNode node = adjacencyList[position];
        if(node == null){
            adjacencyList[position] = new ListNode(to, edge);
        }else{
            while(node.next != null){
                node = node.next;
            }
            node.next = new ListNode(to, edge);
            node.next.prev = node;
        }

        return true;
    }

    @Override
    public boolean removeEdge(E edge) {
        for (ListNode anAdjacencyList : adjacencyList) {
            listDeleteEdge(edge, anAdjacencyList);
        }

        return true;
    }

    @Override
    public V[] getAdjacentVertices(V vertex) {
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return getArray(vertexClass, 0);

        V[] temp = getArray(vertexClass, vertices.length);

        int i = 0;
        ListNode node = adjacencyList[position];
        while (node != null){
            temp[i] = (V) node.vertex;
            i++;
            node = node.next;
        }
        V[] result = getArray(vertexClass, i);
        System.arraycopy(temp,0,result,0,i);
        return result;
    }

    @Override
    public E[] getAdjacentEdges(V vertex) {
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return getArray(edgeClass, 0);

        E[] temp = getArray(edgeClass, vertices.length);

        int i = 0;
        ListNode node = adjacencyList[position];
        while (node != null){
            temp[i] = (E) node.edge;
            i++;
            node = node.next;
        }
        E[] result = getArray(edgeClass, i);
        System.arraycopy(temp,0,result,0,i);
        return result;
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.length;
    }

    @Override
    public int getNumberOfEdges() {
        int result = 0;
        for(int i = 0; i<vertices.length; i++){
            ListNode node = adjacencyList[i];
            while(node != null){
                E reverseEdge = getEdge((V) node.vertex, vertices[i]);
                if(reverseEdge == null || !reverseEdge.equals(node.edge)){
                    result++;
                }else if(reverseEdge.equals(node.edge) && i < indexOfVertex((V) node.vertex, vertices)){
                    result++;
                }
                node = node.next;
            }
        }
        return result;
    }

    @Override
    public boolean existsEdge(V from, V to, boolean directional) {
        return existsEdge(from, to) || (!directional && existsEdge(to, from));
    }

    private boolean existsEdge(V from, V to){
        int position = indexOfVertex(from, vertices);
        if(position == -1 || indexOfVertex(to, vertices) == -1) return false;
        ListNode node = adjacencyList[position];
        boolean result = false;
        while(node != null){
            if(node.vertex.equals(to)){
                result = true;
                break;
            }
        }
        return result;
    }

    private E getEdge(V from, V to){
        int position = indexOfVertex(from, vertices);
        if(position == -1 || indexOfVertex(to, vertices) == -1) return null;
        ListNode node = adjacencyList[position];
        E result = null;
        while(node != null){
            if(node.vertex.equals(to)){
                result = (E) node.edge;
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void listDeleteVertex(V vertex, ListNode startNode){
        ListNode current = startNode;
        while(current != null){
            if(current.next != null && ((V)current.next.vertex).equals(vertex)){
                current.next.next.prev = current;
                current.next = current.next.next;
            }
            current = current.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void listDeleteEdge(E edge, ListNode startNode){
        ListNode current = startNode;
        while(current != null){
            if(current.next != null && ((E)current.next.edge).equals(edge)){
                current.next.next.prev = current;
                current.next = current.next.next;
            }
            current = current.next;
        }
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

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
        return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
    }
}
