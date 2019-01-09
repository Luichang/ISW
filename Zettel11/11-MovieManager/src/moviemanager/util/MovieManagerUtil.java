package moviemanager.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.swt.program.Program;

import moviemanager.MovieManager;
import moviemanager.data.Movie;
import moviemanager.data.Performer;

/**
 * Provides various utility functions for the Movie Manager application.
 *
 */
public class MovieManagerUtil {

    /**
     * Main folder name for the files stored by the Movie Manager application.
     **/
    public static final String MOVIEMANAGER_FILE_DIR = ".moviemanager";
    /**
     * File extension name for the files stored by the Movie Manager
     * application.
     **/
    public static final String MOVIEMANAGER_FILE_EXT = ".wtf";

    public static final String OMDB_ATTRIBUTE_NULL = "N/A";

    /**
     * Sets the attribute with the given name for the given movie to the given
     * value.
     * 
     * @param attributeName the attribute name
     * @param m the movie
     * @param value the new value
     */
    public static void setMovieAttribute(String attributeName, Movie m, Object value) {
        try {
            Method setter = new PropertyDescriptor(attributeName, Movie.class).getWriteMethod();
            // Convert string values if necessary
            if (value instanceof String) {
                String strValue = (String) value;
                // We can derive the type we need to convert the string into
                // from the getter method
                Method getter = new PropertyDescriptor(attributeName, Movie.class).getReadMethod();
                if (getter.getReturnType() == String.class) {
                    setter.invoke(m, value);
                } else if (getter.getReturnType() == int.class && !strValue.equals(OMDB_ATTRIBUTE_NULL)) {
                    setter.invoke(m, Integer.valueOf(strValue));
                } else if (getter.getReturnType() == Date.class && attributeName.equals("releaseDate")
                        && !strValue.equals(OMDB_ATTRIBUTE_NULL)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
                    setter.invoke(m, formatter.parse(strValue));
                } else if (getter.getReturnType() == IObservableList.class && attributeName.equals("performers")
                        && !strValue.equals(OMDB_ATTRIBUTE_NULL)) {
                    for (String performerFullName : strValue.split(", ")) {
                        Performer performer = MovieManager.getInstance().getPerformer(performerFullName);
                        if (performer == null) {
                            Performer p = new Performer();
                            String performerFirstName = "";
                            String performerLastName = "";
                            // Crude way of handling middle-names and
                            // middle-name initials
                            String[] performerNameTokens = performerFullName.split(" ");
                            if (performerNameTokens.length > 1) {
                                for (int i = 0; i < performerNameTokens.length - 1; i++) {
                                    performerFirstName += performerNameTokens[i].trim() + " ";
                                }
                                performerFirstName = performerFirstName.substring(0, performerFirstName.length() - 1);
                                performerLastName = performerNameTokens[performerNameTokens.length - 1].trim();
                            } else {
                                performerFirstName = performerNameTokens[0].trim();
                            }

                            p.setFirstName(performerFirstName);
                            p.setLastName(performerLastName);

                            p.linkMovie(m);

                            MovieManager.getInstance().addPerformer(p, true);
                        } else {
                            performer.linkMovie(m);
                        }
                    }
                }
            }
            // Else just assume that the given value is of the correct type
            else {
                setter.invoke(m, value);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException
                | IntrospectionException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the file or directory with the given path exists.
     * 
     * @param path the path
     * @return true if the file or directory exists, false otherwise
     */
    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * Opens the given URL in the user's default browser.
     * 
     * @param url the URL
     */
    public static void openWebPage(String url) {
        Program.launch(url);
    }

    /**
     * Gets a list of movies sorted by their watch date in ascending order, i.e.
     * oldest first.
     * 
     * @param ignoreUnwatched flag to indicate whether unwatched movies are to
     * be ignored
     * @param minRating movies below the rating threshold are ignored
     * @param ignoreList optional list of movies that are to be ignored. May be
     * null
     * @return List of movies sorted by their watch date
     */
    public static IObservableList<Movie> getOldestWatchedMovies(boolean ignoreUnwatched, int minRating,
            List<Movie> ignoreList) {
        List<Movie> moviesList = new ArrayList<Movie>();
        IObservableList<Movie> movies = new WritableList<Movie>();

        for (Movie m : MovieManager.getInstance().getMovies()) {
            if (ignoreUnwatched && m.getWatchDate() == null || m.getRating() < minRating
                    || (ignoreList != null && ignoreList.contains(m))) {
                continue;
            }
            moviesList.add(m);
        }

        Collections.sort(moviesList, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                Date d1 = m1.getWatchDate();
                Date d2 = m2.getWatchDate();
                if (d1 == null && d2 == null) {
                    return 0;
                } else if (d1 == null && d2 != null) {
                    return -1;
                } else if (d1 != null && d2 == null) {
                    return 1;
                } else if (d1 != null && d2 != null) {
                    return d1.compareTo(d2);
                }
                return 0;
            }
        });

        movies.addAll(moviesList);

        return movies;
    }

    /**
     * Calculates the overall rating for the given movie.
     * 
     * @param m the movie
     * @return the overall rating of the movie
     */
    public static int calculateOverallRatingOfMovie(Movie m) {
        if (m == null) {
            throw new IllegalArgumentException("The movie must not be null");
        }
        int performersRating = 1;
        int performersWithRating = 0;
        for (Performer p : m.getPerformers()) {
            if (p.getRating() != 0) {
                performersRating += p.getRating();
                performersWithRating++;
            }
        }
        if (performersRating > 0) {
            if (m.getRating() > 0) {
                return ((performersRating / performersWithRating) + m.getRating()) / 2;
            } else {
                return performersRating / performersWithRating;
            }
        } else {
            return 5;
        }
    }

    /**
     * Gets the absolute path to the directory used by the Movie Manager
     * application for saving and loading its data.
     * 
     * @return the absolute path
     */
    public static String getPathToMovieManagerDirectory() {
        return System.getProperty("user.home") + File.separator + MOVIEMANAGER_FILE_DIR + File.separator;
    }

}
