import java.util.ArrayList;
import java.util.List;

public class Record {

    private final String id;
    private String name;
    private final List<Double> coords;
    private int size;


    public Record(String id, List<Double> coords) {
        this.id = id;
        this.coords = coords;
        this.size = this.id.length() + coords.size()*8;
    }

    public Record(String id, List<Double> coords, String name) {
        this(id, coords);
        this.name = name;
        this.size += name.length();
    }

    public byte[] writeToFile() {
        String record = this.id + " " + this.coords.get(0) + " " + this.coords.get(1);

        if(!this.name.equals(""))
        {
            record += " " + this.name;
        }
        record += "\n";
        return record.getBytes();
    }

    public int get_size(){
        return this.size;
    }

    public String getId() {
        return id;
    }

    public List<Double> getCords() { return this.coords; }

    public String getName() {
        return name;
    }
}
