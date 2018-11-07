package pokemon;

import java.util.Date;

import pokemon.data.Pokemon;

public class Competition extends Swap {

    private Pokemon winner = null;
    private Pokemon loser = null;

    @Override
    public void execute(Pokemon p1, Pokemon p2) {
        if (p1.getTrainer() != p2.getTrainer()) {
            setDate(new Date());
            // the game logic +1 since first ordinal is 0
            double score1 = (p1.getType().ordinal() + 1) * Math.random();
            double score2 = (p2.getType().ordinal() + 1) * Math.random();
            System.out.printf("Pokemon '%s has score: %f\n", p1, score1);
            System.out.printf("Pokemon '%s has score: %f\n", p2, score2);
            if (score1 > score2) {
                // p1 wins -> assign p2 to p1.trainer
                p2.getTrainer().getPokemons().remove(p2);
                p1.getTrainer().addPokemon(p2);
                setWinner(p1);
                setLoser(p2);
                System.out.printf("Pokemon '%s wins!\n", p1);
            } else if (score1 < score2) {
                // p2 wins -> assign p1 to p2.trainer
                p1.getTrainer().getPokemons().remove(p1);
                p2.getTrainer().addPokemon(p1);
                setWinner(p2);
                setLoser(p1);
                System.out.printf("Pokemon '%s wins!\n", p2);
            } else {
                // draw
            }
            // store in history
            p2.addCompetition(this);
            p1.addCompetition(this);
        } else {
            System.out.printf("No competition: Trainers '%s' == '%s' are identical!\n", p1.getTrainer(),
                    p2.getTrainer());
        }
    }

    public Pokemon getWinner() {
        return winner;
    }

    public void setWinner(Pokemon winner) {
        this.winner = winner;
    }

    public Pokemon getLoser() {
        return loser;
    }

    public void setLoser(Pokemon loser) {
        this.loser = loser;
    }
}
