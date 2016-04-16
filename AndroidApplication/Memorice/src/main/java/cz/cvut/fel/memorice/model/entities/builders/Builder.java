package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.gui.StandardInputReader;
import cz.cvut.fel.memorice.gui.StandardOutputWriter;
import cz.cvut.fel.memorice.model.entities.Entity;

/**
 * Created by sheemon on 21.3.16.
 */
public abstract class Builder {

    protected StandardInputReader reader = StandardInputReader.getInstance();
    protected StandardOutputWriter writer = StandardOutputWriter.getInstance();
    public abstract Entity create();

}
