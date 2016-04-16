package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.model.database.NameDatabase;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.entries.GroupEntry;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

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
            try {
                g.addEntry(entry);
            } catch (TermAlreadyUsedException e) {
                e.printStackTrace();
                System.out.println("Term already used!");
            }
        }
        return g;
    }
}
