package pokemon;
 
import java.util.ArrayList;
import java.util.List;
 
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 
import pokemon.data.Pokemon;
import pokemon.data.Trainer;
import pokemon.data.Type;
import pokemon.ui.TrainerUI;
 
public class PokemonTrainerManager {
 
    /***/
    private static List<Trainer> trainers = new ArrayList<Trainer>();
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        // create a SWT window
        Display display = new Display();
        Shell shell = new Shell(display);
        TrainerUI pui = new TrainerUI(shell, createTrainers());
        pui.open();
    }
 
    private static List<Trainer> createTrainers() {
        Pokemon p0 = new Pokemon("Pikachu", Type.Poison);
        Pokemon p1 = new Pokemon("Carapuce", Type.Water);
        p1.setSwapAllow(false);
        Pokemon p2 = new Pokemon("Raupy", Type.Fire);
        Trainer t0 = new Trainer("Peter", "Lustig");
        t0.addPokemon(p0);
        Trainer t1 = new Trainer("Alisa", "Traurig");
        t1.addPokemon(p1);
        t1.addPokemon(p2);
        trainers.add(t0);
        trainers.add(t1);
        return trainers;
 
    }
}
