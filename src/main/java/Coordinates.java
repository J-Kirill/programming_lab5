public class Coordinates {
    private Double x;
    private float y;
    public Coordinates(Double x, Float y) throws InvalidData {
        this.x = x;
        this.y = y;
        checkForCorrectness();
    }
    public Coordinates() {
        this.x = 0.0;
        this.y = 0.0F;
    }
    public void checkForCorrectness() throws InvalidData {
        if (x>128){
            throw new InvalidData("x is out of limit");
        }
    }
    public Double getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    @Override
    public String toString() {
        return "{" +
                "\"x\": " + x +
                ", \"y\": " + y +
                '}';
    }
}
