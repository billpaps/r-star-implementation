import java.io.*;
import java.nio.Buffer;


public class Main {


    public static void main(String[] args){

        String file_path = "files/newmap.csv";
        String row;

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(file_path));
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split("  ");
//                System.out.print("ID: " + data[0] + " lat: " + data[1] + " lon: ");
                System.out.println(data[1]);

                System.out.println();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }



    }

}
