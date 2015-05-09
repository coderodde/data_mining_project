package net.coderodde.moviemine;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;
import net.coderodde.associationanalysis.model.support.AprioriFrequentItemsetGenerator;
import net.coderodde.moviemine.loader.AbstractDataLoader;
import net.coderodde.moviemine.loader.support.MovieLens1MDataLoader;
import net.coderodde.moviemine.model.DefaultDatabase;
import net.coderodde.moviemine.model.Movie;
import net.coderodde.moviemine.util.Utilities;

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
        File dataDirectoryFile = getFile(args);
        
        final DefaultDatabase db = load(dataDirectoryFile);
        
        Logger.getLogger("Loading").info("Loading stats");
        
        System.out.println("Users:   " + db.getUserView().size());
        System.out.println("Movies:  " + db.getMovieView().size());
        System.out.println("Ratings: " + db.getRatingView().size());
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator);
        
        final double minimumSupport = 0.2;
        
        final FrequentItemsetData<Movie> data = 
                minePatternsWithApriori(db.select(), minimumSupport);
        
        for (final Set<Movie> itemset : data.getFrequentItemsets()) {
            System.out.print(Utilities.toString(itemset) + ", support: ");
            System.out.println(data.getSupportCountFunction()
                                   .getSupport(itemset));
            
        }
    }
    
    private static File getFile(final String... args) {
        if (args.length == 0) {
            String path = System.getProperty("user.dir");
            final int index = path.lastIndexOf(File.separator + "movie_mine");
            path = path.substring(0, index);
            path += File.separator + "data" + File.separator + "ml-1m";
            return new File(path);
        } else {
            return new File(args[0]);
        }
    }
    
    private static DefaultDatabase load(final File dataDirectoryFile) {
        long ta = System.currentTimeMillis();
        final AbstractDataLoader dataLoader = 
                new MovieLens1MDataLoader(dataDirectoryFile);
        
        final DefaultDatabase db = dataLoader.load();
        long tb = System.currentTimeMillis();
        
        System.out.println("Loading done in " + (tb - ta) + " ms.");
        
        return db;
    }
    
    private static FrequentItemsetData<Movie> 
    minePatternsWithApriori(final List<Set<Movie>> transactionList,
                            final double minimumSupport) {
        final long ta = System.currentTimeMillis();
        final FrequentItemsetData<Movie> data = 
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator)
                .findFrequentItemsets(transactionList, minimumSupport);
        final long tb = System.currentTimeMillis();
        
        Logger.getLogger("LOADING").info("Mining results");
        
        System.out.println("Mined the frequent patterns with minimum support " +
                           minimumSupport + " in " + (tb - ta) + 
                           " milliseconds. Patterns found: " +
                           data.getFrequentItemsets().size());
        return data;
    }
}
