package pokemon.ui;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Text;

import pokemon.data.Pokemon;
import pokemon.data.Type;

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
        
        
        
        Menu menuTable = new Menu(table);
        table.setMenu(menuTable);
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        // Create menu item
        MenuItem create = new MenuItem(menuTable, SWT.NONE);
        create.setText("Create Pokemon");
        create.addSelectionListener(new SelectionListener() {
            @Override
	    public void widgetSelected(SelectionEvent e) {
		// TODO Automatisch generierter Methodenstub
		//Table createEingabe = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        	
        	Pokemon temp = new Pokemon(" ", Type.Fire);
        	
        	TableItem item = new TableItem(table, SWT.NONE);
        	
        	item.setText(0, String.valueOf(temp.getNumber()));
        	item.setText(1, temp.getName());
                item.setText(2, temp.getType().name());
                item.setText(3, String.valueOf(temp.getTrainer()));
                item.setText(4, String.valueOf(temp.getSwaps().size()));
                item.setText(5, String.valueOf(temp.isSwapAllow()));
                item.setText(6, String.valueOf(temp.getCompetitions().size()));
                
                
                
                
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Automatisch generierter Methodenstub
		
	    }
        });
        
        MenuItem delete = new MenuItem(menuTable, SWT.NONE);
        delete.setText("Delete Pokemon");
        delete.addSelectionListener(new SelectionListener() {
            @Override
	    public void widgetSelected(SelectionEvent e) {
        	Point pt = new Point(e.x, e.y);
        	int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                	Rectangle rect = item.getBounds(i);
                	if (rect.contains(pt)) {
                	    final int column = i;
                	    final Text text = new Text(table, SWT.NONE);
                	    Listener textListener = new Listener() {
                		public void handleEvent(final Event e) {
                		    switch (e.type) {
                		    case SWT.FocusOut:
                			item.setText(column, text.getText());
                			text.dispose();
                			break;
                		    case SWT.Traverse:
                			switch (e.detail) {
                			case SWT.TRAVERSE_RETURN:
                			    item.setText(column, text.getText());
                			    // FALL THROUGH
                			case SWT.TRAVERSE_ESCAPE:
                			    text.dispose();
                			    e.doit = false;
                			}
                			break;
                		    }
                		}
                	    };
                	    text.addListener(SWT.FocusOut, textListener);
                	    text.addListener(SWT.Traverse, textListener);
                	    editor.setEditor(text, item, i);
                	    text.setText(item.getText(i));
                	    text.selectAll();
                	    text.setFocus();
                	    return;
                    }
                  }
                }
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Automatisch generierter Methodenstub
		
	    }
        });
        MenuItem swap = new MenuItem(menuTable, SWT.NONE);
        swap.setText("Swap Pokemon");

        // Do not show menu, when no item is selected
        table.addListener(SWT.MenuDetect, new Listener() {
            public void handleEvent(Event event) {
                if (table.getSelectionCount() <= 0) {
                    event.doit = false;
                }
            }
        });
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
/*package pokemon.ui;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
//import org.eclipse.jface.viewers.CellEditor;
//import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import pokemon.data.Pokemon;
import pokemon.data.Trainer;
import pokemon.data.Type;

/**
 * Pokemon UIDialog displays Pokemons in SWT Table Widget
 *
 */
/*public class PokemonUI extends Dialog {

	private List<Pokemon> pokemons = new ArrayList<Pokemon>();

	*//**
	 * @param parent
	 * @param pokemons
	 *//*
	public static void storePokemons(List<Pokemon> ps) {
		String STORAGE_PATH = new String("pokemons.data");
		try {
	        System.out.println("Storing " + ps.size() + " pokemons");
	        // use ObjectOutputStream to write Objects
	        // use FileOutputStream to write to a File
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_PATH));
	        oos.writeObject(ps);
	        oos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@SuppressWarnings("unchecked")
	public static List<Pokemon> loadPokemons() {
		String STORAGE_PATH = new String("pokemons.data");
	    List<Pokemon> ps = new ArrayList<Pokemon>();
	    try {
	        // use ObjectInputStream to read Objects
	        // use FileInputStream to read from a File
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_PATH));
	        ps = (List<Pokemon>) ois.readObject();
	        ois.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    System.out.println("Loaded " + ps.size() + " pokemons");
	    return ps;
	}
	
	public PokemonUI(Shell parent, List<Pokemon> pokemons) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, pokemons);
	}

	*//**
	 * @param parent
	 * @param style
	 * @param pokemons
	 *//*
	public PokemonUI(Shell parent, int style, List<Pokemon> pokemons) {
		// Let users override the default styles
		super(parent, style);
		setText("Pokemon Manager");
		setPokemons(pokemons);
	}

	*//**
	 * Opens the dialog
	 *//*
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

	*//**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 *//*
	private Table createDisplay(final Shell shell){
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
        int j = 0;
		for (Pokemon p : pokemons) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(p);
		    
            item.setText(j++, p.getName());
            item.setText(j++, p.getType().name());
            Trainer trainer=p.getTrainer();
            item.setText(j++, trainer.getFirstname() + " " + trainer.getLastname());
            item.setText(j++, "" +p.getNumber());
            item.setText(j++, ""+p.getSwaps().size());
            item.setText(j++, ""+p.isSwapAllow());
            item.setText(j++, ""+p.getCompetitions().size());
            j = 0;	
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
                                 // TODO: create table rows using TableItem, each row of the table is one Pokemon
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
        return table;
	}
	private void createContents(final Shell shell) {

		Table table = createDisplay(shell);
       
      
     // create a context menu
        Menu contextMenu = new Menu(shell);
        table.setMenu(contextMenu);
        MenuItem miCreate = new MenuItem(contextMenu, SWT.None);
        miCreate.setText("Create Pokemon");
     // Listener for the action performed when menu item is selected
     miCreate.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
             Object o = e.getSource();
             if (o != null) {
                 if (o instanceof MenuItem) {
                     System.out.println(o.getClass().getSimpleName() + " '"
                             + ((MenuItem) o).getText() + "' has been selected");
                 }
             }
             //Add default Pokemon 
             //Pokemon p0 = trainer.getPokemon(0);
             Pokemon p = new Pokemon("Domi", Type.Fire);
             //Trainer t = p0.getTrainer();
             p.setSwapAllow(true);
             //p.setTrainer(t); 
             // TODO: Create a new TableItem with the Pokemon data
             TableItem item = new TableItem(table, SWT.NONE);
             // attach the Pokemon instance to the item
             item.setData(p);
             item.setText(0, p.getName());
             item.setText(1, p.getType().name());
             item.setText(2, " ");
             item.setText(3, "" + p.getNumber());
             item.setText(4, String.valueOf(p.getSwaps().size()));
             item.setText(5, java.lang.Boolean.toString(p.isSwapAllow()));
             item.setText(6, String.valueOf(p.getCompetitions().size()));
             pokemons.add(p);
             //t.addPokemon(p);
             
             // store
     		 storePokemons(pokemons);
         }
     });
     // Listener to show the Menu when a right click is performed in the
     // table
     table.addListener(SWT.MouseDown, new Listener() {
         @Override
         public void handleEvent(Event event) {
             TableItem[] selection = table.getSelection();
             if (selection.length != 0 && (event.button == 3)) {
                 contextMenu.setVisible(true);
             }
         }
     });
     
      Handle deletePokemon
     
     MenuItem miDelete = new MenuItem(contextMenu, SWT.None);
     miDelete.setText("Delete Pokemon");
  // Listener for the action performed when menu item is selected
     miDelete.addSelectionListener(new SelectionAdapter() {
     @Override
     public void widgetSelected(SelectionEvent e) {
    	 Object o = e.getSource();
			if (o != null) {
				if (o instanceof MenuItem) {
					System.out.println(o.getClass().getSimpleName() + " '"
							+ ((MenuItem) o).getText() + "' has been selected");

				}
			}
			if (pokemons.size()>0){
			final TableItem item = table.getSelection()[0];
			
			if (item.getData() != null) {
				if (item.getData() instanceof Pokemon)
				pokemons.remove(((Pokemon)item.getData()));
				System.out.print(((Pokemon) item.getData()).getName() + " gel�scht");
				
	            // store
				storePokemons(pokemons);
			}
			for(int i=0; i<table.getItems().length;i++){
				if(table.getItems()[i] == item){
					table.remove(i);	
				}
			}
     }
     }
     });
     
  // editor for the text table cell
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		// and its listener for MouseDoubleClick events and text editor
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = table.getTopIndex();
				// iterate through the tables row
				while (index < table.getItemCount()) {
					boolean visible = false;
					final TableItem item = table.getItem(index);
					
					// aendere Name
					int columnIndex = 0;
					Rectangle rect = item.getBounds(columnIndex);
					if (rect.contains(pt)) {
						// create a text input box
						final Text text = new Text(table, SWT.NONE);
		                 String AlterText = item.getText();
		                 Listener textListener = new Listener() {
		                   public void handleEvent(final Event e) {
		                     switch (e.type) {
		                     case SWT.FocusOut:
		                       item.setText(columnIndex, text.getText());
		                       text.dispose();
		                       int Pokenr = Integer.parseInt(item.getText(3));
		                       Pokemon p = Trainer.getPokemon(Pokenr);
		                       //Setze den Namen des betreffenden Pokemons
		                       if(AlterText != item.getText()){
		                    	   p.setName(item.getText());
		                    	   System.out.println("Name des Pokemons von " + AlterText + " geaendert in " + item.getText());
		                           // store
		                   		   storePokemons(pokemons);
		                       }
		                      break;
		                     case SWT.Traverse:
		                       switch (e.detail) {
		                       case SWT.TRAVERSE_RETURN:
		                         item
		                             .setText(columnIndex, text
		                                 .getText());
		                         int Pokenr2 = Integer.parseInt(item.getText(3));
		                         Pokemon p2 = Trainer.getPokemon(Pokenr2);
		                         //Setze den Namen des betreffenden Pokemons
		                         if(AlterText != item.getText()){
		                      	   p2.setName(item.getText());
		                      	   System.out.println("Name des Pokemons von " + AlterText + " ge�ndert in " + item.getText());
		                           // store
		                   		  storePokemons(pokemons);
		                         }
										// FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										text.dispose();
										e.doit = false;
									}
									break;
								}
							}
						};
						// add the listener to the text input box for
						// deselection
						text.addListener(SWT.FocusOut, textListener);
						// and selection
						text.addListener(SWT.Traverse, textListener);
						editor.setEditor(text, item, columnIndex);
						text.setText(item.getText(columnIndex));
						text.selectAll();
						text.setFocus();
						return;
					}
					
					//TYPE
					// the tables columns index creating the text editor is the
					// 2nd column (=1) of table
					rect = item.getBounds(1);
					if (rect.contains(pt)){
						if (item.getData() != null) {
							if (item.getData() instanceof Pokemon)
								
							createCComboEditor(table, item, 1, ((Pokemon)item.getData()).getType(), pokemons);
						}
						
					}
					
					//SWAPABLE
					// the tables columns index creating the text editor is the
					// 6th column (=5) of table
					rect = item.getBounds(5);
					if (rect.contains(pt)){
						if (item.getData() != null) {
							if (item.getData() instanceof Pokemon){
								item.setText(5,"");
								createCComboEditor(table, item, 5, java.lang.Boolean.toString(((Pokemon)item.getData()).isSwapAllow()), pokemons);
							}
						}
						
					}
					
					
					
					
					if (!visible && rect.intersects(clientArea)) {
						visible = true;
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
//     
     
		
	}

	*//**
	 * Create table headers String
	 * 
	 * @return
	 *//*
	private List<String> getTableHeaders() {
		List<String> ret = new ArrayList<String>();
		for (Field f : Pokemon.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                ret.add(f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1, f.getName().length()));
            }
        }
		return ret;
	}


*//**
 * Creates a CCombo with an attached Editor for a TableItem in the specified
 * columnIndex with selected Type as default
 *
 * @param table
 *            the Table to create the CCombo in
 * @param item
 *            the TableItem (row) to create the CCombo in
 * @param columnIndex
 *            the column index to create the CCombo in
 * @param selected
 *            the Type selected as default
 *//*
private static void createCComboEditor(Table table, TableItem item, int columnIndex, Type selected, List<Pokemon> pokemons) {
    // editor for the drop down element
    final TableEditor ceditor = new TableEditor(table);
    // create a Drop Down box based on the contents of a enum
    CCombo combo = new CCombo(table, SWT.NONE);
    combo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
//            System.out.println(e);
            // get the selected Type
            Type t = Type.valueOf(combo.getText());
            // set the selected type to the pokemon instance attached
            if (item.getData() != null) {
                if(item.getData() instanceof Pokemon){
                    ((Pokemon) item.getData()).setType(t);
                    // print changed pokemon
                    System.out.println("Typ des Pokemons ge�ndert in " + t.name());   
                 // store
           		 storePokemons(pokemons);
                }
            }
        }
    });
    String tname = "";
    for (Type t : Type.values()) {
        tname = t.name();
        combo.add(tname);
    }
    if (selected == null) {
        // set the last element as selected
        combo.setText(tname);
    } else {
        combo.setText(selected.name());
    }
    ceditor.grabHorizontal = true;
    // place the combo box in the actual row of
    // table
    ceditor.setEditor(combo, item, columnIndex);
}
*//**
 * Creates a CCombo with an attached Editor for a TableItem in the specified
 * columnIndex with selected SwapAllow as default
 *
 * @param table
 *            the Table to create the CCombo in
 * @param item
 *            the TableItem (row) to create the CCombo in
 * @param columnIndex
 *            the column index to create the CCombo in
 * @param selected
 *            the SwapAllow selected as default
 *//*
private static void createCComboEditor(Table table, TableItem item, int columnIndex, String selected, List<Pokemon> pokemons) {
	 // editor for the drop down element
    final TableEditor ceditor = new TableEditor(table);
    // create a Drop Down box based on the contents of a enum
    CCombo combo = new CCombo(table, SWT.NONE);
    combo.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            // get the selected Type
            Boolean t = Boolean.valueOf(combo.getText());
            // set the selected type to the pokemon instance attached
            if (item.getData() != null) {
                    ((Pokemon) item.getData()).setSwapAllow(t);
                    // print changed pokemon
                    
                    System.out.println("SwapAllow von "+ ((Pokemon) item.getData()).getName() +"  auf " + java.lang.Boolean.toString(((Pokemon)item.getData()).isSwapAllow()) + " ge�ndert");
                 // store
           		 storePokemons(pokemons);
            }
        }
    });
    
        combo.add("true");
        combo.add("false");
    
        combo.setText(selected);
    
    ceditor.grabHorizontal = true;
    // place the combo box in the actual row of
    // table
    ceditor.setEditor(combo, item, columnIndex);
}
}*/