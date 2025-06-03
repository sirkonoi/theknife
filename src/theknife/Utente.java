package theknife;

import java.io.*;
import java.util.*;

public class Utente extends GestioneUtenti {
    protected String nome, cognome;
    protected static String username;
    protected String psw;
    protected String domicilio;
    protected String ruolo;

    // costruttore
    public Utente(String username, String psw, String nome, String cognome, String domicilio, String ruolo) {
        this.username = username;
        this.psw = psw;
        this.nome = nome;
        this.cognome = cognome;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    public static void visualizzaProfilo(Utente user) throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {
        String sceltaMenu = "";
        String psw = "";
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<user.getPsw().length(); i++) {
            psw +="*";
        }

        while (true) {
            TheKnife.pulisci();
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
            System.out.println("4 - Torna al menu' principale.");
            sceltaMenu = sc.nextLine();

            if (sceltaMenu.equals("1")) {
                TheKnife.pulisci();TheKnife.printLogo();
                psw = Password.decrypt(user.getPsw());
            } 
            else if (sceltaMenu.equals("2")) {
                Utility.stampaRicerca(ListaRistorante.getRistoranti(), "preferiti", user, "Lista dei ristoranti preferiti", true);
            } 
            else if(sceltaMenu.equals("3")) {
                Recensione.visualizzaRecensioniUtente(user, Recensione.getRecensioni(user.getUsername()));
            }  
            else if (sceltaMenu.equals("4")) {
                TheKnife.main_menu();
                break;
            }                                 
            else {
                TheKnife.pulisci();TheKnife.printLogo();
                System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    //Aggiunge un ristorante ai preferiti
    public static void aggiungiPreferiti(String username, String nomeRistorante) throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "preferiti.csv", true);
        try {
            fr.write("\n" + username + "," + "\"" + nomeRistorante + "\"");
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore...");
        } 
    }

    //Restituisce linkedlist con tutti i ristoranti preferiti di un utente
    public static LinkedList<String> getPreferiti(String user) throws IOException {
        LinkedList<List<String>> filePreferiti = GestioneFile.getFilePreferiti();
        LinkedList<String> preferitiUtente = new LinkedList<>();
        for (List<String> preferiti : filePreferiti) {
            if (preferiti.get(0).equals(user)) {
                preferitiUtente.add(preferiti.get(1).replace("\"", ""));
            }
        }
        return preferitiUtente;
    }

    public static boolean checkPreferiti(String user, String name) throws IOException {
        LinkedList<String> listaPreferiti = getPreferiti(user);
        for (String restaurant : listaPreferiti) {
            if (!restaurant.isEmpty() && restaurant.replace("\"", "").trim().equalsIgnoreCase(name.replace("\"", "").trim())) {
                return true;
            }
        }
        return false;
    }
    
    // metodi Get
    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPsw() {
        return this.psw;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public String getRuolo() {
        return this.ruolo;
    }

}