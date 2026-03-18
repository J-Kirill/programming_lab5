/**
 * Класс, хранящийся в объектах класса Route.
 */
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

    /**
     * Метод, проверяющий данные полей на корректность.
     * @throws InvalidData Данные некорректны.
     */
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
    /**
     * Метод, возвращающий json представление объекта в виде строки.
     * @return Строка - json.
     */
    @Override
    public String toString() {
        return "{" +
                "\"x\": " + x +
                ", \"y\": " + y +
                '}';
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
                "\n" + "\t".repeat(tabs) + "}";
    }
}
