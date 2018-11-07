package pokemon;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pokemon.data.Pokemon;
import pokemon.data.Trainer;
import pokemon.data.Type;
import pokemon.ui.PokemonUI;


public class PokemonManager {

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
        p0.addSwap(new Swap());
        p0.addSwap(new Swap());
        p0.addSwap(new Swap());
        p0.addSwap(new Swap());
        p0.addCompetition(new Competition());
        p0.addCompetition(new Competition());
        Pokemon p1 = new Pokemon("Carapuce", Type.Water);
        p1.setSwapAllow(false);
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        p1.addCompetition(new Competition());
        Pokemon p2 = new Pokemon("Raupy", Type.Fire);
        p2.addSwap(new Swap());
        p2.addSwap(new Swap());
        p2.addSwap(new Swap());
        p2.addCompetition(new Competition());
        pokemons.add(p0);
        pokemons.add(p1);
        pokemons.add(p2);
        Trainer t0 = new Trainer("Peter", "Lustig");
        t0.addPokemon(p0);
        Trainer t1 = new Trainer("Alisa", "Traurig");
        t1.addPokemon(p1);
        t1.addPokemon(p2);
        return pokemons;
    }
}
