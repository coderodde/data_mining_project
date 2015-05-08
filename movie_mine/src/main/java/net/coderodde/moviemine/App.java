package net.coderodde.moviemine;

import java.io.File;
import net.coderodde.moviemine.loader.AbstractDataLoader;
import net.coderodde.moviemine.loader.support.MovieLens1MDataLoader;
import net.coderodde.moviemine.model.DefaultDatabase;

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
        File dataDirectoryFile;
        
        if (args.length == 0) {
            String path = System.getProperty("user.dir");
            final int index = path.lastIndexOf(File.separator + "movie_mine");
            path = path.substring(0, index);
            path += File.separator + "data" + File.separator + "ml-1m";
            dataDirectoryFile = new File(path);
        } else {
            dataDirectoryFile = new File(args[0]);
        }
        
        System.out.println(dataDirectoryFile.getAbsolutePath());
        
        long ta = System.currentTimeMillis();
        final AbstractDataLoader dataLoader = 
                new MovieLens1MDataLoader(dataDirectoryFile);
        
        final DefaultDatabase db = dataLoader.load();
        long tb = System.currentTimeMillis();
        
        System.out.println("Loading done in " + (tb - ta) + " ms.");
        System.out.println("Users:   " + db.getUserView().size());
        System.out.println("Movies:  " + db.getMovieView().size());
        System.out.println("Ratings: " + db.getRatingView().size());
    }
}
