package pokemon;

import java.util.Date;

import pokemon.data.Pokemon;
import pokemon.data.Trainer;

public class Swap {

    private Date date;
    private String id;
    private Pokemon p1;
    private Pokemon p2;
    private Trainer t1;
    private Trainer t2;

    public void execute(Pokemon p1, Pokemon p2) {
        if (p1.isSwapAllow() && p2.isSwapAllow()) {
            if (p1.getTrainer() != p2.getTrainer()) {
                // swapping is allowed
                // store Pokemons and Trainers in the swap
                this.p1 = p1;
                this.p2 = p2;
                this.t1 = p1.getTrainer();
                this.t2 = p2.getTrainer();
                this.date = new Date();
                this.id = "" + System.currentTimeMillis();
                // remove the Pokemons from the Trainers
                this.t1.getPokemons().remove(p1);
                this.t2.getPokemons().remove(p2);
                // reassign the Pokemons to the Trainers
                this.t1.addPokemon(p2);
                this.t2.addPokemon(p1);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Pokemon getP1() {
        return p1;
    }

    public void setP1(Pokemon p1) {
        this.p1 = p1;
    }

    public Pokemon getP2() {
        return p2;
    }

    public void setP2(Pokemon p2) {
        this.p2 = p2;
    }

    public Trainer getT1() {
        return t1;
    }

    public void setT1(Trainer t1) {
        this.t1 = t1;
    }

    public Trainer getT2() {
        return t2;
    }

    public void setT2(Trainer t2) {
        this.t2 = t2;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
