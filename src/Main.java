/**
 * Created by MIC on 2016-10-10.
 */
public class Main {
    public static void main(String[] args) {
        MatrixGraph<Integer, String> graph = new MatrixGraph<>(Integer.class, String.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge("1to2", 1, 2, false);

        Integer[] list = graph.getAdjacentVertices(1);
        System.out.println(list[0]);
    }
}
