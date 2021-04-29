import java.io.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args){

        String file_path = "file/points.csv";
        String row;
        String[] data;

        try {
            Scanner csvReader = new Scanner(new FileReader(file_path));
            while (csvReader.hasNextLine()) {
                row = csvReader.nextLine();
                data = row.split("\t");
//                System.out.print("ID: " + data[0] + " lat: " + data[1] + " lon: ");
                try{
                    System.out.println(data[3]);

                }
                catch (ArrayIndexOutOfBoundsException ex){
                    System.out.print("");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }



    }

}
