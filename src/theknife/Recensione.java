package theknife;

import java.io.*;
import java.util.*;

/**
 * La classe {@code Recensione} rappresenta una recensione fatta da un utente per un dato ristorante.
 * Gestisce la creazione, modifica, eliminazione e visualizzazione delle recensioni e delle risposte 
 * dei corrispettivi ristoratori.
 */
public class Recensione {
    String usernameUtente, nomeRistorante, desc;
    double valutazione = 0.0;

    /**
     * Costruisce un oggetto "Recensione".
     * 
     * @param usernameUtente Username dell'utente che scrive la recensione.
     * @param nomeRistorante Nome del ristorante che viene recensito.
     * @param valutazione Valutazione numerica (es. da 1 a 5).
     * @param desc Testo della recensione.
     */    
    public Recensione(String usernameUtente, String nomeRistorante, double valutazione, String desc) {
        this.usernameUtente = usernameUtente;
        this.nomeRistorante = nomeRistorante;
        this.valutazione = valutazione;
        this.desc = desc;
    }

    /**
     * Verifica se un utente ha gia' scritto una recensione per un dato ristorante.
     * Nota, un utente puo' scrivere solo una recensione per ristorante.
     * 
     * @param username Username dell'utente.
     * @param nomeRistorante Nome del ristorante.
     * @return True se esiste già una recensione, false altrimenti.
     * @throws IOException Se si verifica un errore durante il caricamento della lista delle recensioni.
     */    
    public static boolean checkRecensione(String username, String nomeRistorante) throws IOException {
        LinkedList<List<String>> listaRecensioni = getRecensioni(username);

        for (List<String> recensioni : listaRecensioni) {
            if (recensioni.get(0).equalsIgnoreCase(username) && recensioni.get(1).equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }

        return false;
    }
    /**
     * Verifica se un ristoratore ha gia' scritto una risposta a una recensione.
     * Nota, un ristoratore puo' scrivere solo una risposta per recensione.
     * 
     * @param usernameUtente Username dell'utente che ha recensito il ristorante.
     * @param usernameRistoratore Username del ristoratore.
     * @param nomeRistorante Nome del ristorante.
     * @return True se la risposta esiste, false altrimenti.
     * @throws IOException Se si verifica un errore durante il caricamento della lista delle risposte.
     */   
    public static boolean checkRisposta(String usernameUtente, String usernameRistoratore, String nomeRistorante) throws IOException {
        LinkedList<List<String>> listaRisposte = GestioneFile.getFileRisposteRecensioni();

        for (List<String> risposta : listaRisposte) {
            if (risposta.get(0).equalsIgnoreCase(usernameUtente) && risposta.get(1).equalsIgnoreCase(nomeRistorante) && risposta.get(2).equalsIgnoreCase(usernameRistoratore)) {
                return true;
            }
        }

        return false;
    }    

    /**
     * Permette ad un utente di scrivere una nuova recensione per un ristorante.
     * Se l'utente ha già scritto una recensione per quel ristorante, lancia l'eccezione {@link RecensioneAlreadyExists}.
     * 
     * @param nomeRistorante Nome del ristorante da recensire
     * @param user Utente che scrive la recensione
     * @throws IOException
     * @throws RecensioneAlreadyExists Se la recensione gia' esiste.
     */
    public static void aggiungiRecensione(String nomeRistorante, Utente user)
            throws IOException, RecensioneAlreadyExists {

        if (checkRecensione(user.getUsername(), nomeRistorante)) {
            throw new RecensioneAlreadyExists("Errore! Hai gia' scritto una recensione per questo ristorante!");
        }

        Scanner sc = new Scanner(System.in);
        Utility.pulisci();
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
    /**
     * Modifica una recensione esistente. La elimina e richiede all'utente la nuova recensione.
     * 
     * @param user Utente che vuole modificare la recensione.
     * @param nomeRistorante Nome del ristorante della recensione da modificare.
     * @throws IOException Se errore di I/O.
     * @throws RecensioneAlreadyExists La recensione e' gia' esistente.
     */
    public static void modificaRecensione(Utente user, String nomeRistorante) throws IOException, RecensioneAlreadyExists {
        Scanner sc = new Scanner(System.in);
        String nomeUtente = user.getUsername();

        if (checkRecensione(nomeUtente, nomeRistorante) == true) 
        {
            eliminaRecensione(user, nomeRistorante);
            aggiungiRecensione(nomeRistorante, user);
        }
    }
    /**
     * Elimina la recensione di un utente per un dato ristorante.
     * 
     * @param user Utente che ha scritto la recensione.
     * @param nomeRistorante Nome del ristorante della recensione da eliminare.
     * @throws IOException Se errore di I/O.
     */
    public static void eliminaRecensione(Utente user, String nomeRistorante) throws IOException {
        String nomeUtente = user.getUsername();
        if (!checkRecensione(nomeUtente, nomeRistorante)) {
            System.out.println("Recensione non trovata.");
            return;
        }

        LinkedList<List<String>> fileRecensioni = GestioneFile.getFileRecensioni();
        for (int i = 0; i < fileRecensioni.size(); i++) {
            List<String> riga = fileRecensioni.get(i);
            if (riga.size() >= 2 &&
                    riga.get(0).equals(nomeUtente) &&
                    riga.get(1).equalsIgnoreCase(nomeRistorante)) {
                fileRecensioni.remove(i);
                break;
            }
        }
        GestioneFile.salvaFileRecensioni(fileRecensioni);
    }

    /**
     * Scrive una nuova recensione sul file recensioni.csv.
     * 
     * @param utente_recensore Username dell'utente che scrive la recensione.
     * @param nomeRistorante Nome del ristorante che viene recensito.
     * @param valutazione Valutazione numerica (1-5) che viene passata come stringa.
     * @param recensione Testo della recensione.
     * @throws IOException Se errore di I/O.
     */
     public static void scriviRecensione(String utente_recensore, String nomeRistorante, String valutazione, String recensione) throws IOException {

            FileWriter fr = new FileWriter(GestioneFile.getPathRecensioni(), true);
            try {
                fr.write(utente_recensore + "," + "\"" + nomeRistorante + "\"" + "," + valutazione + "," + "\"" + recensione + "\"");
            } catch (IOException e) {
                System.out.println("Errore!!!!!");
            } finally {
                fr.close();
            }
        }
    /**
     * Scrive una nuova risposta a una recensione sul file recensioni_responses.csv.
     * 
     * @param recensione Recensione a cui il ristoratore risponde.
     * @param usernameRistoratore Username del ristoratore che risponde.
     * @param risposta Testo della risposta.
     * @throws IOException Se errore di I/O.
     */
        public static void scriviRisposta(Recensione recensione, String usernameRistoratore, String risposta) throws IOException {

            FileWriter fr = new FileWriter(GestioneFile.getPathRisposteRecensioni(), true);
            try {
                fr.write(recensione.getUser() + "," + "\"" + recensione.nomeRistorante + "\"" + "," + usernameRistoratore + "," + "\"" + risposta + "\" " + "\n");
            } catch (IOException e) {
                System.out.println("Errore!!!!!");
            } finally {
                fr.close();
            }
        }        

    /**
     * Permette a un ristoratore di rispondere a una recensione, se non ha già risposto.
     *
     * @param username Il nome utente del ristoratore.
     * @param recensione Oggetto Recensione a cui rispondere.
     * @throws IOException Se errore di I/O.
     */        
    public static void rispondiRecensione(String username, Recensione recensione) throws IOException {
        Scanner sc = new Scanner(System.in);
        String risposta;
        if(checkRisposta(recensione.getUser(), username, recensione.getNomeRistorante())) {
            System.out.println("Errore. Hai gia' risposto a questa recensione!\nPremi invio per continuare...");
            sc.nextLine();
            return;
        }
        Utility.pulisci();
        System.out.println("Scrivi una risposta alla recensione: ");
        risposta = sc.nextLine();
        scriviRisposta(recensione, username, risposta);
        System.out.println("Fatto!\nPremi invio per continuare");
        sc.nextLine();
        return;
    }

    /**
     * Restituisce la lista di recensioni scritte da un dato utente.
     *
     * @param username Il nome utente dell'utente..
     * @return Una lista di liste di stringhe contenenti i dati delle recensioni dell'utente.
     * @throws IOException Se errore durante lettura del file.
     */
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

    /**
     * Restituisce tutte le recensioni associate a un ristorante.
     *
     * @param nomeRistorante Il nome del ristorante.
     * @return Una lista di liste di stringhe contenenti i dati delle recensioni del ristorante.
     * @throws IOException Se errore durante lettura del file.
     */
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

    /**
     * Visualizza la risposta di un ristoratore a una data recensione.
     *
     * @param usernameUtente Il nome utente dell'autore della recensione.
     * @param nomeRistorante Il nome del ristorante della recensione.
     * @throws IOException Se errore durante lettura del file.
     */    
    public static void visualizzaRisposta(String usernameUtente, String nomeRistorante) throws IOException {
            LinkedList<List<String>> fileRisposte = GestioneFile.getFileRisposteRecensioni();
            Scanner sc = new Scanner(System.in);
            boolean recensioneTrovata = false;

            for (List<String> risposta : fileRisposte) {
                if (risposta.get(0).equalsIgnoreCase(usernameUtente) &&
                    risposta.get(1).replaceAll("\"", "").equalsIgnoreCase(nomeRistorante)) {

                    String testo_risposta = risposta.get(3).replaceAll("\"", "");

                    Utility.pulisci();
                    System.out.println("==========================================");
                    System.out.println("Risposta del ristoratore (" + risposta.get(2) + "):");
                    System.out.println(testo_risposta);
                    System.out.println("==========================================");
                    recensioneTrovata = true;
                    break;
                }
            }

            if (!recensioneTrovata) {
                System.out.println("Errore: nessuna risposta trovata per questa recensione.");
            }

            System.out.println("Premi invio per continuare...");
            sc.nextLine();
        }

    /**
     * Consente a un utente di visualizzare le proprie recensioni, modificarle ed eliminarle.
     *
     * @param user Utente.
     * @param recensioniRistorante La lista delle recensioni.
     * @throws IOException Se errore durante di I/O.
     * @throws RecensioneAlreadyExists
     */
    public static void visualizzaRecensioniUtente(Utente user, LinkedList<List<String>> recensioniRistorante) throws IOException, RecensioneAlreadyExists {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);
        String nomeRistorante = "";
        boolean stampa = true;

        Recensione recensioneCorrente = null;     
        while (stampa) {
            Utility.pulisci();

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
                recensioneCorrente = new Recensione(recensione.get(0), recensione.get(1), Double.parseDouble(recensione.get(2)), recensione.get(3));                
            }

            System.out.println("\nProssima Recensione:  >");
            System.out.println("Recensione precedente: <");
            System.out.println("\nVISUALIZZA RISPOSTA - Visualizza la risposta del ristoratore.");
            System.out.println("MODIFICA - Modifica la recensione.");
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
                case "visualizza risposta":
                visualizzaRisposta(user.getUsername(), nomeRistorante);          
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
    /**
     * Consente di visualizzare tutte le recensioni di un ristorante e, se l'utente e' un ristoratore 
     * ed e' il proprietario del ristorante, consente di rispondere ad esse.
     *
     * @param nomeRistorante Il nome del ristorante.
     * @param username Username utente.
     * @param recensioniUtente Lista delle recensioni associate al ristorante.
     * @throws FileNotFoundException Il file delle recensioni non viene trovato.
     * @throws IOException Se errore durante la lettura.
     */
    public static void visualizzaRecensioniRistorante(String nomeRistorante, String username,
            LinkedList<List<String>> recensioniUtente) throws FileNotFoundException, IOException {
        int count = 0;
        int new_count = 1;
        Scanner sc = new Scanner(System.in);
        Recensione recensioneCorrente = null;
        boolean stampa = true;
        while (stampa) {
            Utility.pulisci();

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
                recensioneCorrente = new Recensione(recensione.get(0), recensione.get(1), Double.parseDouble(recensione.get(2)), recensione.get(3));                 
            }

            System.out.println("\nProssima Recensione:  >");
            System.out.println("Recensione precedente: <");
            System.out.println("\nVISUALIZZA RISPOSTA - Visualizza la risposta del ristoratore.");
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
                    if(checkRisposta(recensioneCorrente.getUser(), username, nomeRistorante)) {
                        System.out.println("Errore: hai gia' scritto una risposta!\nPremi invio per continuare...");
                        sc.nextLine();
                        break;                        
                    }                  
                    rispondiRecensione(username, recensioneCorrente);
                    break;
                case "visualizza risposta":
                visualizzaRisposta(recensioneCorrente.getUser(), recensioneCorrente.getNomeRistorante());                        
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

    /**
     * Calcola la media dei voti delle recensioni di un dato ristorante.
     *
     * @param nomeRistorante Il nome del ristorante.
     * @return La media dei voti delle recensioni.
     * @throws FileNotFoundException Il file delle recensioni non viene trovato.
     * @throws IOException Se errore durante la lettura.
     */
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
    /**
     * Visualizza un riepilogo delle recensioni di un ristorante, ovvero
     * il numero totale di recensioni e la media dei voti.
     *
     * @param nomeRistorante Il nome del ristorante di cui mostrare il riepilogo.
     * @throws IOException Se si verifica un errore durante la lettura dei file.
     */
    public static void visualizzaRiepilogo(String nomeRistorante) throws IOException {
        System.out.println("Numero di recensioni: " + getRecensioniRistorante(nomeRistorante).size());
        System.out.println("Media Voti: " + getMediaVoti(nomeRistorante));
    }

    /**
     * Restituisce l'username dell'autore della recensione.
     *
     * @return Username dell'utente.
     */
    public String getUser() {
        return this.usernameUtente;
    }
    /**
     * Restituisce la valutazione della recensione come stringa.
     *
     * @return La valutazione della recensione come stringa.
     */    
    public String getValutazione() {
        return Double.toString(this.valutazione);
    }
    /**
     * Restituisce il nome del ristorante recensito.
     *
     * @return Il nome del ristorante.
     */    
    public String getNomeRistorante() {
        return this.nomeRistorante;
    }
    /**
     * Restituisce il testo della recensione.
     *
     * @return Testo della recensione.
     */    
    public String getDesc() {
        return this.desc;
    }            
}
