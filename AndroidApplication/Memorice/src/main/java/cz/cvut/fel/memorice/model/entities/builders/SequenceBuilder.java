package cz.cvut.fel.memorice.model.entities.builders;


import cz.cvut.fel.memorice.model.database.NameDatabase;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class SequenceBuilder extends Builder {

    @Override
    public Sequence create() {
        writer.writeLn("Insert name of the sequence");
        String line = reader.readLine().trim();
        Sequence s = new Sequence(line);
        try {
            NameDatabase.getInstance().addName(s);
        } catch (NameAlreadyUsedException | InvalidNameException e) {
            e.printStackTrace();
            return null;
        }
        writer.writeLn("Insert terms, each on its own line. They will be numbered in order you write them. Press Q to quit");
        SequenceEntry entry;
        int number = 1;
        while (!((line = reader.readLine().trim()).equals("Q"))) {
            entry = new SequenceEntry(line, number++);
            s.addEntry(entry);
        }
        return s;
    }
}
