package common.commands;

import common.InvalidData;
import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.MetaHashSet.getIds;
import static common.commands.History.history;
import static common.stored.Route.getObjectFromString;

public class Update extends Command {
    public Update(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename,  history);
    }
    @Override
    public void execute(CArgs args) throws InvalidData {
        Route newRoute = getObjectFromString(args.group3());
        for (Route route : this.collection) {
            if (route.getId() == Integer.parseInt(args.group2())) {
                this.collection.remove(route);
                newRoute.setId(route.getId());
                newRoute.checkForCorrectness(getIds(this.collection));
                this.collection.add(newRoute);
            }
        }
        history(args.group1(), this.history);
    }
}
