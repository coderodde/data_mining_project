package net.coderodde.moviemine;

import java.io.File;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;
import net.coderodde.associationanalysis.model.support.AprioriFrequentItemsetGenerator;
import net.coderodde.moviemine.loader.AbstractDataLoader;
import net.coderodde.moviemine.loader.support.MovieLens1MDataLoader;
import net.coderodde.moviemine.model.DefaultDatabase;
import net.coderodde.moviemine.model.Movie;

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
        
        for (int i = 0; i < 3; ++i) {
            System.out.println(db.getUserView().get(i));
            System.out.println(db.getMovieView().get(i));
            System.out.println(db.getRatingView().get(i));
        }
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator);
        
        ta = System.currentTimeMillis();
        final FrequentItemsetData<Movie> data = 
                generator.findFrequentItemsets(db.select(), 0.4);
        tb = System.currentTimeMillis();
        
        System.out.println("Mining done in " + (tb - ta) + " ms.");
        
        System.out.println(data.getFrequentItemsets().size());
        
        for (final Set<Movie> itemset : data.getFrequentItemsets()) {
            System.out.println(1.0 * data.getSupportCountFunction()
                                         .getSupportCount(itemset) / db.size());
        }
    }
}
