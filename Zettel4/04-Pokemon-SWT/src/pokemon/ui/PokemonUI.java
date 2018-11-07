package pokemon.ui;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pokemon.data.Pokemon;

/**
 * Pokemon UIDialog displays Pokemons in SWT Table Widget
 *
 */
public class PokemonUI extends Dialog {

    private List<Pokemon> pokemons = new ArrayList<Pokemon>();

    /**
     * @param parent
     * @param pokemons
     */
    public PokemonUI(Shell parent, List<Pokemon> pokemons) {
        // Pass the default styles here
        this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, pokemons);
    }

    /**
     * @param parent
     * @param style
     * @param pokemons
     */
    public PokemonUI(Shell parent, int style, List<Pokemon> pokemons) {
        // Let users override the default styles
        super(parent, style);
        setText("Pokemon Manager");
        setPokemons(pokemons);
    }

    /**
     * Opens the dialog
     */
    public void open() {
        // Create the dialog window
        Shell shell = new Shell(getParent(), getStyle());
        shell.setText(getText());
        createContents(shell);
        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    /**
     * Creates the dialog's contents
     * 
     * @param shell the dialog window
     */
    private void createContents(final Shell shell) {

        shell.setLayout(new GridLayout());
        Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 71;
        table.setLayoutData(data);
        // table headers
        List<String> heads = getTableHeaders();
        for (String head : heads) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(head);
            column.pack();
        }
        // table contents: each row is one Pokemon
        int i = 0;
        for (Pokemon p : getPokemons()) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(i++, String.valueOf(p.getNumber()));
            item.setText(i++, p.getName());
            item.setText(i++, p.getType().name());
            item.setText(i++, String.valueOf(p.getTrainer()));
            item.setText(i++, String.valueOf(p.getSwaps().size()));
            item.setText(i++, String.valueOf(p.isSwapAllow()));
            item.setText(i++, String.valueOf(p.getCompetitions().size()));
            i = 0;
        }
        // sorting
        for (TableColumn column : table.getColumns()) {
            // create a generic sort listener for each column which sorts
            // columns descend order
            column.setData("SortOrder", 0);
            column.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    // determine the column index
                    int index = 0;
                    if (event.widget instanceof TableColumn) {
                        index = table.indexOf((TableColumn) event.widget);
                    }
                    TableItem[] items = table.getItems();
                    Collator collator = Collator.getInstance(Locale.getDefault());
                    // fetch the actual sort order for the column
                    int sortOrder = 0;
                    try {
                        sortOrder = Integer.valueOf(column.getData("SortOrder").toString());
                    } catch (Exception e) {
                        sortOrder = 0;
                    }

                    for (int i = 0; i < items.length; i++) {
                        String value1 = items[i].getText(index);
                        for (int j = 0; j < i; j++) {
                            String value2 = items[j].getText(index);
                            // sort in descend order
                            if (sortOrder == 0) {
                                if (collator.compare(value1, value2) < 0) {
                                    List<String> values = new ArrayList<String>();
                                    for (int k = 0; k < heads.size(); k++) {
                                        values.add(items[i].getText(k));
                                    }
                                    items[i].dispose();
                                    TableItem item = new TableItem(table, SWT.NONE, j);
                                    item.setText(values.toArray(new String[values.size()]));
                                    items = table.getItems();
                                    break;
                                }
                            }
                            // sort ascend order
                            if (sortOrder == 1) {
                                if (collator.compare(value1, value2) > 0) {
                                    List<String> values = new ArrayList<String>();
                                    for (int k = 0; k < heads.size(); k++) {
                                        values.add(items[i].getText(k));
                                    }
                                    items[i].dispose();
                                    TableItem item = new TableItem(table, SWT.NONE, j);
                                    item.setText(values.toArray(new String[values.size()]));
                                    items = table.getItems();
                                    break;
                                }
                            }
                        }
                    }
                    // change the actual sort order to the opposite value
                    if (sortOrder == 0) {
                        column.setData("SortOrder", 1);
                    } else {
                        column.setData("SortOrder", 0);
                    }
                }
            });
            // stretch columns to the required width
            column.pack();
        }
    }

    /**
     * Create table headers String
     * 
     * @return a list of the table headers
     */
    private List<String> getTableHeaders() {
        List<String> ret = new ArrayList<String>();
        for (Field f : Pokemon.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                ret.add(f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1, f.getName().length()));
            }
        }
        if (ret.contains("Number")) {
            ret.remove("Number");
            ret.add(0, "ID");
        }
        return ret;
    }

}