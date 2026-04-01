package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Comparator;
import java.util.Deque;

import static common.commands.History.history;

public class PrintDescending extends Command {
    public PrintDescending(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) {
        this.collection.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
        history(args.group1(), this.history);
    }
}
