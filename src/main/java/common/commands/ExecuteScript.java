package common.commands;

import common.InvalidData;
import common.MetaHashSet;
import common.stored.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import static common.commands.History.history;

public class ExecuteScript extends Command {
    protected ArrayList<String> paths;
    public ExecuteScript(MetaHashSet<Route> collection, String filename, Deque<String> history, ArrayList<String> paths) {
        super(collection, filename, history);
        this.paths = paths;
    }
    @Override
    public void execute(CArgs args) throws InvalidData {
        StringBuilder input1 = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(args.group2().trim().replaceAll("^\"|\"$", "")))) {
            while (scanner.hasNextLine()) {
                input1.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new InvalidData(e.getMessage());
        }
        history(args.group1(), this.history);
        doCommands(input1.toString());
    }
    @Override
    public void executionOfScript(CArgs args) throws InvalidData {
        if (this.paths.contains(args.group2())) {
            throw new InvalidData("Цикл запрещён");
        }

        ArrayList<String> newPaths = (ArrayList<String>) this.paths.clone();
        newPaths.add(args.group2());
        new ExecuteScript(this.collection, this.filename, this.history, newPaths).execute(args);
    }
}
