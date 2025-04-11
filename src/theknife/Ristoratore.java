package theknife;

import java.io.*;
import java.util.*;
import theknife.*;

public class Ristoratore extends Utente {
    // campi
    private LinkedList<Ristorante> ristoranti;

    // costruttore
    public Ristoratore(String username, String psw, String nome, String cognome, String domicilio) {
        super(username, psw, nome, cognome, domicilio, "ristoratore");
        LinkedList<Ristorante> ristoranti = new LinkedList<Ristorante>();
        this.ruolo = "ristoratore";
    }

    // metodi
    public static void aggiungiRistorante(String name, String address, String location, String price, String cuisine,
            String phoneNumber, int award, String greenStar, String facilitiesAndServices) throws IOException, RestaurantAlreadyExists {
        /*
         * LinkedList<List<String>> user = getUsers();
         * for (List<String> user : users) {
         * if (user.get(0).equals(username) && user.get(5).equals("Ristoratore")) {
         * break;
         * }
         * }
         */
        if (Ristorante.checkRistoranti(name)) {
            throw new RestaurantAlreadyExists("Ristorante già presente");
        } else {
            Ristorante.scriviRistorante(name, address, location, price, cuisine, phoneNumber, award, greenStar,
                    facilitiesAndServices);
            FileWriter fr = new FileWriter("data" + sep + "ristoratori.csv", true);

            try {
                fr.write("\n" + username + "," + name);
                fr.close();
            }

            catch (IOException e) {
                System.out.println("Errore...");
            }
        }

    }

}