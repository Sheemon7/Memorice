package cz.cvut.fel.memorice.gui;

import java.util.logging.Logger;

/**
 * {@inheritDoc}
 *
 * @deprecated
 */
@Deprecated
public class StandardOutputWriter implements IStdOut {

    private static final Logger LOG = Logger.getLogger(StandardOutputWriter.class.getName());

    private static StandardOutputWriter singleton = new StandardOutputWriter();

    private StandardOutputWriter() {

    }

    /**
     * Returns an instance of {@link StandardOutputWriter}
     *
     * @return an instance of {@link StandardOutputWriter}
     */
    public static StandardOutputWriter getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    public void write(String string) {
        LOG.info(string);
    }

    /**
     * {@inheritDoc}
     */
    public void writeLn(String string) {
        LOG.info(string);
    }
}
