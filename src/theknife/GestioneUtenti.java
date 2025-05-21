package theknife;

import java.io.*;
import java.util.*;

public abstract class GestioneUtenti {

    public static String sep = (File.separator);

    // Restituisce LinkedList di List (una lista = 1 utente), presi da users.csv
    public static LinkedList<List<String>> getUsers() throws IOException {
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data" + sep + "users.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        } finally {
            if (br != null)
                br.close();
        }

        return users;
    }

    // REGISTRAZIONE
    public static GestioneUtenti register(String username, String psw, String nome, String cognome, String domicilio,
            String ruolo) throws UserAlreadyExists, IOException {
        if (checkUser(username))
            throw new UserAlreadyExists("Errore: L'utente e' gia' esistente.");

        GestioneUtenti newUser = null;

        FileWriter fr = new FileWriter("data" + sep + "users.csv", true);
        try {
            fr.write("\n" + username + "," + Password.encrypt(psw) + "," + nome + "," + cognome + ","
                    + domicilio.replace(",", "") + "," + ruolo.toLowerCase());
            if (ruolo.equals("utente")) {
                newUser = new Utente(username, psw, nome, cognome, domicilio, ruolo);
            } else if (ruolo.equals("ristoratore")) {
                newUser = new Ristoratore(username, psw, nome, cognome, domicilio);
            }
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore durante la registrazione...");
        }

        return newUser;

    }

    // Login (check username, password)
    public static Utente login(String username, String psw) throws ErroreLogin, IOException {
        LinkedList<List<String>> users = getUsers();
        Utente u;
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                if (Password.decrypt(user.get(1)).equals(psw)) {
                    return u = new Utente(user.get(0), user.get(1), user.get(2), user.get(3), user.get(4), user.get(5));
                }
            }
        }

        throw new ErroreLogin("Errore. Utente non esistente o credenziali errate.");
    }

    // Controlla se un utente è già registrato
    public static boolean checkUser(String username) throws IOException {
        boolean isRegistered = false;
        LinkedList<List<String>> users = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                isRegistered = true;
                break;
            }
        }
        return isRegistered;
    }

    // Controlla il ruolo di un dato utente
    public static String checkRuolo(String username) throws IOException {
        LinkedList<List<String>> users = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                return user.get(5);
            }
        }
        return null;

    }

    public static Ristorante cercaVicinanza(String indirizzo, int raggio) throws IOException {
        Ristorante listaFiltrati = Ristorante.getRistoranti();
        listaFiltrati = geoTheKnife.filtraVicinanza(listaFiltrati, indirizzo, raggio);
        return listaFiltrati;
    }

    public static Ristorante cercaFiltri(Ristorante listaFiltrati, String[] filtri) throws IOException {
        Scanner sc = new Scanner(System.in);

        for (String filtro : filtri) {
            // Tipologia
            if (filtro.equals("1")) {
                String tipologia = getTipologia();
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

    public static void stampaRicerca(Ristorante ristoranti, GestioneUtenti user) throws IOException {
        boolean stampa = true;
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int new_count = 10;

        LinkedList<List<String>> datiRistoranti = ristoranti.getListaRistoranti();

        while (stampa) {
            TheKnife.pulisci();

            int paginaCorrente = (count / 10) + 1;
            int totalePagine = (datiRistoranti.size() + 9) / 10;

            System.out.println("Lista ristoranti filtrati (Pagina " + paginaCorrente + " di " + totalePagine + ")\n");

            for (int i = count; i < new_count && i < datiRistoranti.size(); i++) {
                List<String> riga = datiRistoranti.get(i);
                if (!riga.isEmpty()) {
                    System.out.println((i + 1) + ") " + riga.get(0) + ", " + riga.get(2));
                }
            }

            System.out.println("\nProssima Pagina:  >");
            System.out.println("Pagina precedente: <");
            System.out.println("CERCA <nomeRistorante> - Visualizza le informazioni di un ristorante.");
            System.out.println("ESCI - Torna al menu' principale.");

            String controller;
            String nomeRistorante = null;

            do {
                controller = sc.nextLine().trim();

                if (controller.toLowerCase().startsWith("cerca ")) {
                    nomeRistorante = controller.substring(6).trim(); // essenzialmente guarda cosa c'è dopo il cerca
                    controller = "cerca";
                    break;
                }

                if (!(controller.equals("<") || controller.equals(">") || controller.equalsIgnoreCase("cerca")
                        || controller.equalsIgnoreCase("esci"))) {
                    System.out.println("Input non valido. Inserisci nuovamente.");
                }

            } while (!(controller.equals("<") || controller.equals(">") || controller.equalsIgnoreCase("cerca")
                    || controller.equalsIgnoreCase("esci")));

            switch (controller.toLowerCase()) {
                case ">":
                    if (new_count < datiRistoranti.size()) {
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
                    boolean cerca = true;
                    while (cerca) {
                        if (nomeRistorante == null || nomeRistorante.isEmpty()) {
                            System.out.println("Inserisci il nome del ristorante che vuoi visualizzare: ");
                            nomeRistorante = sc.nextLine().trim();
                        }

                        if (Ristorante.checkRistoranti(nomeRistorante)) {
                            while (true) {
                                Ristorante.visualizzaRistorante(ristoranti, nomeRistorante);

                                if (!user.getRuolo().equals("guest")) {
                                    System.out.println("\n1 - Inserisci il ristorante nella lista dei preferiti.");
                                }
                                System.out.println("ESCI - Torna ai ristoranti filtrati");

                                String scelta = sc.nextLine().trim().toLowerCase();

                                if (scelta.equals("1") && !user.getRuolo().equals("guest")) {
                                    if (Utente.checkPreferiti(user.getUsername(), nomeRistorante)) {
                                        System.out.println(
                                                "Errore: il ristorante è già nella tua lista preferiti!\nPremi invio per continuare...");
                                        sc.nextLine();
                                    } else {
                                        Utente.aggiungiPreferiti(user.getUsername(), nomeRistorante);
                                        System.out.println(
                                                "Ristorante aggiunto ai preferiti.\nPremi invio per continuare...");
                                        sc.nextLine();
                                        break;
                                    }
                                } else if (scelta.equals("esci")) {
                                    break;
                                } else {
                                    System.out.println("Scelta non valida.\nPremi invio per continuare...");
                                    sc.nextLine();
                                }
                            }
                        } else {
                            System.out.println("Il ristorante non esiste.\nPremi invio per tornare alla lista.");
                            sc.nextLine();
                        }

                        cerca = false;
                        nomeRistorante = null;
                    }
                    break;

                case "esci":
                default:
                    TheKnife.main_menu();
                    stampa = false;
                    break;
            }
        }
    }

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
                    if (tipiCucina.contains(input)) {
                        tipologia = input;
                        stampa = false;
                    } else {
                        System.out.println("Tipo di cucina non valido.");
                    }
                    break;
            }
        }

        return tipologia;
    }

    public static Ristorante cercaFiltri(String[] filtri) throws IOException {
        Scanner sc = new Scanner(System.in);
        Ristorante listaFiltrati = Ristorante.getRistoranti();
        for (String filtro : filtri) {
            // tipologia
            if (filtro.equals("1")) {
                // FAI LISTA CUCINE
                System.out.println("Inserisci la tipologia di cucina desiderata tra: LISTA CUCINA DA FARE...");
                String tipologia = "";
                tipologia = sc.nextLine();
                listaFiltrati = Ristorante.filtraTipologia(listaFiltrati, tipologia);
            }

            // Delivery ON
            if (filtro.equals("2")) {
                listaFiltrati = Ristorante.filtraDelivery(listaFiltrati);
            }

            // Booking ON
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
            // etc.. etc...

        }

        return listaFiltrati;
    }

    // metodi get
    public abstract String getUsername();

    public abstract String getRuolo();

    public abstract String getDomicilio();

}
