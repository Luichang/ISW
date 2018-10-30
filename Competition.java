
public class Competition extends Swap {
  private String id;
  private java.util.Date date;
  //private static int nextID = 1;
  private Trainer trainer1;
  private Trainer trainer2;
  private Pokemon pokemon1;
  private Pokemon pokemon2;
      
    public Competition(Trainer trainer1, Trainer trainer2, Pokemon pokemon1, Pokemon pokemon2) {
      super(trainer1, trainer2, pokemon1, pokemon2);
      this.date = new java.util.Date();
      //this.id = Swap.nextID;
      //Swap.nextID++;
      this.trainer1 = trainer1;
      this.trainer2 = trainer2;
      this.pokemon1 = pokemon1;
      this.pokemon2 = pokemon2;
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
