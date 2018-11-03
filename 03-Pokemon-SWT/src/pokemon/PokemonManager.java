package pokemon;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pokemon.data.Pokemon;
import pokemon.data.Trainer;
import pokemon.data.Type;
import pokemon.ui.PokemonUI;

/**
 * The PokemonManager Class
 */
public class PokemonManager {

    private static List<Trainer> trainers = new ArrayList<Trainer>();
    private static List<Pokemon> pokemons = new ArrayList<Pokemon>();


    public static void main(String[] args) {
        // create a SWT window
        Display display = new Display();
        Shell shell = new Shell(display);
        PokemonUI pui = new PokemonUI(shell, createPokemons());
        pui.open();
    }
    
    private static List<Pokemon> createPokemons() {
        Pokemon p0 = new Pokemon("Pikachu", Type.Poison);
        pokemons.add(p0);
        Pokemon p1 = new Pokemon("Carapuce", Type.Water);
        pokemons.add(p1);
        p1.setSwapAllow(false);
        Pokemon p2 = new Pokemon("Raupy", Type.Fire);
        pokemons.add(p2);
        Trainer t0 = new Trainer("Peter", "Lustig");
        t0.addPokemon(p0);
        Trainer t1 = new Trainer("Alisa", "Traurig");
        t1.addPokemon(p1);
        t1.addPokemon(p2);
        trainers.add(t0);
        trainers.add(t1);
        return pokemons;
    }
}
