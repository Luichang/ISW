package moviemanager.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import moviemanager.MovieManager;
import moviemanager.data.Movie;
import moviemanager.data.Performer;

/**
 * Shows movies and performers matching the given search.
 *
 */
@Deprecated
public class SearchResultsWidget {
    private Composite resultsContainer;
    private List<Movie> movies;
    private List<Performer> performers;
    private String search;
    private Text searchText;

    // Widgets
    private ScrolledComposite containerSC;
    private Composite container;
    private Composite moviesContainer;
    private Label moviesLabel;
    private Composite performersContainer;
    private Label performersLabel;
    private List<CLabel> movieLabels;
    private List<CLabel> performerLabels;

    public SearchResultsWidget(Composite resultsContainer, Point origin, List<Movie> movies, List<Performer> performers,
            String search, Text searchText) {
        this.resultsContainer = resultsContainer;
        this.movies = movies;
        this.performers = performers;
        this.search = search;
        this.searchText = searchText;

        this.movieLabels = new ArrayList<CLabel>();
        this.performerLabels = new ArrayList<CLabel>();
        resultsContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        createWidgets();
        updateWidget(this.movies, this.performers, this.search);

    }

    private void createWidgets() {
        GridLayout shellLayout = new GridLayout();
        shellLayout.numColumns = 1;
        resultsContainer.setLayout(shellLayout);

        containerSC = new ScrolledComposite(resultsContainer, SWT.NONE | SWT.V_SCROLL | SWT.H_SCROLL);
        GridLayout containerSCLayout = new GridLayout();
        containerSCLayout.marginHeight = 0;
        GridData containerSCLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        containerSCLayoutData.heightHint = 200;
        containerSC.setLayout(containerSCLayout);
        containerSC.setLayoutData(containerSCLayoutData);
        containerSC.setBackground(resultsContainer.getBackground());

        container = new Composite(containerSC, SWT.NONE);
        GridLayout containerLayout = new GridLayout();
        containerLayout.numColumns = 2;
        GridData containerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        container.setLayout(containerLayout);
        container.setLayoutData(containerLayoutData);
        container.setBackground(resultsContainer.getBackground());

        moviesContainer = new Composite(container, SWT.NONE);
        GridLayout moviesContainerLayout = new GridLayout();
        GridData moviesContainerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        moviesContainer.setLayout(moviesContainerLayout);
        moviesContainer.setLayoutData(moviesContainerLayoutData);
        moviesContainer.setBackground(resultsContainer.getBackground());

        moviesLabel = new Label(moviesContainer, SWT.NONE);
        FontDescriptor boldFontDescriptor = FontDescriptor.createFrom(moviesLabel.getFont()).setStyle(SWT.BOLD);
        moviesLabel.setFont(boldFontDescriptor.createFont(moviesLabel.getDisplay()));
        moviesLabel.setBackground(resultsContainer.getBackground());

        performersContainer = new Composite(container, SWT.NONE);
        GridLayout performersContainerLayout = new GridLayout();
        GridData performersContainerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        performersContainer.setLayout(performersContainerLayout);
        performersContainer.setLayoutData(performersContainerLayoutData);
        performersContainer.setBackground(resultsContainer.getBackground());

        performersLabel = new Label(performersContainer, SWT.NONE);
        performersLabel.setFont(boldFontDescriptor.createFont(performersLabel.getDisplay()));
        performersLabel.setBackground(resultsContainer.getBackground());

        containerSC.setContent(container);
        containerSC.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        containerSC.setExpandVertical(true);
        containerSC.setExpandHorizontal(true);

        resultsContainer.pack();
    }

    public void updateWidget(List<Movie> movies, List<Performer> performers, String searchString) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (searchString.isEmpty()) {
                    close();
                } else {
                    for (CLabel c : movieLabels) {
                        c.dispose();
                    }
                    for (CLabel c : performerLabels) {
                        c.dispose();
                    }

                    movieLabels.clear();
                    performerLabels.clear();

                    if (movies.size() > 0) {
                        moviesLabel.setText("Movies (" + movies.size() + ")");
                    } else {
                        moviesLabel.setText("No movies found");
                    }
                    if (performers.size() > 0) {
                        performersLabel.setText("Performers (" + performers.size() + ")");
                    } else {
                        performersLabel.setText("No performers found");
                    }

                    moviesLabel.pack();
                    performersLabel.pack();

                    for (Movie m : movies) {
                        CLabel c = new CLabel(moviesContainer, SWT.NONE);
                        c.setText(m.getTitle());
                        c.setImage(m.getThumbnailImage());
                        c.setBackground(resultsContainer.getBackground());
                        c.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
                        moviesContainer.layout();
                        resultsContainer.layout();
                        c.setToolTipText("Click to view details");

                        c.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseDown(MouseEvent e) {
                                MovieManager.getInstance().getDialog().showMovie(m);
                                searchText.setText("");
                            }
                        });
                        c.addMouseTrackListener(new MouseTrackListener() {
                            private Color bgColor = c.getBackground();
                            private Color fgColor = c.getForeground();

                            @Override
                            public void mouseEnter(MouseEvent arg0) {
                                c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION));
                                c.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
                            }

                            @Override
                            public void mouseExit(MouseEvent arg0) {
                                c.setBackground(bgColor);
                                c.setForeground(fgColor);
                            }

                            @Override
                            public void mouseHover(MouseEvent arg0) {
                                // Do nothing
                            }
                        });

                        movieLabels.add(c);
                    }

                    for (Performer p : performers) {
                        CLabel c = new CLabel(performersContainer, SWT.NONE);
                        c.setText(p.getFirstName() + " " + p.getLastName());
                        c.setImage(p.getThumbnailImage());
                        c.setBackground(resultsContainer.getBackground());
                        c.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
                        performersContainer.layout();
                        resultsContainer.layout();
                        c.setToolTipText("Click to view details");

                        c.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseDown(MouseEvent e) {
                                MovieManager.getInstance().getDialog().showPerformer(p);
                                searchText.setText("");
                            }
                        });
                        c.addMouseTrackListener(new MouseTrackListener() {
                            private Color bgColor = c.getBackground();
                            private Color fgColor = c.getForeground();

                            @Override
                            public void mouseEnter(MouseEvent arg0) {
                                c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION));
                                c.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
                            }

                            @Override
                            public void mouseExit(MouseEvent arg0) {
                                c.setBackground(bgColor);
                                c.setForeground(fgColor);
                            }

                            @Override
                            public void mouseHover(MouseEvent arg0) {
                                // Do nothing
                            }
                        });

                        performerLabels.add(c);
                    }

                    containerSC.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

                    resultsContainer.pack();
                }
            }
        });
    }

    public void close() {
        resultsContainer.setVisible(false);
    }

    public void open() {
        resultsContainer.layout();
        resultsContainer.getParent().layout();
        resultsContainer.setVisible(true);
    }

    public boolean isOpen() {
        return resultsContainer.isVisible();
    }

    // public void setLocation(Point p) {
    // // resultsContainer.setLocation(p.x + offset_x, p.y + offset_y);
    // }
}
