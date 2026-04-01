package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class Exit extends Command {
    public Exit(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) {
        history(args.group1(), this.history);
        System.exit(0);
    }
}
