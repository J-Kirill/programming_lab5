package common;

import common.commands.Command;
import common.stored.Route;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.*;
import java.util.*;

import static common.MetaHashSet.*;

/**
 * Класс для управления чтением из файла и записью в файл.
 */
public class IOManager {
    /**
     * Метод, читающий файл по пути.
     * @param filename Путь файла.
     * @return Возвращает коллекцию.
     * @throws Exception Ошибка при чтении.
     */
    public static MetaHashSet<Route> readFile(String filename) throws Exception {
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
                new TypeReference<>() {
                }
        );
        List<Route> checkedList = new ArrayList<>();
        for (Route route : list) {
            try {
                route.checkForCorrectness(getIds(checkedList));
                checkedList.add(route);
            } catch (InvalidData e) {
                System.err.println(route.toString());
                System.err.println(e.getMessage());
            }
        }
        return new MetaHashSet<>(Route.class, checkedList);
    }
    /**
     * Метод, записывающий коллекцию в файл.
     *
     * @param fileName   Путь для записи.
     * @param collection Коллекция.
     */
    public static void writeFile(String fileName, MetaHashSet<Route> collection) {
        String jsonString = MetaHashSet.toString(collection);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileName), 16384);
            stream.write(jsonString.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Метод, запускающий CLI, изменяет коллекцию из файла.
     *
     * @param collection Коллекция.
     * @param filename   Путь для записи файла.
     * @throws IOException Ошибка ввода/вывода.
     */
    public static void startCLI(MetaHashSet<Route> collection, String filename) throws IOException {
        System.out.println("Type \"help;\" for a list of available commands");
        Deque<String> history = new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null"));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            StringBuilder input = new StringBuilder();
            String line;
            do {
                line = scanner.nextLine();
                input.append(line).append("\n");
            } while (!line.endsWith(";") || System.in.available() > 0);
            new Command(collection, filename, history).doCommands(input.toString());
        }
    }
}
