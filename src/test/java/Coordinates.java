public class Coordinates {
    private Double x;
    private float y;
    public Coordinates(Double x, Float y) throws InvalidData {
        if (x>128){
            throw new InvalidData("x is out of limit");
        } else {
            this.x = x;
        }
        this.y = y;
    }
}
