import java.io.*;
import java.util.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class Main {
    public static void main(String[] args) throws Exception{
        String filename = args[0];
        StringBuilder jsonString = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(args[0]))) {
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
        for (Route route : collection) {
            System.out.println(route.toString());
        }
        filename = filename.replace(".", "out.");
        writeFile(filename, collection);
    }
    public static void writeFile(String fileName, HashSet<Route> collection){
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("[");
        for (Route route : collection) {
            jsonString.append(route.toString());
            jsonString.append(",");
        }
        jsonString.deleteCharAt(jsonString.length() - 1);
        jsonString.append("]");
        try{
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileName), 16384);
            stream.write(jsonString.toString().getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}