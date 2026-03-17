import java.io.*;
import java.util.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class Main {
    public static void main(String[] args) throws Exception{
        String filename = args[0];
        HashSet<Route> collection = readFile(filename);
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        /*while (!input.toString().endsWith(";") || scanner.hasNextLine()) {
            input.append(scanner.nextLine());
        }
        String[] commands = input.toString().split(";");
        for (String command : commands) {

        }*/
        filename = filename.replace(".", "out.");
        writeFile(filename, collection);
    }
    public static HashSet<Route> readFile(String filename) throws Exception {
        StringBuilder jsonString = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                jsonString.append(scanner.nextLine());
            }
        }
        JsonMapper mapper = JsonMapper.builder()
                .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .build();
        List<Route> list = mapper.readValue(
                jsonString.toString(),
                new TypeReference<List<Route>>() {}
        );
        List<Route> checkedList = new ArrayList<>();
        for (Route route : list) {
            try{
                route.checkForCorrectness();
                checkedList.add(route);
            } catch (InvalidData e) {
                System.err.println(route.toString());
                System.err.println(e.getMessage());
            }
        }
        HashSet<Route> collection = new HashSet<>(checkedList);
        Main.show(collection);
        return collection;
    }
    public static void writeFile(String fileName, HashSet<Route> collection){
        String jsonString = Main.toString(collection);
        try{
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileName), 16384);
            stream.write(jsonString.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String toString(HashSet<Route> collection){
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (Route route : collection) {
            out.append("\n");
            out.append(route.toString(1));
            out.append(",");
        }
        out.deleteCharAt(out.length() - 1);
        out.append("\n]");
        return out.toString();
    }
    public static void show(HashSet<Route> collection){
        String out = Main.toString(collection);
        System.out.println(out);
    }
}