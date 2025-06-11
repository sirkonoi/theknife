package theknife;

import java.io.*;
import java.util.*;

/**
 * La classe Utility fornisce metodi per la gestione delle interfacce utenti
 * e di funzionalità comuni nell'applicazione TheKnife. Gestione menu', gestione
 * stampa delle ricerche, stampa logo e moltro altro..
 */
public class Utility {

    private static final Scanner sc = new Scanner(System.in);

    /**
     * Stampa il logo ASCII dell'applicazione TheKnife.
     */
    public static void printLogo() {
        System.out.println("""
                 ______  __ __    ___      __  _  ____   ____  _____  ___
                |      ||  |  |  /  _]    |  |/ ]|    \\ |    ||     |/  _]
                |      ||  |  | /  [_     |  ' / |  _  | |  | |   __/  [_
                |_|  |_||  _  ||    _]    |    \\ |  |  | |  | |  |_|    _]
                  |  |  |  |  ||   [_     |     ||  |  | |  | |   _]   [_
                  |  |  |  |  ||     |    |  .  ||  |  | |  | |  | |     |
                  |__|  |__|__||_____|    |__|\\_||__|__||____||__| |_____|
                """);
    }

    /**
     * Pulisce il terminale.
     */
    public static void pulisci() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Stampa una lista di ristoranti filtrati, preferiti o del ristoratore, con
     * supporto alla paginazione
     * e interazione tramite terminale (es: cerca, aggiungi, recensioni, riepilogo).
     *
     * @param ristoranti         Lista dei ristoranti da visualizzare.
     * @param tipoMenu           Tipo di menu da stampare: "filtri", "preferiti" o
     *                           "ristoratore".
     * @param user               Utente che e' attualmente loggato.
     * @param titolo             Titolo che risulterà nella ricerca. Esempio: "Lista
     *                           dei ristoranti filtrati".
     * @param mostraAddPreferiti Specifica se mostrare l'opzione aggiungi preferiti
     *                           (true) o meno (false).
     * @throws IOException             Errore durante input/output.
     * @throws RestaurantAlreadyExists Il ristorante e' gia' esistente.
     * @throws RecensioneAlreadyExists L'utente ha gia' recensito il dato
     *                                 ristorante.
     */

    public static void stampaRicerca(ListaRistorante ristoranti, String tipoMenu, GestioneUtenti user, String titolo,
            boolean mostraAddPreferiti) throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {

        boolean stampa = true;
        int count = 0;
        int new_count = 10;
        boolean isMenuRistoratore = false;

        List<Ristorante> listaDaStampare = new LinkedList<>();

        if (tipoMenu.equals("filtri")) {
            listaDaStampare = ristoranti.getDatiRistoranti();
        } else if (tipoMenu.equals("ristoratore")) {
            listaDaStampare = Ristoratore.getRistorantiRistoratore(user.getUsername()).getDatiRistoranti();
            isMenuRistoratore = true;
        } else if (tipoMenu.equals("preferiti")) {
            LinkedList<String> listaPreferiti = Utente.getPreferiti(user.getUsername());
            List<Ristorante> listaRistoranti = ListaRistorante.getRistoranti().getDatiRistoranti();
            for (String nomePreferito : listaPreferiti) {
                for (Ristorante ristorante : listaRistoranti) {
                    if (ristorante.getNome().equalsIgnoreCase(nomePreferito)) {
                        listaDaStampare.add(ristorante);
                        break;
                    }
                }
            }
        } else {
            System.out.println("Errore. Tipologia menu' non esistente.");
        }

        while (stampa) {
            pulisci();
            int paginaCorrente = (count / 10) + 1;
            if (listaDaStampare.size() == 0)
                paginaCorrente = 0;
            int totalePagine = (listaDaStampare.size() + 9) / 10;

            System.out.println(titolo + " (Pagina " + paginaCorrente + " di " + totalePagine + ")\n");

            for (int i = count; i < new_count && i < listaDaStampare.size(); i++) {
                Ristorante riga = listaDaStampare.get(i);
                if (riga != null) {
                    System.out.println((i + 1) + ") " + riga.getNome() + ", " + riga.getIndirizzo());
                }
            }

            System.out.println("\nProssima Pagina:  >");
            System.out.println("Pagina precedente: <");
            System.out.println("CERCA <nomeRistorante> - Visualizza le informazioni di un ristorante.");
            if (isMenuRistoratore) {
                System.out.println("AGGIUNGI - Aggiungi un nuovo ristorante.");
            }
            System.out.println("ESCI - Torna al menu' precedente.");

            String controller;
            String nomeRistorante = null;
            boolean inputValido = false;

            do {
                controller = sc.nextLine().trim();

                if (controller.equalsIgnoreCase("<") || controller.equalsIgnoreCase(">")
                        || controller.equalsIgnoreCase("esci")) {
                    inputValido = true;
                } else if (controller.toLowerCase().startsWith("cerca ")) {
                    nomeRistorante = controller.substring(6).trim();
                    controller = "cerca";
                    inputValido = true;
                } else if (isMenuRistoratore && controller.equalsIgnoreCase("aggiungi")) {
                    inputValido = true;
                } else {
                    System.out.println("Input non valido. Inserisci nuovamente.");
                }
            } while (!inputValido);

            switch (controller.toLowerCase()) {
                case ">":
                    if (new_count < listaDaStampare.size()) {
                        count += 10;
                        new_count += 10;
                    } else {
                        System.out.println("Errore. Non sono presenti altri ristoranti.");
                    }
                    break;

                case "<":
                    if (count >= 10) {
                        count -= 10;
                        new_count -= 10;
                    } else {
                        System.out.println("Errore. Sei già alla prima pagina.");
                    }
                    break;

                case "cerca":
                    if (nomeRistorante == null || nomeRistorante.isEmpty()) {
                        System.out.println("Inserisci il nome del ristorante che vuoi visualizzare: ");
                        nomeRistorante = sc.nextLine().trim();
                    }

                    if (!nomeRistorante.isEmpty() && Ristorante.checkRistoranti(nomeRistorante)) {
                        while (true) {
                            Ristorante.visualizzaRistorante(new ListaRistorante(listaDaStampare), nomeRistorante);

                            System.out.println("\n1 - Visualizza tutte le recensioni del ristorante.");
                            if (mostraAddPreferiti && !user.getRuolo().equals("guest")) {
                            System.out.println("2 - Inserisci/Togli il ristorante nella lista dei preferiti. (E' nei preferiti? " + (Utente.checkPreferiti(user.getUsername(), nomeRistorante) ? "si" : "no") + ")");

                            }

                            if (!user.getRuolo().equals("guest")) {
                                System.out.println("RECENSIONE - Scrivi una recensione.");
                            }

                            if (Ristoratore.isProprietario(user.getUsername(), nomeRistorante)) {
                                System.out.println(
                                        "RIEPILOGO - Ricevi un riepilogo delle recensioni del tuo ristorante.");
                            }

                            System.out.println("ESCI - Torna alla lista dei ristoranti.");

                            String scelta = sc.nextLine().trim().toLowerCase();

                            if (scelta.equals("1")) {
                                Recensione.visualizzaRecensioniRistorante(nomeRistorante, user.getUsername(),
                                        Recensione.getRecensioniRistorante(nomeRistorante));
                            } else if (scelta.equals("2") && mostraAddPreferiti && !user.getRuolo().equals("guest")) {
                                if (Utente.checkPreferiti(user.getUsername(), nomeRistorante)) {
                                    Utente.togliPreferiti((Utente)user, nomeRistorante);
                                    System.out.println("Ristorante tolto dai preferiti.");
                                } else {
                                    Utente.aggiungiPreferiti(user.getUsername(), nomeRistorante);
                                    System.out.println("Ristorante aggiunto ai preferiti.");
                                }
                                System.out.println("Premi invio per continuare..");
                                sc.nextLine();
                            } else if (scelta.equalsIgnoreCase("recensione")) {
                                try {
                                    Recensione.aggiungiRecensione(nomeRistorante, (Utente) user);
                                } catch (RecensioneAlreadyExists e) {
                                    System.out.println(
                                            "Errore: Hai già scritto una recensione per questo ristorante! Modificala/Eliminala dal tuo profilo!");
                                    System.out.println("Premi invio per continuare...");
                                    sc.nextLine();
                                }
                            } else if (Ristoratore.isProprietario(user.getUsername(), nomeRistorante)
                                    && scelta.equalsIgnoreCase("riepilogo")) {
                                pulisci();
                                Recensione.visualizzaRiepilogo(nomeRistorante);
                                System.out.println("Premi invio per continuare..");
                                sc.nextLine();
                            } else if (scelta.equals("esci")) {
                                break;
                            } else {
                                System.out.println("Input non valido. Ritenta!\nPremi invio per continuare...");
                                sc.nextLine();
                            }
                        }
                    } else {
                        System.out.println("Il ristorante non esiste.\nPremi invio per tornare alla lista.");
                        sc.nextLine();
                    }
                    break;

                case "aggiungi":
                    menuAggiungiRistorante(user.getUsername());
                    break;

                default:
                    stampa = false;
                    TheKnife.main_menu();
                    break;
            }
        }
    }

    /**
     * Stampa tutti i tipi di cucina disponibili e permette all'utente,
     * di scegliere la tipologia desiderata.
     *
     * @return Il nome della tipologia scelta dall'utente.
     * @throws IOException Errore durante l'input/output.
     */

    public static String getTipologia() throws IOException {
        List<String> tipiCucina = ListaRistorante.getTipiCucina();
        int count = 0;
        int new_count = 10;

        String tipologia = "";

        boolean stampa = true;
        while (stampa) {
            pulisci();

            int paginaCorrente = (count / 10) + 1;
            int totalePagine = (tipiCucina.size() + 9) / 10;

            System.out.println("Tipi di cucina disponibili (Pagina " + paginaCorrente + " di " + totalePagine + "):\n");

            for (int i = count; i < new_count && i < tipiCucina.size(); i++) {
                System.out.println("- " + tipiCucina.get(i));
            }

            System.out.println("\nProssima Pagina:  >");
            System.out.println("Pagina precedente: <");
            System.out.println("Digita il nome della cucina per selezionarla.");

            String input = sc.nextLine().trim();

            switch (input.toLowerCase()) {
                case ">":
                    if (new_count < tipiCucina.size()) {
                        count += 10;
                        new_count += 10;
                    } else {
                        System.out.println("Errore. Non sono presenti altri tipi di cucina.");
                    }
                    break;

                case "<":
                    if (count >= 10) {
                        count -= 10;
                        new_count -= 10;
                    } else {
                        System.out.println("Errore. Sei già alla prima pagina.");
                    }
                default:
                    boolean trovato = false;
                    for (String tipo : tipiCucina) {
                        if (tipo.equalsIgnoreCase(input)) {
                            tipologia = tipo.toLowerCase();
                            stampa = false;
                            trovato = true;
                            break;
                        }
                    }

                    if (!trovato) {
                        System.out.println("Tipo di cucina non valido.");
                        System.out.println("Premi invio per continuare...");
                        sc.nextLine();
                    }
                    break;
            }
        }

        return tipologia;
    }

    /**
     * Filtra la lista COMPLETA dei ristoranti in base ai filtri scelti dall'utente.
     * I filtri sono:
     * 1 - Tipologia cucina
     * 2 - Delivery (ON/OFF)
     * 3 - Booking (ON/OFF)
     * 4 - Fascia di prezzo (€ - €€€€)
     * 5 - Numero di stelle (1-5)
     *
     * @param filtri Array di stringhe, contiene i filtri selezionati dall'utente.
     * @return Lista filtrata di ristoranti.
     * @throws IOException Errore durante la lettura dei dati.
     */
    public static ListaRistorante cercaFiltri(String[] filtri) throws IOException {
        ListaRistorante listaFiltrati = ListaRistorante.getRistoranti();
        for (String filtro : filtri) {
            // tipologia
            if (filtro.equals("1")) {
                System.out.println("Inserisci la tipologia di cucina desiderata tra: LISTA CUCINA DA FARE...");
                String tipologia = "";
                tipologia = sc.nextLine();
                listaFiltrati = ListaRistorante.filtraTipologia(listaFiltrati, tipologia);
            }

            // delivery:on
            if (filtro.equals("2")) {
                listaFiltrati = ListaRistorante.filtraDelivery(listaFiltrati);
            }

            // booking:on
            if (filtro.equals("3")) {
                listaFiltrati = ListaRistorante.filtraBooking(listaFiltrati);
            }

            // fascia prezzo
            if (filtro.equals("4")) {
                String prezzo;
                boolean isValido;

                do {
                    System.out.println("Inserisci la fascia di prezzo: (€, €€, €€€, €€€€, €€€€):");
                    prezzo = sc.nextLine().trim();
                    isValido = true;

                    if (prezzo.length() < 1 || prezzo.length() > 4) {
                        isValido = false;
                    } else {
                        for (int i = 0; i < prezzo.length(); i++) {
                            if (prezzo.charAt(i) != '€') {
                                isValido = false;
                                break;
                            }
                        }
                    }

                    if (!isValido) {
                        System.out.println("Input non valido. (es: €, €€, €€€, €€€€).");
                    }

                } while (!isValido);

                listaFiltrati = ListaRistorante.filtraPrezzo(listaFiltrati, prezzo);
            }

            if (filtro.equals("5")) {
                int stelle = -1;

                while (stelle < 1 || stelle > 5) {
                    try {
                        System.out.print("Inserisci il numero di stelle (1-5): ");
                        stelle = sc.nextInt();

                        if (stelle < 1 || stelle > 5) {
                            System.out.println("Errore: il numero deve essere compreso tra 1 e 5.");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Errore: devi inserire un numero valido.");
                        sc.next();
                    }
                }

                listaFiltrati = ListaRistorante.filtraStelle(listaFiltrati, stelle);
            }

        }
        return listaFiltrati;
    }
    /**
     * Data una lista di ristoranti, applica i filtri forniti dall'utente.
     * ATTENZIONE: Viene usato quando si parte da una lista già filtrata in precedenza!!!!
     *
     * @param listaFiltrati Lista di ristoranti che vanno ulteriormente filtrati.
     * @param filtri Array di stringhe, contiene i filtri selezionati dall'utente.
     * @return Lista di ristoranti filtrata ulteriormente.
     * @throws IOException Errore durante la lettura dei dati.
     */
    public static ListaRistorante cercaFiltri(ListaRistorante listaFiltrati, String[] filtri) throws IOException {

        for (String filtro : filtri) {
            // Tipologia
            if (filtro.equals("1")) {
                String tipologia = Utility.getTipologia();
                listaFiltrati = ListaRistorante.filtraTipologia(listaFiltrati, tipologia);
            }

            // Delivery
            if (filtro.equals("2")) {
                listaFiltrati = ListaRistorante.filtraDelivery(listaFiltrati);
            }

            // Booking
            if (filtro.equals("3")) {
                listaFiltrati = ListaRistorante.filtraBooking(listaFiltrati);
            }

            // Fascia prezzo
            if (filtro.equals("4")) {
                String prezzo;
                do {
                    pulisci();
                    System.out.println("Inserisci la fascia di prezzo: (€, €€, €€€, €€€€)");
                    prezzo = sc.nextLine().trim();

                    if (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")
                            || prezzo.equals("€€€€€"))) {
                        System.out.println("Input non valido. Inserisci una fascia valida (€, €€, €€€, €€€€).");
                    }

                } while (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")
                        || prezzo.equals("€€€€€")));

                listaFiltrati = ListaRistorante.filtraPrezzo(listaFiltrati, prezzo);
            }
            if (filtro.equals("5")) {
                int stelle = -1;

                while (stelle < 1 || stelle > 5) {
                    try {
                        System.out.print("Inserisci il numero di stelle (1-5): ");
                        stelle = sc.nextInt();

                        if (stelle < 1 || stelle > 5) {
                            System.out.println("Errore: il numero deve essere compreso tra 1 e 5.");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Errore: devi inserire un numero valido.");
                        sc.next();
                    }
                }

                listaFiltrati = ListaRistorante.filtraStelle(listaFiltrati, stelle);
            }
        }
        return listaFiltrati;
    }

    /**
     * Consente a un ristoratore (ruolo necessario!) di aggiungere un nuovo ristorante.
     * Viene gestita la richiesta dei dati (Esempio: nomeRistorante, luogo, prezzo, numTelefono etc.. etc...),
     *
     * @param username Nome utente del ristoratore che aggiunge il ristorante.
     * @throws IOException Errore durante input/output.
     * @throws RestaurantAlreadyExists Il ristorante che si sta creando gia' esiste!
     */
    public static void menuAggiungiRistorante(String username) throws IOException, RestaurantAlreadyExists {
        String nomeRistorante, viaRistorante, nazione, prezzo, tipologiaCucina, numeroCell, url = "", website,
                GreenStar = "", FacilitiesAndServices = "", descrizione;
        int stelle = 0;
        boolean booking, delivery;

        pulisci();
        do {
            System.out.println("Inserisci il nome del ristorante");
            nomeRistorante = sc.nextLine();
            if (Ristorante.checkRistoranti(nomeRistorante)) {
                System.out.println("Errore: Nome già esistente, riprova.");
            }
        } while (Ristorante.checkRistoranti(nomeRistorante));

        pulisci();
        System.out.println(
                "Inserisci l'indirizzo del ristorante (FORMAT: VIA NOMEVIA NUMCIVICO CITTA' NAZIONE)\nEsempio: Viale Stelvio 17 Busto Arsizio Italia");
        do {
            viaRistorante = sc.nextLine();
            if (!geoTheKnife.domicilioEsistente(viaRistorante)) {
                System.out.println("Errore. Inserisci un indirizzo esistente!");
            }
        } while (!geoTheKnife.domicilioEsistente(viaRistorante));

        pulisci();
        System.out.println("Inserisci la nazione: ");
        nazione = sc.nextLine();

        pulisci();
        do {
            System.out.println("Inserisci la fascia di prezzo ($, $$, $$$, $$$$):");
            prezzo = sc.nextLine().trim();

            if (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€"))) {
                System.out.println("Input non valido. Inserisci una fascia valida ($, $$, $$$, $$$$).");
            }

        } while (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")));

        pulisci();
        System.out.println("Inserisci la tipologia di cucina: ");
        tipologiaCucina = sc.nextLine();

        pulisci();
        System.out
                .println("Inserisci il numero di cellulare (ATTENZIONE, includi il prefisso):\nEsempio: +393928847562");
        do {
            numeroCell = sc.nextLine().trim();

            if (!numeroCell.startsWith("+") || numeroCell.length() < 10) {
                System.out.println(
                        "Numero non valido. Assicurati di includere il prefisso (es. +39) e di inserire almeno 10 cifre.");
            }

        } while (!numeroCell.startsWith("+") || numeroCell.length() < 10);

        pulisci();
        System.out.println(
                "Inserisci il link del sito web del ristorante (Formato: www.nomesito.dominio, se non ne possiedi uno scrivi semplicemente \"No\"):");
        do {
            website = sc.nextLine().trim();

            if (!website.equalsIgnoreCase("no") &&
                    !(website.startsWith("www.") && website.contains(".") && website.length() > 7)) {
                System.out.println(
                        "Link non valido. Inserisci un sito nel formato www.nomesito.dominio oppure scrivi \"no\".");
            }

            pulisci();
        } while (!website.equalsIgnoreCase("no") && !(website.startsWith("www.") && website.contains(".")));

        System.out.println("Inserisci il numero di stelle Michelin (da 0 a 3):");
        do {
            String input = sc.nextLine().trim();

            try {
                stelle = Integer.parseInt(input);
                if (stelle < 0 || stelle > 3) {
                    System.out.println("Numero non valido. Deve essere compreso tra 0 e 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: Input non valido. Inserisci un numero intero.");
            }

        } while (stelle < 0 || stelle > 3);

        pulisci();
        System.out.println("Il ristorante offre un servizio di delivery? (Si/No)");
        do {
            String risposta = sc.nextLine().trim().toLowerCase();

            if (risposta.equals("si")) {
                delivery = true;
                break;
            } else if (risposta.equals("no")) {
                delivery = false;
                break;
            } else {
                System.out.println("Errore: Risposta non valida. Inserisci \"Si\" oppure \"No\".");
            }
        } while (true);

        pulisci();
        System.out.println("Il ristorante offre un servizio di prenotazione? (Si/No)");
        do {
            String risposta = sc.nextLine().trim().toLowerCase();

            if (risposta.equals("si")) {
                booking = true;
                break;
            } else if (risposta.equals("no")) {
                booking = false;
                break;
            } else {
                System.out.println("Errore: Risposta non valida. Inserisci \"Si\" oppure \"No\".");
            }
        } while (true);

        pulisci();
        System.out.println("Inserisci una descrizione per il tuo ristorante (Almeno 30 caratteri):");

        do {
            descrizione = sc.nextLine().trim();
            if (descrizione.length() < 30) {
                System.out.println("La descrizione e' troppo breve. Inserisci almeno 30 caratteri:");
            }
        } while (descrizione.length() < 30);

        pulisci();
        System.out.println("Ristorante creato con successo.\nPremi invio per continuare...");
        Ristoratore.aggiungiRistorante(username, nomeRistorante, viaRistorante, nazione, prezzo, tipologiaCucina,
                numeroCell, url, website, stelle, GreenStar, FacilitiesAndServices, descrizione, delivery, booking);
        sc.nextLine();
    }
}
