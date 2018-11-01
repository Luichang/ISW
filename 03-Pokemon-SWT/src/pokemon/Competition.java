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
      
    public Competition(Pokemon p1, Pokemon p2) {
      this.date = new java.util.Date();
      //this.id = Swap.nextID;
      //Swap.nextID++;
      this.p1 = p1;
      this.p2 = p2;
      
      this.id = this.date.toString() + this.t1.toString() + this.t2.toString() 
      + this.p1.toString() + this.p2.toString();
    }
    
    public String getid() {
      return this.id;
    }
    
    public void execute() {
      if (this.t1 != this.t2) {
        if(this.p1.won(this.p2)) {
          System.out.println("Trade was successful");
          this.p2.lose(this, this.t1);
        } else { // ignoring the case of a tie, which in this assignment won't happen
          System.out.println("Trade was successful");
          this.p1.lose(this, this.t2);
        } 
      } else {
        System.out.println("ERROR: You can't comptete with yourself");
      }
    }
}