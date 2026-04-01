package common.commands;

import common.InvalidData;
import common.MetaHashSet;
import common.stored.Route;

import java.util.Arrays;
import java.util.Deque;

import static common.MetaHashSet.compareTo;
import static common.MetaHashSet.getIds;
import static common.commands.History.history;
import static common.stored.Route.getObjectFromString;

public class AddIfMax extends Command {
    public AddIfMax(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) throws InvalidData {
        Route route = getObjectFromString(args.group2());
        if (Arrays.stream(compareTo(this.collection, route)).filter(i -> i <= 0).findFirst().isEmpty()) {
            route.checkForCorrectness(getIds(this.collection));
            this.collection.add(route);
        }
        history(args.group1(), this.history);
    }
}
