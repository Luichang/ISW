package pokemon.data;

public class Swap {
  //private int id;
  private String id;
  private java.util.Date date;
  //private static int nextID = 1;
  private Trainer trainer1;
  private Trainer trainer2;
  private Pokemon pokemon1;
  private Pokemon pokemon2;
  
  /** Used to trade two Pokemon between two different Trainer. */
  public Swap(Trainer trainer1, Trainer trainer2, Pokemon pokemon1, Pokemon pokemon2) {
    this.date = new java.util.Date();
    //this.id = Swap.nextID;
    //Swap.nextID++;
    this.trainer1 = trainer1;
    this.trainer2 = trainer2;
    this.pokemon1 = pokemon1;
    this.pokemon2 = pokemon2;
    
    /* using this id as the assignment wants us to create a string ID and this seems to be
     * the only option to have a string, use the date and try to be unique. In our (Jasmin and 
     * Charles) mind it seems more practical to use an incrementing int as we provided in comments 
     * above, however for the sake of the assignment this is what we came up with  
     */
    
    this.id = this.date.toString() + this.trainer1.toString() + this.trainer2.toString() 
      + this.pokemon1.toString() + this.pokemon2.toString();
  }
  
  /* this function is added to please Checkstyle as it is not required in any tests or other 
   * functions
   */
  public String getid() {
    return this.id;
  }
  
  /** this function allows for a change in permissions before the trade happens, 
   also makes the constructor cleaner. */
  public void execute() {
    if (this.pokemon1.getSwapAllow() && this.pokemon2.getSwapAllow()) {
      if (trainer1 != trainer2) {
        this.pokemon1.trade(this, trainer2);
        this.pokemon2.trade(this, trainer1);
        this.trainer1.trade(this.pokemon1, this.pokemon2);
        this.trainer2.trade(this.pokemon2, this.pokemon1);
        System.out.println("Trade was successful");
      } else {
        System.out.println("ERROR: You can't trade with yourself");
      }
    } else {
      if (this.pokemon2.getSwapAllow()) {
        System.out.println("ERROR: Pokemon " + this.pokemon1.getName() + " is not up for trade");
      } else if (this.pokemon1.getSwapAllow()) {
        System.out.println("ERROR: Pokemon " + this.pokemon2.getName() + " is not up for trade");
      } else {
        System.out.println("ERROR: Both Pokemon " + this.pokemon1.getName() 
                + " and " + this.pokemon2.getName() + " is not up for trade");
      }
    }
  }
}