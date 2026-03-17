import java.io.*;
import java.util.*;
import java.util.regex.*;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class Main {
    public static void main(String[] args) throws Exception{
        String filename = args[0];
        HashSet<Route> collection = readFile(filename);
        startCLI(collection, filename);
    }
    public static void startCLI(HashSet<Route> collection, String filename) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            StringBuilder input = new StringBuilder();
            String line;
            do {
                line = scanner.nextLine();
                input.append(line).append("\n");
            } while (!line.endsWith(";") || System.in.available() > 0);
            String[] commands = input.toString().split(";");
            Pattern p = Pattern.compile(
                    "(\\w+)" +
                            "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?" +
                            "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?",
                    Pattern.DOTALL
            );
            for (String command : commands) {
                String cleaned = input.toString().stripTrailing();
                if (cleaned.endsWith(";")) {
                    cleaned = cleaned.substring(0, cleaned.length() - 1);
                }
                Matcher m = p.matcher(cleaned);
                try {
                    if (m.find()) {
                        switch (m.group(1)) {
                            case "exit":
                                System.exit(0);
                                break;
                            case "help":
                                System.out.println("help : вывести справку по доступным командам\n" +
                                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                                        "add {element} : добавить новый элемент в коллекцию\n" +
                                        "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                                        "remove_by_id id : удалить элемент из коллекции по его id\n" +
                                        "clear : очистить коллекцию\n" +
                                        "save : сохранить коллекцию в файл\n" +
                                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                                        "exit : завершить программу (без сохранения в файл)\n" +
                                        "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                        "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                                        "history : вывести последние 5 команд (без их аргументов)\n" +
                                        "count_less_than_distance distance : вывести количество элементов, значение поля distance которых меньше заданного\n" +
                                        "print_descending : вывести элементы коллекции в порядке убывания\n" +
                                        "print_field_descending_distance : вывести значения поля distance всех элементов в порядке убывания");
                                break;
                            case "info":

                                break;
                            case "show":
                                show(collection);
                                break;
                            case "clear":
                                collection.clear();
                                break;
                            case "save":{
                                filename = filename.replace(".", "out.");
                                writeFile(filename, collection);}
                                break;
                            case "history":

                                break;
                            case "add":

                                break;
                            case "update":

                                break;
                            case "remove_by_id":

                                break;
                            case "execute_script":

                                break;
                            case "add_if_max":

                                break;
                            case "remove_greater":

                                break;
                            case "count_less_than_distance":

                                break;
                            case "print_descending":

                                break;
                            case "print_field_descending_distance":

                                break;
                            default:
                                throw new InvalidData("The command does not exists");
                        }
                    }
                } catch (InvalidData e) {
                    System.err.println(e.getMessage());
                }
            }
        }
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
        //Main.show(collection);
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
    @FunctionalInterface
    public static interface Command {
        void execute();
    }
}