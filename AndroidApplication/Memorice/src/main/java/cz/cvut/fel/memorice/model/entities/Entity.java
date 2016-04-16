package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by sheemon on 18.3.16.
 */
public abstract class Entity implements Serializable, Iterable{


    private String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

//    public boolean setName(String name) {
//        try {
//            NameDatabase.getInstance().addName(this);
//            this.name = name;
//            return true;
//        } catch (NameAlreadyUsedException e) {
//            e.printStackTrace();
//        } catch (InvalidNameException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    public abstract EntityEnum getType();

    public abstract int size();

    public abstract Iterator iterator();

}
