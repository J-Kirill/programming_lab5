package common.commands;

import common.InvalidData;
import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;
import static common.stored.Route.getObjectFromString;

public class RemoveGreater extends Command {
    public RemoveGreater(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) throws InvalidData {
        Route route = getObjectFromString(args.group2());
        route.checkForCorrectness();
        this.collection.removeIf(route1 -> route.compareTo(route1) < 0);
        history(args.group1(), this.history);
    }
}
