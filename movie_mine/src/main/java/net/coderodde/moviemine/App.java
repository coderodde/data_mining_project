package net.coderodde.moviemine;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.associationanalysis.model.AbstractDatabase;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.support.AprioriFrequentItemsetGenerator;
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
        
        userList.add(User.createUser()
                        .withId("1")
                        .asFemale()
                        .withAge(22)
                        .withOccupation("nurse")
                        .withZipCode("00520"));
        
        userList.add(User.createUser()
                        .withId("2")
                        .asMale()
                        .withAge(26)
                        .withOccupation("coder")
                        .withZipCode("00520"));
        
        userList.add(User.createUser()
                        .withId("3")
                        .asFemale()
                        .withAge(32)
                        .withOccupation("dj")
                        .withZipCode("00100"));
        
        userList.add(User.createUser()
                        .withId("4")
                        .asMale()
                        .withAge(12)
                        .withOccupation("pupil")
                        .withZipCode("00200"));
        
        userList.add(User.createUser()
                        .withId("5")
                        .asFemale()
                        .withAge(18)
                        .withOccupation("mc")
                        .withZipCode("00430"));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("10")
                        .withTitle("Bread")
                        .withGenre("food")
                        .endGenres());
        
        movieList.add(Movie.createMovie()
                        .withMovieId("11")
                        .withTitle("Milk")
                        .withGenre("food")
                        .endGenres());
        
        movieList.add(Movie.createMovie()
                        .withMovieId("12")
                        .withTitle("Diapers")
                        .withGenre("food")
                        .endGenres());
        
        movieList.add(Movie.createMovie()
                        .withMovieId("13")
                        .withTitle("Beer")
                        .withGenre("food")
                        .endGenres());
        
        movieList.add(Movie.createMovie()
                        .withMovieId("14")
                        .withTitle("Eggs")
                        .withGenre("food")
                        .endGenres());
        
        movieList.add(Movie.createMovie()
                        .withMovieId("15")
                        .withTitle("Cola")
                        .withGenre("food")
                        .endGenres());
        
        // 1
        ratingList.add(Rating.createRating()
                        .withUserId("1")
                        .withMovieId("10")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("1")
                        .withMovieId("11")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        // 2
        ratingList.add(Rating.createRating()
                        .withUserId("2")
                        .withMovieId("10")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("2")
                        .withMovieId("12")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("2")
                        .withMovieId("13")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("2")
                        .withMovieId("14")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        // 3
        ratingList.add(Rating.createRating()
                        .withUserId("3")
                        .withMovieId("11")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("3")
                        .withMovieId("12")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("3")
                        .withMovieId("13")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("3")
                        .withMovieId("15")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        // 4
        ratingList.add(Rating.createRating()
                        .withUserId("4")
                        .withMovieId("10")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("4")
                        .withMovieId("11")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("4")
                        .withMovieId("12")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("4")
                        .withMovieId("13")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        // 5
        ratingList.add(Rating.createRating()
                        .withUserId("5")
                        .withMovieId("10")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("5")
                        .withMovieId("11")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("5")
                        .withMovieId("12")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        ratingList.add(Rating.createRating()
                        .withUserId("5")
                        .withMovieId("15")
                        .withScore(1.0f)
                        .withTimestamp(0L));
        
        final AbstractDatabase<User, Movie> db = 
                new DefaultDatabase(userList,
                                    movieList,
                                    ratingList);
        
        System.out.println(db);
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator);
        
        
    }
}
