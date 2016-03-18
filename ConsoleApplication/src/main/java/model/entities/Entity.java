package model.entities;

import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;
import model.util.NameTest;

import java.io.Serializable;

/**
 * Created by sheemon on 18.3.16.
 */
public abstract class Entity implements Serializable{


    private String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        try {
            NameTest.addName(name);
            this.name = name;
            return true;
        } catch (NameAlreadyUsedException e) {
            e.printStackTrace();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    public abstract int size();
}
