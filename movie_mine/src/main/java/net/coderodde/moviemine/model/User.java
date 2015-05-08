package net.coderodde.moviemine.model;

import static net.coderodde.util.Validation.checkIntegerNotNegative;
import static net.coderodde.util.Validation.checkNotNull;

/**
 * This class models a user.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class User {
    
    /**
     * Defines a gender of a user.
     */
    public enum Gender {
        FEMALE,
        MALE
    }
    
    /**
     * The ID of this user.
     */
    private final String id;
    
    /**
     * The gender of this user.
     */
    private final Gender gender;
    
    /**
     * The age of this user.
     */
    private final int age;
    
    /**
     * The occupation of this user.
     */
    private final String occupation;
    
    /**
     * The ZIP code of this user.
     */
    private final String zipCode;
    
    /**
     * Constructs a new user.
     * 
     * @param id         the ID of the new user.
     * @param gender     the gender of the new user.
     * @param age        the age of the new user.
     * @param occupation the occupation of the new user.
     * @param zipCode    the ZIP code of the new user.
     */
    public User(final String id,
                final Gender gender,
                final int age,
                final String occupation,
                final String zipCode) {
        checkNotNull(id, "The user ID is null.");
        checkIntegerNotNegative(age, "The age of a user is invalid: " + age);
        
        // Other reference fields are allowed to be null.
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
        this.zipCode = zipCode;
    }
    
    /**
     * Return the ID of this user.
     * 
     * @return the ID.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the gender of this user.
     * 
     * @return the gender. 
     */
    public Gender getGender() {
        return gender;
    }
    
    /**
     * Returns the age of this user.
     * 
     * @return the age.
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Returns the occupation of this user.
     * 
     * @return the occupation.
     */
    public String getOccupation() {
        return occupation;
    }
    
    /**
     * Returns the ZIP code of this user.
     * 
     * @return the ZIP code.
     */
    public String getZipCode() {
        return zipCode;
    }
    
    /**
     * Returns the hash code of this user, which depends only on the ID of this
     * user.
     * 
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Checks whether this user and <code>obj</code> encode the same movie. Two
     * movies are considered same if and only if their ID's are equal.
     * 
     * @param  obj the user object candidate.
     * @return <code>true</code> if and only if the two objects encode the same
     *         user.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        
        return ((User) obj).getId().equals(getId());
    }
    
    /**
     * Initiates the fluent API for constructing a user.
     * 
     * @return the ID selector.
     */
    public static IdSelector createUser() {
        return new IdSelector();
    }
    
    /**
     * This class implements a user ID selector.
     */
    public static class IdSelector {
        
        /**
         * Selects the ID of the user being constructed.
         * 
         * @param  id the ID.
         * @return gender selector.
         */
        public GenderSelector withId(final String id) {
            return new GenderSelector(id);
        }
    }
    
    /**
     * This class implements a gender selector.
     */
    public static class GenderSelector {
        
        /**
         * The ID of the user being constructed.
         */
        private final String id;
        
        /**
         * Constructs a gender selector for a user being constructed.
         * 
         * @param id the user ID.
         */
        public GenderSelector(final String id) {
            this.id = id;
        }
        
        /**
         * Sets the gender of the user being constructed as female.
         * 
         * @return the age selector.
         */
        public AgeSelector asFemale() {
            return new AgeSelector(id, Gender.FEMALE);
        }
        
        
        /**
         * Sets the gender of the user being constructed as male.
         * 
         * @return the age selector.
         */
        public AgeSelector asMale() {
            return new AgeSelector(id, Gender.MALE);
        }
        
        /**
         * Selects the gender of the user being constructed.
         * 
         * @param  gender the gender to set.
         * @return age selector.
         */
        public AgeSelector as(final Gender gender) {
            return new AgeSelector(id, gender);
        }
    } 
    
    /**
     * This class implements an age selector.
     */
    public static class AgeSelector {
        
        /**
         * The ID of the user being constructed.
         */
        private final String id;
        
        /**
         * The gender of the user being constructed.
         */
        private final Gender gender;
        
        /**
         * Constructs an age selector for the user being constructed.
         * 
         * @param id     the ID to set.
         * @param gender the gender to set.
         */
        public AgeSelector(final String id, final Gender gender) {
            this.id = id;
            this.gender = gender;
        }
        
        /**
         * Selects the age of the user being constructed.
         * 
         * @param  age the age to set.
         * @return occupation selector.
         */
        public OccupationSelector withAge(final int age) {
            return new OccupationSelector(id, gender, age);
        }
    }
    
    /**
     * This class implements an occupation selector.
     */
    public static class OccupationSelector {
        
        /**
         * The ID of the user being constructed.
         */
        private final String id;
        
        /**
         * The gender of the user being constructed.
         */
        private final Gender gender;
        
        /**
         * The age of the user being constructed.
         */
        private final int age;
        
        /**
         * Constructs an occupation selector for the user being constructed.
         * 
         * @param id     the ID to set.
         * @param gender the gender to set.
         * @param age    the age to set.
         */
        public OccupationSelector(final String id,
                                  final Gender gender,
                                  final int age) {
            this.id = id;
            this.gender = gender;
            this.age = age;
        }
        
        /**
         * Selects the occupation of the user being constructed.
         * 
         * @param  occupation the occupation to set.
         * @return ZIP code selector.
         */
        public ZipCodeSelector withOccupation(final String occupation) {
            return new ZipCodeSelector(id, gender, age, occupation);
        }
    }
    
    /**
     * This class implements a ZIP code selector.
     */
    public static class ZipCodeSelector {
        
        /**
         * The ID of the user being constructed.
         */
        private final String id;
        
        /**
         * The gender of the user being constructed.
         */
        private final Gender gender;
        
        /**
         * The age of the user being constructed.
         */
        private final int age;
        
        /**
         * The occupation of the user being constructed.
         */
        private final String occupation;
        
        /**
         * Constructs a ZIP code selector for the user being constructed.
         * 
         * @param id         the ID to set.
         * @param gender     the gender to set.
         * @param age        the age to set.
         * @param occupation the occupation to set.
         */
        public ZipCodeSelector(final String id,
                               final Gender gender,
                               final int age,
                               final String occupation) {
            this.id = id;
            this.gender = gender;
            this.age = age;
            this.occupation = occupation;
        }
        
        /**
         * Selects the ZIP code of the user being constructed and returns the 
         * user.
         * 
         * @param  zipCode the ZIP code to set.
         * @return a new user.
         */
        public User withZipCode(final String zipCode) {
            return new User(id, gender, age, occupation, zipCode);
        }
    }
}
