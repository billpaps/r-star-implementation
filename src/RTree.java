import javax.xml.crypto.Data;
import java.util.ArrayList;

public class RTree {

    private int dim, M = 4, n = 2;
    private DataFile dataFile;
    private Node root;

    public RTree() {

    }

    public RTree(int dim, DataFile data) {
        this.dim = dim;
        this.dataFile = data;
        this.root = new Node(dim, null);
    }

    public void BuildRTree() {

    }


    public Node chooseLeaf(Node currNode, Record record) {

        double[] overlapChildren = new double[currNode.getSize()];
        // Node is a children
        if (currNode.getIsLeaf()) {
            return currNode;
        }
        // Node has leaf children. Select the one with the
        // least overlap
        else if (currNode.getHasLeaf()) {
            // For each leaf_Children calculate overlap. We need to find the minimum

            for (int i = 0; i < currNode.getSize(); i++) {
                overlapChildren[i] = calcRectangleOverlap(currNode.getChildren().get(i), record);
            }

            // Find the minimum index(Child) of the list with the lowest overlap
            int minIndex = findMinIndex(overlapChildren);
            return chooseLeaf(currNode.getChildren().get(minIndex), record);
        }
        // Node has mbr's as children. Select the one with the
        // lest overlap
        else
        {
            for (int i = 0; i < currNode.getSize(); i++) {
                overlapChildren[i] = calcRectangleOverlap(currNode.getChildren().get(i), record);
            }
            int minIndex = findMinIndex(overlapChildren);
            return chooseLeaf(currNode.getChildren().get(minIndex), record);

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


    public double calcRectangleOverlap(Node currNode, Record record) {
        double area = 1;
        double[][] axis_range = findAxisMinMax(currNode.getMbr());
        double distance;

        for (int axis = 0; axis < currNode.getDim(); axis++) {

            // Calculating the biggest distance between min and max. We dont need all the points
            // where points = dim. So if we have 5 dimentions we will have 5 points to define the mbr.
            // We have a function which finds the min and the max for each axis so we just need 2 calculations
            // instead of 5 (dim calcs).
            double axis_distance_min = Math.abs(axis_range[axis][0] - record.getCords().get(axis));
            double axis_distance_max = Math.abs(axis_range[axis][1] - record.getCords().get(axis));
            // Calculating the biggest distance for each axis.
            double real_axis_distance = Math.abs(axis_range[axis][1] - axis_range[axis][0]);

            distance = Math.max(Math.max(axis_distance_min, axis_distance_max), real_axis_distance);

            area *= distance;

            // Saving the range of each axis. We need it in order to calculate if
            // the record is inside the mbr node we are looking (currNode).
        }

        // // Check if the record is inside the currNode mbr.
        for (int axis = 0; axis < currNode.getDim(); axis++) {
            // Check if the record is inside the currNode mbr.
            if (record.getCords().get(axis) < axis_range[axis][0]
                    || record.getCords().get(axis) > axis_range[axis][1]) {
                return Math.abs(area - getArea(currNode.getMbr())) ;
            }
        }

        // The record belongs to the mbr of the currNode. So overlap = 0
        return 0.0;
    }

    // Return a double list of [dims][2] where we save for each axis the min and the max.
    public double[][] findAxisMinMax(double[][] mbr) {
        // Min - Max for every axis(dimension)
        double[][] glob_extreme = new double[mbr.length][2];
        for (int axis = 0; axis < mbr.length; axis++) {
            //Initialization
            double min = mbr[0][axis];
            double max = mbr[0][axis];
            // Finding the min/max value of the axis
            for (double[] doubles : mbr) {
                if (doubles[axis] > max) {
                    max = doubles[axis];
                } else if (doubles[axis] < min) {
                    min = doubles[axis];
                }
            }
            // Save min for the current axis
            glob_extreme[axis][0] = min;
            // Save max for the current axis
            glob_extreme[axis][1] = max;
        }
        return glob_extreme;
    }

    public double getArea(double[][] mbr) {
        double[][] axis_distance = findAxisMinMax(mbr);
        double area = 1;
        for (double[] axis: axis_distance) {
            area *= (axis[1] - axis[0]);
        }
        return area;
    }


    public void Insert(Record record) {
        assert true;
    }

}
