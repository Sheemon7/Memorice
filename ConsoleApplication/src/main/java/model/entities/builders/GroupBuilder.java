package model.entities.builders;

import model.database.NameDatabase;
import model.entities.Group;
import model.entities.entries.GroupEntry;
import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class GroupBuilder extends Builder {

    @Override
    public Group create() {
        writer.writeLn("Insert name of the group");
        String line = reader.readLine().trim();
        Group g = new Group(line);
        try {
            NameDatabase.getInstance().addName(g);
        } catch (NameAlreadyUsedException | InvalidNameException e) {
            e.printStackTrace();
            return null;
        }
        writer.writeLn("Insert terms, each on its own line. Press Q to quit");
        GroupEntry entry;
        while (!((line = reader.readLine().trim()).equals("Q"))) {
            entry = new GroupEntry(line);
            g.addEntry(entry);
        }
        return g;
    }
}
