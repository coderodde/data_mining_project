package net.coderodde.moviemine.loader;

import net.coderodde.moviemine.model.DefaultDatabase;

/**
 * This abstract class defines the API for all classes loading the MovieLens 
 * data.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public abstract class AbstractDataLoader {
    
    /**
     * Loads the data and constructs a default database from it.
     * 
     * @return the database.
     */
    public abstract DefaultDatabase load();
}
