package net.coderodde.moviemine.model;

import static net.coderodde.moviemine.util.Validation.checkNotNull;

/**
 * This class models movie ratings given by users.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Rating {
   
    /**
     * The ID of the user this rating is given by.
     */
    private final String userId;
    
    /**
     * The ID of the movie being rated.
     */
    private final String movieId;
    
    /**
     * The actual score.
     */
    private final float score;
    
    /**
     * The timestamp of the rating.
     */
    private final long timestamp;
    
    /**
     * Constructs a new rating.
     * 
     * @param userId    the ID of the user this rating belongs to.
     * @param movieId   the ID of the movie being rated.
     * @param score     the actual rating score.
     * @param timestamp the timestamp of this rating.
     */
    public Rating(final String userId,
                  final String movieId,
                  final float score,
                  final long timestamp) {
        checkNotNull(userId, "The user ID is null.");
        checkNotNull(movieId, "The movie ID is null.");
        this.userId = userId;
        this.movieId = movieId;
        this.score = score;
        this.timestamp = timestamp;
    }
    
    /**
     * Returns the ID of the user who made this rating.
     * 
     * @return the user ID.
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Returns the ID of the movie being rated by this rating.
     * 
     * @return the movie ID.
     */
    public String getMovieId() {
        return movieId;
    }
    
    /**
     * Returns the rating score.
     * 
     * @return the rating score.
     */
    public float getScore() {
        return score;
    }
    
    /**
     * Returns the time stamp of this rating.
     * 
     * @return the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Initiates a fluent API for rating creation.
     * 
     * @return the user ID selector.
     */
    public static UserIdSelector createRating() {
        return new UserIdSelector();
    }
    
    /**
     * This class implements a user ID selector.
     */
    public static class UserIdSelector {
        
        /**
         * Selects the user ID of the rating being constructed.
         * 
         * @param  userId the user ID to set.
         * @return the movie ID selector.
         */
        public MovieIdSelector withUserId(final String userId) {
            return new MovieIdSelector(userId);
        }
    }
    
    /**
     * This class implements a movie ID selector.
     */
    public static class MovieIdSelector {
        
        /**
         * The user ID of the rating being constructed.
         */
        private final String userId;
        
        /**
         * Constructs the movie ID selector for the rating being constructed.
         * 
         * @param userId the user ID to set.
         */
        public MovieIdSelector(final String userId) {
            this.userId = userId;
        }
        
        /**
         * Selects the movie ID for the rating being constructed.
         * 
         * @param  movieId the movie ID to set.
         * @return the score selector.
         */
        public ScoreSelector withMovieId(final String movieId) {
            return new ScoreSelector(userId, movieId);
        }
    }
    
    /**
     * This class implements a score selector.
     */
    public static class ScoreSelector {
        
        /**
         * The user ID of the rating being constructed.
         */
        private final String userId;
        
        /**
         * The movie ID of the rating being constructed.
         */
        private final String movieId;
        
        /**
         * Constructs the score selector for the rating being constructed.
         * 
         * @param userId  the user ID to set.
         * @param movieId the movie ID to set.
         */
        public ScoreSelector(final String userId, final String movieId) {
            this.userId = userId;
            this.movieId = movieId;
        }
        
        /**
         * Selects the score for the rating being constructed.
         * 
         * @param  score the score to set.
         * @return the timestamp selector.
         */
        public TimestampSelector withScore(final float score) {
            return new TimestampSelector(userId, movieId, score);
        }
    }
    
    /**
     * This class implements a timestamp selection.
     */
    public static class TimestampSelector {
        
        /**
         * The user ID of the rating being constructed.
         */
        private final String userId;
        
        /**
         * The movie ID of the rating being constructed.
         */
        private final String movieId;
        
        /**
         * The score of the rating being constructed.
         */
        private final float score;
        
        /**
         * Constructs the timestamp selector for the rating being constructed.
         * 
         * @param userId  the user ID to set.
         * @param movieId the movie ID to set.
         * @param score   the score to set.
         */
        public TimestampSelector(final String userId,
                                 final String movieId,
                                 final float score) {
            this.userId = userId;
            this.movieId = movieId;
            this.score = score;
        }
        
        /**
         * Selects the timestamp for the rating being constructed.
         * 
         * @param  timestamp the timestamp to set.
         * @return a new rating.
         */
        public Rating withTimestamp(final long timestamp) {
            return new Rating(userId, movieId, score, timestamp);
        }
    }
}
