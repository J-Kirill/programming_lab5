package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class Clear extends Command {
    public Clear(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename,  history);
    }
    @Override
    public void execute(CArgs args) {
        this.collection.clear();
        history(args.group1(), this.history);
    }
}
