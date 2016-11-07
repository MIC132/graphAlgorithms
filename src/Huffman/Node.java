package Huffman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIC on 2016-11-07.
 */
public class Node implements Comparable<Node>{
    String character;
    int count;
    String prefix = "";
    public Node parent;
    public List<Node> children = new ArrayList<>();

    public Node(String character, int count){
        this.character = character;
        this.count = count;
    }

    public void updatePrefix(String pre){
        this.prefix = pre+prefix;
        for(Node child : children){
            child.updatePrefix(pre);
        }
    }

    public Node find(String chara){
        if(this.character.equals(chara)){
            return this;
        }else{
            for(Node child : children){
                Node result = child.find(chara);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    public Node findByPrefix(String pre){
        if(this.children.isEmpty() && this.prefix.equals(pre)){
            return this;
        }else{
            for(Node child : children){
                Node result = child.findByPrefix(pre);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(Node o) {
        return this.count - o.count;
    }
}
