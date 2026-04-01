package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class Info extends Command {
    public Info(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename,  history);
    }
    @Override
    public void execute(CArgs args) {
        System.out.println("type: " + this.collection.getType() +
                "\ninitialization_date: " + this.collection.getCreatedAt() +
                "\nsize: " + this.collection.size());
        history(args.group1(), this.history);
    }
}
