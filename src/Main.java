import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MIC on 2016-10-10.
 */
public class Main {
    static final Pattern p = Pattern.compile("(\\d*);\\s*(\\d*);\\s*(\\d*).*");

    public static void main(String[] args) throws FileNotFoundException {
        Graph<Integer, Integer> graph = new MatrixGraph<>(Integer.class, Integer.class);

        Scanner s = new Scanner(new BufferedReader(new FileReader(new File("flow.txt"))));
        while (s.hasNextLine()){
            String line = s.nextLine();
            Matcher m = p.matcher(line);
            if(m.find()){
                int a = Integer.parseInt(m.group(1));
                int b = Integer.parseInt(m.group(2));
                int c = Integer.parseInt(m.group(3));
                graph.addVertex(a);
                graph.addVertex(b);
                graph.addEdge(c, a, b, true);
            }
        }
        System.out.println("Finished parsing");

        System.out.println(FordFulkerson.getMaximumFlow(graph, 109, 609));
    }

    private static <V> int indexOfVertex(V vertex, V[] array){
        int output = -1;
        for(int i = 0; i<array.length; i++){
            if(array[i].equals(vertex)) output = i;
        }
        return output;
    }
}
