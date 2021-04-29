public class Point {

    private long id;
    private int size;
    private double lat, lon;
    private String name = "";


    public Point(long id, double lat, double lon){
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.size = 24;
    }

    public Point(long id, double lat, double lon, String name) {
        this(id, lat, lon);
        this.name = name;
        this.size += name.getBytes().length;
    }

    public String toString() {
        return this.id + " " + this.lat + " " + this.lon + " " + this.name + "THE SIZE IS " + this.size;
    }


    }
