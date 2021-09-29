import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class RTree {

    private int dim;
    private final ArrayList<Record> records;
    private Node root;
    private final int M = 4;
    private final int m = 2;

    public RTree(int dim, DataFile data) {
        this.dim = dim;
        records = data.getRecords();
        root = new Node(dim, null);
        BuildRTree();
    }

    public void BuildRTree() {
        for (Record rec : records) {
            Insert(root, rec);
        }


    }

    private void Insert(Node currNode, Record newRec) {
        Node selected_node = chooseLeaf(currNode, newRec);
        if (selected_node.getRecords().size() <= M) {
            selected_node.getRecords().add(newRec);
            selected_node.setMbr(newRec);
        } else {
            SplitNode(currNode, newRec);
        }
    }

    private void SplitNode(Node currNode, Record newRec) {
        int axis = ChooseSplitAxis(currNode, newRec);
        ArrayList<ArrayList<Record>> min_distribution = Calculate_Minimum_Distribution(currNode, newRec, axis);

        // Root
        if (currNode == root) {
            Node child_node1 = new Node(dim, currNode);
            Node child_node2 = new Node(dim, currNode);

            currNode.getChildren().add(child_node1);
            currNode.getChildren().add(child_node2);
            currNode.setLeaf(false);

            for (int i = 0; i < min_distribution.get(0).size(); i++) {
                Record rec = min_distribution.get(0).get(i);
                child_node1.getRecords().add(rec);
            }
            for (int i = 0; i < min_distribution.get(1).size(); i++) {
                Record rec = min_distribution.get(1).get(i);
                child_node2.getRecords().add(rec);
            }

        }

        // Normal Node
        else if (currNode.getChildren().size() == M) {
            assert true;
        }
        // Node with M children
        else if (currNode.getIsLeaf()) {
            assert true;
        }
    }

    public Node chooseLeaf(Node currNode, Record newRec) {

        currNode.setMbr(newRec);
        // Node is a children
        if (currNode.getIsLeaf()) {
            return currNode;
        }
        // Node has leaf children. Select the one with the
        // least overlap
        else if (currNode.getHasLeaf()) {
            double[] overlapChildren = new double[currNode.getSize()];
            // For each leaf_Children calculate overlap. We need to find the minimum

            for (int i = 0; i < currNode.getSize(); i++) {
                Node temp = new Node(dim, null);
                temp.setMbr(currNode.getChildren(i));
                temp.setMbr(newRec);
                for (int j = 0; j < currNode.getSize(); j++) {
                    if (i != j) {
                        overlapChildren[i] += calcRectangleOverlap(temp.getMbr(), currNode.getChildren(i).getMbr());
                    }
                }
            }

            // Find the minimum index(Child) of the list with the lowest overlap
            int minIndex = findMinIndex(overlapChildren);
            return chooseLeaf(currNode.getChildren(minIndex), newRec);
        }
        // Node has nodes as children. Select the one with the
        // lest overlap
        else {
            double[] areaOverlapChildren = new double[currNode.getSize()];
            for (int i = 0; i < currNode.getSize(); i++) {
                areaOverlapChildren[i] = 1.0;
                Node temp = new Node(dim, null);
                temp.setMbr(currNode.getChildren(i));
                temp.setMbr(newRec);
                areaOverlapChildren[i] = getArea(temp.getMbr()) - getArea(currNode.getMbr());
            }
            int minIndex = findMinIndex(areaOverlapChildren);
            return chooseLeaf(currNode.getChildren(minIndex), newRec);
        }
    }

    // Find the child node/leaf with the minimum overlap.
    // Return its index.
    public int findMinIndex(double[] overlapChildren) {
        double min = overlapChildren[0];
        int minIndex = 0;
        for (int i = 1; i < overlapChildren.length; i++) {
            if (overlapChildren[i] < min){
                min = overlapChildren[i];
                minIndex = i;
            }
        }
        return minIndex;
    }


    public double calcRectangleOverlap(double[][] mbr1, double[][] mbr2) {
        double overlap = 1;
        for (int axis = 0; axis < dim; axis++) {
            overlap *= Math.max(0, Math.min(mbr1[axis][1], mbr2[axis][1]) - Math.max(mbr1[axis][0], mbr2[axis][0]));
        }
        return overlap;
    }

    // Return a double list of [dims][2] where we save for each axis the min and the max.

    public double getArea(double[][] mbr) {
        double area = 1;
        for (int axis = 0; axis < dim; axis++) {
            area *= Math.abs(mbr[axis][0] - mbr[axis][1]);
        }
        return area;
    }

    // Method used to choose the axis with the minimum sum
    // of margin values to split
    private int ChooseSplitAxis(Node currnode, Record newRec){
        double [][] SMV = new double[dim][2];
        int min_axis;
        for (int i = 0; i < dim; i++){
            ArrayList<Record> sortedRecords = sortRecords(currnode, newRec, i);

            SMV[i][0] = sCalculate(sortedRecords);
            SMV[i][1] = i;
        }

        Arrays.sort(SMV, Comparator.comparingDouble(s -> s[0]));
        min_axis = (int) SMV[0][1];

        return min_axis;
    }

    private ArrayList<ArrayList<Record>> Calculate_Minimum_Distribution(Node currnode, Record NewRec, int axis){
        double area;
        double overlap;
        double minOverlap = Double.MAX_VALUE;
        double minArea = Double.MAX_VALUE;
        ArrayList<ArrayList<Record>> minDistr_Overlap = new ArrayList<>();
        ArrayList<ArrayList<Record>> minDistr_Area = new ArrayList<>();
        ArrayList<Record> sortedRecords = sortRecords(currnode,NewRec,axis);
        ArrayList<Record> group_a = new ArrayList<>();
        ArrayList<Record> group_b = new ArrayList<>();

        for (int k = 1;k < M - (2*m) + 2;k++){

            int i = 0;
            for(Record rec: sortedRecords){
                if(i < (m - 1) + k) {
                    group_a.add(rec);
                }
                else {
                    group_b.add(rec);
                }
                i++;
            }

            overlap = calcRectangleOverlap(mbrCalculate(group_a), mbrCalculate(group_b));
            area = getArea(mbrCalculate(group_a)) + getArea(mbrCalculate(group_b));

            if (overlap < minOverlap){
                minOverlap = overlap;
                minDistr_Overlap = new ArrayList<>();
                minDistr_Overlap.add(group_a);
                minDistr_Overlap.add(group_b);
            }

            if(area < minArea){
                minArea = area;
                minDistr_Area = new ArrayList<>();
                minDistr_Area.add(group_a);
                minDistr_Area.add(group_b);
            }

            group_a = new ArrayList<>();
            group_b = new ArrayList<>();

        }
        // Resovle with ties:
        // if there is no minimum overlap, choose
        // distribution with minimum area.
        if(minOverlap == 0){
            return minDistr_Area;
        }
        else {
            return minDistr_Overlap;
        }
    }


    // Method to calculate the sum of all Margin Values of
    // the 2 record groups.
    private double sCalculate(ArrayList<Record> sortedRecords){
        double sum_margin = 0;
        ArrayList<Record> group_a = new ArrayList<>();
        ArrayList<Record> group_b = new ArrayList<>();

        for (int k = 1;k < M - (2*m) + 2;k++){

            int i = 0;
            for(Record rec: sortedRecords){
                if(i < (m - 1) + k) {
                    group_a.add(rec);
                }
                else {
                    group_b.add(rec);
                }
                i++;
            }
            sum_margin+= marginCalculate(mbrCalculate(group_a)) + marginCalculate(mbrCalculate(group_b));
        }

        return sum_margin;
    }

    // Method to calculate MBR of Records
    private double[][] mbrCalculate(ArrayList<Record> records){
        double [][] mbr = new double[dim][dim];

        for(int i = 0;i < dim;i++){
            for(Record rec: records){
                for (int j = 0; j < dim; j++){
                    if (rec.getCords().get(i) > mbr[i][j] || mbr[i][j] == 0){
                        mbr[i][j] = rec.getCords().get(i);
                    }
                }
            }
        }
        return mbr;
    }

    // Method to calculate Margin Value of a given MBR
    private double marginCalculate(double[][] mbr){
        double margin = 0;
        for (int i = 0;i < dim;i++){
            for (int j = dim - 1;j > 0;j--){
                margin+= Math.abs(mbr[i][j-1] - mbr[i][j]);
            }
        }
        return margin;
    }


    // Method to sort the records accordingly depending on
    // the axis / dimension
    private ArrayList<Record> sortRecords(Node node, Record NewRec, int axis) {
        ArrayList<Record> sortedRecords = new ArrayList<>();

        for (int j = 0; j < M; j++) {
            sortedRecords.add(node.getRecords().get(j));
        }

        sortedRecords.add(NewRec);
        sortedRecords.sort(Comparator.comparingDouble(k -> k.getCords().get(axis)));

        return sortedRecords;
    }

}





