package net.coderodde.moviemine.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MovieTest {

    private final Movie movie;
    
    public MovieTest() {
        this.movie = Movie.createMovie()
                          .withMovieId("21")
                          .withTitle("Sample movie (1999)")
                          .withGenre("Action")
                          .withGenre("Romance")
                          .withGenres("Comedy", "Funky")
                          .withGenre("Family")
                          .endGenres();
    }

    @Test
    public void testGetId() {
        assertEquals("21", movie.getId());
        assertFalse(movie.getId().equals("20"));
        assertFalse(movie.getId().equals("22"));
        assertFalse(movie.getId().equals("21 fd"));
    }

    @Test
    public void testGetTitle() {
        assertEquals("Sample movie (1999)", movie.getTitle());
        assertFalse(movie.getTitle().equals("Sapmle movie (1999)"));
        assertFalse(movie.getTitle().equals("Sample movie (1998)"));
    }

    @Test
    public void testGetGenres() {
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Romance"));
        assertTrue(movie.getGenres().contains("Comedy"));
        assertTrue(movie.getGenres().contains("Funky"));
        assertTrue(movie.getGenres().contains("Family"));
        assertFalse(movie.getGenres().contains("Arnold Schwarznegger"));
    }
}
