package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.IOManager.writeFile;
import static common.commands.History.history;

public class Save extends Command {
    public Save(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename,  history);
    }
    @Override
    public void execute(CArgs args) {
        writeFile(this.filename, this.collection);
        history(args.group1(), this.history);
    }
}
