package theknife;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void visualizzaProfilo(Utente user) throws IOException {
        TheKnife.pulisci();TheKnife.printLogo();
        String sceltaMenu = "";
        String psw = "";
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<user.getPsw().length(); i++) {
            psw +="*";
        }

        while (true) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + psw);                 
            System.out.println("Nome: " + user.getNome().toUpperCase());
            System.out.println("Cognome: " + user.getCognome().toUpperCase());
            System.out.println("Domicilio: " + user.getDomicilio().toUpperCase());      
            System.out.println("Sei un: " + user.getRuolo().toUpperCase());

            System.out.println("1 - Visualizza password.");            
            System.out.println("2 - Visualizza i tuoi ristoranti preferiti.");              
            System.out.println("3 - Torna al menu' principale.");
            sceltaMenu = sc.nextLine();

            if (sceltaMenu.equals("1")) {
                TheKnife.pulisci();TheKnife.printLogo();
                psw = Password.decrypt(user.getPsw());
            } 
            else if (sceltaMenu.equals("2")) {
                visualizzaPreferiti(user.getUsername());
            } 
            else if (sceltaMenu.equals("3")) {
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

    //RICORDATI DI FIXARE!!!!!!!!!!!
    public static void visualizzaPreferiti(String username) throws IOException {
        boolean stampa = true;
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int new_count = 10;

        LinkedList<String> ristorantiPreferiti = getPreferiti(username);
        Ristorante ristoranti = Ristorante.getRistoranti(); 

        while (stampa) {
            TheKnife.pulisci();

            int paginaCorrente = (count / 10) + 1;
            int totalePagine = (ristorantiPreferiti.size() + 9) / 10;

            System.out.println("Lista ristoranti preferiti (Pagina " + paginaCorrente + " di " + totalePagine + ")\n");
                if(ristorantiPreferiti.size()==0) {
                    System.out.println("Nessun ristorante preferito.");
                }            

            for (int i = count; i < new_count && i < ristorantiPreferiti.size(); i++) {
                    System.out.println((i + 1) + ") " + ristorantiPreferiti.get(i));
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
                    if (new_count < ristorantiPreferiti.size()) {
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
                        // controllo, se l'utente scrive solo cerca allora l'app chiede il nome del ristorante
                        if (nomeRistorante == null || nomeRistorante.isEmpty()) {
                            System.out.println("Inserisci il nome del ristorante che vuoi visualizzare: ");
                            nomeRistorante = sc.nextLine().trim();
                        }

                        if (Ristorante.checkRistoranti(nomeRistorante)) {
                            TheKnife.pulisci();
                            Ristorante.visualizzaRistorante(ristoranti, nomeRistorante);
                        } else {
                            System.out.println("Il ristorante non esiste.");
                        }

                        String scelta;
                        do {
                            System.out.println("\nESCI - Torna ai ristoranti filtrati");
                            scelta = sc.nextLine().trim().toLowerCase();
                            if (!scelta.equals("esci")) {
                                System.out.println("Scelta non valida.");
                            }
                        } while (!scelta.equals("esci"));

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