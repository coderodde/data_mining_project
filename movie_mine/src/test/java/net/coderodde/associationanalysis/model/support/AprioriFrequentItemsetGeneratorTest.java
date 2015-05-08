package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractDatabase;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.AbstractSupportCountFunction;
import net.coderodde.associationanalysis.model.FrequentItemsetData;
import net.coderodde.moviemine.model.DefaultDatabase;
import net.coderodde.moviemine.model.Movie;
import net.coderodde.moviemine.model.Rating;
import net.coderodde.moviemine.model.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class AprioriFrequentItemsetGeneratorTest {

    @Test
    public void testFindFrequentItemsets() {
        final List<User> userList = new ArrayList<>();
        final List<Movie> movieList = new ArrayList<>();
        final List<Rating> ratingList = new ArrayList<>();
        final Map<String, Movie> map = new HashMap<>();
        
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
        
        map.put("Bread", movieList.get(0));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("11")
                        .withTitle("Milk")
                        .withGenre("food")
                        .endGenres());
        
        map.put("Milk", movieList.get(1));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("12")
                        .withTitle("Diapers")
                        .withGenre("food")
                        .endGenres());
        
        map.put("Diapers", movieList.get(2));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("13")
                        .withTitle("Beer")
                        .withGenre("food")
                        .endGenres());
        
        map.put("Beer", movieList.get(3));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("14")
                        .withTitle("Eggs")
                        .withGenre("food")
                        .endGenres());
        
        map.put("Eggs", movieList.get(4));
        
        movieList.add(Movie.createMovie()
                        .withMovieId("15")
                        .withTitle("Cola")
                        .withGenre("food")
                        .endGenres());
        
        map.put("Cola", movieList.get(5));
        
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
        
        final FrequentItemsetData<Movie> data =
                generator.findFrequentItemsets(db.select(), 0.4);
        
        assertTrue(data.getFrequentItemsets().size() > 0);
        
        final List<Set<Movie>> frequentItemsetList = data.getFrequentItemsets();
        final AbstractSupportCountFunction<Movie> sf = 
                data.getSupportCountFunction();
        
        final Set<Movie> work = new HashSet<>();
        
        work.add(map.get("Bread"));
        work.add(map.get("Milk"));
        
        assertTrue(frequentItemsetList.contains(work));
        assertEquals(3, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Bread"));
        work.add(map.get("Diapers"));
        
        assertTrue(frequentItemsetList.contains(work));
        assertEquals(3, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Milk"));
        work.add(map.get("Diapers"));
        
        assertTrue(frequentItemsetList.contains(work));
        assertEquals(3, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Eggs"));
        
        assertFalse(frequentItemsetList.contains(work));
        assertEquals(1, sf.getSupportCount(work));
        
        work.add(map.get("Cola"));
        
        assertFalse(frequentItemsetList.contains(work));
        assertEquals(0, sf.getSupportCount(work));
    }
}
