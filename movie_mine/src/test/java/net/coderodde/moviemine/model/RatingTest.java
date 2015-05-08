package net.coderodde.moviemine.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class RatingTest {
    
    private final Rating rating;
    
    public RatingTest() {
        rating = Rating.createRating()
                    .withUserId("2")
                    .withMovieId("42")
                    .withScore(4.5f)
                    .withTimestamp(1000L);
    }

    @Test
    public void testGetUserId() {
        assertEquals("2", rating.getUserId());
        assertFalse(rating.getUserId().equals("-1"));
        assertFalse(rating.getUserId().equals("0"));
        assertFalse(rating.getUserId().equals("1"));
        assertFalse(rating.getUserId().equals("3"));
        assertFalse(rating.getUserId().equals("4"));
    }

    @Test
    public void testGetMovieId() {
        assertEquals("42", rating.getMovieId());
        assertFalse(rating.getMovieId().equals("40"));
        assertFalse(rating.getMovieId().equals("41"));
        assertFalse(rating.getMovieId().equals("43"));
        assertFalse(rating.getMovieId().equals("44"));
    }

    @Test
    public void testGetScore() {
        assertEquals(4.5f, rating.getScore(), 0.0001f);
        assertFalse(rating.getScore() == 4.3f);
        assertFalse(rating.getScore() == 4.4f);
        assertFalse(rating.getScore() == 4.6f);
        assertFalse(rating.getScore() == 4.7f);
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(1000L, rating.getTimestamp());
        assertFalse(rating.getTimestamp() == 998L);
        assertFalse(rating.getTimestamp() == 999L);
        assertFalse(rating.getTimestamp() == 1001L);
        assertFalse(rating.getTimestamp() == 1002L);
    }
}
