import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.ZonedDateTime;
import java.util.Arrays;

public class Route implements Comparable<Route>{
    private static int globalId=1;
    private int id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Location from;
    private Location to;
    private float distance;
    public Route(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Location from, Location to, float distance) throws InvalidData {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        checkForCorrectness();
        globalId=Integer.max(globalId,id);
    }
    public Route(String name, Coordinates coordinates, Location from, Location to, float distance) throws InvalidData {
        this.id = globalId;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.from = from;
        this.to = to;
        this.distance = distance;
        checkForCorrectness();
        globalId++;
    }
    public Route(){
        this.id = globalId++;
        this.creationDate = ZonedDateTime.now();
    }
    public static void checkForCorrectness(Route route) throws InvalidData {
        if (route.getId()<1){
            throw new InvalidData("id is out of range. Something went really wrong");
        }
        if (route.getName()==null || route.getName().isEmpty()) {
            throw new InvalidData("name is empty");
        }
        if (route.getCoordinates()==null){
            throw new InvalidData("no coordinates");
        } else {
            route.getCoordinates().checkForCorrectness();
        }
        if (route.getFrom()==null){
            throw new InvalidData("no start location");
        } else {
            route.getFrom().checkForCorrectness();
        }
        if (route.getTo()==null){
            throw new InvalidData("no end location");
        } else {
            route.getTo().checkForCorrectness();
        }
        if (route.getDistance()<=1){
            throw new InvalidData("distance is too small or negative");
        }
    }
    public void checkForCorrectness() throws InvalidData {
        checkForCorrectness(this);
    }
    public void checkForCorrectness(int[] ids) throws InvalidData {
        checkForCorrectness();
        if (Arrays.stream(ids).anyMatch(id -> id == this.id)){
            throw new InvalidData("id already exists");
        }
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public float compareDistance(float distance) {
        return this.distance-distance;
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
        return "{\n\"id\": " +
                this.id +
                ",\n\"name\": " +
                "\""+ this.name + "\"" +
                ",\n\"coordinates\": " +
                this.coordinates.toString() +
                ",\n\"creationDate\": \"" +
                this.creationDate.toString() +
                "\",\n\"from\": " +
                this.from.toString() +
                ",\n\"to\": " +
                this.to.toString() +
                ",\n\"distance\": " +
                this.distance +
                "\n}";
    }
    public String toString(int tabs){
        return "\t".repeat(tabs) + "{\n" + "\t".repeat(tabs+1) + "\"id\": " +
                this.id +
                ",\n" + "\t".repeat(tabs+1) + "\"name\": " +
                "\""+ this.name + "\"" +
                ",\n" + "\t".repeat(tabs+1) + "\"coordinates\": " +
                this.coordinates.toString(tabs+1) +
                ",\n" + "\t".repeat(tabs+1) + "\"creationDate\": \"" +
                this.creationDate.toString() +
                "\",\n" + "\t".repeat(tabs+1) + "\"from\": " +
                this.from.toString(tabs+1) +
                ",\n" + "\t".repeat(tabs+1) + "\"to\": " +
                this.to.toString(tabs+1) +
                ",\n" + "\t".repeat(tabs+1) + "\"distance\": " +
                this.distance +
                "\n" + "\t".repeat(tabs) + "}";
    }
}
