package theknife;

import java.io.*;
import java.util.*;

public class Ristoratore extends Utente {
    //campi
    private LinkedList<Ristorante> ristoranti;

    //costruttore
    public Ristoratore(String username, String psw, String nome, String cognome, String domicilio) {
        super(username, psw, nome, cognome, domicilio, "ristoratore");
        LinkedList<Ristorante> ristoranti = new LinkedList<Ristorante>();
        this.ruolo = "ristoratore";
    }

    //metodi
    public void aggiungiRistorante(String s) throws IOException {
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