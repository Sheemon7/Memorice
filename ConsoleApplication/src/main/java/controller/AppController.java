package controller;

import controller.resources.Strings;
import gui.IStdIn;
import gui.IStdOut;
import gui.StandardInputReader;
import gui.StandardOutputWriter;
import model.help.Helper;

import java.util.logging.Logger;

/**
 * Created by sheemon on 19.3.16.
 */
public class AppController {

    private final static Logger LOGGER = Logger.getLogger(AppController.class.getName());

    private IStdOut writer = new StandardOutputWriter();
    private IStdIn reader = new StandardInputReader();

    public void runApplication() {
        LOGGER.info("Starting Application");
        writer.writeLn(Strings.WELCOME);
        mainMenu();
    }

    private void mainMenu() {
        switch (probe("Training", "My Sets", "Help", "Add new sets")) {
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

    public int probe(String... options) {
        writer.writeLn(Strings.PROBE);
        for (int i = 0; i < options.length; i++) {
            writer.write(String.format(Strings.OPTION, i + 1, options[i]));
        }
        return reader.probe();
    }

    public void invokeTraining() {

    }

    public void invokeAdding() {

    }

    public void invokeHelp() {
        Helper helper = new Helper();
        helper.printHelp(writer);
        switch (probe("Main menu", "Exit")) {
            case 1:
                mainMenu();
                break;
            case 2:
                exit();
        }
    }

    public void invokeMySets() {

    }
}
