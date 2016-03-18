package entities;

import entries.ListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheemon on 18.3.16.
 */
public class DataList extends Entity {

    private List<ListEntry> data = new ArrayList<ListEntry>();


    @Override
    public int size() {
        return data.size();
    }
}
