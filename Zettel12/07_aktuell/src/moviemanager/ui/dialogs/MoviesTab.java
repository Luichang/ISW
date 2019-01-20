package moviemanager.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;

import moviemanager.MovieManager;
import moviemanager.data.AbstractModelObject;
import moviemanager.data.Movie;
import moviemanager.data.Performer;
import moviemanager.ui.listeners.EditOverlayMouseTrackListener;
import moviemanager.util.MovieManagerUIUtil;

public class MoviesTab extends AbstractModelObjectTab {

    @SuppressWarnings("rawtypes")
    public MoviesTab(TabFolder tabFolder, String title) {
        super();

        context = new DataBindingContext(MovieManagerUIUtil.getDefaultRealm());

        // Generate properties for movies
        propertyObservables = new HashMap<IBeanValueProperty, IObservableValue>();
        propertyWidgets = new HashMap<IBeanValueProperty, Object>();
        // Initialize the value properties
        properties = new HashMap<String, IBeanValueProperty>();

        propertiesList = new ArrayList<String>();
        propertiesList.add("title");
        propertiesList.add("watchDate");
        propertiesList.add("returnDate");
        propertiesList.add("description");
        propertiesList.add("releaseDate");
        propertiesList.add("rating");
        propertiesList.add("overallRating");
        propertiesList.add("country");
        propertiesList.add("runtime");
        propertiesList.add("filmingLocations");
        propertiesList.add("alternativeTitles");
        propertiesList.add("imdbID");
        propertiesList.add("performers");

        for (String property : propertiesList) {
            properties.put(property, BeanProperties.value(Movie.class, property));
        }

        createTab(tabFolder, title);
    }

    @Override
    protected void createTab(TabFolder tabFolder, String title) {
        super.createTab(tabFolder, title);
        setContentAndLabelProvider();
        setViewerInput();

        if (MovieManager.getInstance().getMovies().size() > 0) {
            viewer.setSelection(new StructuredSelection(MovieManager.getInstance().getMovies().get(0)));
            // Add fields for all movie attributes
            addFields((AbstractModelObject) ((StructuredSelection) viewer.getSelection()).getFirstElement());
        } else {
            createNoModelObjectLink();
        }

        createSelectionChangedListener();
        sortNewModelObjects();
        createContextMenu();

    }

    // Creates the sort menu and relevant comparators
    private MenuManager createSort() {
        MenuManager sortByMenu = new MenuManager("Sort by", null);
        String suffixDescending = " (descending)";
        String suffixAscending = " (ascending)";
        // Title
        String prefix = "Title";
        sortByMenu.add(new Action(prefix + suffixDescending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Movie m1 = (Movie) o1;
                        Movie m2 = (Movie) o2;
                        return (-1) * m1.getTitle().compareTo(m2.getTitle());
                    }
                });
            }
        });
        sortByMenu.add(new Action(prefix + suffixAscending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Movie m1 = (Movie) o1;
                        Movie m2 = (Movie) o2;
                        return m1.getTitle().compareTo(m2.getTitle());
                    }
                });
            }
        });
        // Rating
        prefix = "Rating";
        sortByMenu.add(new Action(prefix + suffixDescending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Movie m1 = (Movie) o1;
                        Movie m2 = (Movie) o2;
                        return (-1) * Integer.compare(m1.getRating(), m2.getRating());
                    }
                });
            }
        });
        sortByMenu.add(new Action(prefix + suffixAscending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Movie m1 = (Movie) o1;
                        Movie m2 = (Movie) o2;
                        return Integer.compare(m1.getRating(), m2.getRating());
                    }
                });
            }
        });
        return sortByMenu;
    }

    @Override
    protected void createContextMenu() {
        MenuManager movieViewerMenuManager = new MenuManager();
        movieViewerMenuManager.setRemoveAllWhenShown(true);
        movieViewerMenuManager.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager arg0) {
                movieViewerMenuManager.add(new Action("Create movie") {
                    @Override
                    public void run() {
                        // Make sure the new movie is created on top of the list
                        ViewerComparator lastComparator = viewer.getComparator();
                        viewer.setComparator(new ViewerComparator() {
                            public int compare(Viewer v, Object o1, Object o2) {
                                Movie m1 = (Movie) o1;
                                Movie m2 = (Movie) o2;
                                if (m1.getTitle().equals("New Movie")) {
                                    return -1;
                                } else if (m2.getTitle().equals("New Movie")) {
                                    return 1;
                                } else {
                                    if (lastComparator != null) {
                                        return lastComparator.compare(viewer, m1, m2);
                                    } else {
                                        return 0;
                                    }
                                }
                            }
                        });
                        addMovie();
                    }
                });
                if (!MovieManager.getInstance().getMovies().isEmpty()) {
                    movieViewerMenuManager.add(new Action("Remove selected movie") {
                        @Override
                        public void run() {
                            Movie toRemove = (Movie) ((StructuredSelection) viewer.getSelection()).getFirstElement();
                            String message = "Are you sure you want to remove the selected movie?";
                            List<Performer> performersToRemove = new ArrayList<Performer>();
                            for (Performer p : toRemove.getPerformers()) {
                                if (p.getMovies().size() == 1) {
                                    performersToRemove.add(p);
                                }
                            }
                            if (performersToRemove.size() > 0) {
                                message += " Note that this will also cause the removal of " + performersToRemove.size()
                                        + " performer(s).";
                            }
                            if (MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
                                    "Remove Selected Movie?", message)) {
                                for (Performer p : performersToRemove) {
                                    MovieManager.getInstance().removePerformer(p);
                                    MovieManager.getInstance().getDialog().updatePerfomerDetailView();
                                }
                                removeMovie();
                            }
                        }
                    });
                    // Sorting sub context menu
                    if (MovieManager.getInstance().getMovies().size() > 1) {
                        movieViewerMenuManager.add(createSort());
                    }
                    movieViewerMenuManager.add(new Action("Watched Movies") {
                	@Override
                	public void run() {
                	    WatchedMoviesDialog dialog = new WatchedMoviesDialog(Display.getDefault().getActiveShell());
                	    
                	    if (dialog.open() == Window.OK) {
                		if (dialog.getSelection() instanceof Movie) {
                		    // show the detail view of the selected performer from the dialog
                		    MovieManager.getInstance().getDialog().showMovie((Movie) dialog.getSelection());
                		}
                	    }
                	    
                	    
                	}
                	
                    });
                }
            }
        });
        Menu movieViewerContextMenu = movieViewerMenuManager.createContextMenu(viewer.getTable());
        viewer.getTable().setMenu(movieViewerContextMenu);

    }

    @Override
    protected void sortNewModelObjects() {
        viewer.setComparator(new ViewerComparator() {
            public int compare(Viewer v, Object o1, Object o2) {
                Movie m1 = (Movie) o1;
                Movie m2 = (Movie) o2;
                if (m1.getTitle().equals("New Movie")) {
                    return -1;
                } else if (m2.getTitle().equals("New Movie")) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

    }

    @Override
    protected void createNoModelObjectLink() {
        noModelObjectLink = new Link(detailsContainer, SWT.NONE);
        noModelObjectLink.setText("There are no movies to display. <A>Add a movie now</A>.");
        noModelObjectLink.pack();
        noModelObjectLink.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                widgetSelected(arg0);
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addMovie();
            }
        });

        detailsContainerSC.setMinSize(detailsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public void addMovie() {
        Movie toAdd = new Movie();
        // If the movie database is empty prior to adding the movie, remove the
        // info label and add the widgets for the fields
        if (MovieManager.getInstance().getMovies().isEmpty()) {
            noModelObjectLink.dispose();
            addFields(toAdd);
            detailsContainer.layout();
        }
        MovieManager.getInstance().addMovie(toAdd);
        viewer.setSelection(new StructuredSelection(toAdd));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void addFields(AbstractModelObject modelObject) {
        if (!(modelObject instanceof Movie)) {
            System.err.println("ModelObject is not a movie\n");
        }
        detailsImage = new Label(detailsContainer, SWT.BORDER);
        GridData movieDetailsImageLayoutData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
        movieDetailsImageLayoutData.horizontalSpan = 2;
        detailsImage.setLayoutData(movieDetailsImageLayoutData);
        detailsImage.setImage(modelObject.getImage());

        detailsImageMouseTrackListener = new EditOverlayMouseTrackListener(modelObject, detailsImage);
        detailsImage.addMouseTrackListener(detailsImageMouseTrackListener);
        detailsImage.addMouseListener(detailsImageMouseTrackListener.getEditMouseListener());

        for (String attribute : propertiesList) {
            String label = "";
            if (attribute.equals("imdbID")) {
                label = "IMDB ID";
            }
            // The "year" attribute is not represented in the UI in order to
            // avoid user confusion. The "overallRating" attribute is calculated
            // automatically, thus it is hidden as well
            else if (attribute.equals("year") || attribute.equals("overallRating")) {
                continue;
            } else {
                // Split the label along uppercase letters. Taken from
                // 'http://stackoverflow.com/questions/3752636/java-split-string-when-an-uppercase-letter-is-found'
                String strLabel = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
                for (String s : strLabel.split("(?=\\p{Upper})")) {
                    label += s + " ";
                }
                if (label.length() > 0) {
                    label = label.substring(0, label.length() - 1);
                }
            }
            IBeanValueProperty modelProperty = properties.get(attribute);
            IObservableValue modelObservable = modelProperty.observe(modelObject);
            propertyObservables.put(modelProperty, modelObservable);
            Object widget = MovieManagerUIUtil.createFieldEditor(label, detailsContainer, modelObservable, attribute,
                    context, modelObject);
            propertyWidgets.put(modelProperty, widget);
        }

        detailsContainerSC.setMinSize(detailsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    @Override
    protected void setViewerInput() {
        viewer.setInput(MovieManager.getInstance().getMovies());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void setContentAndLabelProvider() {
        viewer.setContentProvider(new ObservableListContentProvider());

        IObservableSet knownElements = ((ObservableListContentProvider) viewer.getContentProvider()).getKnownElements();
        IObservableMap movieTitle = properties.get("title").observeDetail(knownElements);
        IObservableMap movieRating = properties.get("rating").observeDetail(knownElements);
        IObservableMap movieOverallRating = properties.get("overallRating").observeDetail(knownElements);

        IObservableMap[] observedProperties = { movieTitle, movieOverallRating, movieRating };

        viewer.setLabelProvider(new ObservableMapLabelProvider(observedProperties) {
            private Map<Object, Image> movieImages = new HashMap<Object, Image>();

            @Override
            public Image getColumnImage(Object o, int columnIndex) {
                Movie m = (Movie) o;
                switch (columnIndex) {
                    case 0:
                        // For now, we combine the movie and rating images into
                        // one image in order to fix the image dimension issue
                        Image movieImage = null;
                        if (movieImages.containsKey(o)) {
                            movieImages.get(o).dispose();
                        }
                        movieImage = MovieManagerUIUtil.createTableItemImage(m.getThumbnailImage(), m, false);
                        movieImages.put(o, movieImage);
                        return movieImage;
                    default:
                        return null;
                }
            }

            @Override
            public String getColumnText(Object o, int columnIndex) {
                Movie m = (Movie) o;
                switch (columnIndex) {
                    case 0:
                        return m.getTitle();
                    default:
                        return null;
                }
            }
        });

    }

    /**
     * Removes the currently selected movie from the database and handles the
     * disposal and creation of widgets.
     */
    protected void removeMovie() {
        Movie toRemove = (Movie) ((StructuredSelection) viewer.getSelection()).getFirstElement();
        // If the movie database will be empty after removing the selected
        // movie, remove the widgets for the fields and add the info label
        if (MovieManager.getInstance().getMovies().size() == 1) {
            for (Control c : detailsContainer.getChildren()) {
                c.dispose();
            }
            createNoModelObjectLink();
            detailsContainer.layout();
        }
        MovieManager.getInstance().removeMovie(toRemove);
    }

    @Override
    protected void showModelObject(AbstractModelObject modelObject) {
        if (!(modelObject instanceof Movie)) {
            System.err.println("ModelObject is not a movie\n");
        }

        viewer.setSelection(new StructuredSelection(modelObject));
        viewer.reveal(modelObject);

    }

    /**
     * Can be used to trigger an update of a selection of widgets in the movie
     * detail view.
     */
    @Override
    public void updateDetailView() {

    }

    @Override
    public void refreshViewers() {
        super.refreshViewers();

        TableViewer movieDetailsPerformersViewer = (TableViewer) propertyWidgets.get(properties.get("performers"));
        if (movieDetailsPerformersViewer != null && !movieDetailsPerformersViewer.getTable().isDisposed()) {
            movieDetailsPerformersViewer.refresh();
        }
    }

    @Override
    protected void createSelectionChangedListener() {
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public void selectionChanged(SelectionChangedEvent e) {
                StructuredSelection s = (StructuredSelection) e.getSelection();
                ((ScrolledComposite) detailsContainer.getParent()).setOrigin(new Point(0, 0));
                if (s.getFirstElement() != null) {
                    // Rebuild the data binding context
                    context.dispose();
                    context = new DataBindingContext(MovieManagerUIUtil.getDefaultRealm());
                    for (String attribute : properties.keySet()) {
                        IBeanValueProperty p = properties.get(attribute);
                        IObservableValue v = p.observe(s.getFirstElement());
                        MovieManagerUIUtil.updateBinding(propertyWidgets.get(p), v, attribute, context,
                                (Movie) s.getFirstElement());
                    }
                    // Update the detail image and the attached listeners
                    detailsImage.setImage(((Movie) s.getFirstElement()).getImage());
                    detailsImageMouseTrackListener.setHandledObject((AbstractModelObject) s.getFirstElement());
                } else {
                    if (!MovieManager.getInstance().getMovies().isEmpty()) {
                        viewer.setSelection(new StructuredSelection(MovieManager.getInstance().getMovies().get(0)));
                    }
                }
            }
        });
    }

}
