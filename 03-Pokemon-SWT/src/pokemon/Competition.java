package pokemon;

import pokemon.data.Pokemon;

import java.util.Date;

//import pokemon.Swap;
import pokemon.data.Trainer;

public class Competition extends Swap {
    private Date date;
    private String id;
    private Pokemon p1;
    private Pokemon p2;
    private Trainer t1;
    private Trainer t2;
      
    public Competition(Pokemon p1, Pokemon p2) {
	this.date = new Date();
        //this.id = Swap.nextID;
        //Swap.nextID++;
        this.p1 = p1;
        this.p2 = p2;
        this.t1 = p1.getTrainer();
        this.t2 = p2.getTrainer();
      
        this.id = this.date.toString() + this.t1.toString() + this.t2.toString() 
        	+ this.p1.toString() + this.p2.toString();
    }
    
    public String getcompid() {
	return this.id;
    }
    
    public void execute() {
        if (this.t1 != this.t2) {
            if (this.p1.won(this.p2)) {
                System.out.println(this.p1.getName() + " has won against " 
                	+ this.p2.getName());
                this.t2.release(p2);
                this.t1.addPokemon(p2);
            } else { // ignoring the case of a tie, which in this assignment won't happen
        	System.out.println(this.p2.getName() + " has won against " 
                	+ this.p1.getName());
                this.t1.release(p1);
                this.t2.addPokemon(p1);
            } 
        } else {
            System.err.println(this.p1.getName() + " can't compete against " 
            	+ this.p2.getName() + " as they have the same Trainer " 
        	+ this.t1.getFirstname() + " " + this.t1.getLastname());
        }
    }
}