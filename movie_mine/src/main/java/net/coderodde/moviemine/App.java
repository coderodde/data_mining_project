package net.coderodde.moviemine;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.associationanalysis.model.AbstractDatabase;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.moviemine.model.DefaultDatabase;
import net.coderodde.moviemine.model.Movie;
import net.coderodde.moviemine.model.Rating;
import net.coderodde.moviemine.model.User;

/**
 * This class defines the entry point into a data mining program.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class App {
    
    /**
     * The entry point into the program.
     * 
     * @param args 
     */
    public static void main(final String... args) {
        final List<User> userList = new ArrayList<>();
        final List<Movie> movieList = new ArrayList<>();
        final List<Rating> ratingList = new ArrayList<>();
        
        final AbstractDatabase<User, Movie> db = 
                new DefaultDatabase(userList,
                                    movieList,
                                    ratingList);
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                null;
    }
}
