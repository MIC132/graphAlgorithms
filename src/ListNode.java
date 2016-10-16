/**
 * Created by MIC on 2016-10-15.
 */
public class ListNode {
    ListNode next;
    ListNode prev;

    final Object vertex;
    final Object edge;

    public ListNode(Object vertex, Object edge) {
        this.vertex = vertex;
        this.edge = edge;
    }
}
