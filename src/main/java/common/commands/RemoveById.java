package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class RemoveById extends Command{
    public RemoveById(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename,  history);
    }
    @Override
    public void execute(CArgs args){
        this.collection.removeIf(route -> route.getId() == Integer.parseInt(args.group2()));
        history(args.group1(), this.history);
    }
}
