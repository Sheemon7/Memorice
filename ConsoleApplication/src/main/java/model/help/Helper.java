package model.help;

import gui.IStdOut;

/**
 * Created by sheemon on 19.3.16.
 */
public class Helper {

    private String help = "HELP WILL BE THERE\n";

    public void printHelp(IStdOut printer) {
        printer.writeLn(help);
    }
}
