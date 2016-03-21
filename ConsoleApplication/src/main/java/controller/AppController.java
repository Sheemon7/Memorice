package controller;

import com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_SAXEvent;
import controller.resources.Strings;
import gui.IStdIn;
import gui.IStdOut;
import gui.StandardInputReader;
import gui.StandardOutputWriter;
import model.database.FileDataWriter;
import model.database.NameDatabase;
import model.entities.Entity;
import model.entities.EntityEnum;
import model.entities.builders.*;
import model.help.Helper;

import java.util.logging.Logger;

/**
 * Created by sheemon on 19.3.16.
 */
public class AppController {

    private final static Logger LOGGER = Logger.getLogger(AppController.class.getName());

    private IStdOut writer = StandardOutputWriter.getInstance();
    private IStdIn reader = StandardInputReader.getInstance();

    public void runApplication() {
        LOGGER.info("Starting Application");
        LOGGER.info("Updating data");
        NameDatabase.getInstance().updateNames();
        writer.writeLn(Strings.WELCOME);
        mainMenu();
    }

    private void mainMenu() {
        switch (probe(Strings.PROBE, "Training", "My Sets", "Help", "Add new sets")) {
            case 1:
                invokeTraining();
                break;
            case 2:
                invokeMySets();
                break;
            case 3:
                invokeHelp();
                break;
            case 4:
                invokeAdding();
                break;
        }
    }

    public void exit() {
        LOGGER.info("Exiting...");
    }

    public int probe(String question, String... options) {
        writer.writeLn(question);
        for (int i = 0; i < options.length; i++) {
            writer.write(String.format(Strings.OPTION, i + 1, options[i]));
        }
        return reader.probe();
    }

    public void invokeTraining() {
        System.out.println("In the future releases, my app will include also an effective training of saved data. " +
                "This may change after examination of my possibilities within android app");
        probeExit();
    }

    public void invokeAdding() {
        Builder builder = null;
        EntityEnum type = null;
        switch (probe("Which type would you like to add?", "Dictionary", "Sequence", "Group")) {
            case 1:
                builder = new DictionaryBuilder();
                type = EntityEnum.values()[0];
                break;
            case 2:
                builder = new SequenceBuilder();
                type = EntityEnum.values()[1];
                break;
            case 3:
                builder = new GroupBuilder();
                type = EntityEnum.values()[2];
                break;
        }
        Entity e = builder.create();
        FileDataWriter writer = new FileDataWriter();
        writer.writeEntity(e, type);
        probeExit();
    }

    public void invokeHelp() {
        Helper helper = new Helper();
        helper.printHelp(writer);
        probeExit();
    }

    public void invokeMySets() {
        writer.writeLn("My sets:");
        for (String name : NameDatabase.getInstance().getNames()) {
            writer.writeLn(name);
        }
        probeExit();
    }

    private void probeExit() {
        switch (probe(Strings.PROBE, "Main menu", "Exit")) {
            case 1:
                mainMenu();
                break;
            case 2:
                exit();
        }
    }

}
