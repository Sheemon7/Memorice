package database;

import entries.Entry;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataWriter {

    void writeEntry(Entry entry);
    Entry readEntry(String name);
}
