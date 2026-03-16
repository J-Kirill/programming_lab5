import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        StringBuilder jsonString = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(args[0]))) {
            while (scanner.hasNextLine()) {
                jsonString.append(scanner.nextLine());
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Route> list = mapper.readValue(
                jsonString.toString(),
                new TypeReference<List<Route>>() {}
        );
        HashSet<Route> collection = new HashSet<>(list);
        for (Route route : collection) {
            System.out.println(route.toString());
        }
    }
}