package net.coderodde.moviemine.loader.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.coderodde.moviemine.loader.AbstractDataLoader;
import net.coderodde.moviemine.model.DefaultDatabase;
import net.coderodde.moviemine.model.Movie;
import net.coderodde.moviemine.model.Rating;
import net.coderodde.moviemine.model.User;
import static net.coderodde.util.Validation.checkFileExists;
import static net.coderodde.util.Validation.checkFileIsDirectory;
import static net.coderodde.util.Validation.checkFileIsRegular;
import static net.coderodde.util.Validation.checkNotNull;

/**
 * This class is responsible for extracting the movie database from 
 * MovieLens 1M - data package.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class MovieLens1MDataLoader extends AbstractDataLoader {

    /**
     * The name of the file containing user descriptions.
     */
    private static final String USER_DATA_FILE_NAME = "users.dat";
    
    /**
     * The name of the file containing movie descriptions.
     */
    private static final String MOVIE_DATA_FILE_NAME = "movies.dat";
    
    /**
     * The name of the file containing ratings descriptions.
     */
    private static final String RATING_DATA_FILE_NAME = "ratings.dat";
    
    /**
     * This map maps occupation numbers to the occupation names.
     */
    private static final Map<Integer, String> map = new HashMap<>();
    
    static {
        map.put(0, "other");
        map.put(1, "academic/educator");
        map.put(2, "artist");
        map.put(3, "clerical/admin");
        map.put(4, "college/grad student");
        map.put(5, "customer service");
        map.put(6, "doctor/health care");
        map.put(7, "executive/managerial");
        map.put(8, "farmer");
        map.put(9, "homemaker");
        map.put(10, "K-12 student");
        map.put(11, "lawyer");
        map.put(12, "programmer");
        map.put(13, "retired");
        map.put(14, "sales/marketing");
        map.put(15, "scientist");
        map.put(16, "self-employed");
        map.put(17, "technician/engineer");
        map.put(18, "tradesman/craftsman");
        map.put(19, "unemployed");
        map.put(20, "writer");
    }
    
    /**
     * The file handle to the directory containing the M1 data.
     */
    private final File file;
    
    /**
     * Constructs this data loader.
     * 
     * @param file the file handle to directory containing data files.
     */
    public MovieLens1MDataLoader(final File file) {
        checkNotNull(file, "The file is null.");
        checkFileExists(file, "The file \"" + file.getAbsolutePath() + "\"" +
                              " does not exist.");
        checkFileIsDirectory(file, "The file \"" + file.getAbsolutePath() + 
                                   "\" is not a directory.");
        this.file = file;
    }
    
    @Override
    public DefaultDatabase load() {
        final List<User> userList = loadUserList();
        
        if (userList == null) {
            return null;
        }
        
        final List<Movie> movieList = loadMovieList();
        
        if (movieList == null) {
            return null;
        }
        
        final List<Rating> ratingList = loadRatingList();
        
        if (ratingList == null) {
            return null;
        }
        
        return new DefaultDatabase(userList, movieList, ratingList);
    }
    
    private List<User> loadUserList() {
        final List<User> userList = new ArrayList<>();
        
        return userList;
    }
    
    private List<Movie> loadMovieList() {
        final List<Movie> movieList = new ArrayList<>();
        
        return movieList;
    }
    
    private List<Rating> loadRatingList() {
        final List<Rating> ratingList = new ArrayList<>();
        
        return ratingList;
    }
}
