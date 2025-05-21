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

    public static void scriviRecensione(String utente_recensore, String nomeRistorante, String valutazione,
            String recensione) throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "recensioni.csv", true);
        try {
            fr.write("\n" + utente_recensore + "," + "\"" + nomeRistorante + "\"" + "," + valutazione + "," + "\"" + recensione + "\"");
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore...");
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

    public static void visualizzaRecensioniUtente(String nomeUtente, LinkedList<List<String>> recensioniRistorante) {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);

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

    public static void visualizzaRecensioniRistorante(String nomeRistorante,
            LinkedList<List<String>> recensioniUtente) {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);

        boolean stampa = true;
        while (stampa) {
            TheKnife.pulisci();

            System.out.println(nomeRistorante.toUpperCase() + " - Recensione (Numero " + (count + 1) + " di "
                    + recensioniUtente.size() + " totali):\n");

            for (int i = count; i < new_count && i < recensioniUtente.size(); i++) {
                List<String> recensione = recensioniUtente.get(i);
                int numStelle = Integer.parseInt(recensione.get(2));

                String stelle = "";
                for (int j = 0; j < numStelle; j++) {
                    stelle += "*";
                }
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
            System.out.println("ESCI - Torna indietro.");

            String input = sc.nextLine().trim();

            switch (input.toLowerCase()) {
                case ">":
                    if (new_count < recensioniUtente.size()) {
                        count += 1;
                        new_count += 1;
                    } else {
                        System.out.println("Errore. Non sono presenti altre recensioni.");
                    }
                    break;

                case "<":
                    if (count >= 1) {
                        count -= 1;
                        new_count -= 1;
                    } else {
                        System.out.println("Errore. Sei già alla prima recensione.");
                    }
                    break;
                default:
                    stampa = false;
                    break;
            }
        }
    }

    public static double getMediaVoti(String nomeRistorante) throws FileNotFoundException, IOException {
        LinkedList<List<String>> recensioniUtente = getRecensioniRistorante(nomeRistorante);
        double totvoti = 0.0;
        int count = 0;
        for (List<String> recensioni : recensioniUtente) {
            totvoti+=Double.parseDouble(recensioni.get(2));
            count++;
        }
        return totvoti/count;

    }

    public static void visualizzaRiepilogo(String nomeRistorante) throws IOException {
        System.out.println("Numero di recensioni: " + getRecensioniRistorante(nomeRistorante).size());
        System.out.println("Media Voti: " + getMediaVoti(nomeRistorante));        
    }
}
