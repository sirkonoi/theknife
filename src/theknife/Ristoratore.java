package theknife;

import java.io.*;
import java.util.*;

public class Ristoratore extends Utente {
    // costruttore
    public Ristoratore(String username, String psw, String nome, String cognome, String domicilio) {
        super(username, psw, nome, cognome, domicilio, "ristoratore");
        LinkedList<Ristorante> ristoranti = new LinkedList<Ristorante>();
        this.ruolo = "ristoratore";
    }

    // metodi
    //nota ricorda di aggiungere le virgolette (plesa sa)
    public static void aggiungiRistorante(String username, String name, String address, String location, String price, String cuisine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description, boolean delivery, boolean booking) throws IOException, RestaurantAlreadyExists {

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
            Ristorante.scriviRistorante(name, address, location, price, cuisine, longitudine, latitudine, phoneNumber,url, webSiteUrl, award, greenStar, facilitiesAndServices, description, delivery, booking );
            FileWriter fr = new FileWriter("data" + sep + "ristoratori.csv", true);           

            try {
                fr.write("\n" + username + "," + "\"" + name + "\"");
                fr.close();
            }
            catch (IOException e) {
                System.out.println("Errore...");
            }
        }

    }

    public static Ristorante getRistorantiRistoratore(String username) throws FileNotFoundException, IOException {
        LinkedList<List<String>> ristorantiRistoratore = new LinkedList<>();
        Ristorante listaRistoranti = Ristorante.getRistoranti();
        LinkedList<List<String>> fileRistoratori = GestioneFile.getFileRistoratori();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            for (List<String> riga : fileRistoratori) {
                if (riga.get(0).equalsIgnoreCase(username) && ristorante.get(0).equalsIgnoreCase(riga.get(1).replace("\"", ""))) {
                    ristorantiRistoratore.add(ristorante);
                }
            }

        }
        return new Ristorante(ristorantiRistoratore);
    }

    public static boolean isProprietario(String username, String nomeRistorante) throws FileNotFoundException, IOException {
        Ristorante listaRistoranti = getRistorantiRistoratore(username);
        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            if(ristorante.get(0).equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

}