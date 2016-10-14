/**
 * Created by MIC on 2016-10-10.
 */
public class Main {
    public static void main(String[] args) {
        MatrixGraph<String, Integer> graph = new MatrixGraph<>();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.removeVertex(1);
    }
}
