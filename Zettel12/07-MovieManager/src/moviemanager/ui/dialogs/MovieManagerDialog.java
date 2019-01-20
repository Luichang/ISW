package moviemanager.ui.dialogs;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolBar;

import moviemanager.MovieManager;
import moviemanager.data.Movie;
import moviemanager.data.Performer;
import moviemanager.ui.widgets.SearchWidget;
import moviemanager.util.MovieManagerUIUtil;

/**
 * The main dialog for the Movie Manager application.
 *
 */

public class MovieManagerDialog extends Dialog {

    private static final String DIALOG_TITLE = "Movie Manager";
    private static final int DIALOG_WIDTH = 1000;
    private static final int DIALOG_HEIGHT = 800;

    private static final int DIALOG_MIN_WIDTH = 800;
    private static final int DIALOG_MIN_HEIGHT = 400;

    // Tabs
    private MoviesTab moviesTab;
    private PerformersTab performersTab;

    // Widgets
    // Toolbar
    private ToolBarManager toolBarManager;
    private SearchWidget searchWidget;
    // Main dialog area
    private TabFolder tabFolder;

    /**
     * Creates a new instance of this dialog under the given shell.
     * 
     * @param parentShell the parent shell
     */
    public MovieManagerDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MIN);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Create the container for all widgets in the movie manager dialog
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout containerLayout = new GridLayout();
        GridData containerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        container.setLayout(containerLayout);
        container.setLayoutData(containerLayoutData);

        // Create the info toolbar
        ToolBar toolBar = new ToolBar(container, SWT.FLAT);
        toolBarManager = new ToolBarManager(toolBar);
        // Search button
        Action searchAction = new Action("Click to search for movies and performers") {
            @Override
            public void run() {
                searchWidget = new SearchWidget(Display.getDefault(),
                        toolBarManager.getControl().toDisplay(toolBarManager.getControl().getLocation()));
                searchWidget.open();
            }
        };
        searchAction.setImageDescriptor(ImageDescriptor.createFromImage(MovieManagerUIUtil.getSearchImage()));
        toolBarManager.add(searchAction);
        toolBarManager.add(new Separator());

        toolBarManager.createControl(container);
        toolBarManager.update(true);

        // Create the tab folder containing the 'Movies' and 'Performers' tabs
        tabFolder = new TabFolder(container, SWT.BORDER);
        GridData tabFolderLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tabFolder.setLayoutData(tabFolderLayoutData);

        // Create the 'Movies' tab
        createMoviesTab();
        // Create the 'Performers' tab
        createPerformersTab();

        getShell().addControlListener(new ControlListener() {

            @Override
            public void controlMoved(ControlEvent arg0) {
                if (searchWidget != null && !searchWidget.getShell().isDisposed()) {
                    searchWidget.setLocation(
                            toolBarManager.getControl().toDisplay(toolBarManager.getControl().getLocation()));
                }
            }

            @Override
            public void controlResized(ControlEvent arg0) {
                if (searchWidget != null && !searchWidget.getShell().isDisposed()) {
                    searchWidget.setLocation(
                            toolBarManager.getControl().toDisplay(toolBarManager.getControl().getLocation()));
                }
            }

        });

        return container;
    }

    /**
     * Creates the 'Movies' tab.
     */
    private void createMoviesTab() {
        moviesTab = new MoviesTab(tabFolder, "Movies");
    }

    /**
     * Creates the 'Performers' tab.
     */
    private void createPerformersTab() {
        performersTab = new PerformersTab(tabFolder, "Performers");
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(DIALOG_TITLE);
        newShell.setMinimumSize(DIALOG_MIN_WIDTH, DIALOG_MIN_HEIGHT);
        newShell.setImage(MovieManagerUIUtil.getMovieManagerImage());
    }

    @Override
    protected Point getInitialSize() {
        return new Point(DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    // We do not need a button bar for the movie manager dialog. Taken from
    // 'http://stackoverflow.com/questions/25424150/remove-button-bar-from-jface-dialog'
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.marginHeight = 0;
    }

    /**
     * Adds a new movie to the movie database and handles the disposal and
     * creation of widgets.
     */
    public void addMovie() {
        moviesTab.addMovie();
    }

    /**
     * Adds a new performer to the performer database and handles the disposal
     * and creation of widgets.
     * 
     * @param m the movie that the performer is linked to. Can be null, in which
     * case the movie is selected from a dialog
     */
    public void addPerformer(Movie m) {
        performersTab.addPerformer(m);
    }

    public TableViewer getMovieViewer() {
        return moviesTab.getViewer();
    }

    /**
     * Refreshes all viewers in the movie manager dialog.
     */
    public void refreshViewers() {
        moviesTab.refreshViewers();
        performersTab.refreshViewers();
    }

    /**
     * Shows the detail view for the given movie.
     * 
     * @param m the movie
     */
    public void showMovie(Movie m) {
        tabFolder.setSelection(0);
        moviesTab.showModelObject(m);
    }

    /**
     * Shows the detail view for the given performer.
     * 
     * @param p the performer
     */
    public void showPerformer(Performer p) {
        tabFolder.setSelection(1);
        performersTab.showModelObject(p);
    }

    /**
     * Gets the toolbar manager for the info toolbar.
     * 
     * @return the toolbar manager
     */
    public ToolBarManager getToolBarManager() {
        return toolBarManager;
    }

    @Override
    public void handleShellCloseEvent() {
        // Ask users if they want to save the changes if the movie manager's
        // data has been modified
        if (MovieManager.getInstance().isDirty()) {
            if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Save Changes?",
                    "Some data has been modified. Do you want to save the changes?")) {
                MovieManager.getInstance().saveData();
            }
        }

        super.handleShellCloseEvent();
    }

    /**
     * Triggers an update of a selection of widgets in the movie detail view.
     */
    public void updateMovieDetailView() {
        moviesTab.updateDetailView();
    }

    /**
     * Triggers an update of the performers detail view.
     */
    public void updatePerfomerDetailView() {
        performersTab.updateDetailView();
    }
}
