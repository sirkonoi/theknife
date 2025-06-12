package theknife;

import java.io.*;
import java.util.*;
/**
 * Classe che rappresenta un utente.
 * 
 * La classe estende {@link GestioneUtenti} e fornisce diverse funzionalità, per esempio
 * visualizzazione del profilo utente e la gestione dei ristoranti preferiti.
 * 
 * L'utente puo' avere un ruolo tra "ristoratore" oppure "utente", memorizzati come stringa.
 */

public class Utente extends GestioneUtenti {
    protected String nome, cognome;
    protected String username;
    protected String psw;
    protected String domicilio;
    protected String ruolo;    

    /**
     * Costruisce un nuovo oggetto "Utente".
     * 
     * @param username Username dell'utente.
     * @param psw Password  dell'utente.
     * @param nome Nome dell'utente.
     * @param cognome Cognome dell'utente.
     * @param domicilio Indirizzo di domicilio dell'utente.
     * @param ruolo Ruolo assegnato all'utente.
     */
    public Utente(String username, String psw, String nome, String cognome, String domicilio, String ruolo) {
        this.username = username;
        this.psw = psw;
        this.nome = nome;
        this.cognome = cognome;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    /**
     * Visualizza il profilo utente, tramite menu' che consente di,
     * visualizzare la propria password, lista dei ristoranti preferiti, lista delle recensioni.
     * 
     * @param user Utente che deve visualizzare il profilo.
     * @throws IOException Se errori di I/O.
     * @throws RestaurantAlreadyExists
     * @throws RecensioneAlreadyExists
     */
    public static void visualizzaProfilo(Utente user) throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {
        String sceltaMenu = "";
        String psw = "";
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<user.getPsw().length(); i++) {
            psw +="*";
        }

        while (true) {
            Utility.pulisci();
            System.out.println("""
                 ____  ____   ___   _____  ____  _       ___  
                |    \\|    \\ /   \\ |     ||    || |     /   \\ 
                |  o  )  D  )     ||   __| |  | | |    |     |
                |   _/|    /|  O  ||  |_   |  | | |___ |  O  |
                |  |  |    \\|     ||   _]  |  | |     ||     |
                |  |  |  .  \\     ||  |    |  | |     ||     |
                |__|  |__|\\_|\\___/ |__|   |____||_____| \\___/ 
                    """);
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + psw);                 
            System.out.println("Nome: " + user.getNome().toUpperCase());
            System.out.println("Cognome: " + user.getCognome().toUpperCase());
            System.out.println("Domicilio: " + user.getDomicilio().toUpperCase());      
            System.out.println("Sei un: " + user.getRuolo().toUpperCase());

            System.out.println("1 - Visualizza password.");            
            System.out.println("2 - Visualizza i tuoi ristoranti preferiti.");      
            System.out.println("3 - Visualizza le tue recensioni.");                      
            System.out.println("ESCI - Torna al menu' principale.");
            sceltaMenu = sc.nextLine();

            if (sceltaMenu.equals("1")) {
                Utility.pulisci();Utility.printLogo();
                psw = Password.decrypt(user.getPsw());
            } 
            else if (sceltaMenu.equals("2")) {
                Utility.stampaRicerca(ListaRistorante.getRistoranti(), "preferiti", user, "Lista dei ristoranti preferiti", true);
            } 
            else if(sceltaMenu.equals("3")) {
                Recensione.visualizzaRecensioniUtente(user, Recensione.getRecensioni(user.getUsername()));
            }  
            else if (sceltaMenu.equalsIgnoreCase("esci")) {
                TheKnife.main_menu();
                break;
            }                                 
            else {
                Utility.pulisci();Utility.printError();
                System.out.println("Scelta non valida. Riprova.\nPremi invio per continuare...");
                sc.nextLine();
            }
        }
    }

    /**
     * Aggiunge un ristorante ai preferiti di un utente e lo scrive su file.
     * 
     * @param username Username dell'utente.
     * @param nomeRistorante Nome del ristorante da aggiungere ai preferiti.
     * @throws IOException Se ci sono errori nella lettura dal file.
     */
    public static void aggiungiPreferiti(String username, String nomeRistorante) throws IOException {
        FileWriter fr = new FileWriter(GestioneFile.getPathPreferiti(), true);
        try {
            fr.write("\n" + username + "," + "\"" + nomeRistorante + "\"");
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore...");
        } 
    }

    /**
     * Restituisce la lista dei ristoranti preferiti di un dato utente.
     * 
     * @param username Username dell'utente.
     * @return Lista di nomi dei ristoranti preferiti.
     * @throws IOException Se ci sono errori nella lettura dal file.
     */
    public static LinkedList<String> getPreferiti(String username) throws IOException {
        LinkedList<List<String>> filePreferiti = GestioneFile.getFilePreferiti();
        LinkedList<String> preferitiUtente = new LinkedList<>();
        for (List<String> preferiti : filePreferiti) {
            if (preferiti.get(0).equals(username)) {
                preferitiUtente.add(preferiti.get(1).replace("\"", ""));
            }
        }
        return preferitiUtente;
    }

    /**
     * Rimuove un ristorante dai preferiti di un utente e lo toglie dal file.
     * 
     * @param user Oggetto Utente.
     * @param nomeRistorante Nome del ristorante da togliere dai preferiti.
     * @throws IOException Se ci sono errori nella lettura dal file.
     */
    public static void togliPreferiti(Utente user, String nomeRistorante) throws IOException {
        String nomeUtente = user.getUsername();
        if (!checkPreferiti(nomeUtente, nomeRistorante)) {
            System.out.println("Errore: Il ristorante non e' nei preferiti.");
            return;
        }

        LinkedList<List<String>> filePreferiti = GestioneFile.getFilePreferiti();
        for (int i = 0; i < filePreferiti.size(); i++) {
            List<String> riga = filePreferiti.get(i);
            if (riga.get(0).equalsIgnoreCase(nomeUtente) && riga.get(1).equalsIgnoreCase(nomeRistorante)) {
                filePreferiti.remove(i);
                break;
            }
        }
        GestioneFile.salvaFilePreferiti(filePreferiti);
    }    

    /**
     * Controlla se un ristorante è già presente nella lista preferiti preferiti di un dato utente.
     * 
     * @param username Username dell'utente.
     * @param nomeRistorante Nome del ristorante da cercare.
     * @return True se il ristorante è nei preferiti, false altrimenti.
     * @throws IOException Se ci sono errori nella lettura dal file.
     */
    public static boolean checkPreferiti(String username, String nomeRistorante) throws IOException {
        LinkedList<String> listaPreferiti = getPreferiti(username);
        for (String restaurant : listaPreferiti) {
            if (!restaurant.isEmpty() && restaurant.replace("\"", "").trim().equalsIgnoreCase(nomeRistorante.replace("\"", "").trim())) {
                return true;
            }
        }
        return false;
    }
    
    /** @return Il nome dell'utente. */    
    public String getNome() {
        return this.nome;
    }

    /** @return Il cognome dell'utente. */
    public String getCognome() {
        return this.cognome;
    }

    /** @return L'username dell'utente. */
    public String getUsername() {
        return this.username;
    }

    /** @return La password dell'utente. */
    public String getPsw() {
        return this.psw;
    }

    /** @return Il domicilio dell'utente. */
    public String getDomicilio() {
        return this.domicilio;
    }

    /** @return Il ruolo dell'utente. */
    public String getRuolo() {
        return this.ruolo;
    }

}