package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

public class History extends Command {
    public History(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }

    @Override
    public void execute(CArgs args) {
        StringBuilder output = new StringBuilder();
        for (String line1 : this.history) {
            output.append(line1).append("\n");
        }
        System.out.print(output);
        history(args.group1(), this.history);
    }

    /**
     * Метод, реализующий сохранение истории команд.
     *
     * @param command Последняя команда.
     * @param history Deque объект с предыдущими командами.
     */
    static void history(String command, Deque<String> history) {
        history.removeLast();
        history.addFirst(command);
    }
}
