package moviemanager.ui.dialogs;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import moviemanager.data.AbstractModelObject;
import moviemanager.ui.listeners.EditOverlayMouseTrackListener;

/**
 * Provides functionality for the Tabs shown in the MovieManagerDialog. This is
 * the superclass for Tabs showing the Movie Managers Model Objects.
 * 
 * @author twuensche
 *
 */
public abstract class AbstractModelObjectTab {

    // Data binding
    /** The data binding context. **/
    protected DataBindingContext context;
    /**
     * Connects the attributes from the list to their respective bean
     * properties.
     **/
    protected Map<String, IBeanValueProperty> properties;
    /** Connects the bean properties to their respective observable values. **/
    @SuppressWarnings("rawtypes")
    protected Map<IBeanValueProperty, IObservableValue> propertyObservables;
    /** Connects the bean properties to their respective widgets. **/
    protected Map<IBeanValueProperty, Object> propertyWidgets;
    /**
     * List of attributes that are shown in the details view in the tab, in
     * order of their appearance.
     **/
    protected List<String> propertiesList;

    // Widgets
    protected TableViewer viewer;

    public TableViewer getViewer() {
        return viewer;
    }

    protected ScrolledComposite detailsContainerSC;
    protected Composite detailsContainer;
    protected Link noModelObjectLink;
    protected Label detailsImage;
    protected EditOverlayMouseTrackListener detailsImageMouseTrackListener;

    protected void createTab(TabFolder tabFolder, String title) {

        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setText(title);
        {
            Composite tabItemContainer = new Composite(tabFolder, SWT.NONE);

            GridLayout tabItemContainerLayout = new GridLayout();
            tabItemContainerLayout.numColumns = 2;
            tabItemContainerLayout.makeColumnsEqualWidth = false;
            GridData tabItemContainerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            tabItemContainer.setLayout(tabItemContainerLayout);
            tabItemContainer.setLayoutData(tabItemContainerLayoutData);

            // List of available model objects
            viewer = new TableViewer(tabItemContainer,
                    SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
            GridData viewerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            viewerLayoutData.minimumWidth = 300;
            viewerLayoutData.widthHint = 10;
            viewerLayoutData.heightHint = 400;
            viewer.getTable().setLayoutData(viewerLayoutData);

            // TODO: set Content Provider
            // TODO: set Label Provider

            viewer.getTable().setHeaderVisible(false);
            viewer.getTable().setLinesVisible(true);

            // TODO: set viewer input

            // Details for selected model object
            detailsContainerSC = new ScrolledComposite(tabItemContainer, SWT.BORDER | SWT.V_SCROLL);
            GridData movieDetailsContainerSCLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            detailsContainerSC.setLayoutData(movieDetailsContainerSCLayoutData);
            // Possible fix for scrolling not working in Windows. Taken from
            // 'http://stackoverflow.com/questions/25685522/scrolledcomposite-doesnt-scroll-by-mouse-wheel'
            detailsContainerSC.addListener(SWT.Activate, new Listener() {
                public void handleEvent(Event e) {
                    detailsContainerSC.setFocus();
                }
            });
            detailsContainer = new Composite(detailsContainerSC, SWT.NONE);

            GridLayout movieDetailsContainerLayout = new GridLayout();
            movieDetailsContainerLayout.numColumns = 2;
            GridData movieDetailsContainerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            movieDetailsContainerLayoutData.widthHint = 350;
            movieDetailsContainerLayoutData.minimumWidth = 350;
            detailsContainer.setLayout(movieDetailsContainerLayout);
            detailsContainer.setLayoutData(movieDetailsContainerLayoutData);

            // TODO: Add Fields/No Object Link

            // TODO: Make sure new Model Objects always stay on top
            // TODO:Create the context menu for the movie viewer

            detailsContainerSC.setContent(detailsContainer);
            detailsContainerSC.setMinSize(detailsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            detailsContainerSC.setExpandHorizontal(true);
            detailsContainerSC.setExpandVertical(true);

            tabItem.setControl(tabItemContainer);
        }
    }

    /**
     * Create the context menu
     */
    protected abstract void createContextMenu();

    /**
     * Ensure that new model objects always stay on top of the viewer
     */
    protected abstract void sortNewModelObjects();

    /**
     * show a message if the tab is empty
     */
    protected abstract void createNoModelObjectLink();

    /**
     * add fields to tab
     */
    protected abstract void addFields(AbstractModelObject modelObject);

    /**
     * set the viewers input
     */
    protected abstract void setViewerInput();

    /**
     * set Label Provider
     */
    protected abstract void setContentAndLabelProvider();

    /**
     * show the details of the selected ModelObject
     */
    protected abstract void showModelObject(AbstractModelObject modelObject);

    /**
     * Triggers an update of a selection of the detail view.
     */
    public abstract void updateDetailView();

    /**
     * refreshes all viewers
     */
    public void refreshViewers() {
        viewer.refresh();
    }

    /**
     * create a selection changed listener
     */
    protected abstract void createSelectionChangedListener();
}