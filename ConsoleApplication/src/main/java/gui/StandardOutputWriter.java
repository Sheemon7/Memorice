package gui;

/**
 * Created by sheemon on 19.3.16.
 */
public class StandardOutputWriter implements IStdOut {

    public void write(String string) {
        System.out.print(string);
    }

    public void writeLn(String string) {
        System.out.println(string);
    }
}
