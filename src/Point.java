public class Point {

    private String id, lat, lon, name="";
    private int size;


    public Point(String id, String lat, String lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.size = this.id.length() + this.lat.length() + this.lon.length();
    }

    public Point(String id, String lat, String lon, String name) {
        this(id, lat, lon);
        this.name = name;
        this.size += this.name.length();
    }

    public byte[] writeToFile() {
        String point = this.id + " " + this.lat + " " + this.lon;

        if(!this.name.equals(""))
        {
            point += " " + this.name;
        }
        point += "\n";
        return point.getBytes();
    }

    public int get_size(){
        return this.size;
    }

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}
