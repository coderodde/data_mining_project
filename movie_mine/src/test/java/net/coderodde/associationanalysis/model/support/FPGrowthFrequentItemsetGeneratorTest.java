package net.coderodde.associationanalysis.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
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

public class FPGrowthFrequentItemsetGeneratorTest {

    @Test
    public void testPath() {
        final List<User> userList = new ArrayList<>();
        final List<Movie> movieList = new ArrayList<>();
        final List<Rating> ratingList = new ArrayList<>();
        final Map<String, Movie> map = new HashMap<>();
        
        userList.add(User.createUser()
                         .withId("29")
                         .asFemale()
                         .withAge(22)
                         .withOccupation("nobody")
                         .withZipCode("99000"));
        
        final Movie m1 = Movie.createMovie()
                              .withMovieId("1")
                              .withTitle("Kewlee")
                              .withGenres()
                              .endGenres();
        
        final Movie m2 = Movie.createMovie()
                              .withMovieId("2")
                              .withTitle("Newlee")
                              .withGenres()
                              .endGenres();
        
        final Movie m3 = Movie.createMovie()
                              .withMovieId("3")
                              .withTitle("Lewlee")
                              .withGenres()
                              .endGenres();
        
        movieList.add(m1);
        movieList.add(m2);
        movieList.add(m3);
        
        ratingList.add(Rating.createRating()
                             .withUserId("29")
                             .withMovieId("1")
                             .withScore(5.0f)
                             .withTimestamp(1000L));
        
        ratingList.add(Rating.createRating()
                             .withUserId("29")
                             .withMovieId("2")
                             .withScore(5.0f)
                             .withTimestamp(1000L));
        
        ratingList.add(Rating.createRating()
                             .withUserId("29")
                             .withMovieId("3")
                             .withScore(5.0f)
                             .withTimestamp(1000L));
        
        final AbstractDatabase<User, Movie> db =
                new DefaultDatabase(userList, movieList, ratingList);
        
        System.out.println(db);
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                new FPGrowthFrequentItemsetGenerator<>();
        
        final FrequentItemsetData<Movie> data =
                generator.findFrequentItemsets(db.select(), 0.4);   
        
        assertEquals(7, data.getFrequentItemsets().size());
        
        assertTrue(data.getFrequentItemsets().contains(asSet(m1)));
        assertTrue(data.getFrequentItemsets().contains(asSet(m2)));
        assertTrue(data.getFrequentItemsets().contains(asSet(m3)));
   
        assertTrue(data.getFrequentItemsets().contains(asSet(m1, m2)));
        assertTrue(data.getFrequentItemsets().contains(asSet(m1, m3)));
        assertTrue(data.getFrequentItemsets().contains(asSet(m2, m3)));
        
        assertTrue(data.getFrequentItemsets().contains(asSet(m1, m2, m3)));
    }
    
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
                new FPGrowthFrequentItemsetGenerator<>();
        
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
        assertEquals(2, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Milk"));
        work.add(map.get("Diapers"));
        
        assertTrue(frequentItemsetList.contains(work));
        assertEquals(3, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Eggs"));
        
        assertFalse(frequentItemsetList.contains(work));
        assertEquals(0, sf.getSupportCount(work));
        
        work.clear();
        work.add(map.get("Cola"));
        
        assertTrue(frequentItemsetList.contains(work));
        assertEquals(2, sf.getSupportCount(work));
    }
    
    @Test
    public void testFindFrequentItemsets2() {
        final List<Set<String>> transactionList = new ArrayList<>();
        transactionList.add(asSet("a", "b"));
        transactionList.add(asSet("b", "c", "d"));
        transactionList.add(asSet("a", "c", "d", "e"));
        transactionList.add(asSet("a", "d", "e"));
        transactionList.add(asSet("a", "b", "c"));
        transactionList.add(asSet("a", "b", "c", "d"));
        transactionList.add(asSet("a"));
        transactionList.add(asSet("a", "b", "c"));
        transactionList.add(asSet("a", "b", "d"));
        transactionList.add(asSet("b", "c", "e"));
        
        final StringDatabase db = new StringDatabase(transactionList);
        final double minimumSupport = 2.0 / db.size();
        final Comparator<String> cmp = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        
        final FrequentItemsetData<String> data = 
                new FPGrowthFrequentItemsetGenerator<String>()
                .findFrequentItemsets(db.select(), minimumSupport);
        
        System.out.println("Itemsets:");
        
        for (final Set<String> itemset : data.getFrequentItemsets()) {
            System.out.println(itemset);
        }
        
        assertEquals(19, data.getFrequentItemsets().size());
        
        final String a = "a";
        final String b = "b";
        final String c = "c";
        final String d = "d";
        final String e = "e";
        
        assertTrue(data.getFrequentItemsets().contains(asSet(e)));
        assertTrue(data.getFrequentItemsets().contains(asSet(d, e)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, d, e)));
        assertTrue(data.getFrequentItemsets().contains(asSet(c, e)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, e)));
        
        assertTrue(data.getFrequentItemsets().contains(asSet(d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(c, d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(b, c, d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, c, d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(b, d)));
        
        assertTrue(data.getFrequentItemsets().contains(asSet(a, b, d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, d)));
        assertTrue(data.getFrequentItemsets().contains(asSet(c)));
        assertTrue(data.getFrequentItemsets().contains(asSet(b, c)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, b, c)));
        
        assertTrue(data.getFrequentItemsets().contains(asSet(a, c)));
        assertTrue(data.getFrequentItemsets().contains(asSet(b)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a, b)));
        assertTrue(data.getFrequentItemsets().contains(asSet(a)));
        
        assertFalse(data.getFrequentItemsets().contains(asSet(a, b, c, d, e)));
    }
    
    static class StringDatabase extends AbstractDatabase<Object, String> {

        private final List<Set<String>> transactionList;
        
        public StringDatabase(List<Set<String>> transactionList) {
            this.transactionList = new ArrayList<>(transactionList);
        }
        
        @Override
        public List<Set<String>> select(Predicate<Object>... predicates) {
            return Collections.unmodifiableList(transactionList);
        }

        @Override
        public int size() {
            return transactionList.size();
        }
    }
    
    static <I> Set<I> asSet(I... strings) {
        return new HashSet<>(Arrays.asList(strings));
    }
}
