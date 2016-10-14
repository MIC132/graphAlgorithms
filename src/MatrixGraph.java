import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by MIC on 2016-10-10.
 */
public class MatrixGraph<E, V> implements Graph<E, V>{
    V[] vertices = (V[]) new Object[0];
    E[][] edges = (E[][]) new Object[0][0];

    @Override
    public void addVertex(V vertex) {
        V[] tempVertices = (V[]) new Object[vertices.length+1];
        System.arraycopy(vertices,0,tempVertices,0,vertices.length);
        tempVertices[vertices.length] = vertex;


        E[][] tempEdges = (E[][]) new Object[vertices.length+1][vertices.length+1];
        for(int i=0; i<vertices.length; i++){
            System.arraycopy(edges[i],0, tempEdges[i], 0, vertices.length);
        }

        edges = tempEdges;
        vertices = tempVertices;
    }

    @Override
    public void removeVertex(V vertex) {
        int position = indexOfVertex(vertex);
        if(position == -1) return;

        V[] tempVertices = (V[]) new Object[vertices.length-1];
        System.arraycopy(vertices,0,tempVertices,0,position);
        System.arraycopy(vertices,position + 1,tempVertices,position,vertices.length - (position + 1));

        E[][] tempEdges = (E[][]) new Object[vertices.length-1][vertices.length-1];
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
    }

    @Override
    public void addEdge(E edge, V from, V to, boolean directional) {
        int posFrom = indexOfVertex(from);
        int posTo = indexOfVertex(to);

        if(posFrom != -1 && posTo != -1){
            edges[posFrom][posTo] = edge;
            if(!directional){
                edges[posTo][posFrom] = edge;
            }
        }
    }

    @Override
    public void removeEdge(E edge) {
        for(int i = 0; i<vertices.length; i++){
            for (int j = 0; j<vertices.length; j++){
                if(edges[i][j].equals(edge)) edges[i][j] = null;
            }
        }
    }

    @Override
    public V[] getAdjacentVertices(V vertex) {
        return ((V[]) new Object[0]);
    }

    @Override
    public E[] getAdjacentEdges(V vertex) {
        return ((E[]) new Object[0]);
    }

    @Override
    public int getNumberOfVertices() {
        return 0;
    }

    @Override
    public int getNumberOfEdges() {
        return 0;
    }

    @Override
    public boolean existsEdge(V from, V to, boolean directional) {
        return false;
    }

    private int indexOfVertex(V vertex){
        int output = -1;
        for(int i = 0; i<vertices.length; i++){
            if(vertices[i].equals(vertex)) output = i;
        }
        return output;
    }
}
