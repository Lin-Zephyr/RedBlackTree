import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import sun.reflect.generics.tree.Tree;

/**
 * Created by LinGaunnan on 9/10/2018.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TreeNode {
    private int elem;
    private TreeNode left, right;
    private TreeNode parent;
    private NodeColor color;
    public TreeNode (int elem) {
        this.elem = elem;
        color = NodeColor.RED;
    }
    public TreeNode (int elem, NodeColor color) {
        this.elem = elem;
        this.color = color;
    }
    @Override
    public String toString() {
        return "TreeNode{" +
                "elem=" + elem +
                ", left=" + (left == null ? "null" : left.getElem()) +
                ", right=" + (right == null ? "null" : right.getElem()) +
                ", parent=" + (parent == null ? "null" : parent.getElem()) +
                ", color=" + color +
                '}';
    }
}
