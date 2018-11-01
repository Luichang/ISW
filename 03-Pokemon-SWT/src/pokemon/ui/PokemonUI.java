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
import pokemon.data.Type;
import pokemon.data.Trainer;

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
	// perform general setup of the Table       	
        shell.setLayout(new GridLayout());
        Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 71;
        table.setLayoutData(data);
  
        // create table headers using TableColumn
        List<String> heads = getTableHeaders();
        for (String head : heads) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(head);
            column.pack();
        }
        // TODO: create table rows using TableItem, each row of the table is one Pokemon
        int i=0;
        for (Pokemon p : getPokemons()) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(i++, Integer.toString(p.getNumber()));
            item.setText(i++, p.getName());
            item.setText(i++, p.getType().name());
            //item.setText(string);
            i = 0;
        }
        // TODO: implement sorting using addListener(SWT.Selection, new Listener() {...

    }

    /**
     * Create table headers String
     * 
     * @return the table headers as a List
     */
    private List<String> getTableHeaders() {
        List<String> ret = new ArrayList<String>();
        // TODO: Create the headers for the Table based on Pokemon attributes
        return ret;
    }

}