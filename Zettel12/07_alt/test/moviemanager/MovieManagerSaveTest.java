package moviemanager;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import moviemanager.data.Movie;
import moviemanager.data.Performer;

/**
 * Tests {@link MovieManager#saveData()} with non-existing data.
 */
public class MovieManagerSaveTest {
    MovieManager mm;

    @Test
    public void performTest() {
        mm = MovieManager.getInstance();
        Date d = new Date();
        // Create a test movie
        String movieTitle = "A Test Movie (created on " + d.toString() + ")";
        Movie m = new Movie();
        m.setTitle(movieTitle);
        mm.addMovie(m);

        // Create a test performer
        String perfFirstName = "Johnny";
        String perfLastName = "Performer (created on " + d.toString() + ")";
        Performer p = new Performer();
        p.setFirstName(perfFirstName);
        p.setLastName(perfLastName);
        p.linkMovie(m);
        mm.addPerformer(p, false);

        // Save the data
        mm.saveData();

        // Clear and reload the data
        mm.getMovies().clear();
        mm.getPerformers().clear();

        mm.loadData();

        // Check if the test movie and test performer exist
        boolean containsMovie = false;
        boolean containsPerformer = false;

        for (Movie mov : mm.getMovies()) {
            if (mov.getTitle().equals(movieTitle)) {
                containsMovie = true;
                break;
            }
        }

        for (Performer perf : mm.getPerformers()) {
            if (perf.getFirstName().equals(perfFirstName) && perf.getLastName().equals(perfLastName)) {
                containsPerformer = true;
                break;
            }
        }

        assertTrue(containsMovie && containsPerformer);
    }

}
