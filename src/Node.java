import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node parent;
    private boolean leaf;
    private int dim;
    private List<Record> records;
    private ArrayList<Node> children;
    private double[][] mbr;

    public Node(int dim, Node parent) {
        this.parent = parent;
        this.dim = dim;
        this.leaf = true;
        mbr = new double[dim][dim];
        records = new ArrayList<>();
        children = new ArrayList<>();
    }

    public void setMbr(Record rec){

    }

    public Node getParent() {
        return parent;
    }

    public boolean isLeaf() {
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

    public boolean getLeaf(){
        return leaf;
    }

    public void setLeaf(boolean leaf){
        this.leaf = leaf;
    }

}
