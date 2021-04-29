import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args){

        ArrayList<Point> points = new ArrayList<>();
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
                    new_point = new Point(Long.parseLong(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), data[3]);
                }
                catch (IndexOutOfBoundsException e){
                    new_point = new Point(Long.parseLong(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                }
                points.add(new_point);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(points.get(11941));

    }

}
