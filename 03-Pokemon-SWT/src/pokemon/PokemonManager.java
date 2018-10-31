package pokemon;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pokemon.data.Pokemon;
import pokemon.ui.PokemonUI;

/**
 * The PokemonManager Class
 */
public class PokemonManager {

    private static List<Pokemon> pokemons = new ArrayList<Pokemon>();


    public static void main(String[] args) {
        // create a SWT window
        Display display = new Display();
        Shell shell = new Shell(display);
        PokemonUI pui;
        // TODO: initialize and open the PokemonUI right here !
    }
}
