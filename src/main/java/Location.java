public class Location {
    private double x;
    private Float y;
    private Double z;
    private String name;
    public Location (double x, Float y, Double z, String name)  throws InvalidData{
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        checkForCorrectness();
    }
    public Location(){
        this.x = 0.0;
        this.y = 0.0F;
        this.z = 0.0;
        this.name = "";
    }
    public void checkForCorrectness() throws InvalidData {
        if (y==null){
            throw new InvalidData("y is null");
        }
        if (z==null){
            throw new InvalidData("z is null");
        }
        if (name==null || name.isEmpty()){
            throw new InvalidData("name is empty");
        }
    }
    public double getX() {
        return x;
    }
    public Float getY() {
        return y;
    }
    public Double getZ() {
        return z;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "{" +
                "\"x\": " + x +
                ", \"y\": " + y +
                ", \"z\": " + z +
                ", \"name\": " + "\""+ name + "\"}";
    }
}
