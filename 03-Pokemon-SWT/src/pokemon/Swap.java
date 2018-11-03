package pokemon;

import java.util.Date;

import pokemon.data.Pokemon;
import pokemon.data.Trainer;

public class Swap {

    private String id;
    private Date date;
    private Trainer trainer1;
    private Trainer trainer2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;

    // this seems exactly like a constructor, what is the reasoning behind making this a function?
    public void execute(Pokemon p1, Pokemon p2) {
        if (p1.isSwapAllow() && p2.isSwapAllow()) {
            if (p1.getTrainer() != p2.getTrainer()) {
                // swapping is allowed
                // store Pokemons and Trainers in the swap
                this.pokemon1 = p1;
                this.pokemon2 = p2;
                this.trainer1 = p1.getTrainer();
                this.trainer2 = p2.getTrainer();
                this.date = new Date();
                this.id = "" + System.currentTimeMillis();
                // remove the Pokemons from the Trainers
                this.trainer1.getPokemons().remove(p1);
                this.trainer2.getPokemons().remove(p2);
                // reassign the Pokemons to the Trainers
                this.trainer1.addPokemon(p2);
                this.trainer2.addPokemon(p1);
                // store the Swap in Pokemons Swap history
                p1.addSwap(this);
                p2.addSwap(this);
            } else {
                System.out.printf("No swap: Trainers '%s' == '%s' are identical!\n", p1.getTrainer(), p2.getTrainer());
            }
        } else {
            System.out.printf("No swap: Pokemons '%s' and '%s' are NOT both allowed to be swapped!\n", p1.getName(),
                    p2.getName());
        }
    }

    
    // Seems like a less elegant way of creating the class, than just with a constructor
    public Date getDate() {
        return date;
    }

    // Why would you need to change the date, once the trade happened?
    public void setDate(Date date) {
        this.date = date;
    }

    public Pokemon getP1() {
        return this.pokemon1;
    }

    public void setP1(Pokemon p1) {
        this.pokemon1 = p1;
    }

    public Pokemon getP2() {
        return this.pokemon2;
    }

    public void setP2(Pokemon p2) {
        this.pokemon2 = p2;
    }

    public Trainer getT1() {
        return this.trainer1;
    }

    public void setT1(Trainer t1) {
        this.trainer1 = t1;
    }

    public Trainer getT2() {
        return this.trainer2;
    }

    public void setT2(Trainer t2) {
        this.trainer2 = t2;
    }

    public String getid() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
