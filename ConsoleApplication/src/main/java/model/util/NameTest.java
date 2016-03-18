package model.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sheemon on 18.3.16.
 */
public class NameTest {

    static Set<String> names = new HashSet<String>();
//    TODO FIle.exists()

    public static void addName(String name) throws NameAlreadyUsedException, InvalidNameException {
        if (names.contains(name)) {
            throw new NameAlreadyUsedException();
        }
        if (names.equals("")) {
            throw new InvalidNameException();
        }
        names.add(name);
    }
}
