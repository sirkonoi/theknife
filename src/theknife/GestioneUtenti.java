package theknife;

import java.io.*;
import java.util.*;
import theknife.*;

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
            fr.write("\n" + username + "," + Password.encrypt(psw) + "," + nome + "," + cognome + "," + domicilio + ","
                    + ruolo.toLowerCase());
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

    // Ricorda di cambiare PSW !!!!!!
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

    /*
     * //Cerca un ristorante
     * public static void cercaRistorante(int input, String tipologia) throws
     * IOException {
     * Ristorante listaRistoranti = Ristorante.getRistoranti();
     * List<Ristorante> ristorantiFiltrati = new LinkedList<>();
     * 
     * switch (input) {
     * case 12:
     * if (tipologia.equals("delivery")) {
     * 
     * for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
     * if (ristorante.get(13).toLowerCase().contains("deliv") ||
     * ristorante.get(13).toLowerCase().contains("order") ||
     * ristorante.get(13).toLowerCase().contains("takeaway")) {
     * ristorantiFiltrati.add(new Ristorante(ristorante));
     * }
     * }
     * }
     * else if (tipologia.equals("booking")) {
     * for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
     * if (ristorante.get(13).toLowerCase().contains("book") ||
     * ristorante.get(13).toLowerCase().contains("online") ||
     * ristorante.get(13).toLowerCase().contains("reserve")) {
     * ristorantiFiltrati.add(new Ristorante(ristorante));
     * }
     * }
     * }
     * break;
     * default:
     * for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
     * if (ristorante.get(input).equalsIgnoreCase(tipologia)) {
     * ristorantiFiltrati.add(new Ristorante(ristorante));
     * }
     * }
     * break;
     * }
     * 
     * //Stampa con freccetta
     * boolean stampa = true;
     * Scanner sc = new Scanner(System.in);
     * int count = 0;
     * int new_count = 10;
     * while(stampa) {
     * TheKnife.pulisci();
     * 
     * int n1 = new_count/10;
     * int n2 = ristorantiFiltrati.size()/10;
     * 
     * // MANCA DA METTERE IL CERCA INTEGRATO!!!! RICORDATELO
     * System.out.println("Lista ristoranti filtrati per: " +
     * tipologia.toUpperCase() + " (Pagina " + n1 + " di " + n2 + ")\n");
     * //Stampa pagina corrente
     * for(int i = count; i < new_count && i < ristorantiFiltrati.size(); i++) {
     * Ristorante r = ristorantiFiltrati.get(i);
     * System.out.println(i+1 + ")" + r.getDatiRistorante().get(0));
     * }
     * 
     * System.out.
     * println("\nProssima Pagina:  >\nPagina precedente: <\nESCI - Torna al menu'\n"
     * );
     * 
     * String controller = "";
     * do {
     * controller = sc.nextLine();
     * if (!((controller.equals("<")) || (controller.equals(">")) ||
     * (controller.equalsIgnoreCase("esci")))) {
     * System.out.println("Input non valido. Inserisci nuovamente.");
     * }
     * } while (!(controller.equals("<") || controller.equals(">") ||
     * controller.equalsIgnoreCase("esci")));
     * 
     * switch (controller) {
     * case ">":
     * if (new_count < ristorantiFiltrati.size()) {
     * count += 10;
     * new_count += 10;
     * //System.out.println("count con >" + new_count);
     * } else {
     * System.out.
     * println("Errore. Non sono presenti altri ristoranti, esci oppure vai alla pagina precedente!"
     * );
     * }
     * break;
     * 
     * case "<":
     * if (count > 0) {
     * count -= 10;
     * new_count -= 10;
     * //System.out.println("count con <" + new_count);
     * } else {
     * System.out.
     * println("Errore. Non sono presenti altri ristoranti, esci oppure vai alla pagina successiva!"
     * );
     * }
     * break;
     * 
     * default:
     * System.out.println("Esco");
     * stampa = false;
     * break;
     * }
     * }
     * 
     * sc.close();
     * 
     * }
     */

    public static Ristorante cercaVicinanza(String indirizzo, int raggio) throws IOException {
        Ristorante listaFiltrati = Ristorante.getRistoranti();
        listaFiltrati = geoTheKnife.filtraVicinanza(listaFiltrati, indirizzo, raggio);
        return listaFiltrati;
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

    public static Ristorante cercaFiltri(Ristorante listaFiltrati, String[] filtri) throws IOException {
        Scanner sc = new Scanner(System.in);

        for (String filtro : filtri) {
            //Tipologia
            if (filtro.equals("1")) {
                // FAI LISTA CUCINE
                System.out.println("Inserisci la tipologia di cucina desiderata tra: LISTA CUCINA DA FARE...");
                String tipologia = "";
                tipologia = sc.nextLine();
                listaFiltrati = Ristorante.filtraTipologia(listaFiltrati, tipologia);
            }

            //Delivery
            if (filtro.equals("2")) {
                listaFiltrati = Ristorante.filtraDelivery(listaFiltrati);
            }

            //Booking
            if (filtro.equals("3")) {
                listaFiltrati = Ristorante.filtraBooking(listaFiltrati);
            }

            // da fixare..................................................................................
            //Fascia prezzo
            if (filtro.equals("4")) {
                String prezzo;
                do {
                    TheKnife.pulisci();
                    System.out.println("Inserisci la fascia di prezzo: (€, €€, €€€, €€€€)");
                    prezzo = sc.nextLine().trim();

                    if (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€") || prezzo.equals("€€€€€"))) {
                        System.out.println("Input non valido. Inserisci una fascia valida (€, €€, €€€, €€€€).");
                    }

                } while (!(prezzo.equals("€") || prezzo.equals("€€") || prezzo.equals("€€€") || prezzo.equals("€€€€") || prezzo.equals("€€€€€")));

                listaFiltrati = Ristorante.filtraPrezzo(listaFiltrati, prezzo);
            }
        }
        return listaFiltrati;
    }

    public static void stampaRicerca(Ristorante ristoranti) {
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
                    System.out.println((i + 1) + ") " + riga.get(0) + ", "+ riga.get(2));
                }
            }

            System.out.println("\nProssima Pagina:  >\nPagina precedente: <\nESCI - Torna al menu");

            String controller;
            do {
                controller = sc.nextLine().trim();
                if (!(controller.equals("<") || controller.equals(">") || controller.equalsIgnoreCase("esci"))) {
                    System.out.println("Input non valido. Inserisci nuovamente.");
                }
            } while (!(controller.equals("<") || controller.equals(">") || controller.equalsIgnoreCase("esci")));

            switch (controller) {
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
                default:
                    System.out.println("Esco...");
                    stampa = false;
                    break;
            }
        }
    }

    public static void visualizzaRistorante(Ristorante ristorantiFiltrati, String nomeRistorante) { 
        TheKnife.pulisci();
        for (List<String> ristorante : ristorantiFiltrati.getListaRistoranti()) {
            if (ristorante.get(0).equals(nomeRistorante)) {
                System.out.println(ristorante.get(0) + "(" + ristorante.get(10) + "), " + ristorante.get(7));
                System.out.println("Si trova in: " + ristorante.get(1));
                System.out.println("Tipo di cucina: " + ristorante.get(4));
                System.out.println("Booking: ");
                System.out.println("Delivery: ");
                System.out.println("Website: " + ristorante.get(9));
                System.out.println("Descrizione: \n" + ristorante.get(13));
            }
        }
    }

    // metodi get
    public abstract String getUsername();

    public abstract String getRuolo();

    public abstract String getDomicilio();

}
