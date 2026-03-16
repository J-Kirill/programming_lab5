public class Location {
    private double x;
    private Float y;
    private Double z;
    private String name;
    public Location (double x, Float y, Double z, String name)  throws InvalidData{
        this.x = x;
        if (y==null){
            throw new InvalidData("y is null");
        }
        else {
            this.y = y;
        }
        if (z==null){
            throw new InvalidData("z is null");
        }
        else {
            this.z = z;
        }
        if (name==null || name.isEmpty()){
            throw new InvalidData("name is empty");
        }
        else {
            this.name = name;
        }
    }
}
