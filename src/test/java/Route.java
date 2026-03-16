import java.time.ZonedDateTime;
public class Route implements Comparable<Route>{
    private int id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Location from;
    private Location to;
    private float distance;
    public Route(int id, String name, Coordinates coordinates, Location from, Location to, float distance) throws InvalidData {
        if (id<1){
            throw new InvalidData("id is out of range. Something went really wrong");
        } else {
            this.id = id;
        }
        if (name==null || name.isEmpty()){
            throw new InvalidData("name is empty");
        }
        else {
            this.name = name;
        }
        if (coordinates==null){
            throw new InvalidData("no coordinates");
        }
        else {
            this.coordinates = coordinates;
        }
        this.creationDate = ZonedDateTime.now();
        if (from==null){
            throw new InvalidData("no start location");
        }
        else {
            this.from = from;
        }
        if (to==null){
            throw new InvalidData("no end location");
        }
        else {
            this.to = to;
        }
        if (distance<=1){
            throw new InvalidData("distance is too small or negative");
        } else {
            this.distance = distance;
        }
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    public Location getFrom() {
        return from;
    }
    public Location getTo() {
        return to;
    }
    public float getDistance() {
        return distance;
    }
    @Override
    public int compareTo(Route o) {
        return this.getId()-o.getId();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Route)) return false;
        Route other = (Route) obj;
        return this.id == other.id;
    }
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
    @Override
    public String toString() {
        return "{\"id\": " +
                this.id +
                ", \"name\": " +
                this.name +
                ", \"coordinates\": " +
                this.coordinates.toString() +
                ", \"creationDate\": " +
                this.creationDate.toString() +
                ", \"from\": " +
                this.from.toString() +
                ", \"to\": " +
                this.to.toString() +
                ", \"distance\": " +
                this.distance +
                "}";
    }
}
