package theknife;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Classe per la gestione dei file CSV relativi all'applicazione TheKnife.
 * <p>
 * La classe gestisce il caricamento e il salvataggio dei dati da e verso file CSV,
 * relativi a ristoranti, utenti, recensioni, preferiti e risposte alle recensioni.
 * Include anche un parser per la lettura corretta di righe CSV con virgolette, per evitare problemi che possono derivarne.
 */

public class GestioneFile {

    /** Percorso del file degli utenti. */        
    static Path pathUtenti = Paths.get("data", "users.csv");   
    /** Percorso del file dei ristoratori. */        
    static Path pathRistoratori = Paths.get("data", "ristoratori.csv"); 
    /** Percorso del file dei ristoranti. */        
    static Path pathRistoranti = Paths.get("data", "restaurants.csv");
    /** Percorso del file delle recensioni. */        
    static Path pathRecensioni = Paths.get("data", "recensioni.csv");    
    /** Percorso del file delle risposte alle recensioni. */        
    static Path pathRisposteRecensioni = Paths.get("data", "recensioni_responses.csv");             
    /** Percorso del file dei preferiti. */    
    static Path pathPreferiti = Paths.get("data", "preferiti.csv");         
    
    /**
     * Restituisce il percorso del file degli utenti.
     *
     * @return Percorso del file users (Stringa).
     */        
    public static String getPathUtenti() {
        return pathUtenti.toString();
    }
    /**
     * Restituisce il percorso del file dei ristoratori.
     *
     * @return Percorso del file ristoratori (Stringa).
     */    
    public static String getPathRistoratori() {
        return pathRistoratori.toString();
    }
    /**
     * Restituisce il percorso del file dei ristoranti.
     *
     * @return Percorso del file restaurants (Stringa).
     */     
    public static String getPathRistoranti() {
        return pathRistoranti.toString();
    }      
    /**
     * Restituisce il percorso del file delle recensioni.
     *
     * @return Percorso del file recensioni (Stringa).
     */      
    public static String getPathRecensioni() {
        return pathRecensioni.toString();
    }  
    /**
     * Restituisce il percorso del file delle risposte alle recensioni.
     *
     * @return Percorso del file recensioni_responses (Stringa).
     */      
    public static String getPathRisposteRecensioni() {
        return pathRisposteRecensioni.toString();
    }  
    /**
     * Restituisce il percorso del file dei preferiti.
     *
     * @return Percorso del file preferiti (Stringa).
     */      
    public static String getPathPreferiti() {
        return pathPreferiti.toString();
    }    

    /**
     * Esegue il parsing di una riga CSV, le virgolette vengono considerate come modo per delimitare i differenti campi.
     *
     * @param line riga CSV da analizzare
     * @return lista dei campi contenuti nella riga
     */
    public static List<String> parseRiga(String line) {
        List<String> riga = new ArrayList<>();
        String campo = "";
        boolean isVirgolette = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                isVirgolette = !isVirgolette;
            } else if (c == ',' && !isVirgolette) {
                riga.add(campo.trim());
                campo = "";
            } else {
                campo += c;
            }
        }

        riga.add(campo.trim());
        return riga;
    }
    /**
     * Legge il file dei preferiti e restituisce una lista contenente le righe.
     *
     * @return Lista di righe, rappresentata come una lista di stringhe (Una stringa = un campo)
     * @throws IOException Il file non puo' essere letto.
     */
    public static LinkedList<List<String>> getFilePreferiti() throws IOException {
        LinkedList<List<String>> preferiti = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathPreferiti.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> riga = GestioneFile.parseRiga(line);
                preferiti.add(riga);
            }
        }

        return preferiti;
    }

    /**
     * Legge il file delle recensioni e restituisce una lista contenente le righe.
     *
     * @return Lista di recensioni.
     * @throws IOException Il file non puo' essere letto.
     */
    public static LinkedList<List<String>> getFileRecensioni() throws FileNotFoundException, IOException {
        LinkedList<List<String>> recensioni = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathRecensioni.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> recensione = GestioneFile.parseRiga(line);
                recensioni.add(recensione);
            }
        }

        return recensioni;
    }

    /**
     * Legge il file dei ristoratori e restituisce una lista contenente le righe.
     *
     * @return Lista dei ristoratori.
     * @throws IOException Il file non puo' essere letto.
     */
    public static LinkedList<List<String>> getFileRistoratori() throws FileNotFoundException, IOException {
        LinkedList<List<String>> ristoratori = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathRistoratori.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> recensione = GestioneFile.parseRiga(line);
                ristoratori.add(recensione);
            }
        }

        return ristoratori;
    }

    /**
     * Legge il file delle risposte alle recensioni e restituisce una lista contenente le righe.
     *
     * @return Lista delle risposte alle recensioni.
     * @throws IOException Il file non puo' essere letto.
     */
    public static LinkedList<List<String>> getFileRisposteRecensioni() throws FileNotFoundException, IOException {
        LinkedList<List<String>> risposte = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathRisposteRecensioni.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> risposta = GestioneFile.parseRiga(line);
                risposte.add(risposta);
            }
        }

        return risposte;
    }

    /**
     * Scrive una lista di recensioni nel file recensioni.csv, viene utilizzato per
     * la modifica, eliminazione delle recensioni da parte di un utente.
     *
     * @param recensioni Lista delle recensioni da scrivere.
     * @throws IOException Errore durante la scrittura.
     */    
    public static void salvaFileRecensioni(List<List<String>> recensioni) throws IOException {
        File file = new File(pathRecensioni.toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("utente_recensore,\"Nome Ristorante\",Valutazione,\"Recensione\"");
            writer.newLine();

            for (List<String> riga : recensioni) {
                if (riga.get(0).equalsIgnoreCase("utente_recensore")) {
                    continue;
                }

                for (int i = 0; i < riga.size(); i++) {
                    String campo = riga.get(i);
                    if (campo.contains(",") || campo.contains("\"")) {
                        campo = "\"" + campo.replace("\"", "\"\"") + "\"";
                    }
                    writer.write(campo);
                    if (i < riga.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        }
    }

}
