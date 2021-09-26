import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;


public class DataFile {

    private ArrayList<Record> records;
    private ArrayList<Record> blockIndex;

    public DataFile(){
        records = new ArrayList<>();
        initialize();
    }

    private void initialize(){
        String file_path = "file/points.csv";
        String row;
        String[] data;
        Record new_record;

//      Storing Points in an ArrayList. Every element is the List is an Object.
        try {
            Scanner csvReader = new Scanner(new FileReader(file_path));
            while (csvReader.hasNextLine()) {
                row = csvReader.nextLine();
                data = row.split("\t");

                List<Double> coords = Arrays.asList(Double.parseDouble(data[0]),
                                                    Double.parseDouble(data[1]));
                try{
                    new_record = new Record(data[0], coords, data[3]);
                }
                catch (IndexOutOfBoundsException e){
                    new_record = new Record(data[0], coords, data[2]);
                }
                records.add(new_record);

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

            for (int i = 0; i < records.size(); i++) {
                if(chunkSize + records.get(i).get_size() <= 32768) {
                    // Update chunksize
                    chunkSize += records.get(i).get_size();
                    // Write point i in the block
                    writer.write(records.get(i).writeToFile());
                }
                else {
                    writer.write(temp.getBytes());
                    blockIndex.add(blockId - 1, records.get(counter));
                    counter = i;
                    chunkSize = 0;
                    blockId++;
                    i--;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(blockId);
    }

    public ArrayList<Record> getPoints() {
        return records;
    }

    public ArrayList<Record> getBlockIndex() {
        return blockIndex;
    }

    // Returns arrayList of records in each block
    public ArrayList<Record> getBlockRecords(int ind){
        ArrayList<Record> blockRecords = new ArrayList<>();
        try{
            int counter = records.indexOf(blockIndex.get(ind - 1));
            int sum = 0;
            if (ind == blockIndex.size()){
                while (counter < records.size()){
                    blockRecords.add(records.get(counter));
                    sum ++;
                    counter ++;
                }
            }

            else {
                while ((records.get(counter) != records.get(records.indexOf(blockIndex.get(ind)))) && (counter < records.size())){
                    blockRecords.add(records.get(counter));
                    sum ++;
                    counter ++;
                }
            }
            System.out.println("Total number of points in block " + ind + ": " + sum);
            return blockRecords;
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
