import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args){
        DataFile data = new DataFile();
//        IndexFile index = new IndexFile(data);

        List<Double> point = new ArrayList<>();
        point.add(3.0);
        point.add(3.0);
        Record rec = new Record("123321321", point);

        double[][] mbr_1 = new double[2][2];
        mbr_1[0][0] = 5;
        mbr_1[0][1] = 2;
        mbr_1[1][0] = 2;
        mbr_1[1][1] = 4;
        Node a1 = new Node(2, null);
        a1.setMbr(mbr_1);

        double[][] mbr_2 = new double[2][2];
        mbr_2[0][0] = 2;
        mbr_2[0][1] = 6;
        mbr_2[1][0] = 4;
        mbr_2[1][1] = 5;
        Node a2 = new Node(2, null);
        a2.setMbr(mbr_2);

        RTree theRTree = new RTree();
        double x1 = theRTree.calcRectangleOverlap(a1, rec);
        double x2 = theRTree.calcRectangleOverlap(a2, rec);

        System.out.println(x1);
        System.out.println(x2);
    }
}
