package theknife;

import java.io.*;
import java.util.*;
/**
 * Classe che rappresenta un utente con ruolo di ristoratore.
 * <p>
 * Estende {@link Utente} e consente l'aggiunta e la visualizzazione di ristoranti associati al proprio account.
 * Ogni ristoratore può essere proprietario di uno o più ristoranti.
 * </p>
 */
public class Ristoratore extends Utente {
    /**
     * Costruttore della classe {@code Ristoratore}.
     *
     * @param username  nome utente
     * @param psw       password
     * @param nome      nome del ristoratore
     * @param cognome   cognome del ristoratore
     * @param domicilio indirizzo di domicilio
     */
    public Ristoratore(String username, String psw, String nome, String cognome, String domicilio) {
        super(username, psw, nome, cognome, domicilio, "ristoratore");
        this.ruolo = "ristoratore";
    }

    /**
     * Aggiunge un nuovo ristorante.
     *
     * @param username Username del ristoratore.
     * @param name Nome del ristorante.
     * @param address Indirizzo del ristorante.
     * @param location Città, zona.
     * @param price Fascia di prezzo (€, €€, €€€, €€€€).
     * @param cuisine Tipo di cucina.
     * @param phoneNumber Numero di telefono.
     * @param url URL di riferimento (esempio instagram, facebook etc...).
     * @param webSiteUrl Sito web.
     * @param award Numero di premi ottenuti.
     * @param greenStar Stella verde (sostenibilità).
     * @param facilitiesAndServices Servizi e strutture che vengono offerti.
     * @param description Descrizione del ristorante.
     * @param delivery Se offre il servizio di delivery.
     * @param booking Se accetta prenotazioni.
     * @throws IOException Se errori di I/O.
     * @throws RestaurantAlreadyExists Se il ristorante è gia' presente.
     */
    public static void aggiungiRistorante(String username, String name, String address, String location, String price,
            String cuisine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar,
            String facilitiesAndServices, String description, boolean delivery, boolean booking)
            throws IOException, RestaurantAlreadyExists {

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
            Ristorante.scriviRistorante(name, address, location, price, cuisine, longitudine, latitudine, phoneNumber,
                    url, webSiteUrl, award, greenStar, facilitiesAndServices, description, delivery, booking);
            FileWriter fr = new FileWriter(GestioneFile.getPathRistoratori(), true);

            try {
                fr.write("\n" + username + "," + "\"" + name + "\"");
                fr.close();
            } catch (IOException e) {
                System.out.println("Errore...");
            }
        }

    }

    /**
     * Restituisce la lista dei ristoranti di un dato ristoratore.
     *
     * @param username Username del ristoratore.
     * @return Lista di ristoranti del ristoratore.
     * @throws IOException Se errori di I/O
     */
    public static ListaRistorante getRistorantiRistoratore(String username) throws IOException {
        List<Ristorante> ristorantiRistoratore = new LinkedList<>();
        ListaRistorante listaRistoranti = ListaRistorante.getRistoranti();
        LinkedList<List<String>> fileRistoratori = GestioneFile.getFileRistoratori(); // CORRETTO tipo

        for (List<String> riga : fileRistoratori) {
            String usernameRistoratore = riga.get(0).replace("\"", "");
            String nomeRistorante = riga.get(1).replace("\"", "");

            if (usernameRistoratore.equalsIgnoreCase(username)) {
                for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
                    if (ristorante.getNome().equalsIgnoreCase(nomeRistorante)) {
                        ristorantiRistoratore.add(ristorante);
                        break;
                    }
                }
            }
        }

        return new ListaRistorante(ristorantiRistoratore);
    }

    /**
     * Verifica se un ristoratore è proprietario di un dato ristorante.
     *
     * @param username       Username del ristoratore.
     * @param nomeRistorante Nome del ristorante.
     * @return True se è proprietario, false altrimenti
     * @throws IOException Se errori di I/O
     */
    public static boolean isProprietario(String username, String nomeRistorante)
            throws FileNotFoundException, IOException {
        ListaRistorante listaRistoranti = getRistorantiRistoratore(username);
        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            if (ristorante.getNome().equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

}