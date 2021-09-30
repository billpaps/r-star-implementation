public class Main {

    public static void main(String[] args){
        RTree rtree = new RTree(2);
        rtree.buildTree();
        Node root = rtree.getRoot().getChildren(0);
//        while (true){
//            System.out.println(root.getChildren().size());
//            try {
//                root = root.getChildren(0);
//            } catch (IndexOutOfBoundsException e) {
//                System.out.println(root.getIsLeaf());
//                System.out.println("enddddd");
//                break;
//            }
//        }
    }
}
