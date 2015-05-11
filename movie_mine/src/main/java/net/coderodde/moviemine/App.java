package net.coderodde.moviemine;

import java.io.File;
import java.util.List;
import java.util.Set;
import net.coderodde.associationanalysis.model.AbstractAssociationRuleGenerator;
import net.coderodde.associationanalysis.model.AbstractFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.FrequentItemsetData;
import net.coderodde.associationanalysis.model.support.AprioriFrequentItemsetGenerator;
import net.coderodde.associationanalysis.model.support.DefaultAssociationRuleGenerator;
import net.coderodde.moviemine.loader.AbstractDataLoader;
import net.coderodde.moviemine.loader.support.MovieLens10MDataLoader;
import net.coderodde.moviemine.loader.support.MovieLens1MDataLoader;
import net.coderodde.moviemine.model.AssociationRule;
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
     * The entry point into the program. If <code>args</code> is not empty,
     * <code>args[0]</code> is assumed to be the absolute path to the data 
     * directory.
     * 
     * @param args the command line arguments. 
     */
    public static void main(final String... args) {
        File dataDirectoryFile = getFile10M(args);
        
        final DefaultDatabase db = load10M(dataDirectoryFile);
        
        System.out.println("Users:   " + db.getUserView().size());
        System.out.println("Movies:  " + db.getMovieView().size());
        System.out.println("Ratings: " + db.getRatingView().size());
        
        final AbstractFrequentItemsetGenerator<Movie> generator =
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator);
        
        final double minimumSupport = 0.15;
        final double minimumConfidence = 0.7;
        
        final FrequentItemsetData<Movie> data = 
                minePatternsWithApriori(db.select(), minimumSupport);
        
        System.out.println("Frequent itemsets:");
        
        for (final Set<Movie> itemset : data.getFrequentItemsets()) {
            System.out.print(Utilities.toString(itemset) + ", support: ");
            System.out.println(data.getSupportCountFunction()
                                   .getSupport(itemset));
            
        }
        
        System.out.println("Association rules:");
        
        final List<AssociationRule<Movie>> ruleList = 
                mineAssociationRules(data, minimumConfidence);
        
        for (final AssociationRule<Movie> rule : ruleList) {
            System.out.print(Utilities.toString(rule));
            System.out.print(", support: ");
            System.out.print(data.getSupportCountFunction().getSupport(rule));
            System.out.print(", confidence: ");
            System.out.println(data.getSupportCountFunction()
                                   .getConfidence(rule));
        }
    }
    
    /**
     * Returns the file possibly pointing into a directory containing the 
     * actual data files of the 1M data package.
     * 
     * @param  args the command line arguments.
     * @return the file handle into data directory.
     */
    private static File getFileM1(final String... args) {
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
    
    /**
     * Returns the file possibly pointing into a directory containing the 
     * actual data files of the 10M data package.
     * 
     * @param  args the command line arguments.
     * @return the file handle into data directory.
     */
    private static File getFile10M(final String... args) {
        if (args.length == 0) {
            String path = System.getProperty("user.dir");
            final int index = path.lastIndexOf(File.separator + "movie_mine");
            path = path.substring(0, index);
            path += File.separator + "data" + File.separator + "ml-10M100K";
            return new File(path);
        } else {
            return new File(args[0]);
        }
    }
    
    /**
     * Loads the database from a directory and prints the duration of that 
     * operation. Targets the 1M MovieLens data package.
     * 
     * @param  dataDirectoryFile the file handle pointing to the data directory.
     * @return the database.
     */
    private static DefaultDatabase load1M(final File dataDirectoryFile) {
        long ta = System.currentTimeMillis();
        final AbstractDataLoader dataLoader = 
                new MovieLens1MDataLoader(dataDirectoryFile);
        
        final DefaultDatabase db = dataLoader.load();
        long tb = System.currentTimeMillis();
        
        System.out.println("Loading done in " + (tb - ta) + " ms.");
        
        return db;
    }
    
    /**
     * Loads the database from a directory and prints the duration of that 
     * operation. Targets the 10M MovieLens data package.
     * 
     * @param  dataDirectoryFile the file handle pointing to the data directory.
     * @return the database.
     */
    private static DefaultDatabase load10M(final File dataDirectoryFile) {
        long ta = System.currentTimeMillis();
        final AbstractDataLoader dataLoader = 
                new MovieLens10MDataLoader(dataDirectoryFile);
        
        final DefaultDatabase db = dataLoader.load();
        long tb = System.currentTimeMillis();
        
        System.out.println("Loading done in " + (tb - ta) + " ms.");
        
        return db;
    }
    
    /**
     * Mines the frequent patterns, prints the duration of that operation, and
     * finally returns the frequent itemsets.
     * 
     * @param  transactionList the list of transactions to mine.
     * @param  minimumSupport  the minimum support.
     * @return the frequent itemset data.
     */
    private static FrequentItemsetData<Movie> 
    minePatternsWithApriori(final List<Set<Movie>> transactionList,
                            final double minimumSupport) {
        final long ta = System.currentTimeMillis();
        final FrequentItemsetData<Movie> data = 
                new AprioriFrequentItemsetGenerator<>
                    (Movie.defaultMovieComparator)
                .findFrequentItemsets(transactionList, minimumSupport);
        final long tb = System.currentTimeMillis();
        
        System.out.println("Mined the frequent patterns with minimum support " +
                           minimumSupport + " in " + (tb - ta) + 
                           " milliseconds. Patterns found: " +
                           data.getFrequentItemsets().size());
        return data;
    }
    
    private static List<AssociationRule<Movie>>
    mineAssociationRules(final FrequentItemsetData<Movie> data,
                         final double minimumConfidence) {
        final AbstractAssociationRuleGenerator<Movie> arg = 
                new DefaultAssociationRuleGenerator<>();
        
        final long ta = System.currentTimeMillis();
        final List<AssociationRule<Movie>> ruleList = 
                arg.mine(data, minimumConfidence);
        final long tb = System.currentTimeMillis();
    
        System.out.println("Mined the association patterns in " + 
                           (tb - ta) + " milliseconds. Rules found: " +
                           ruleList.size());
        
        return ruleList;
    }
}
