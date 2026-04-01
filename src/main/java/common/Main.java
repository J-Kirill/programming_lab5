package common;

import common.stored.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static common.IOManager.*;
import static common.MetaHashSet.*;
import static common.stored.Route.getObjectFromString;

/**
 * Класс, управляющий выполнением программы.
 */
public class Main {
    public static HashMap<String, MyFunction> CommandList;
    /**
     * Метод, запускающий выполнение программы, выбирая способ запуска по количеству аргументов, читает файл.
     *
     * @param args Аргументы командной строки. 1-й - путь до файла с данными, 2-й - путь куда сохранять, 3-й - путь до скрипта для работы без CLI
     * @throws Exception Возникла ошибка.
     */
    public static void main(String[] args) throws Exception {
        String filename = args[0];
        MetaHashSet<Route> collection = readFile(filename);
        Deque<String> history = new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null"));
        CommandList = new HashMap<String, MyFunction>();
        CommandList.put("help", (CommandManager.CArgs cArgs) -> {
                    System.out.println("""
                            help : вывести справку по доступным командам
                            info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                            show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                            add {element} : добавить новый элемент в коллекцию
                            update id {element} : обновить значение элемента коллекции, id которого равен заданному
                            remove_by_id id : удалить элемент из коллекции по его id
                            clear : очистить коллекцию
                            save : сохранить коллекцию в файл
                            execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                            exit : завершить программу (без сохранения в файл)
                            add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                            remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
                            history : вывести последние 5 команд (без их аргументов)
                            count_less_than_distance distance : вывести количество элементов, значение поля distance которых меньше заданного
                            print_descending : вывести элементы коллекции в порядке убывания
                            print_field_descending_distance : вывести значения поля distance всех элементов в порядке убывания""");
                    history(cArgs.group1(), history);
        });
        CommandList.put("exit", (CommandManager.CArgs cArgs) -> {
            history(cArgs.group1(), history);
            System.exit(0);
        });
        CommandList.put("info", (CommandManager.CArgs cArgs) -> {
            System.out.println("type: " + collection.getType() +
                    "\ninitialization_date: " + collection.getCreatedAt() +
                    "\nsize: " + collection.size());
            history(cArgs.group1(), history);
        });
        CommandList.put("show", (CommandManager.CArgs cArgs) -> {
            System.out.println(MetaHashSet.toString(collection));
            history(cArgs.group1(), history);
        });
        CommandList.put("clear", (CommandManager.CArgs cArgs) -> {
            collection.clear();
            history(cArgs.group1(), history);
        });
        CommandList.put("save", (CommandManager.CArgs cArgs) -> {
            writeFile(filename, collection);
            history(cArgs.group1(), history);
        });
        CommandList.put("history", (CommandManager.CArgs cArgs) -> {
            StringBuilder output = new StringBuilder();
            for (String line1 : history) {
                output.append(line1).append("\n");
            }
            System.out.print(output);
            history(cArgs.group1(), history);
        });
        CommandList.put("add", (CommandManager.CArgs cArgs) -> {
            try {
                addObject(cArgs.group2(), collection);
            }
            catch (Exception e){
                throw e;
            }
            history(cArgs.group1(), history);
        });
        CommandList.put("update", (CommandManager.CArgs cArgs) -> {
            try {
                Route newRoute = Route.getObjectFromString(cArgs.group3());
            for (Route route : collection) {
                if (route.getId() == Integer.parseInt(cArgs.group2())) {
                    collection.remove(route);
                    newRoute.setId(route.getId());
                    newRoute.checkForCorrectness(getIds(collection));
                    collection.add(newRoute);
                }
            }
            } catch (InvalidData e) {
                throw new RuntimeException(e);
            }
            history(cArgs.group1(), history);
        });
        CommandList.put("remove_by_id", (CommandManager.CArgs cArgs) -> {
            collection.removeIf(route -> route.getId() == Integer.parseInt(cArgs.group2()));
            history(cArgs.group1(), history);
        });
        CommandList.put("add_if_max", (CommandManager.CArgs cArgs) -> {
            try {
                Route route = getObjectFromString(cArgs.group2());
                if (Arrays.stream(compareTo(collection, route)).filter(i -> i <= 0).findFirst().isEmpty()) {
                    route.checkForCorrectness(getIds(collection));
                    collection.add(route);
                }
            } catch (InvalidData e) {
                throw new RuntimeException(e);
            }
            history(cArgs.group1(), history);
        });
        CommandList.put("remove_greater", (CommandManager.CArgs cArgs) -> {
            try {
                Route route = getObjectFromString(cArgs.group2());
                route.checkForCorrectness();
                collection.removeIf(route1 -> route.compareTo(route1) < 0);
            } catch (InvalidData e) {
                throw new RuntimeException(e);
            }
            history(cArgs.group1(), history);
        });
        CommandList.put("count_less_than_distance", (CommandManager.CArgs cArgs) -> {
            int count = 0;
            for (Route route1 : collection) {
                if (route1.compareDistance(Float.parseFloat(cArgs.group2())) < 0) {
                    count++;
                }
            }
            System.out.println(count);
            history(cArgs.group1(), history);
        });
        CommandList.put("print_descending", (CommandManager.CArgs cArgs) -> {
            collection.stream()
                    .sorted(Comparator.reverseOrder())
                    .forEach(System.out::println);
            history(cArgs.group1(), history);
        });
        CommandList.put("print_field_descending_distance", (CommandManager.CArgs cArgs) -> {
            collection.stream()
                    .map(Route::getDistance)
                    .sorted(Comparator.reverseOrder())
                    .forEach(System.out::println);
            history(cArgs.group1(), history);
        });

        CommandList.put("execute_script", (CommandManager.CArgs cArgs) -> {
            try (Scanner scanner = new Scanner(new File(cArgs.group2().trim().replaceAll("^\"|\"$", "")))) {
                while (scanner.hasNextLine()) {
                    history(cArgs.group1(), history);
                    CommandManager.doCommand(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });



        if (args.length == 1) {
            startCLI(collection, filename);
        } else if (args.length == 2) {
            startCLI(collection, args[1]);
        } /*
        else if (args.length == 3) {
            new ExecuteScript(collection, args[1], new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null")), new ArrayList<String>()).execute(new CommandManager.CArgs("execute_script", args[2], null));
        }
        */
        else {
            throw new Exception("Wrong number of arguments");
        }
    }
    /**
     * Метод, реализующий сохранение истории команд.
     *
     * @param command Последняя команда.
     * @param history Deque объект с предыдущими командами.
     */
    public static void history(String command, Deque<String> history) {
        history.removeLast();
        history.addFirst(command);
    }
    /*
    public void executionOfScript(CArgs args) throws InvalidData {
        if (this.paths.contains(args.group2())) {
            throw new InvalidData("Цикл запрещён");
        }

        ArrayList<String> newPaths = (ArrayList<String>) this.paths.clone();
        newPaths.add(args.group2());
        new ExecuteScript(this.collection, this.filename, this.history, newPaths).execute(args);
    }
     */
}