package theknife;

public class Ristoratore extends Utente {
    //campi
    private String username;
    private LinkedList<Ristorante> ristorante;

    //costruttore
    public Ristoratore() {
        super();
        LinkedList<Ristorante> ristorante = new LinkedList<Ristorante>;
        this.ruolo = "ristoratore";
    }

    //metodi
    public aggiungiRistorante(String s) {
        /*
        LinkedList<List<String>> user = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username) && user.get(5).equals("Ristoratore")) {
                break;
            }
        }
        */
        FileWriter fr = new FileWriter("data" + sep + "ristoratori.csv", true);
        try {
            fr.write("\n" + username + "," + s);
            fr.close();
        }

        catch(IOException e) {
            System.out.println("Errore...");
        }
    }


}