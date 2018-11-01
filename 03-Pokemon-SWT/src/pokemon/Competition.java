package pokemon;

import pokemon.data.Pokemon;

import java.util.Date;

import pokemon.Swap;
import pokemon.data.Trainer;

public class Competition extends Swap {
    private Date date;
    private String id;
    private Pokemon p1;
    private Pokemon p2;
    private Trainer t1;
    private Trainer t2;
      
    public Competition(Pokemon pokemon1, Pokemon pokemon2) {
      this.date = new java.util.Date();
      //this.id = Swap.nextID;
      //Swap.nextID++;
          this.pokemon1 = pokemon1;
      this.pokemon2 = pokemon2;
      
      this.id = this.date.toString() + this.trainer1.toString() + this.trainer2.toString() 
      + this.pokemon1.toString() + this.pokemon2.toString();
    }
    
    public String getid() {
      return this.id;
    }
    
    public void execute() {
      if (this.trainer1 != this.trainer2) {
        if(this.pokemon1.won(this.pokemon2)) {
          System.out.println("Trade was successful");
          this.pokemon2.lose(this, this.trainer1);
        } else { // ignoring the case of a tie, which in this assignment won't happen
          System.out.println("Trade was successful");
          this.pokemon1.lose(this, this.trainer2);
        } 
      } else {
        System.out.println("ERROR: You can't comptete with yourself");
      }
    }
}