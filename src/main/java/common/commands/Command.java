package common.commands;

import common.*;
import common.stored.Route;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс от которого наследуются все классы команд CLI, нужен для реализации CLI.
 */
public class Command {
    protected final Deque<String> history;
    protected final MetaHashSet<Route> collection;
    protected final String filename;

    public Command(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        this.collection = collection;
        this.filename = filename;
        this.history = history;
    }

    /**
     * Метод который каждая команда переопределяет для собственной реализации
     * @param args Record слов команды из терминала.
     * @throws InvalidData Данные некорректны.
     */
    public void execute(CArgs args) throws InvalidData{}

    /**
     * Метод, реализующий команды CLI.
     *
     * @param input Строка с командами для исполнения.
     */
    public void doCommands(String input) {
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
                chooseCommand(m);
            } catch (InvalidData e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Внутренний метод для выбора команды CLI.
     * @param m Matcher регулярного выражения.
     * @throws InvalidData Такой команды нет, аргументы некорректны и тд.
     */
    public void chooseCommand(Matcher m) throws InvalidData {
        if (m.find()) {
            switch (m.group(1)) {
                case "exit": {
                    new Exit(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "help": {
                    new Help(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "info": {
                    new Info(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "show": {
                    new Show(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "clear": {
                    new Clear(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "save": {
                    new Save(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "history": {
                    new History(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "add": {
                    new Add(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "update": {
                    new Update(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "remove_by_id": {
                    new RemoveById(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "execute_script": {
                    executionOfScript(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "add_if_max": {
                    new AddIfMax(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "remove_greater": {
                    new RemoveGreater(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "count_less_than_distance": {
                    new CountLessThanDistance(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "print_descending": {
                    new PrintDescending(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                case "print_field_descending_distance": {
                    new PrintFieldDescendingDistance(this.collection, this.filename, this.history).execute(new CArgs(m.group(1), m.group(2), m.group(3)));
                    break;
                }
                default: {
                    throw new InvalidData("The command does not exist");
                }
            }
        }
    }

    /**
     * Внутренний метод для реализации рекурсивного выполнения команд из файлов.
     * @param args Record слов команды из терминала.
     * @throws InvalidData Данные некорректны.
     */
    public void executionOfScript(CArgs args) throws InvalidData {
        new ExecuteScript(this.collection, this.filename, this.history, new ArrayList<String>(List.of(args.group2()))).execute(args);
    }

    public record CArgs(String group1, String group2, String group3) {
    }
}
