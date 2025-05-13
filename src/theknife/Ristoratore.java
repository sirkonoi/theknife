package theknife;

import java.io.*;
import java.util.*;
import theknife.*;

public class Ristoratore extends Utente {
    // costruttore
    public Ristoratore(String username, String psw, String nome, String cognome, String domicilio) {
        super(username, psw, nome, cognome, domicilio, "ristoratore");
        LinkedList<Ristorante> ristoranti = new LinkedList<Ristorante>();
        this.ruolo = "ristoratore";
    }

    // metodi
    
    //Manca printare in ristoratori nome ristoratore + nome ristorante
    //nota ricorda di aggiungere le virgolette (plesa sa)
    public static void aggiungiRistorante(String name, String address, String location, String price, String cuisine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description) throws IOException, RestaurantAlreadyExists {
        /*
         * LinkedList<List<String>> user = getUsers();
         * for (List<String> user : users) {
         * if (user.get(0).equals(username) && user.get(5).equals("Ristoratore")) {
         * break;
         * }
         * }
         */
        double longitudine = 0;
        double latitudine = 0;
        if (Ristorante.checkRistoranti(name)) {
            throw new RestaurantAlreadyExists("Ristorante già presente");
        } else {
            if (geoTheKnife.domicilioEsistente(address)) {
                float[] coordinate = geoTheKnife.getLatitudineLongitudine(address);
                latitudine = coordinate[0];
                longitudine = coordinate[1];
                
            } 
            Ristorante.scriviRistorante(name, address, location, price, cuisine, longitudine, latitudine, phoneNumber,url, webSiteUrl, award, greenStar, facilitiesAndServices, description);
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