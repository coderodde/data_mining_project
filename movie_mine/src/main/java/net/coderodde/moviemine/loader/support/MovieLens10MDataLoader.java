package net.coderodde.moviemine.loader.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Loads the data from 10 million MovieLens package.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class MovieLens10MDataLoader extends AbstractDataLoader {

    /**
     * The name of the file containing movie descriptions.
     */
    private static final String MOVIE_DATA_FILE_NAME = "movies.dat";
    
    /**
     * The name of the file containing ratings descriptions.
     */
    private static final String RATING_DATA_FILE_NAME = "ratings.dat";
    
    /**
     * The file handle to the directory containing the M1 data.
     */
    private final File dataDirectoryFile;
    
    /**
     * Constructs this data loader.
     * 
     * @param file the file handle to directory containing data files.
     */
    public MovieLens10MDataLoader(final File file) {
        checkNotNull(file, "The file is null.");
        checkFileExists(file, "The file \"" + file.getAbsolutePath() + "\"" +
                              " does not exist.");
        checkFileIsDirectory(file, "The file \"" + file.getAbsolutePath() + 
                                   "\" is not a directory.");
        this.dataDirectoryFile = file;
    }
    
    /**
     * Loads the 10M MovieLens data package.
     * 
     * @return the database holding the data set.
     */
    @Override
    public DefaultDatabase load() {
        final RatingsAndUsers ratingsAndUsers = loadRatingAndUserList();
        return new DefaultDatabase(ratingsAndUsers.userList, 
                                   loadMovieList(),
                                   ratingsAndUsers.ratingList);
    }
    
    /**
     * Loads the list of movies from the data package.
     * 
     * @return the list of movies.
     */
    private List<Movie> loadMovieList() {
        final List<Movie> movieList = new ArrayList<>();
        final File file = new File(dataDirectoryFile.getAbsolutePath() +
                                   File.separator + MOVIE_DATA_FILE_NAME);
        checkFileExists(file, MOVIE_DATA_FILE_NAME + " does not exist.");
        checkFileIsRegular(file, 
                           MOVIE_DATA_FILE_NAME + " is not a regular file.");
        
        try {
            final Scanner scanner = new Scanner(new FileReader(file));
            
            while (scanner.hasNextLine()) {
                final String[] parts = scanner.nextLine().trim().split("::");
                final String[] genres = parts[2].split("\\|");
                final Movie movie = Movie.createMovie()
                                         .withMovieId(parts[0])
                                         .withTitle(parts[1])
                                         .withGenres(genres)
                                         .endGenres();
                movieList.add(movie);
            }
        } catch (final FileNotFoundException ex) {
            Logger.getLogger(MovieLens1MDataLoader.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
        
        return movieList;
    }
    
    private static class RatingsAndUsers {
        List<User> userList;
        List<Rating> ratingList;
    }
    
    /**
     * For performance, loads both rating and user lists.
     * 
     * @return rating and user list.
     */
    private RatingsAndUsers loadRatingAndUserList() {
        final Set<User> userSet = new HashSet<>();
        final List<Rating> ratingList = new ArrayList<>();
        
        final File file = new File(dataDirectoryFile.getAbsolutePath() +
                                   File.separator + RATING_DATA_FILE_NAME);
        checkFileExists(file, RATING_DATA_FILE_NAME + " does not exist.");
        checkFileIsRegular(file, 
                           RATING_DATA_FILE_NAME + " is not a regular file.");
        
        try {
            final Scanner scanner = new Scanner(new FileReader(file));
            
            while (scanner.hasNextLine()) {
                final String[] parts = scanner.nextLine().trim().split("::");
                final Rating rating = new Rating(parts[0],
                                                 parts[1],
                                                 Float.parseFloat(parts[2]),
                                                 Long.parseLong(parts[3]));
//                Too slow.
//                final Rating rating = Rating.createRating()
//                                      .withUserId(parts[0])
//                                      .withMovieId(parts[1])
//                                      .withScore(Float.parseFloat(parts[2]))
//                                      .withTimestamp(Long.parseLong(parts[3]));
                
                final User user = new User(parts[0], null, 0, null, null);
                                  
                ratingList.add(rating);
                userSet.add(user);
            }
        } catch (final FileNotFoundException ex) {
            Logger.getLogger(MovieLens1MDataLoader.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
        
        final RatingsAndUsers ret = new RatingsAndUsers();
        ret.userList = new ArrayList<>(userSet);
        ret.ratingList = ratingList;
        return ret;
    }    
}
