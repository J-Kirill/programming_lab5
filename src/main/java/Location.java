/**
 * Класс, хранящийся в объектах класса Route.
 */
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
    /**
     * Метод, проверяющий данные полей на корректность.
     * @throws InvalidData Данные некорректны.
     */
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
    /**
     * Метод, возвращающий json представление объекта в виде строки.
     * @return Строка - json.
     */
    @Override
    public String toString() {
        return "{" +
                "\"x\": " + x +
                ", \"y\": " + y +
                ", \"z\": " + z +
                ", \"name\": " + "\""+ name + "\"}";
    }
    /**
     * Метод, возвращающий json представление объекта в виде строки с табуляцией.
     * @param tabs Количество отступов.
     * @return Строка - json.
     */
    public String toString(int tabs) {
        return "{\n" +
                "\t".repeat(tabs+1) + "\"x\": " + x +
                ",\n" + "\t".repeat(tabs+1) + "\"y\": " + y +
                ",\n" + "\t".repeat(tabs+1) + "\"z\": " + z +
                ",\n" + "\t".repeat(tabs+1) + "\"name\": " + "\""+ name + "\"\n" +
                "\t".repeat(tabs) + "}";
    }
}
