package model.entities;

import model.database.NameDatabase;
import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;

import java.io.Serializable;

/**
 * Created by sheemon on 18.3.16.
 */
public abstract class Entity implements Serializable{


    private String name;


    private final EntityEnum type;

    public Entity(String name) {
        this.name = name;
        type = null;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        try {
            NameDatabase.addName(this);
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


    public EntityEnum getType() {
        return type;
    }

    public abstract int size();
}
