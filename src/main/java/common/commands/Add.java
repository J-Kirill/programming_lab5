package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.MetaHashSet.addObject;
import static common.commands.History.history;

public class Add extends Command{
    public Add(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args){
        addObject(args.group2(), this.collection);
        history(args.group1(), this.history);
    }
}
