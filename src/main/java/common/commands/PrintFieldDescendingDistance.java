package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Comparator;
import java.util.Deque;

import static common.commands.History.history;

public class PrintFieldDescendingDistance extends Command {
    public PrintFieldDescendingDistance(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) {
        collection.stream()
                .map(Route::getDistance)
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
        history(args.group1(), history);
    }
}
