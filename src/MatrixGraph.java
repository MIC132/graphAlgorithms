/**
 * Created by MIC on 2016-10-10.
 */
public class MatrixGraph<V, E> implements Graph<V, E>{
    V[] vertices = (V[]) new Object[0];
    E[][] edges = (E[][]) new Object[0][0];

    @Override
    public void addVertex(V vertex) {
        if(indexOfVertex(vertex, vertices) != -1) return;

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
        int position = indexOfVertex(vertex,vertices);

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
        int posFrom = indexOfVertex(from,vertices);
        int posTo = indexOfVertex(to,vertices);

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
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return (V[]) new Object[0];

        V[] temp = (V[]) new Object[vertices.length];

        int j = 0;
        for(int i = 0; i<vertices.length; i++){
            if(edges[position][i] != null){
                temp[j] = vertices[i];
                j++;
            }
        }

        V[] result = (V[]) new Object[j];
        System.arraycopy(temp,0,result,0,j);
        return result;
    }

    @Override
    public E[] getAdjacentEdges(V vertex) {
        int position = indexOfVertex(vertex, vertices);
        if(position == -1) return (E[]) new Object[0];

        E[] temp = (E[]) new Object[(vertices.length*(vertices.length-3))/2+vertices.length];

        int j = 0;
        for(int i = 0; i<vertices.length; i++){
            if(edges[position][i] != null){
                temp[j] = edges[position][i];
                j++;
            }
        }

        E[] result = (E[]) new Object[j];
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
}
