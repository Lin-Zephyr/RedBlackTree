/**
 * Created by LinGaunnan on 8/30/2018.
 */
public class TreeUtils {
    public static void swapTreeElem(TreeNode node1, TreeNode node2) {
        int elem = node1.getElem();
        node1.setElem(node2.getElem());
        node2.setElem(elem);
    }
    public static void swapNodeColor(TreeNode node1, TreeNode node2) {
        NodeColor color = node1.getColor();
        node1.setColor(node2.getColor());
        node2.setColor(color);
    }
}
