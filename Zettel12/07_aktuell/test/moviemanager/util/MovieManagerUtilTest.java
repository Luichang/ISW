package moviemanager.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import moviemanager.MovieManager;
import moviemanager.data.Movie;
import moviemanager.data.Performer;

public class MovieManagerUtilTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private MovieManager movieManager;
    private Movie movie;

    @Before
    public void setUp() {
        movieManager = MovieManager.getInstance();
        assertNotNull(movieManager);

        movie = new Movie();
    }
    
    /**
     * Tests
     * {@link MovieManagerUtil#calculateOverallRatingOfMovie(moviemanager.data.Movie)}
     * with a movie that contains one performer.
     */
    //@Test
    public void testCalculateOverallRatingOfMovieWithMovieAndOnePerformer() {
        Movie m = new Movie();
        m.setRating(5);

        Performer p = new Performer();
        p.linkMovie(m);
        p.setRating(5);

        assertTrue(MovieManagerUtil.calculateOverallRatingOfMovie(m) >= 0);
    }

    // Eigene Tests beginnen hier

    /**
     * Tests
     * {@link MovieManagerUtil#calculateOverallRatingOfMovie(moviemanager.data.Movie)}
     * with a movie that contains multiple rated and unrated performer.
     */
    @Test
    public void testCalculateOverallRatingOfMovieWithMovieAndMultipleRatedAndUnratedPerformer() {
        Movie m = new Movie();
        m.setRating(5);

        int performerRating = 0;
        
        for(int i = 0; i < 4; i++) {
            Performer p = new Performer();
            p.linkMovie(m);
            if(i % 2 == 1) {
        	p.setRating(10 * i);
            	performerRating += 10 * i;
            }
        }
        
        int out = (int)(5 + performerRating / 2) / 2;
        //System.out.println("Erwartetes Ergebnis: " + out);
        //System.out.println("Tatzächliches Ergebnis: " + MovieManagerUtil.calculateOverallRatingOfMovie(m));
        assertTrue(MovieManagerUtil.calculateOverallRatingOfMovie(m) == out);
    }
    
    
    @Test
    public void testUnlinkPerformer() {
	Movie m1 = new Movie();
	m1 = null;
	Performer p = new Performer();
	exception.expect(NullPointerException.class);
	m1.unlinkPerformer(p);
	
    }
    
    /**
     * Tests
     * {@link MovieManagerUtil#calculateOverallRatingOfMovie(moviemanager.data.Movie)}
     * with a movie that contains no performer.
     */
    @Test
    public void testCalculateOverallRatingOfMovieWithMovieAndNoPerformer() {
        Movie m = new Movie();
        m.setRating(5);

        assertTrue(MovieManagerUtil.calculateOverallRatingOfMovie(m) == 5);
    }    
    
    // Eigene Tests enden hier

    /**
     * Tests
     * {@link MovieManagerUtil#calculateOverallRatingOfMovie(moviemanager.data.Movie)}
     * with an uninitialized movie, i.e. null.
     */
    //@Test
    public void testCalculateOverallRatingOfMovieWithUninitializedMovie() {
        // This should throw an IAE
        exception.expect(IllegalArgumentException.class);
        MovieManagerUtil.calculateOverallRatingOfMovie(null);
    }

    /**
     * Tests {@link MovieManagerUtil#fileExists(String)} with an existing path.
     */
    //@Test
    public void testFileExistsWithExistingPath() {
        assertTrue(MovieManagerUtil.fileExists(System.getProperty("user.home")));
    }

    /**
     * Tests {@link MovieManagerUtil#fileExists(String)} with a non-existing
     * path.
     */
    //@Test
    public void testFileExistsWithNonExistingPath() {
        assertTrue(!MovieManagerUtil.fileExists(System.getProperty("user.home") + "blaaah"));
    }

    /**
     * Tests {@link MovieManagerUtil#setMovieAttribute(String, Movie, Object)}.
     */
    //@Test
    public void testSetMovieAttribute() {
        String attribute = "title";

        String strValue = "Some Movie";

        // Test setting of string attributes
        MovieManagerUtil.setMovieAttribute(attribute, movie, strValue);
        assertTrue(movie.getTitle().equals(strValue));

        // Test setting of integer attributes
        attribute = "rating";
        strValue = "10";
        MovieManagerUtil.setMovieAttribute(attribute, movie, strValue);

        assertTrue(movie.getRating() == Integer.parseInt(strValue));

        // Test setting of date attributes
        attribute = "releaseDate";
        strValue = "2011.03.02";
        MovieManagerUtil.setMovieAttribute(attribute, movie, strValue);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        try {
            assertTrue(movie.getReleaseDate().equals(formatter.parse(strValue)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Test setting of performer list
        int performerCountBefore = movieManager.getPerformers().size();
        attribute = "performers";
        String perfName = "Anne Romero R. Goose";
        strValue = perfName + ", " + perfName;
        MovieManagerUtil.setMovieAttribute(attribute, movie, strValue);

        Performer p = movieManager.getPerformer(perfName);
        int performerCountAfter = movieManager.getPerformers().size();

        assertNotNull(p);
        assertTrue(p.getFirstName().equals("Anne Romero R."));
        assertTrue(p.getLastName().equals("Goose"));
        assertTrue(p.getMovies().contains(movie));
        // The same performer must not be added twice to the list of performers
        assertTrue(performerCountBefore == performerCountAfter - 1);

        // Adding the performers to another movie should now only add the movie
        // to the list of movies for the given performers. No changes should be
        // made to the list of performers in the movie manager
        Movie m1 = new Movie();
        movie.setTitle("Bleh");

        MovieManagerUtil.setMovieAttribute(attribute, m1, strValue);
        assertTrue(performerCountBefore == performerCountAfter - 1);
        assertTrue(p.getMovies().contains(m1));

    }

    /**
     * Tests {@link MovieManagerUtil#fileExists(String)}.
     */
    @Test
    public void testFileExists() {
        String path = System.getProperty("user.home") + File.separator + MovieManagerUtil.MOVIEMANAGER_FILE_DIR
                + File.separator + "test.wtf";
        assertFalse(MovieManagerUtil.fileExists(path));

        File testFile = new File(path);
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(MovieManagerUtil.fileExists(path));

        testFile.delete();
    }

}
