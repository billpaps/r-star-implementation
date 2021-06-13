import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataFile {

    private ArrayList<Point> points;
    private ArrayList<Point> blockIndex;

    public DataFile(){
        points = new ArrayList<>();
        initialize();
    }

    private void initialize(){
        String file_path = "file/points.csv";
        String row;
        String[] data;
        Point new_point;

//      Storing Points in an ArrayList. Every element is the List is an Object.
        try {
            Scanner csvReader = new Scanner(new FileReader(file_path));
            while (csvReader.hasNextLine()) {
                row = csvReader.nextLine();
                data = row.split("\t");
                try{
                    new_point = new Point(data[0], data[1], data[2], data[3]);
                }
                catch (IndexOutOfBoundsException e){
                    new_point = new Point(data[0], data[1], data[2]);
                }
                points.add(new_point);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        write();
    }

    private void write(){
        int chunkSize = 0, counter = 0;
        int blockId = 1;
        blockIndex = new ArrayList<>();
        try{
            File datafile = new File("file/datafile.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }


        String temp = "\n\n";
//        Writing to datafile
        try{
            FileOutputStream writer = new FileOutputStream("file/datafile.txt");

            for (int i = 0; i < points.size(); i++) {
                if(chunkSize + points.get(i).get_size() <= 32768) {
                    //               Update chunksize
                    chunkSize += points.get(i).get_size();
                    //                Write point i in the block
                    writer.write(points.get(i).writeToFile());
                }
                else {
                    writer.write(temp.getBytes());
                    blockIndex.add(blockId - 1, points.get(counter));
                    counter = i;
                    chunkSize = 0;
                    blockId++;
                    i--;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Point> getBlockIndex() {
        return blockIndex;
    }

    // Returns arrayList of points in each block
    public ArrayList<Point> getBlockPoints(int ind){
        ArrayList<Point> blockPoints = new ArrayList<>();
        try{
            int counter = points.indexOf(blockIndex.get(ind - 1));
            int sum = 0;
            if (ind == blockIndex.size()){
                while (counter < points.size()){
                    blockPoints.add(points.get(counter));
                    sum ++;
                    counter ++;
                }
            }
            else {
                while ((points.get(counter) != points.get(points.indexOf(blockIndex.get(ind)))) && (counter < points.size())){
                    blockPoints.add(points.get(counter));
                    sum ++;
                    counter ++;
                }
            }
            System.out.println("Total number of points in block " + ind + ": " + sum);
            return blockPoints;
        }
        catch (IndexOutOfBoundsException ind_ex){
            System.out.println("Error! Index must be less or equal than " + blockIndex.size());
            System.out.println("You entered: " + ind);
            return null;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
