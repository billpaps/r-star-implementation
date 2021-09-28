import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node parent;
    private boolean leaf, hasleaf;
    private int dim, size;
    private List<Record> records;
    private ArrayList<Node> children;
    private double[][] mbr;


    public Node(int dim, Node parent) {
        this.parent = parent;
        this.dim = dim;
        this.leaf = true;
        this.size = 0;
        mbr = new double[dim][dim];
        records = new ArrayList<>();
        children = new ArrayList<>();
    }


    public void setMbr(double[][] mbr) {
        this.mbr = mbr;
    }

    public void addChildSize(){
        this.size++;
    }

    public Node getParent() {
        return parent;
    }

    public int getSize() {
        return size;
    }


    public boolean getIsLeaf() {
        return leaf;
    }

    public boolean getHasLeaf() {
        return leaf;
    }

    public int getDim() {
        return dim;
    }

    public List<Record> getRecords() {
        return records;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public double[][] getMbr() {
        return mbr;
    }

//    public double getArea() {
//        double area = 1;
//        for (int axis = 0; axis < dim; axis++) {
//            double min, max;
//            min = max = mbr[0][axis];
//            for (int i = 0; i < dim; i++) {
//                if (mbr[i][axis] < min) {
//                    min = mbr[i][axis];
//                }
//                else if (mbr[i][axis] > max) {
//                    max = mbr[i][axis];
//                }
//            }
//            area *= min;
//        }
//        return area;
//    }
}
