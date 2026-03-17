import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.*;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = args[0];
        MetaHashSet<Route> collection = readFile(filename);
        if (args.length == 1) {
            startCLI(collection, filename);
        } else if (args.length == 2) {
            doCommands("execute_script "+"\""+args[1]+"\"", collection, filename, new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null")));
        } else {
            throw new Exception("Wrong number of arguments");
        }
    }

    public static void startCLI(MetaHashSet<Route> collection, String filename) throws IOException {
        System.out.println("Type \"help\" for a list of available commands");
        Scanner scanner = new Scanner(System.in);
        Deque<String> history = new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null"));
        while (true) {
            StringBuilder input = new StringBuilder();
            String line;
            do {
                line = scanner.nextLine();
                input.append(line).append("\n");
            } while (!line.endsWith(";") || System.in.available() > 0);
            doCommands(input.toString(), collection, filename, history);
        }
    }

    public static void doCommands(String input, MetaHashSet<Route> collection, String filename, Deque<String> history) {
        String[] commands = input.split(";");
        Pattern p = Pattern.compile(
                "([\\wА-Яа-яёЁ]+)" +
                        "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?" +
                        "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?",
                Pattern.DOTALL
        );
        for (String command : commands) {
            String cleaned = command.stripTrailing();
            if (cleaned.endsWith(";")) {
                cleaned = cleaned.substring(0, cleaned.length() - 1);
            }
            Matcher m = p.matcher(cleaned);
            try {
                if (m.find()) {
                    switch (m.group(1)) {
                        case "exit": {
                            Main.history(m.group(1), history);
                            System.exit(0);
                            break;
                        }
                        case "help": {
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
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "info": {
                            System.out.println("type: " + collection.getType() +
                                    "\ninitialization_date: " + collection.getCreatedAt() +
                                    "\nsize: " + collection.size());
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "show": {
                            show(collection);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "clear": {
                            collection.clear();
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "save": {
                            writeFile(filename.replace(".", "out."), collection);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "history": {
                            StringBuilder output = new StringBuilder();
                            for (String line1 : history) {
                                output.append(line1).append("\n");
                            }
                            System.out.print(output);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "add": {
                            addObject(m.group(2), collection);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "update": {
                            Route newRoute = getObjectFromString(m.group(3));
                            for (Route route : collection) {
                                if (route.getId() == Integer.parseInt(m.group(2))) {
                                    collection.remove(route);
                                    newRoute.setId(route.getId());
                                    newRoute.checkForCorrectness(getIds(collection));
                                    collection.add(newRoute);
                                }
                            }
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "remove_by_id": {
                            collection.removeIf(route -> route.getId() == Integer.parseInt(m.group(2)));
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "execute_script": {
                            StringBuilder input1 = new StringBuilder();
                            try (Scanner scanner = new Scanner(new File(m.group(2).trim().replaceAll("^\"|\"$", "")))) {
                                while (scanner.hasNextLine()) {
                                    String line = scanner.nextLine();
                                    input1.append(line).append("\n");
                                }
                            } catch (FileNotFoundException e) {
                                throw new InvalidData(e.getMessage());
                            }
                            doCommands(input1.toString(), collection, m.group(2), history);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "add_if_max": {
                            Route route = getObjectFromString(m.group(2));
                            if (Arrays.stream(compareTo(collection, route)).filter(i -> i <= 0).findFirst().isEmpty()) {
                                route.checkForCorrectness(getIds(collection));
                                collection.add(route);
                            }
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "remove_greater": {
                            Route route = getObjectFromString(m.group(2));
                            route.checkForCorrectness();
                            collection.removeIf(route1 -> route.compareTo(route1) < 0);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "count_less_than_distance": {
                            int count = 0;
                            for (Route route1 : collection) {
                                if (route1.compareDistance(Float.parseFloat(m.group(2))) < 0) {
                                    count++;
                                }
                            }
                            System.out.println(count);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "print_descending": {
                            collection.stream()
                                    .sorted(Comparator.reverseOrder())
                                    .forEach(System.out::println);
                            Main.history(m.group(1), history);
                            break;
                        }
                        case "print_field_descending_distance": {
                            collection.stream()
                                    .map(Route::getDistance)
                                    .sorted(Comparator.reverseOrder())
                                    .forEach(System.out::println);
                            Main.history(m.group(1), history);
                            break;
                        }
                        default: {
                            throw new InvalidData("The command does not exist");
                        }
                    }
                }
            } catch (InvalidData e) {
                System.err.println(e.getMessage());
            }
        }
    }

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

    public static int[] compareTo(Collection<Route> collection, Route route) {
        int[] vals = new int[collection.size()];
        int i = 0;
        for (Route route1 : collection) {
            vals[i++] = route.compareTo(route1);
        }
        return vals;
    }

    public static int[] getIds(Collection<Route> collection) {
        int[] ids = new int[collection.size()];
        int i = 0;
        for (Route route : collection) {
            ids[i++] = route.getId();
        }
        return ids;
    }

    public static Route getObjectFromString(String jsonObj) throws InvalidData {
        JsonMapper mapper = JsonMapper.builder()
                .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .build();
        Route route = mapper.readValue(jsonObj, new TypeReference<>() {
        });
        route.checkForCorrectness();
        return route;
    }

    public static void addObject(String jsonObj, MetaHashSet<Route> collection) {
        try {
            Route route = getObjectFromString(jsonObj);
            route.checkForCorrectness(getIds(collection));
            collection.add(route);
        } catch (InvalidData e) {
            System.err.println(e.getMessage());
        }
    }

    public static void writeFile(String fileName, MetaHashSet<Route> collection) {
        String jsonString = Main.toString(collection);
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

    public static String toString(MetaHashSet<Route> collection) {
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

    public static void show(MetaHashSet<Route> collection) {
        String out = Main.toString(collection);
        System.out.println(out);
    }

    public static void history(String command, Deque<String> history) {
        history.removeLast();
        history.addFirst(command);
    }

    public static class MetaHashSet<E> implements Set<E> {
        private final HashSet<E> delegate;
        private final Class<E> type;
        private final ZonedDateTime createdAt;

        public MetaHashSet(Class<E> type) {
            this.delegate = new HashSet<>();
            this.type = type;
            this.createdAt = ZonedDateTime.now();
        }

        public MetaHashSet(Class<E> type, int initialCapacity) {
            this.delegate = new HashSet<>(initialCapacity);
            this.type = type;
            this.createdAt = ZonedDateTime.now();
        }

        public MetaHashSet(Class<E> type, Collection<? extends E> c) {
            this.delegate = new HashSet<>(c);
            this.type = type;
            this.createdAt = ZonedDateTime.now();
        }

        public Class<E> getType() {
            return type;
        }

        public ZonedDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public boolean add(E e) {
            return delegate.add(e);
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        @Override
        public boolean remove(Object o) {
            return delegate.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return delegate.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return delegate.removeAll(c);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public Iterator<E> iterator() {
            return delegate.iterator();
        }

        @Override
        public Object[] toArray() {
            return delegate.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return delegate.toArray(a);
        }
    }
}