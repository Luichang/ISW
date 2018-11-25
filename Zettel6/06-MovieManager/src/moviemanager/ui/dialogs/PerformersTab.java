package moviemanager.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
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

public class PerformersTab extends AbstractModelObjectTab {

    @SuppressWarnings("rawtypes")
    public PerformersTab(TabFolder tabFolder, String title) {
        super();

        context = new DataBindingContext(MovieManagerUIUtil.getDefaultRealm());

        // Generate properties for movies
        propertyObservables = new HashMap<IBeanValueProperty, IObservableValue>();
        propertyWidgets = new HashMap<IBeanValueProperty, Object>();
        // Initialize the value properties
        properties = new HashMap<String, IBeanValueProperty>();

        propertiesList = new ArrayList<String>();
        propertiesList.add("firstName");
        propertiesList.add("lastName");
        propertiesList.add("biography");
        propertiesList.add("alternateNames");
        propertiesList.add("country");
        propertiesList.add("dateOfBirth");
        propertiesList.add("rating");
        propertiesList.add("imdbID");
        propertiesList.add("movies");

        for (String property : propertiesList) {
            properties.put(property, BeanProperties.value(Performer.class, property));
        }

        createTab(tabFolder, title);
    }

    @Override
    protected void createTab(TabFolder tabFolder, String title) {
        super.createTab(tabFolder, title);
        setContentAndLabelProvider();
        setViewerInput();

        if (MovieManager.getInstance().getPerformers().size() > 0) {
            viewer.setSelection(new StructuredSelection(MovieManager.getInstance().getPerformers().get(0)));
            // Add fields for all performer attributes
            addFields((AbstractModelObject) ((StructuredSelection) viewer.getSelection()).getFirstElement());
        } else {
            createNoModelObjectLink();
        }

        createSelectionChangedListener();
        sortNewModelObjects();
        createContextMenu();

    }
    // Creates the different sorting options and their relevant comparators

    private MenuManager createSort() {
        MenuManager sortByMenu = new MenuManager("Sort by", null);
        String suffixDescending = " (descending)";
        String suffixAscending = " (ascending)";
        // Title
        String prefix = "First name";
        sortByMenu.add(new Action(prefix + suffixDescending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Performer m1 = (Performer) o1;
                        Performer m2 = (Performer) o2;
                        return (-1) * m1.getFirstName().compareTo(m2.getFirstName());
                    }
                });
            }
        });
        sortByMenu.add(new Action(prefix + suffixAscending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Performer m1 = (Performer) o1;
                        Performer m2 = (Performer) o2;
                        return m1.getFirstName().compareTo(m2.getFirstName());
                    }
                });
            }
        });
        // Rating
        prefix = "Last name";
        sortByMenu.add(new Action(prefix + suffixDescending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Performer m1 = (Performer) o1;
                        Performer m2 = (Performer) o2;
                        return (-1) * m1.getLastName().compareTo(m2.getLastName());
                    }
                });
            }
        });
        sortByMenu.add(new Action(prefix + suffixAscending) {
            @Override
            public void run() {
                viewer.setComparator(new ViewerComparator() {
                    public int compare(Viewer v, Object o1, Object o2) {
                        Performer m1 = (Performer) o1;
                        Performer m2 = (Performer) o2;
                        return m1.getLastName().compareTo(m2.getLastName());
                    }
                });
            }
        });
        return sortByMenu;
    }

    @Override
    protected void createContextMenu() {
        // Create the context menu for the performer viewer
        MenuManager performerViewerMenuManager = new MenuManager();
        performerViewerMenuManager.setRemoveAllWhenShown(true);
        performerViewerMenuManager.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager arg0) {
                if (!MovieManager.getInstance().getPerformers().isEmpty()) {
                    performerViewerMenuManager.add(new Action("Remove selected performer") {
                        @Override
                        public void run() {
                            String message = "Are you sure you want to remove the selected performer?";
                            int size = ((Performer) ((StructuredSelection) viewer.getSelection()).getFirstElement())
                                    .getMovies().size();
                            if (size > 0) {
                                message += "\nNote that this performer is associated with " + size + " movie(s).";
                            }
                            if (MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
                                    "Remove Selected Performer?", message)) {
                                removePerformer();
                            }
                        }
                    });
                    // Sorting sub context menu
                    if (MovieManager.getInstance().getMovies().size() > 1) {
                        performerViewerMenuManager.add(createSort());
                    }
                }
            }
        });
        Menu performerViewerContextMenu = performerViewerMenuManager.createContextMenu(viewer.getTable());
        viewer.getTable().setMenu(performerViewerContextMenu);

    }

    @Override
    protected void sortNewModelObjects() {
        viewer.setComparator(new ViewerComparator() {
            public int compare(Viewer v, Object o1, Object o2) {
                Performer p1 = (Performer) o1;
                Performer p2 = (Performer) o2;
                if (p1.getFirstName().equals("New") && p1.getLastName().equals("Performer")) {
                    return -1;
                } else if (p2.getFirstName().equals("New") && p2.getLastName().equals("Performer")) {
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
        noModelObjectLink.setText("There are no performers to display.");
        noModelObjectLink.pack();

        detailsContainerSC.setMinSize(detailsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * Adds editable fields for all defined attributes to the performer details
     * container. The fields contain the values from the given movie.
     * 
     * @param p the performer whose attributes are to be shown in the fields
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void addFields(AbstractModelObject modelObject) {
        if (!(modelObject instanceof Performer)) {
            System.err.println("ModelObject is not a performer\n");
        }

        detailsImage = new Label(detailsContainer, SWT.BORDER);
        GridData performerDetailsImageLayoutData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
        performerDetailsImageLayoutData.horizontalSpan = 2;
        detailsImage.setLayoutData(performerDetailsImageLayoutData);
        detailsImage.setImage(modelObject.getImage());

        detailsImageMouseTrackListener = new EditOverlayMouseTrackListener(modelObject, detailsImage);
        detailsImage.addMouseTrackListener(detailsImageMouseTrackListener);
        detailsImage.addMouseListener(detailsImageMouseTrackListener.getEditMouseListener());

        for (String attribute : propertiesList) {
            String label = "";
            if (attribute.equals("imdbID")) {
                label = "IMDB ID";
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
        viewer.setInput(MovieManager.getInstance().getPerformers());

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void setContentAndLabelProvider() {
        viewer.setContentProvider(new ObservableListContentProvider());

        IObservableSet knownElements = ((ObservableListContentProvider) viewer.getContentProvider()).getKnownElements();
        IObservableMap firstName = properties.get("firstName").observeDetail(knownElements);
        IObservableMap lastName = properties.get("lastName").observeDetail(knownElements);
        IObservableMap rating = properties.get("rating").observeDetail(knownElements);

        IObservableMap[] observerdProperties = { firstName, lastName, rating };

        viewer.setLabelProvider(new ObservableMapLabelProvider(observerdProperties) {
            private Map<Object, Image> performerImages = new HashMap<Object, Image>();

            @Override
            public Image getColumnImage(Object o, int columnIndex) {
                Performer p = (Performer) o;
                switch (columnIndex) {
                    case 0:
                        // For now, we combine the performer and rating images
                        // into one image in order to fix the image dimension
                        // issue
                        Image performerImage = null;
                        if (performerImages.containsKey(o)) {
                            performerImages.get(o).dispose();
                        }
                        performerImage = MovieManagerUIUtil.createTableItemImage(p.getThumbnailImage(), p, false);
                        performerImages.put(o, performerImage);
                        return performerImage;
                    // return p.getThumbnailImage();
                    default:
                        return null;
                }
            }

            @Override
            public String getColumnText(Object o, int columnIndex) {
                Performer p = (Performer) o;
                switch (columnIndex) {
                    case 0:
                        return p.getFirstName() + " " + p.getLastName();
                    default:
                        return null;
                }
            }
        });
    }

    /**
     * Removes the currently selected performer from the database and handles
     * the disposal and creation of widgets.
     */
    protected void removePerformer() {
        Performer toRemove = (Performer) ((StructuredSelection) viewer.getSelection()).getFirstElement();
        // If the performer database will be empty after removing the selected
        // performer, remove the widgets for the fields and add the info label
        if (MovieManager.getInstance().getPerformers().size() == 1) {
            for (Control c : detailsContainer.getChildren()) {
                c.dispose();
            }
            createNoModelObjectLink();
            detailsContainer.layout();
        }
        MovieManager.getInstance().removePerformer(toRemove);
    }

    /**
     * Adds a new performer to the performer database and handles the disposal
     * and creation of widgets.
     * 
     * @param m the movie that the performer is linked to. Can be null, in which
     * case the movie is selected from a dialog
     */
    public void addPerformer(Movie m) {
        if (m == null) {
            // Make sure the newly created performer is always linked to a movie
            ElementSelectionDialog dialog = new ElementSelectionDialog(Display.getDefault().getActiveShell(),
                    Movie.class);

            if (dialog.open() == Window.OK) {
                m = (Movie) dialog.getSelection();
            }
        }
        if (m != null) {
            Performer toAdd = new Performer();
            toAdd.setFirstName("New");
            toAdd.setLastName("Performer");
            toAdd.linkMovie(m);

            // If the movie database is empty prior to adding the movie, remove
            // the info label and add the widgets for the fields
            if (MovieManager.getInstance().getPerformers().isEmpty()) {
                noModelObjectLink.dispose();
                noModelObjectLink = null;
                addFields(toAdd);
                detailsContainer.layout();
            }
            // Make sure the new performer is created on top of the list
            ViewerComparator lastComparator = viewer.getComparator();
            viewer.setComparator(new ViewerComparator() {
                public int compare(Viewer v, Object o1, Object o2) {
                    Performer p1 = (Performer) o1;
                    Performer p2 = (Performer) o2;
                    if (p1.getFirstName().equals("New") && p1.getLastName().equals("Performer")) {
                        return -1;
                    } else if (p2.getFirstName().equals("New") && p2.getLastName().equals("Performer")) {
                        return 1;
                    } else {
                        if (lastComparator != null) {
                            return lastComparator.compare(viewer, p1, p2);
                        } else {
                            return 0;
                        }
                    }
                }
            });

            MovieManager.getInstance().addPerformer(toAdd, false);
            viewer.setSelection(new StructuredSelection(toAdd));

            showModelObject(toAdd);
        }

    }

    @Override
    protected void showModelObject(AbstractModelObject modelObject) {
        if (!(modelObject instanceof Performer)) {
            System.err.println("ModelObject is not a performer\n");
        }
        viewer.setSelection(new StructuredSelection(modelObject));
        viewer.reveal(modelObject);

    }

    /**
     * Triggers an update of the performers detail view.
     */
    @Override
    public void updateDetailView() {
        if (noModelObjectLink != null && !MovieManager.getInstance().getPerformers().isEmpty()) {
            noModelObjectLink.dispose();
            noModelObjectLink = null;
            Performer p = (Performer) ((StructuredSelection) viewer.getSelection()).getFirstElement();
            if (p == null) {
                p = MovieManager.getInstance().getPerformers().get(0);
            }
            addFields(p);
        } else if (noModelObjectLink == null && MovieManager.getInstance().getPerformers().isEmpty()) {
            for (Control c : detailsContainer.getChildren()) {
                c.dispose();
            }
            createNoModelObjectLink();
            detailsContainer.layout();
        }
    }

    @Override
    public void refreshViewers() {
        super.refreshViewers();

        TableViewer movieDetailsPerformersViewer = (TableViewer) propertyWidgets.get(properties.get("movies"));
        if (movieDetailsPerformersViewer != null && !movieDetailsPerformersViewer.getTable().isDisposed()) {
            movieDetailsPerformersViewer.refresh();
        }
    }

    @Override
    protected void createSelectionChangedListener() {
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
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
                                (Performer) s.getFirstElement());
                    }
                    // Update the detail image and the attached listeners
                    detailsImage.setImage(((Performer) s.getFirstElement()).getImage());
                    detailsImageMouseTrackListener.setHandledObject((AbstractModelObject) s.getFirstElement());
                } else {
                    if (!MovieManager.getInstance().getPerformers().isEmpty()) {
                        viewer.setSelection(new StructuredSelection(MovieManager.getInstance().getPerformers().get(0)));
                    }
                }
            }
        });
    }

}
