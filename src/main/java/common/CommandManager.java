package common;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс от которого наследуются все классы команд CLI, нужен для реализации CLI.
 */
public class CommandManager {
    /**
     * Метод, реализующий команды CLI.
     *
     * @param command Команда для исполнения.
     */
    public static void doCommand(String command) {
        Pattern p = Pattern.compile(
                "([\\wА-Яа-яёЁ]+)" +
                        "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?" +
                        "(?:[ \\t\\n]+(-?\\d+(?:\\.\\d+)?|\"(?:[^\"\\\\]|\\\\.)*\"|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\}|\\w+))?",
                Pattern.DOTALL
        );
        String cleaned = command.stripTrailing();
        if (cleaned.endsWith(";")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }
        Matcher m = p.matcher(cleaned);
        try {
            if (m.find()) {
                CArgs args = new CArgs(m.group(1), m.group(2), m.group(3));
                Main.CommandList.get(args.group1()).execute(args);
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

    }

    public record CArgs(String group1, String group2, String group3) {
    }

    /**
     * Внутренний метод для реализации рекурсивного выполнения команд из файлов.
     *
     * @param args Record слов команды из терминала.
     * @throws InvalidData Данные некорректны.
     */
    /*
    public void executionOfScript(CArgs args) throws InvalidData {
        new ExecuteScript(this.collection, this.filename, this.history, new ArrayList<String>(List.of(args.group2()))).execute(args);
    }
     */
}
