package model.entities.builders;

import model.database.NameDatabase;
import model.entities.Dictionary;
import model.entities.entries.DictionaryEntry;
import model.entities.entries.GroupEntry;
import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;
import model.util.TermAlreadyUsedException;

import java.util.StringTokenizer;

/**
 * Created by sheemon on 21.3.16.
 */
public class DictionaryBuilder extends Builder {

    @Override
    public Dictionary create() {
        writer.writeLn("Insert name of the dictionary");
        String line = reader.readLine().trim();
        Dictionary d = new Dictionary(line);
        try {
            NameDatabase.getInstance().addName(d);
        } catch (NameAlreadyUsedException | InvalidNameException e) {
            e.printStackTrace();
            return null;
        }
        writer.writeLn("Insert couples of terms and definitions, each on its own line. Press Q to quit");
        DictionaryEntry entry;
        String definition, term;
        while (!((line = reader.readLine().trim()).equals("Q"))) {
            definition = line;
            term = reader.readLine();
            entry = new DictionaryEntry(definition, term);
            try {
                d.addEntry(entry);
            } catch (TermAlreadyUsedException e) {
                e.printStackTrace();
                writer.writeLn("Term already used!");
            }
        }
        return d;
    }
}
