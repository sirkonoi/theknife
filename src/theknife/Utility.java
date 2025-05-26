package theknife;

import java.io.*;
import java.util.*;

public class Utility {

    /*
     * I CAMPI DA INSERIRE SONO:
     * LISTA_RISTORANTI_DA_VISUALIZZARE (listaFiltrati se scegli filtro,
     * getRistoranti() se scegli preferiti),
     * tipoMenu ----> filtri (se vuoi stampare una lista filtrata) oppure preferiti
     * (se vuoi stampare ristoranti preferiti)
     * user ---> devi passare user dal main
     * titolo ----> Il menu stampa Lista ristoranti + titolo quindi titolo =
     * "filtrati" oppure "preferiti"
     * mostraAddPreferiti = true / false , specifica se mostrare o meno il pulsante
     * "Aggiungi ristorante ai preferiti"
     */
    public static void stampaRicerca(Ristorante ristoranti, String tipoMenu, GestioneUtenti user, String titolo,
            boolean mostraAddPreferiti) throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {
        boolean stampa = true;
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int new_count = 10;
        boolean isMenuRistoratore = false;

        LinkedList<List<String>> listaDaStampare = new LinkedList<>();

        if (tipoMenu.equals("filtri")) {
            listaDaStampare = ristoranti.getListaRistoranti();
        } else if (tipoMenu.equals("ristoratore")) {
            listaDaStampare = Ristoratore.getRistorantiRistoratore(user.getUsername()).getListaRistoranti();
            isMenuRistoratore = true;
        } else if (tipoMenu.equals("preferiti")) {
            LinkedList<String> listaPreferiti = Utente.getPreferiti(user.getUsername());
            LinkedList<List<String>> listaRistoranti = Ristorante.getRistoranti().getListaRistoranti();

            for (String nomePreferito : listaPreferiti) {
                for (List<String> riga : listaRistoranti) {
                    if (riga.get(0).equalsIgnoreCase(nomePreferito)) {
                        listaDaStampare.add(riga);
                        break;
                    }
                }
            }
        } else {
            System.out.println("Errore. Tipologia menu' non esistente.");
        }
        while (stampa) {
            TheKnife.pulisci();
            int paginaCorrente = (count / 10) + 1;
            if (listaDaStampare.size() == 0)
                paginaCorrente = 0;
            int totalePagine = (listaDaStampare.size() + 9) / 10;

            System.out.println(
                    titolo + " (Pagina " + paginaCorrente + " di " + totalePagine + ")\n");

            for (int i = count; i < new_count && i < listaDaStampare.size(); i++) {
                List<String> riga = listaDaStampare.get(i);
                if (!riga.isEmpty()) {
                    System.out.println((i + 1) + ") " + riga.get(0) + ", " + riga.get(2));
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
                }

                else if (controller.toLowerCase().startsWith("cerca ")) {
                    nomeRistorante = controller.substring(6).trim();
                    controller = "cerca";
                    inputValido = true;
                }

                else if (isMenuRistoratore && controller.equalsIgnoreCase("aggiungi")) {
                    inputValido = true;
                }

                else {
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
                            Ristorante.visualizzaRistorante(new Ristorante(listaDaStampare), nomeRistorante);

                            if (mostraAddPreferiti && !user.getRuolo().equals("guest")) {
                                System.out.println("\n1 - Visualizza tutte le recensioni del ristorante.");
                                System.out.println("2 - Inserisci il ristorante nella lista dei preferiti.");
                            }
                            if (!user.getRuolo().equals("guest")) {
                                System.out.println("RECENSIONE - Scrivi una recensione.");
                            }
                            if(Ristoratore.isProprietario(user.getUsername(), nomeRistorante)) {
                                System.out.println("RIEPILOGO - Ricevi un riepilogo delle recensioni del tuo ristorante.");                                
                            }                            
                            System.out.println("ESCI - Torna alla lista dei ristoranti.");

                            String scelta = sc.nextLine().trim().toLowerCase();
                            if (scelta.equals("1")) {
                                Recensione.visualizzaRecensioniRistorante(nomeRistorante, user.getUsername(),
                                        Recensione.getRecensioniRistorante(nomeRistorante));
                            } else if (scelta.equals("2") && mostraAddPreferiti && !user.getRuolo().equals("guest")) {
                                if (Utente.checkPreferiti(user.getUsername(), nomeRistorante)) {
                                    System.out.println("Errore: il ristorante è già nei preferiti!");
                                } else {
                                    Utente.aggiungiPreferiti(user.getUsername(), nomeRistorante);
                                    System.out.println("Aggiunto ai preferiti.");
                                }
                                System.out.println("Premi invio per continuare..");
                                sc.nextLine();
                            } else if (scelta.equals("3")) {
                                try {
                                    Recensione.aggiungiRecensione(nomeRistorante, (Utente) user);
                                } catch (RecensioneAlreadyExists e) {
                                    System.out.println(
                                            "Errore: Hai gia' scritto una recensione per questo ristorante! Modificala/Eliminala dal tuo profilo!");
                                    System.out.println("Premi invio per continuare...");
                                    sc.nextLine();
                                } 
                            } else if(Ristoratore.isProprietario(user.getUsername(), nomeRistorante) && scelta.equalsIgnoreCase("riepilogo")) {
                                TheKnife.pulisci();
                                Recensione.visualizzaRiepilogo(nomeRistorante);
                                System.out.println("Premi invio per continuare..");
                                sc.nextLine();                               
                            }
                            else if (scelta.equals("esci")) {
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

    // Serve per selezione nei filtri della tipologia del ristorante
    public static String getTipologia() throws IOException {
        List<String> tipiCucina = Ristorante.getTipiCucina();
        int count = 0;
        int new_count = 10;
        Scanner sc = new Scanner(System.in);
        String tipologia = "";

        boolean stampa = true;
        while (stampa) {
            TheKnife.pulisci();

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

    // da fixare prezzo
    public static Ristorante cercaFiltri(String[] filtri) throws IOException {
        Scanner sc = new Scanner(System.in);
        Ristorante listaFiltrati = Ristorante.getRistoranti();
        for (String filtro : filtri) {
            // tipologia
            if (filtro.equals("1")) {
                System.out.println("Inserisci la tipologia di cucina desiderata tra: LISTA CUCINA DA FARE...");
                String tipologia = "";
                tipologia = sc.nextLine();
                listaFiltrati = Ristorante.filtraTipologia(listaFiltrati, tipologia);
            }

            // delivery:on
            if (filtro.equals("2")) {
                listaFiltrati = Ristorante.filtraDelivery(listaFiltrati);
            }

            // booking:on
            if (filtro.equals("3")) {
                listaFiltrati = Ristorante.filtraBooking(listaFiltrati);
            }

            // fascia prezzo
            if (filtro.equals("4")) {
                String prezzo;
                do {
                    System.out.println("Inserisci la fascia di prezzo: (€, €€, €€€, €€€€, €€€€€):");
                    prezzo = sc.nextLine().trim();
                    if (!prezzo.matches("€{1,5}")) {
                        System.out.println(
                                "Input non valido. Inserisci solo da 1 a 5 simboli di euro (es: €, €€, €€€, ecc.).");
                    }
                } while (!prezzo.matches("€{1,5}"));

                listaFiltrati = Ristorante.filtraPrezzo(listaFiltrati, prezzo);
            }

            // Prezzo
            if (filtro.equals("5")) {
                System.out.println("Inserisci la locazione geografica (citta, stato): ");
                String localita = "";
                localita = sc.nextLine();
                listaFiltrati = Ristorante.filtraPosizione(listaFiltrati, localita);
            }
        }
        return listaFiltrati;
    }

    public static Ristorante cercaFiltri(Ristorante listaFiltrati, String[] filtri) throws IOException {
        Scanner sc = new Scanner(System.in);

        for (String filtro : filtri) {
            // Tipologia
            if (filtro.equals("1")) {
                String tipologia = Utility.getTipologia();
                listaFiltrati = Ristorante.filtraTipologia(listaFiltrati, tipologia);
            }

            // Delivery
            if (filtro.equals("2")) {
                listaFiltrati = Ristorante.filtraDelivery(listaFiltrati);
            }

            // Booking
            if (filtro.equals("3")) {
                listaFiltrati = Ristorante.filtraBooking(listaFiltrati);
            }

            // dafixare..................................................................................
            // Fascia prezzo
            if (filtro.equals("4")) {
                String prezzo;
                do {
                    TheKnife.pulisci();
                    System.out.println("Inserisci la fascia di prezzo: (€, €€, €€€, €€€€)");
                    prezzo = sc.nextLine().trim();

                    if (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")
                            || prezzo.equals("€€€€€"))) {
                        System.out.println("Input non valido. Inserisci una fascia valida (€, €€, €€€, €€€€).");
                    }

                } while (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")
                        || prezzo.equals("€€€€€")));

                listaFiltrati = Ristorante.filtraPrezzo(listaFiltrati, prezzo);
            }
        }
        return listaFiltrati;
    }

    public static void menuAggiungiRistorante(String username) throws IOException, RestaurantAlreadyExists {
        Scanner sc = new Scanner(System.in);
        String nomeRistorante, viaRistorante, nazione, prezzo, tipologiaCucina, numeroCell, url = "", website,
                GreenStar = "", FacilitiesAndServices = "", descrizione;
        int stelle = 0;
        boolean booking, delivery;

        TheKnife.pulisci();
        do {
            System.out.println("Inserisci il nome del ristorante");
            nomeRistorante = sc.nextLine();
            if (Ristorante.checkRistoranti(nomeRistorante)) {
                System.out.println("Errore: Nome già esistente, riprova.");
            }
        } while (Ristorante.checkRistoranti(nomeRistorante));

        TheKnife.pulisci();
        System.out.println(
                "Inserisci l'indirizzo del ristorante (FORMAT: VIA NOMEVIA NUMCIVICO CITTA' NAZIONE)\nEsempio: Viale Stelvio 17 Busto Arsizio Italia");
        do {
            viaRistorante = sc.nextLine();
            if (!geoTheKnife.domicilioEsistente(viaRistorante)) {
                System.out.println("Errore. Inserisci un indirizzo esistente!");
            }
        } while (!geoTheKnife.domicilioEsistente(viaRistorante));

        TheKnife.pulisci();
        System.out.println("Inserisci la nazione: ");
        nazione = sc.nextLine();

        TheKnife.pulisci();
        do {
            System.out.println("Inserisci la fascia di prezzo (€, €€, €€€, €€€€):");
            prezzo = sc.nextLine().trim();

            if (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€"))) {
                System.out.println("Input non valido. Inserisci una fascia valida (€, €€, €€€, €€€€).");
            }

        } while (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€")));

        TheKnife.pulisci();
        System.out.println("Inserisci la tipologia di cucina: ");
        tipologiaCucina = sc.nextLine();

        TheKnife.pulisci();
        System.out
                .println("Inserisci il numero di cellulare (ATTENZIONE, includi il prefisso):\nEsempio: +393928847562");
        do {
            numeroCell = sc.nextLine().trim();

            if (!numeroCell.startsWith("+") || numeroCell.length() < 10) {
                System.out.println(
                        "Numero non valido. Assicurati di includere il prefisso (es. +39) e di inserire almeno 10 cifre.");
            }

        } while (!numeroCell.startsWith("+") || numeroCell.length() < 10);

        TheKnife.pulisci();
        System.out.println(
                "Inserisci il link del sito web del ristorante (Formato: www.nomesito.dominio, se non ne possiedi uno scrivi semplicemente \"No\"):");
        do {
            website = sc.nextLine().trim();

            if (!website.equalsIgnoreCase("no") &&
                    !(website.startsWith("www.") && website.contains(".") && website.length() > 7)) {
                System.out.println(
                        "Link non valido. Inserisci un sito nel formato www.nomesito.dominio oppure scrivi \"no\".");
            }

            TheKnife.pulisci();
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

        TheKnife.pulisci();
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

        TheKnife.pulisci();
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

        TheKnife.pulisci();
        System.out.println("Inserisci una descrizione per il tuo ristorante (Almeno 30 caratteri):");

        do {
            descrizione = sc.nextLine().trim();
            if (descrizione.length() < 30) {
                System.out.println("La descrizione e' troppo breve. Inserisci almeno 30 caratteri:");
            }
        } while (descrizione.length() < 30);

        TheKnife.pulisci();
        System.out.println("Ristorante creato con successo.\nPremi invio per continuare...");
        Ristoratore.aggiungiRistorante(username, nomeRistorante, viaRistorante, nazione, prezzo, tipologiaCucina,
                numeroCell, url, website, stelle, GreenStar, FacilitiesAndServices, descrizione, delivery, booking);
        sc.nextLine();
    }
}
