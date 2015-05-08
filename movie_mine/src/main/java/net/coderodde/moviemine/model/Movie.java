package net.coderodde.moviemine.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import static net.coderodde.util.Validation.checkNotNull;

/**
 * This class models a movie.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Movie {
    
    /**
     * Holds the ID of this movie.
     */
    private final String id;
    
    /**
     * Holds the title of this movie.
     */
    private final String title;
    
    /**
     * The set of genres applicable to this movie.
     */
    private final Set<String> genreSet;
    
    /**
     * Constructs a new movie with given ID, title and genre list.
     * 
     * @param id     the ID of this movie. Must not be <code>null</code>.
     * @param title  the title of this movie. Must not be <code>null</code>.
     * @param genres the array of genres applicable to this movie.
     */
    public Movie(final String id, final String title, final String... genres) {
        checkNotNull(id, "The ID of a movie is null.");
        checkNotNull(title, "The title of a movie is null.");
        
        this.id = id;
        this.title = title;
        this.genreSet = new TreeSet<>(Arrays.asList(genres));
    }
    
    /**
     * Returns the ID of this movie.
     * 
     * @return the ID.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the title of this movie.
     * 
     * @return the title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns an unmodifiable set of genres applicable to this movie.
     * 
     * @return set of genres.
     */
    public Set<String> getGenres() {
        return Collections.<String>unmodifiableSet(genreSet);
    }
    
    /**
     * Returns the hash code for this movie, which depends only on its ID.
     * 
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
    
    /**
     * Checks whether this movie and <code>obj</code> encode the same movie.
     * The two movies are considered same if they have identical ID.
     * 
     * @param  obj the movie candidate object.
     * @return <code>true</code> if and only if the two movies are same.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Movie)) {
            return false;
        }
        
        return ((Movie) obj).getId().equals(getId());
    }
    
    /**
     * Initiates a fluent API for creating a movie.
     * 
     * @return the movie ID selector.
     */
    public static MovieIdSelector createMovie() {
        return new MovieIdSelector();
    }
    
    /**
     * This class implements a movie ID selector.
     */
    public static class MovieIdSelector {
        
        /**
         * Selects a movie ID for the movie being constructed.
         * 
         * @param  id the movie ID to set.
         * @return title selector.
         */
        public TitleSelector withMovieId(final String id) {
            return new TitleSelector(id);
        }
    }
    
    /**
     * This class implements a movie title selector.
     */
    public static class TitleSelector {
        
        /**
         * The ID of the movie being constructed.
         */
        private final String id;
        
        /**
         * Constructs a new title selector for the movie being constructed.
         * 
         * @param id the ID of the movie to set.
         */
        public TitleSelector(final String id) {
            this.id = id;
        }
        
        /**
         * Selects a title for the movie being constructed.
         * 
         * @param  title the title to set.
         * @return genre selector.
         */
        public GenreSelector withTitle(final String title) {
            return new GenreSelector(id, title);
        }
    }
    
    /**
     * This class implements selection of genres.
     */
    public static class GenreSelector {
        
        /**
         * The ID of the movie being constructed.
         */
        private final String id;
        
        /**
         * The title of the movie being constructed.
         */
        private final String title;
        
        /**
         * The set of genres applicable to the movie being constructed.
         */
        private final Set<String> genreSet;
        
        /**
         * Constructs a new movie genre selector with given ID and title.
         * 
         * @param id    the ID of the movie being constructed.
         * @param title the title of the movie being constructed.
         */
        public GenreSelector(final String id, 
                             final String title) {
            this.id = id;
            this.title = title;
            this.genreSet = new TreeSet<>();
        }
        
        /**
         * Selects one genre for a movie being constructed.
         * 
         * @param  genre the genre to select.
         * @return this selector for further genre selection.
         */
        public GenreSelector withGenre(final String genre) {
            genreSet.add(genre);
            return this;
        } 
        
        /**
         * Selects all genres in <code>genres</code> for a movie being 
         * constructed.
         * 
         * @param  genres the set of genres to select.
         * @return this selector for further genre selection.
         */
        public GenreSelector withGenres(final String... genres) {
            genreSet.addAll(Arrays.asList(genres));
            return this;
        }
        
        /**
         * Ends genre selection and constructs a new movie.
         * 
         * @return a new movie.
         */
        public Movie endGenres() {
            return new Movie(id, 
                             title, 
                             genreSet.toArray(new String[genreSet.size()]));
        }
    }
    
    /**
     * Implements a comparator. Uses movie ID's as the sort key.
     */
    public static class DefaultMovieComparator implements Comparator<Movie> {

        @Override
        public int compare(final Movie o1, final Movie o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
    
    /**
     * Caches the movie comparator.
     */
    public static final DefaultMovieComparator defaultMovieComparator = 
            new DefaultMovieComparator();
}
