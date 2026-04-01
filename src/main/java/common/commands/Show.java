package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class Show extends Command{
    public Show(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    /**
     * Метод, отображающий коллекцию в терминал.
     */
    @Override
    public void execute(CArgs args) {
        System.out.println(MetaHashSet.toString(this.collection));
        history(args.group1(), this.history);
    }
}
