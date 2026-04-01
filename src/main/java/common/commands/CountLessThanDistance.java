package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class CountLessThanDistance extends Command {
    public CountLessThanDistance(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) {
        int count = 0;
        for (Route route1 : this.collection) {
            if (route1.compareDistance(Float.parseFloat(args.group2())) < 0) {
                count++;
            }
        }
        System.out.println(count);
        history(args.group1(), this.history);
    }
}
