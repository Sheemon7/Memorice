package model.entities.entries;

/**
 * Created by sheemon on 18.3.16.
 */
public class DictionaryEntry extends GroupEntry {

    private final String key;

    public DictionaryEntry(String key, String value) {
        super(value);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
