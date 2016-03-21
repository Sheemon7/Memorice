package model.entities.builders;

import gui.StandardInputReader;
import gui.StandardOutputWriter;
import model.entities.Entity;

/**
 * Created by sheemon on 21.3.16.
 */
public abstract class Builder {

    protected StandardInputReader reader = StandardInputReader.getInstance();
    protected StandardOutputWriter writer = StandardOutputWriter.getInstance();
    public abstract Entity create();

}
