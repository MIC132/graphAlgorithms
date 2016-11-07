package Huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by MIC on 2016-11-07.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new BufferedReader(new FileReader(new File("compression.txt"))));
        HashMap<String, Node> nodeMap = new HashMap<>();
        StringBuilder unEncoded = new StringBuilder();
        while(s.hasNextLine()){
            String line = s.nextLine();
            unEncoded.append(line);
            for(char c : line.toCharArray()){
                String key = String.valueOf(c);
                if(nodeMap.containsKey(key)){
                    nodeMap.get(key).count++;
                }else{
                    nodeMap.put(key, new Node(key, 1));
                }
            }
        }
        System.out.println("Finished parsing");
        Node tree = huffman(nodeMap.values());

        Scanner s2 = new Scanner(new BufferedReader(new FileReader(new File("compression.txt"))));
        StringBuilder encoded = new StringBuilder();
        while(s2.hasNextLine()){
            encoded.append(huffmanEncode(tree, s2.nextLine()));
        }
        System.out.println((unEncoded.toString().length()*7.0 - encoded.toString().length())/(unEncoded.toString().length()*7.0));

        //System.out.println(huffmanDecode(tree, compressed));
    }

    public static Node huffman(Collection<Node> elements){
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.addAll(elements);
        while (queue.size() > 1){
            Node node1 = queue.poll();
            Node node2 = queue.poll();
            Node mergeNode = new Node(node1.character+node2.character, node1.count+node2.count);
            mergeNode.children.add(node1);
            node1.updatePrefix("0");
            mergeNode.children.add(node2);
            node2.updatePrefix("1");
            node1.parent = mergeNode;
            node2.parent = mergeNode;
            queue.add(mergeNode);
        }
        return queue.poll();
    }

    public static String huffmanEncode(Node tree, String text){
        StringBuilder result = new StringBuilder();
        for(char c : text.toCharArray()){
            String chara = String.valueOf(c);
            result.append(tree.find(chara).prefix);
        }
        return result.toString();
    }

    public static String huffmanDecode(Node tree, String text){
        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();
        int i = 1;
        String prefix = String.valueOf(chars[0]);
        while (i <= chars.length){
            Node n = tree.findByPrefix(prefix);
            if(n != null){
                result.append(n.character);
                prefix = "";
            }else{
                if(i<chars.length){
                    prefix += String.valueOf(chars[i]);
                }
                i++;
            }
        }
        return result.toString();
    }
}
