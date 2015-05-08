package net.coderodde.moviemine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.coderodde.associationanalysis.model.AbstractDatabase;

/**
 * This class implements the <i>movielens</i> database.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DefaultDatabase extends AbstractDatabase<User, Movie>{

//    private final Map<User, List<Rating>> mapUserToRatingList;
//    private final Map<Movie, List<Rating>> mapMovieToRatingList;
    private final Map<String, User> mapUserIdToUser;
    private final Map<String, Movie> mapMovieIdToMovie;
    private final Map<User, Set<Movie>> mainMap;
    
    /**
     * Constructs a new database with given users, movies and ratings.
     * 
     * @param userList   the list of users.
     * @param movieList  the list of movies.
     * @param ratingList the list of ratings.
     */
    public DefaultDatabase(final List<User> userList,
                           final List<Movie> movieList,
                           final List<Rating> ratingList) {
//        this.mapUserToRatingList = new HashMap<>(userList.size());
//        this.mapMovieToRatingList = new HashMap<>(movieList.size());
        
        this.mapUserIdToUser = new HashMap<>(userList.size());
        this.mapMovieIdToMovie = new HashMap<>(movieList.size());
        
        this.mainMap = new HashMap<>(userList.size());
        
        for (final User user : userList) {
            this.mapUserIdToUser.put(user.getId(), user);
        }
            
        for (final Movie movie : movieList) {
            this.mapMovieIdToMovie.put(movie.getId(), movie);
        }
        
        for (final Rating rating : ratingList) {
            final User user = this.mapUserIdToUser.get(rating.getUserId());
            final Movie movie = this.mapMovieIdToMovie.get(rating.getMovieId());
            
//            if (!this.mapUserToRatingList.containsKey(user)) {
//                this.mapUserToRatingList.put(user, new ArrayList<Rating>());
//            }
//            
//            if (!this.mapMovieToRatingList.containsKey(movie)) {
//                this.mapMovieToRatingList.put(movie, new ArrayList<Rating>());
//            }
//            
//            this.mapUserToRatingList.get(user).add(rating);
//            this.mapMovieToRatingList.get(movie).add(rating);
            
            if (!this.mainMap.containsKey(user)) {
                this.mainMap.put(user, new LinkedHashSet<Movie>());
            }
            
            this.mainMap.get(user).add(movie);
        }
    }
    
    /**
     * {@inheritDoc }
     * 
     * @param  predicates the array of predicates each transaction must satisfy.
     * @return the list of those transactions, that obey each predicate.
     */
    @Override
    public List<Set<Movie>> select(final Predicate<User>... predicates) {
        final List<Set<Movie>> ret = new ArrayList<>(mainMap.size());
        
        outer:
        for (final Map.Entry<User, Set<Movie>> entry : mainMap.entrySet()) {
            for (final Predicate<User> predicate : predicates) {
                if (!predicate.test(entry.getKey())) {
                    continue outer;
                }
            }
            
            ret.add(entry.getValue());
        }
        
        return ret;
    }

    /**
     * {@inheritDoc }
     * 
     * @return the amount of transactions in this database.
     */
    @Override
    public int size() {
        return mainMap.size();
    }
}
