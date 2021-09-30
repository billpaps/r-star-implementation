import java.util.ArrayList;
import java.util.Arrays;
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
        mbr = new double[dim][2];
        records = new ArrayList<>();
        children = new ArrayList<>();
    }

    public void setMbr(Record newRec) {

        for (int axis = 0; axis < dim; axis++) {
            // Check min and max for each axis
            if (newRec.getCords().get(axis) < mbr[axis][0] || mbr[axis][0] == 0) {
                mbr[axis][0] = newRec.getCords().get(axis);
            } else if (newRec.getCords().get(axis) > mbr[axis][1] || mbr[axis][1] == 0) {
                mbr[axis][1] = newRec.getCords().get(axis);
            }
//            System.out.println(newRec.getCords().get(axis));

        }
    }

    public void setMbr(Node newRec) {

        for (int axis = 0; axis < dim; axis++) {
            // Check min and max for each axis
            if (newRec.getMbr()[axis][0] < mbr[axis][0] || mbr[axis][0]==0) {
                mbr[axis][0] = newRec.getMbr()[axis][0];
            } else if (newRec.getMbr()[axis][1] > mbr[axis][1] || mbr[axis][1] == 0) {
                mbr[axis][1] = newRec.getMbr()[axis][1];
            }
        }
    }

    public void zeroMbr(){mbr = new double[dim][2];}

    public void zeroRecords(){records = new ArrayList<>();}

    public void zeroChildren(){
        children = new ArrayList<>();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public int getSize() {
        return size;
    }

    public boolean getIsLeaf() {
        return leaf;
    }

    public boolean getHasLeaf() {
        return hasleaf;
    }

    public int getDim() {
        return dim;
    }

    public List<Record> getRecords() {
        return records;
    }

    public Node getChildren(int i) {
        return children.get(i);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public double[][] getMbr() {
        return mbr;
    }

    public boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}

