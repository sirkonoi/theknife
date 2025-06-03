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
        if (ListaRistorante.checkRistoranti(name)) {
            throw new RestaurantAlreadyExists("Ristorante già presente");
        } else {
            if (geoTheKnife.domicilioEsistente(address)) {
                float[] coordinate = geoTheKnife.getLatitudineLongitudine(address);
                latitudine = coordinate[0];
                longitudine = coordinate[1];
                
            } 
            ListaRistorante.scriviRistorante(name, address, location, price, cuisine, longitudine, latitudine, phoneNumber,url, webSiteUrl, award, greenStar, facilitiesAndServices, description, delivery, booking );
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

 public static ListaRistorante getRistorantiRistoratore(String username) throws IOException {
    List<Ristorante> ristorantiRistoratore = new LinkedList<>();
    ListaRistorante listaRistoranti = ListaRistorante.getRistoranti();
    LinkedList<List<String>> fileRistoratori = GestioneFile.getFileRistoratori(); // CORRETTO tipo

    for (List<String> riga : fileRistoratori) {
        String usernameRistoratore = riga.get(0).replace("\"", "");
        String nomeRistorante = riga.get(1).replace("\"", "");

        if (usernameRistoratore.equalsIgnoreCase(username)) {
            for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
                if (ristorante.getNome().equalsIgnoreCase(nomeRistorante)) {
                    ristorantiRistoratore.add(ristorante);
                    break;
                }
            }
        }
    }

    return new ListaRistorante(ristorantiRistoratore);
}

    public static boolean isProprietario(String username, String nomeRistorante) throws FileNotFoundException, IOException {
        ListaRistorante listaRistoranti = getRistorantiRistoratore(username);
        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if(ristorante.getNome().equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

}