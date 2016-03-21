package controller;

import com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_SAXEvent;
import controller.resources.Strings;
import gui.IStdIn;
import gui.IStdOut;
import gui.StandardInputReader;
import gui.StandardOutputWriter;
import model.database.FileDataReader;
import model.database.FileDataWriter;
import model.database.NameDatabase;
import model.entities.Entity;
import model.entities.EntityEnum;
import model.entities.builders.*;
import model.entities.entries.DictionaryEntry;
import model.help.Helper;

import java.util.logging.Logger;

/**
 * Created by sheemon on 19.3.16.
 */
public class AppController {

    private final static Logger LOGGER = Logger.getLogger(AppController.class.getName());
    private final static NameDatabase database = NameDatabase.getInstance();

    private IStdOut writer = StandardOutputWriter.getInstance();
    private IStdIn reader = StandardInputReader.getInstance();

    public void runApplication() {
        LOGGER.info("Starting Application");
        LOGGER.info("Updating data");
        database.updateNames();
        writer.writeLn(Strings.WELCOME);
        mainMenu();
    }

    private void mainMenu() {
        switch (probe(Strings.PROBE, "Training", "My Sets", "Edit", "Help", "Add new sets")) {
            case 1:
                invokeTraining();
                break;
            case 2:
                invokeMySets();
                break;
            case 3:
                invokeModification();
                break;
            case 4:
                invokeHelp();
                break;
            case 5:
                invokeAdding();
                break;
        }
        database.updateNames();
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
        listMySets();
        mainMenu();
    }

    private void listMySets() {
        writer.writeLn("My sets:");
        int number = 1;
        for (String name :database.getTypes().keySet()) {
            writer.write(String.valueOf(number++) + "\t");
            writer.writeLn(name + ", " + database.getTypes().get(name).getName());
        }
    }

    public void invokeModification() {
        switch (probe("Would you like to remove or modify?", "remove", "modify")) {
            case 1:
                remove();
                break;
            case 2:
                modify();
                break;
        }
        probeExit();
    }

    private void remove() {
        listMySets();
        writer.writeLn("Which one would you like to remove? (name)");
        database.deleteName(reader.readLine());
    }

    private void modify() {
        listMySets();
        writer.writeLn("Which one would you like to modify? (name)");
        String name = reader.readLine();
        EntityEnum type = database.getType(name);
        Entity e = new FileDataReader().readEntity(name, type);
        modifyEntity(e);
    }

    private void modifyEntity(Entity entity) {
        switch (probe("What next?", "Read entity", "Delete entry", "Add entry", "Exit")) {
            case 1:
                writer.writeLn(entity.toString());
                modify();
                break;
            case 2:
                modify();
                break;
            case 3:
                modify();
                break;
            case 4:
                probeExit();
        }

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
