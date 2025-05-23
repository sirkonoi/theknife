package theknife;

import java.io.*;
import java.util.*;

public class Recensione {

    public static String sep = (File.separator);

    public static boolean checkRecensione(String username, String nomeRistorante) throws IOException {
        LinkedList<List<String>> listaRecensioni = getRecensioni(username);

        for (List<String> recensioni : listaRecensioni) {
            if (recensioni.get(0).equals(username) && recensioni.get(1).equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }

        return false;
    }

    public static void aggiungiRecensione(String nomeRistorante, Utente user)
            throws IOException, RecensioneAlreadyExists {

        if (checkRecensione(user.getUsername(), nomeRistorante)) {
            throw new RecensioneAlreadyExists("Errore! Hai gia' scritto una recensione per questo ristorante!");
        }

        Scanner sc = new Scanner(System.in);
        TheKnife.pulisci();
        System.out.println("Scrivi la tua recensione per il ristorante " + nomeRistorante + ": ");
        String recensione = sc.nextLine();
        int voto = -15;
        do {
            System.out.println("Inserisci la tua valutazione: (da 1 a 5 stelle): ");
            String input = sc.nextLine();

            try {
                voto = Integer.parseInt(input);
                if (voto < 1 || voto > 5) {
                    System.out.println("Errore! Devi inserire un numero tra 1 e 5!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore! Inserisci solo numeri interi.");
            }

        } while (voto < 1 || voto > 5);
        String username = user.getUsername();
        String valutazione = String.valueOf(voto);
        scriviRecensione(username, nomeRistorante, valutazione, recensione);
    }

        public static void modificaRecensione(Utente user, String nomeRistorante) throws IOException, RecensioneAlreadyExists {
        Scanner sc = new Scanner(System.in);
        String nomeUtente = user.getUsername();

        if (checkRecensione(nomeUtente, nomeRistorante) == true) 
        {
            eliminaRecensione(user, nomeRistorante);
            aggiungiRecensione(nomeRistorante, user);
        }
    }

    public static void eliminaRecensione(Utente user, String nomeRistorante) throws IOException { //mi serviva nomeRistorante come parametro cosi da metterlo in modificaRecensione
        String nomeUtente = user.getUsername();
        if (!checkRecensione(nomeUtente, nomeRistorante)) {
            System.out.println("Recensione non trovata.");
            return;
        }

        LinkedList<List<String>> tutteLeRecensioni = GestioneFile.getFileRecensioni();
        Iterator<List<String>> iterator = tutteLeRecensioni.iterator();
        while (iterator.hasNext()) {
            List<String> riga = iterator.next();
            if (riga.size() >= 2 &&
                    riga.get(0).equals(nomeUtente) &&
                    riga.get(1).equalsIgnoreCase(nomeRistorante)) {
                iterator.remove();
                break;
            }
        }
        GestioneFile.salvaFileRecensioni(tutteLeRecensioni);
    }

        public static void scriviRecensione(String utente_recensore, String nomeRistorante, String valutazione, String recensione) throws IOException {
            File file = new File("data" + sep + "recensioni.csv");
            boolean fileEsistente = file.exists() && file.length() > 0;

            FileWriter fr = new FileWriter(file, true);
            try {
                fr.write(utente_recensore + "," + "\"" + nomeRistorante + "\"" + "," + valutazione + "," + "\"" + recensione + "\"");
            } catch (IOException e) {
                System.out.println("Errore!!!!!");
            } finally {
                fr.close();
            }
        }


    public static LinkedList<List<String>> getRecensioni(String username) throws IOException {
        LinkedList<List<String>> fileRecensioni = GestioneFile.getFileRecensioni();
        LinkedList<List<String>> recensioniUtente = new LinkedList<>();
        for (List<String> recensioni : fileRecensioni) {
            if (recensioni.get(0).equals(username)) {
                recensioniUtente.add(recensioni);
            }
        }
        return recensioniUtente;
    }

    public static LinkedList<List<String>> getRecensioniRistorante(String nomeRistorante) throws IOException {
        LinkedList<List<String>> fileRecensioni = GestioneFile.getFileRecensioni();
        LinkedList<List<String>> recensioniUtente = new LinkedList<>();
        for (List<String> recensioni : fileRecensioni) {
            if (recensioni.get(1).replaceAll("\"", "").equals(nomeRistorante)) {
                recensioniUtente.add(recensioni);
            }
        }
        return recensioniUtente;
    }

    public static void visualizzaRecensioniUtente(Utente user, LinkedList<List<String>> recensioniRistorante) throws IOException, RecensioneAlreadyExists {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);
        String nomeRistorante = "";
        boolean stampa = true;
        while (stampa) {
            TheKnife.pulisci();

            System.out.println(
                    "Recensione (Numero " + (count + 1) + " di " + recensioniRistorante.size() + " totali):\n");

            for (int i = count; i < new_count && i < recensioniRistorante.size(); i++) {
                List<String> recensione = recensioniRistorante.get(i);

                int numStelle = 1;
                String valutazione = recensione.get(2);
                if (valutazione != null && !valutazione.trim().isEmpty()) {
                    try {
                        numStelle = Integer.parseInt(valutazione.trim());
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Ho trovato una valutazione non valida in una recensione, sarà impostata a 1 stella.");
                    }
                }

                String stelle = "";
                for (int j = 0; j < numStelle; j++) {
                    stelle += "*";
                }
                nomeRistorante = recensione.get(1);
                System.out.println("==========================================");
                System.out.println(" Ristorante : " + recensione.get(1));
                System.out.println(" Valutazione: " + stelle + " Stelle");
                System.out.println("------------------------------------------");
                System.out.println(" Recensione:");
                System.out.println(" " + recensione.get(3).replaceAll("\"", ""));
                System.out.println("==========================================\n");
            }

            System.out.println("\nProssima Recensione:  >");
            System.out.println("Recensione precedente: <");
            System.out.println("\nMODIFICA - Modifica la recensione.");
            System.out.println("ELIMINA - Elimina la recensione.");            
            System.out.println("ESCI - Torna indietro.");

            String input = sc.nextLine().trim();

            switch (input.toLowerCase()) {
                case ">":
                    if (new_count < recensioniRistorante.size()) {
                        count += 1;
                        new_count += 1;
                    } else {
                        System.out.println("Errore. Non sono presenti altre recensioni.");
                        System.out.println("Premi invio per continuare...");
                        sc.nextLine();
                    }
                    break;

                case "<":
                    if (count >= 1) {
                        count -= 1;
                        new_count -= 1;
                    } else {
                        System.out.println("Errore. Sei già alla prima recensione.");
                        System.out.println("Premi invio per continuare...");
                        sc.nextLine();
                    }
                    break;
                case "modifica":
                    modificaRecensione(user,nomeRistorante);
                    System.out.println("Recensione modificata con successo.");
                    System.out.println("Premi invio per continuare...");
                    sc.nextLine(); 
                    recensioniRistorante = getRecensioni(user.getUsername());
                    count = 0;
                    new_count = 1;                                        
                    break;
                case "elimina":
                    eliminaRecensione(user, nomeRistorante);
                    System.out.println("Recensione eliminata con successo.");
                    System.out.println("Premi invio per continuare...");
                    sc.nextLine();            
                    recensioniRistorante = getRecensioni(user.getUsername());
                    count = 0;
                    new_count = 1;                             
                    break;                    
                case "esci":
                    stampa = false;
                    break;

                default:
                    System.out.println("Input non valido. Riprova.");
                    System.out.println("Premi invio per continuare...");
                    sc.nextLine();
                    break;
            }
        }
    }

    public static void visualizzaRecensioniRistorante(String nomeRistorante, String username,
            LinkedList<List<String>> recensioniUtente) throws FileNotFoundException, IOException {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);

        boolean stampa = true;
        while (stampa) {
            TheKnife.pulisci();

            int pagina = recensioniUtente.isEmpty() ? 0 : count + 1;

            System.out.println(nomeRistorante.toUpperCase() + " - Recensione (Numero " + pagina + " di "
                    + recensioniUtente.size() + " totali):\n");

            if (recensioniUtente.isEmpty()) {
                System.out.println("Nessuna recensione disponibile per questo ristorante.\n");
            } else {
                List<String> recensione = recensioniUtente.get(count);
                int numStelle = Integer.parseInt(recensione.get(2));
                String stelle = "*".repeat(numStelle);

                System.out.println("==========================================");
                System.out.println(" Utente : " + recensione.get(0));
                System.out.println(" Valutazione: " + stelle + " Stelle");
                System.out.println("------------------------------------------");
                System.out.println(" Recensione:");
                System.out.println(" " + recensione.get(3).replaceAll("\"", ""));
                System.out.println("==========================================\n");
            }

            System.out.println("\nProssima Recensione:  >");
            System.out.println("Recensione precedente: <");
            if (Ristoratore.isProprietario(username, nomeRistorante)) {
                System.out.println("RISPONDI - Rispondi alla recensione dell'utente.");
            }
            System.out.println("ESCI - Torna indietro.");
            String input = sc.nextLine().trim();

            switch (input.toLowerCase()) {
                case ">":
                    if (new_count < recensioniUtente.size()) {
                        count += 1;
                        new_count += 1;
                    } else {
                        System.out.println("Errore. Non sono presenti altre recensioni.");
                        System.out.println("Premi invio per continuare...");
                        sc.nextLine();
                    }
                    break;

                case "<":
                    if (count >= 1) {
                        count -= 1;
                        new_count -= 1;
                    } else {
                        System.out.println("Errore. Sei già alla prima recensione.");
                        System.out.println("Premi invio per continuare...");
                        sc.nextLine();
                    }
                    break;
                case "rispondi":
                    if (!Ristoratore.isProprietario(username, nomeRistorante)) {
                        System.out.println(
                                "Errore: non sei il proprietario del ristorante!\nPremi invio per continuare...");
                        sc.nextLine();
                        break;
                    }
                    // rispondiRecensione();
                    break;
                case "esci":
                    stampa = false;
                    break;
                default:
                    System.out.println("Errore: inserisci un comando valido. Riprova.");
                    System.out.println("Premi invio per continuare...");
                    sc.nextLine();
                    break;
            }
        }
    }

    public static double getMediaVoti(String nomeRistorante) throws FileNotFoundException, IOException {
        LinkedList<List<String>> recensioniUtente = getRecensioniRistorante(nomeRistorante);
        double totvoti = 0.0;
        int count = 0;
        for (List<String> recensioni : recensioniUtente) {
            totvoti += Double.parseDouble(recensioni.get(2));
            count++;
        }
        return totvoti / count;

    }

    public static void visualizzaRiepilogo(String nomeRistorante) throws IOException {
        System.out.println("Numero di recensioni: " + getRecensioniRistorante(nomeRistorante).size());
        System.out.println("Media Voti: " + getMediaVoti(nomeRistorante));
    }
}
