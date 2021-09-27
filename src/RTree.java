import java.util.*;

public class RTree {

    private int dim;
    private final ArrayList<Record> records;
    private Node root;
    private final int M = 4;
    private final int m = 2;

    public RTree(int dim, DataFile data) {
        this.dim = dim;
        records = data.getRecords();
        root = new Node(2, null);
        BuildRTree();
    }

    public void BuildRTree() {
        for(Record rec : records){
            Insert(rec, root);
        }


    }

    private void Insert(Record NewRec, Node currnode){
        Node selected_node = ChooseLeaf(NewRec, currnode);
        if (selected_node.getRecords().size() <= M){
            selected_node.getRecords().add(NewRec);
            selected_node.setMbr(NewRec);
        }
        else{
            SplitNode(currnode, NewRec);
        }
    }

    private void SplitNode(Node currnode, Record NewRec){
        int axis = ChooseSplitAxis(currnode, NewRec);
        ArrayList<ArrayList<Record>> min_distribution = new ArrayList<>();

        min_distribution = Calculate_Minimum_Distr(currnode,NewRec, axis);

    }

    // Method used to choose the axis with the minimum sum
    // of margin values to split
    private int ChooseSplitAxis(Node currnode, Record NewRec){
        double [][] SMV = new double[dim][2];
        int min_axis;
        for (int i = 0; i < dim; i++){
            ArrayList<Record> sortedRecords = sortRecords(currnode, NewRec, i);

            SMV[i][0] = S_Calculate(sortedRecords);
            SMV[i][1] = i;
        }

        Arrays.sort(SMV, Comparator.comparingDouble(s -> s[0]));
        min_axis = (int) SMV[0][1];

        return min_axis;
    }

    private ArrayList<ArrayList<Record>> Calculate_Minimum_Distr(Node currnode, Record NewRec, int axis){
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

        }
        // ------------------------------------------
        return minDistr_Overlap;
    }

    private Node ChooseLeaf(Record record, Node currnode){
        return currnode;
    }

    // Method to calculate the sum of all Margin Values of
    // the 2 record groups.
    private double S_Calculate(ArrayList<Record> sortedRecords){
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
            sum_margin+=Margin_Calculate(MBR_Calculate(group_a)) + Margin_Calculate(MBR_Calculate(group_b));
        }

        return sum_margin;
    }

    // Method to calculate MBR of Records
    private double[][] MBR_Calculate(ArrayList<Record> records){
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
    private double Margin_Calculate(double[][] mbr){
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





