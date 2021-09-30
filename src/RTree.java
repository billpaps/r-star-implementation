import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class RTree {

    private final int dim;
    private final Node root;
    private ArrayList<Record> records;
    private final ArrayList<Record> block_Index;
    private final DataFile data;
    private final int M = 4;
    private final int m = 2;

    public RTree(int dim) {
        this.dim = dim;
        records = new ArrayList<>();
        data = new DataFile();
        block_Index = data.getBlockIndex();
        root = new Node(dim, null);
    }

    public void buildTree() {
        int block_ind;
        for (block_ind = 1; block_ind < block_Index.size(); block_ind ++){
            records = data.getBlockRecords(block_ind);
            for (Record rec : records) {
                Insert(root, rec);
            }
            records = new ArrayList<>();
        }
    }

    private void Insert(Node currNode, Record newRec) {
        Node selected_node = chooseLeaf(currNode, newRec);
        if (selected_node.getRecords().size() < M) {
            selected_node.getRecords().add(newRec);
            selected_node.setMbr(newRec);
        } else {
            // OverFlow treatment is called
            SplitNode(selected_node, newRec);
        }
    }
    // SplitNode method for Leaf nodes
    private void SplitNode(Node currNode, Record newRec) {
        int axis = ChooseSplitAxis(currNode, newRec);
        ArrayList<ArrayList<Record>> min_distribution = Calculate_Minimum_Distribution(currNode, newRec, axis);

        // Root
        if (currNode.getParent() == null) {
            currNode.setMbr(newRec);
            Node child_node1 = new Node(dim, currNode);
            Node child_node2 = new Node(dim, currNode);

            for (int i = 0; i < min_distribution.get(0).size(); i++) {
                Record rec = min_distribution.get(0).get(i);
                child_node1.getRecords().add(rec);
            }
            for (int i = 0; i < min_distribution.get(1).size(); i++) {
                Record rec = min_distribution.get(1).get(i);
                child_node2.getRecords().add(rec);
            }
            currNode.getChildren().add(child_node1);
            currNode.getChildren().add(child_node2);
            currNode.zeroRecords();
            currNode.setLeaf(false);
            System.out.println("root split: and we have: " + currNode.getChildren().size());

        }
        // Not root node
        else {
            if (currNode.getParent().getChildren().size() == M){
                System.out.println("1: parent filled with M entries");
                Node child = new Node(dim, null);

                currNode.zeroMbr();
                currNode.zeroRecords();
                for (Record recA : min_distribution.get(0)){
                    currNode.getRecords().add(recA);
                    currNode.setMbr(recA);
                }
                for (Record recB: min_distribution.get(1)){
                    child.getRecords().add(recB);
                    child.setMbr(recB);
                }
                child.setLeaf(false);
                SplitNode(currNode, child);
            }
            else{
                System.out.println("2: parent has space for more entries");
                currNode.getParent().setMbr(newRec);
                Node child = new Node(dim, currNode.getParent());

                currNode.zeroRecords();
                currNode.zeroMbr();
                for (Record recA: min_distribution.get(0)){
                    currNode.getRecords().add(recA);
                    currNode.setMbr(recA);
                }
                for (Record recB: min_distribution.get(1)){
                    child.getRecords().add(recB);
                    child.setMbr(recB);
                }
                child.getParent().getChildren().add(child);
            }
        }
//        System.out.println("==============================================================");
    }

    // SplitNode method for non-Leaf nodes
    private void SplitNode(Node currNode, Node newNode) {
        int axis = ChooseSplitAxis(currNode, newNode);
        ArrayList<ArrayList<Node>> min_distribution = Calculate_Minimum_Distribution(currNode, newNode, axis);

        System.out.println("=======NODE======");
        // Root
        if (currNode.getParent() == null) {
            Node child_node1 = new Node(dim, currNode);
            Node child_node2 = new Node(dim, currNode);

            for (int i = 0; i < min_distribution.get(0).size(); i++) {
                Node node = min_distribution.get(0).get(i);
                node.setParent(child_node1);
                child_node1.getChildren().add(node);
            }
            for (int i = 0; i < min_distribution.get(1).size(); i++) {
                Node node = min_distribution.get(1).get(i);
                node.setParent(child_node2);
                child_node2.getChildren().add(node);
            }

            currNode.zeroChildren();
            currNode.setLeaf(false);
            currNode.getChildren().add(child_node1);
            currNode.getChildren().add(child_node2);
            System.out.println("0: and we have: " + currNode.getChildren().size());
        }

        // Not root node
        else {
            if (currNode.getParent().getChildren().size() == M){
                System.out.println("1: parent filled with M entries");
                Node child = new Node(dim, null);

                currNode.zeroMbr();
                currNode.zeroChildren();
                for (Node nodeA : min_distribution.get(0)){
                    currNode.getChildren().add(nodeA);
                    currNode.setMbr(nodeA);
                }
                for (Node nodeB: min_distribution.get(1)){
                    child.getChildren().add(nodeB);
                    child.setMbr(nodeB);
                }
                child.setLeaf(false);
                SplitNode(currNode.getParent(), child);
            }
            else{
                System.out.println("Case 2: parent has space for more entries");
                currNode.getParent().setMbr(newNode);
                Node child = new Node(dim, currNode.getParent());

                currNode.zeroChildren();
                for (Node nodeA: min_distribution.get(0)){
                    currNode.getChildren().add(nodeA);
                    currNode.setMbr(nodeA);
                }
                for (Node nodeB: min_distribution.get(1)){
                    child.getChildren().add(nodeB);
                    child.setMbr(nodeB);
                }
                child.getParent().getChildren().add(child);
            }
        }
    }

    private Node chooseLeaf(Node currNode, Record newRec) {

        currNode.setMbr(newRec);
        // Node is a leaf
        if (currNode.getIsLeaf()) {
            return currNode;
        }
        // ============= TIES RESOLVING NOT IMPLEMENTED =============
        else {
            int minIndex;
            // Node has leaf children. Select the one with the
            // least overlap
            if (currNode.getChildren(0).getIsLeaf()) {

                double[] overlapChildren = new double[currNode.getChildren().size()];
                // For each leaf_Children calculate overlap. We need to find the minimum

                for (int i = 0; i < currNode.getChildren().size(); i++) {
                    Node temp = new Node(dim, currNode);
                    temp.setMbr(currNode.getChildren(i));
                    temp.setMbr(newRec);
                    for (int j = 0; j < currNode.getChildren().size(); j++) {
                        if (i != j) {
                            overlapChildren[i] += calcOverlap(temp.getMbr(), currNode.getChildren(i).getMbr());
                        }
                    }
                }

                // Find the minimum index(Child) of the list with the lowest overlap
                minIndex = findMinIndex(overlapChildren);
            }

            // Node has nodes as children. Select the one with the
            // lest overlap
            else {
                double[] areaOverlapChildren = new double[currNode.getChildren().size()];
                for (int i = 0; i < currNode.getChildren().size(); i++) {
                    areaOverlapChildren[i] = 1.0;
                    Node temp = new Node(dim, null);
                    temp.setMbr(currNode.getChildren(i));
                    temp.setMbr(newRec);
                    areaOverlapChildren[i] = getArea(temp.getMbr()) - getArea(currNode.getMbr());
                }
                minIndex = findMinIndex(areaOverlapChildren);

            }
            return chooseLeaf(currNode.getChildren(minIndex), newRec);
        }
    }

    // Method to choose the axis with the minimum sum
    // of margin values to split in leaf nodes
    private int ChooseSplitAxis(Node currNode, Record newRec){
        double [][] SMV = new double[dim][2];
        int min_axis;
        for (int i = 0; i < dim; i++){
            ArrayList<Record> sortedRecords = sortRecords(currNode, newRec, i);

            SMV[i][0] = sCalculate(sortedRecords);
            SMV[i][1] = i;
        }

        Arrays.sort(SMV, Comparator.comparingDouble(s -> s[0]));
        min_axis = (int) SMV[0][1];

        return min_axis;
    }

    // Method to choose the axis with the minimum sum
    // of margin values to split in non - leaf nodes
    private int ChooseSplitAxis(Node currNode, Node newNode){
        double [][] SMV = new double[dim][2];
        int min_axis;
        for (int i = 0; i < dim; i++){
            ArrayList<Node> sortedNodesMin = sortNodes(currNode, newNode, i, 0);
            ArrayList<Node> sortedNodesMax = sortNodes(currNode, newNode, i, 1);

            SMV[i][0] = sCalculate(sortedNodesMin) + sCalculate(sortedNodesMax);
            SMV[i][1] = i;
        }

        Arrays.sort(SMV, Comparator.comparingDouble(s -> s[0]));

        min_axis = (int) SMV[0][1];
        return min_axis;
    }

    private ArrayList<ArrayList<Record>> Calculate_Minimum_Distribution(Node currNode, Record NewRec, int axis){
        double area;
        double overlap;
        double minOverlap = Double.MAX_VALUE;
        double minArea = Double.MAX_VALUE;
        ArrayList<ArrayList<Record>> minDistr_Overlap = new ArrayList<>();
        ArrayList<ArrayList<Record>> minDistr_Area = new ArrayList<>();
        ArrayList<Record> sortedRecords = sortRecords(currNode,NewRec,axis);
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

            overlap = calcOverlap(mbrCalculate(group_a), mbrCalculate(group_b));
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

    private ArrayList<ArrayList<Node>> Calculate_Minimum_Distribution(Node currNode, Node newNode, int axis){
        double[] area = new double[2];
        double[] overlap = new double[2];
        double minOverlap = Double.MAX_VALUE;
        double minArea = Double.MAX_VALUE;
        ArrayList<ArrayList<Node>> minDistr_Overlap = new ArrayList<>();
        ArrayList<ArrayList<Node>> minDistr_Area = new ArrayList<>();
        ArrayList<Node> group_a = new ArrayList<>();
        ArrayList<Node> group_b = new ArrayList<>();

        for (int k = 1;k < M - (2*m) + 2;k++){

            for(int i = 0; i < 2; i++){
                ArrayList<Node> sortedNodes = sortNodes(currNode, newNode, axis, i);

                int j = 0;
                for(Node node: sortedNodes){
                    if(j < (m - 1) + k) {
                        group_a.add(node);
                    }
                    else {
                        group_b.add(node);
                    }
                    j++;
                }

                overlap[i] = calcOverlap(mbrCalculate(group_a), mbrCalculate(group_b));
                area[i] = getArea(mbrCalculate(group_a)) + getArea(mbrCalculate(group_b));

                if (overlap[i] < minOverlap){
                    minOverlap = overlap[i];
                    minDistr_Overlap = new ArrayList<>();
                    minDistr_Overlap.add(group_a);
                    minDistr_Overlap.add(group_b);
                }

                if(area[i] < minArea){
                    minArea = area[i];
                    minDistr_Area = new ArrayList<>();
                    minDistr_Area.add(group_a);
                    minDistr_Area.add(group_b);
                }

                group_a = new ArrayList<>();
                group_b = new ArrayList<>();
            }
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
    private double sCalculate(List<Record> sortedRecords){
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

    private double sCalculate(ArrayList<Node> sortedNodes){
        double sum_margin = 0;
        ArrayList<Node> group_a = new ArrayList<>();
        ArrayList<Node> group_b = new ArrayList<>();

        for (int k = 1;k < M - (2*m) + 2;k++){

            int i = 0;
            for(Node node: sortedNodes){
                if(i < (m - 1) + k) {
                    group_a.add(node);
                }
                else {
                    group_b.add(node);
                }
                i++;
            }
            sum_margin+= marginCalculate(mbrCalculate(group_a)) + marginCalculate(mbrCalculate(group_b));
        }

        return sum_margin;
    }

    // Method to calculate MBR of Records
    private double[][] mbrCalculate(List<Record> records){
        double [][] mbr = new double[dim][2];

        for(int i = 0;i < dim;i++){
            for(Record rec: records){
                if (rec.getCords().get(i) > mbr[i][1] || mbr[i][1] == 0){
                    mbr[i][1] = rec.getCords().get(i);
                }
                if (rec.getCords().get(i) < mbr[i][0] || mbr[i][0] == 0){
                    mbr[i][0] = rec.getCords().get(i);
                    }
                }
            }

        return mbr;
    }

    // Method to calculate MBR of Nodes
    private double[][] mbrCalculate(ArrayList<Node> nodes){
        double [][] mbr = new double[dim][2];

        for(int i = 0;i < dim;i++){
            for(Node node: nodes){
                if (node.getMbr()[i][1] > mbr[i][1] || mbr[i][1] == 0){
                    mbr[i][1] = node.getMbr()[i][1];
                }
                if (node.getMbr()[i][0] < mbr[i][0] || mbr[i][0] == 0){
                    mbr[i][0] = node.getMbr()[i][0];
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

    private ArrayList<Node> sortNodes(Node node, Node newNode, int axis, int mode) {

        ArrayList<Node> sortedNodes = new ArrayList<>(node.getChildren());

        sortedNodes.add(newNode);
        sortedNodes.sort(Comparator.comparingDouble(k -> k.getMbr()[axis][mode]));

        return sortedNodes;
    }

    // Find the child node/leaf with the minimum overlap.
    // Return its index.
    private int findMinIndex(double[] overlapChildren) {
        try {
            double min = overlapChildren[0];
            int minIndex = 0;
            for (int i = 1; i < overlapChildren.length; i++) {
                if (overlapChildren[i] < min) {
                    min = overlapChildren[i];
                    minIndex = i;
                }
            }
            return minIndex;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("EXCEPTION");
            return 0;
        }

    }

    private double calcOverlap(double[][] mbr1, double[][] mbr2) {
        double overlap = 1;
        for (int axis = 0; axis < dim; axis++) {
            overlap *= Math.max(0, Math.min(mbr1[axis][1], mbr2[axis][1]) - Math.max(mbr1[axis][0], mbr2[axis][0]));
        }
        return overlap;
    }

    // Return a double list of [dims][2] where we save for each axis the min and the max.
    private double getArea(double[][] mbr) {
        double area = 1;
        for (int axis = 0; axis < dim; axis++) {
            area *= Math.abs(mbr[axis][0] - mbr[axis][1]);
        }
        return area;
    }

    public Node getRoot() {
        return this.root;
    }

}





