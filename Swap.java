public class Swap {
    private int id;
    private java.util.Date date;
    private static int nextID = 1;
    private Trainer trainer1;
    private Trainer trainer2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
        
    public Swap(Trainer trainer1, Trainer trainer2, Pokemon pokemon1, Pokemon pokemon2) {
        this.date = new java.util.Date();
        this.id = Swap.nextID;
        Swap.nextID++;
        this.trainer1 = trainer1;
        this.trainer2 = trainer2;
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
    }
        
    public void execute() {
        if (this.pokemon1.getSwapAllow() && this.pokemon2.getSwapAllow()) {
            if (trainer1 != trainer2) {
                this.pokemon1.trade(this, trainer2);
                this.pokemon2.trade(this, trainer1);
                this.trainer1.trade(this.pokemon1, this.pokemon2);
                this.trainer2.trade(this.pokemon2, this.pokemon1);
                System.out.println("Trade was successful");
            }
            else {
                System.out.println("ERROR: You can't trade with yourself");
            }
        }
        else {
            if (this.pokemon2.getSwapAllow()) {
                System.out.println("ERROR: Pokemon " + this.pokemon1.getName() + " is not up for trade");
            }
            else if (this.pokemon1.getSwapAllow()) {
                System.out.println("ERROR: Pokemon " + this.pokemon2.getName() + " is not up for trade");
            } 
            else {
                System.out.println("ERROR: Both Pokemon " + this.pokemon1.getName() 
                                + " and " + this.pokemon2.getName() + " is not up for trade");
            }
        }
    }
}