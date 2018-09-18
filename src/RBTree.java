import com.sun.org.apache.regexp.internal.RE;
import sun.reflect.generics.tree.Tree;

import javax.xml.soap.Node;
import java.lang.reflect.Parameter;

/**
 * Author: LinGuannan
 * Create date: 2018-04-04
 * Email: Guannan.Lin@outlook.com
 */

public class RBTree implements ITree {
    private TreeNode root;
    @Override
    public boolean insert(int elem) {
        TreeNode node = new TreeNode(elem);
        boolean inserted = false;
        if (null == this.root) {
            this.root = node;
            inserted = true;
        } else {
            inserted = insertNode(this.root, node);
        }
        setRootBlack(); //the root must be always black
        return inserted;
    }

    @Override
    public boolean delete(int elem) {
        if (null == this.root) {
            return false;
        } else {
            TreeNode node = this.root;
            // find out the node need to be deleted
            while (null != node) {
                if (node.getElem() == elem) {
                    deleteNode(node);
                    return true;
                } else if (node.getElem() > elem) {
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            }
            return false;
        }
    }

    @Override
    public boolean search(int elem) {
        if (null == this.root) {
            return false;
        } else {
            return searchTree(this.root, elem);
        }
    }

    @Override
    public void printTreeScheme() {
        System.out.println("-");
        printTree(this.root, "");
        System.out.println("-");
    }

    private boolean insertNode(TreeNode node, TreeNode newNode) {
        if (node.getElem() == newNode.getElem()) {
            return false; // the element already exist
        } else if (node.getElem() < newNode.getElem()) {
            if (null == node.getRight()) {
                node.setRight(newNode);
                newNode.setParent(node);
                insertFixUp(newNode);
                return true;
            } else {
                return insertNode(node.getRight(), newNode);
            }
        } else {
            if (null == node.getLeft()) {
                node.setLeft(newNode);
                newNode.setParent(node);
                insertFixUp(newNode);
                return true;
            } else {
                return insertNode(node.getLeft(), newNode);
            }
        }
    }

    private void deleteNode(TreeNode node) {
        if(null  == node.getLeft() && null == node.getRight()) {
            if (node.getColor() == NodeColor.RED) {
                delete_red_leaf(node, true);
            } else {
                delete_black_leaf(node, true);
            }
        } else if (null == node.getLeft()) {
            // the node color must be black and the right child must be red node
            // replace the element of node with its right child's
            // cut off the the link between node and its right child
            node.setElem(node.getRight().getElem());
            node.setRight(null);
        } else if (null == node.getRight()) {
            node.setElem(node.getLeft().getElem());
            node.setLeft(null);
        } else {
            // both children are not null
            TreeNode next = node.getRight();
            while (null != next.getLeft()) {
                next = next.getLeft();
            }
            TreeUtils.swapTreeElem(node, next);
            deleteNode(next);
        }
    }
    private void delete_red_leaf(TreeNode node, boolean needDel) {
        TreeNode parent = node.getParent();
        if (node == parent.getLeft()) {
            parent.setLeft(null);
        } else {
            parent.setRight(null);
        }
    }
    private void delete_black_leaf(TreeNode node, boolean needDel) {
        TreeNode parent = node.getParent();
        if (null != parent) {
            boolean nodeInLeft = parent.getLeft() == node;
            TreeNode sibling = nodeInLeft ? parent.getRight() : parent.getLeft();
            TreeNode remoteNephew = null == sibling ? null : (nodeInLeft ? sibling.getRight() : sibling.getLeft());
            TreeNode nearNephew = null == sibling ? null : (nodeInLeft ? sibling.getLeft() : sibling.getRight());
            if (sibling.getColor() == NodeColor.RED) {
                delete_sibling_red(node);
            } else if (null != remoteNephew && remoteNephew.getColor() == NodeColor.RED) {
                delete_remote_nephew_red(node);
            } else if (null != nearNephew && remoteNephew.getColor() == NodeColor.RED) {
                delete_near_nephew_red(node);
            } else {
                // the sibling is also a leaf
                if (parent.getColor() == NodeColor.RED) {
                    delete_parent_red(node);
                } else {
                    sibling.setColor(NodeColor.RED);
                    delete_black_leaf(parent, false);
                }
            }
        }
        if (needDel) {
            if (null == parent) {
              this.root = null;
            } else if (node.getParent().getLeft() == node) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }
    }
    private void delete_parent_red(TreeNode node) {
        TreeNode parent = node.getParent();
        parent.setColor(NodeColor.BLACK);
        TreeNode sibling = parent.getLeft() == node ? parent.getRight() : parent.getLeft();
        sibling.setColor(NodeColor.RED);
    }
    private void delete_sibling_red(TreeNode node) {
        TreeNode parent = node.getParent();
        TreeNode sibling = node == parent.getLeft() ? parent.getRight() : parent.getLeft();
        parent.setColor(NodeColor.RED);
        sibling.setColor(NodeColor.BLACK);
        if (node == parent.getLeft()) {
            rotateLeft(parent);
        } else {
            rotateRight(parent);
        }
        // convert into next state - parent red
        delete_black_leaf(node, false);
    }
    private void delete_remote_nephew_red(TreeNode node) {
        TreeNode parent = node.getParent();
        boolean nodeInLeft = node == parent.getLeft();
        TreeNode sibling = nodeInLeft ? parent.getRight() : parent.getLeft();
        TreeUtils.swapNodeColor(parent, sibling);
        if (nodeInLeft) {
            TreeNode nephew = sibling.getRight();
            rotateLeft(parent);
            nephew.setColor(NodeColor.BLACK);
        } else {
            TreeNode nephew = sibling.getLeft();
            rotateRight(parent);
            nephew.setColor(NodeColor.BLACK);
        }
    }
    private void delete_near_nephew_red(TreeNode node) {
        TreeNode parent = node.getParent();
        boolean nodeInLeft = node == parent.getLeft();
        TreeNode sibling = nodeInLeft ? parent.getRight() : parent.getLeft();
        if (nodeInLeft) {
            TreeNode nephew = sibling.getLeft();
            nephew.setColor(NodeColor.BLACK);
            sibling.setColor(NodeColor.RED);
            rotateRight(sibling);
        } else {
            TreeNode nephew = sibling.getRight();
            nephew.setColor(NodeColor.BLACK);
            sibling.setColor(NodeColor.RED);
            rotateLeft(sibling);
        }
        delete_black_leaf(node, false);
    }

    private void printTree(TreeNode node, String indent) {
        if (null != node) {
            printTree(node.getRight(), indent + "    ");
            System.out.println(indent + node.getElem()+"[" + (node.getColor() == NodeColor.RED ? "R" : "B") + "]");
            printTree(node.getLeft(), indent + "    ");
        }
    }
    private boolean searchTree(TreeNode parent, int elem) {
        if (null == parent) {
            return false;
        } else {
            if (parent.getElem() == elem) {
                return true;
            } else if (parent.getElem() > elem) {
                return searchTree(parent.getLeft(), elem);
            } else {
                return searchTree(parent.getRight(), elem);
            }
        }
    }
    private void setRootBlack() {
        if (null != this.root && this.root.getColor() != NodeColor.BLACK) {
            this.root.setColor(NodeColor.BLACK);
        }
    }

    private void insertFixUp(TreeNode node) {
        TreeNode parent = node.getParent();
        while (null != parent && parent.getColor() == NodeColor.RED) {
            // parent should not be root for root node must be black
            boolean uncleInRight = parent.getParent().getLeft() == parent;
            TreeNode uncle = uncleInRight ? parent.getParent().getRight() : parent.getParent().getLeft();
            if (null == uncle) {
                // uncle is Nil and could not be black node
                if (uncleInRight) {
                    if (node == parent.getLeft()) {
                        // case 1
                        parent.setColor(NodeColor.BLACK);
                        parent.getParent().setColor(NodeColor.RED);
                        rotateRight(parent.getParent());
                        break;
                    } else {
                        // case 2
                        rotateLeft(parent);
                        node = node.getLeft(); // convert to case 1
                    }
                } else {
                    if (node == parent.getRight()) {
                        // case 3
                        parent.setColor(NodeColor.BLACK);
                        parent.getParent().setColor(NodeColor.RED);
                        rotateLeft(parent.getParent());
                        break;
                    } else {
                        // case 4
                        rotateRight(parent);
                        node = node.getRight(); // convert to case 3
                    }
                }
            } else {
                // uncle node is red
                parent.setColor(NodeColor.BLACK);
                uncle.setColor(NodeColor.BLACK);
                parent.getParent().setColor(NodeColor.RED);
                node = parent.getParent();
            }
            parent = node.getParent();
        }
    }


    /**
     * left rotate the subtree base on pivot
     * @param pivot
     */
    private void rotateLeft(TreeNode pivot) {
        TreeNode parent = pivot.getParent();
        TreeNode right = pivot.getRight();
        TreeNode grandchild = right.getLeft();

        if (null == parent) {
            this.root = right;
        } else if (parent.getLeft() == pivot) {
            parent.setLeft(right);
        } else {
            parent.setRight(right);
        }
        right.setParent(parent);
        pivot.setRight(grandchild);
        if (null != grandchild) {
            grandchild.setParent(pivot);
        }

        right.setLeft(pivot);
        pivot.setParent(right);
    }

    /**
     * right rotate the subtre base on pivot
     * @param pivot
     */
    private void rotateRight(TreeNode pivot) {
        TreeNode parent = pivot.getParent();
        TreeNode left = pivot.getLeft();
        TreeNode grandchild = left.getRight();

        if (null == parent) {
            this.root = left;
        } else if (parent.getLeft() == pivot) {
            parent.setLeft(left);
        } else {
            parent.setRight(left);
        }
        left.setParent(parent);
        pivot.setLeft(grandchild);
        if (null != grandchild) {
            grandchild.setParent(pivot);
        }
        left.setRight(pivot);
        pivot.setParent(left);
    }

    // for testing only;
    public void setTestRoot(TreeNode root) {
        this.root = root;
    }
}