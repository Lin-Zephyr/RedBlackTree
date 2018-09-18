import lombok.Cleanup;

import java.util.Scanner;

/**
 * Created by LinGaunnan on 4/4/2018.
 */
public class Main {
    public static void main(String[] arg) {
        //////
        TreeNode root = new TreeNode(8, NodeColor.BLACK);
        root.setLeft(new TreeNode(4, NodeColor.RED)).setRight(new TreeNode(12, NodeColor.BLACK));
        root.getLeft().setParent(root);
        root.getRight().setParent(root);
        root.getLeft().setLeft(new TreeNode(2, NodeColor.BLACK)).setRight(new TreeNode(6, NodeColor.BLACK));
        root.getRight().setLeft(new TreeNode(10, NodeColor.BLACK)).setRight(new TreeNode(14, NodeColor.BLACK));
        root.getLeft().getLeft().setParent(root.getLeft());
        root.getLeft().getRight().setParent(root.getLeft());
        root.getRight().getLeft().setParent(root.getRight());
        root.getRight().getRight().setParent(root.getRight());

        root.getLeft().getLeft().setLeft(new TreeNode(1, NodeColor.BLACK)).setRight(new TreeNode(3, NodeColor.BLACK));
        root.getLeft().getRight().setLeft(new TreeNode(5, NodeColor.BLACK)).setRight(new TreeNode(7, NodeColor.BLACK));
        root.getLeft().getLeft().getLeft().setParent(root.getLeft().getLeft());
        root.getLeft().getLeft().getRight().setParent(root.getLeft().getLeft());
        root.getLeft().getRight().getLeft().setParent(root.getLeft().getRight());
        root.getLeft().getRight().getRight().setParent(root.getLeft().getRight());
        //////
        RBTree tree = null;
        tree = new RBTree();
        tree.setTestRoot(root);
        @Cleanup Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--------------------------------------------------------------");
            System.out.println("1. insert");
            System.out.println("2. delete");
            System.out.println("3. search");
            System.out.println("4. print");
            System.out.println("5. exit");
            System.out.print("->");
            int cmd = scanner.nextInt();
            if (5 == cmd) {
                break;
            }
            switch (cmd) {
                case 1:
                    while (true) {
                        System.out.print("->");
                        int elem = scanner.nextInt();
                        if (-1 == elem) {
                            break;
                        }
                        System.out.println("insert " + (tree.insert(elem) ? "success" : "fail"));
                    }
                    break;
                case 2:
                    System.out.print("->");
                    int elem = scanner.nextInt();
                    System.out.println("delete " + (tree.delete(elem) ? "success" : "fail"));
                    break;
                case 3:
                    System.out.print("->");
                    elem = scanner.nextInt();
                    System.out.println(tree.search(elem) ? "can be found" : "can not be found");
                    break;
                case 4:
                    tree.printTreeScheme();
                default:break;
            }
        }
    }
}
