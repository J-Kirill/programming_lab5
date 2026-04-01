package common;

import common.commands.Command;
import common.commands.ExecuteScript;
import common.stored.Route;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static common.IOManager.readFile;
import static common.IOManager.startCLI;

/**
 * Класс, управляющий выполнением программы.
 */
public class Main {
    /**
     * Метод, запускающий выполнение программы, выбирая способ запуска по количеству аргументов, читает файл.
     *
     * @param args Аргументы командной строки. 1-й - путь до файла с данными, 2-й - путь куда сохранять, 3-й - путь до скрипта для работы без CLI
     * @throws Exception Возникла ошибка.
     */
    public static void main(String[] args) throws Exception {
        String filename = args[0];
        MetaHashSet<Route> collection = readFile(filename);
        if (args.length == 1) {
            startCLI(collection, filename);
        } else if (args.length == 2) {
            startCLI(collection, args[1]);
        } else if (args.length == 3) {
            new ExecuteScript(collection, args[1], new ArrayDeque<>(List.of("Null", "Null", "Null", "Null", "Null")), new ArrayList<String>()).execute(new Command.CArgs("execute_script", args[2], null));
        } else {
            throw new Exception("Wrong number of arguments");
        }
    }

}