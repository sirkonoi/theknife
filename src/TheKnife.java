import java.io.*;
import theknife.*;
import java.util.*;

public class TheKnife {

    public static Scanner sc = new Scanner(System.in);
    public static GestioneUtenti user;

    //pulisce il terminale
    public static void pulisci() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //menu registrazione, login, guest
    public static void menu_log() throws IOException, InterruptedException {
        System.out.println("Menu':");
        System.out.println("1 - Entra come Guest");
        System.out.println("2 - Login");
        System.out.println("3 - Registrati");

        String domicilio = "";
        String m;

        do {
            m = sc.nextLine();
            if (!(m.equals("1") || m.equals("2") || m.equals("3"))) {
                System.out.println("Errore. Inserisci un'opzione valida.");
            }
        } while (!(m.equals("1") || m.equals("2") || m.equals("3")));

        switch (m) {
            case "1":
                pulisci();
                while(true) {
                    System.out.println("Inserisci il tuo domicilio: ");
                    domicilio = sc.nextLine();
                    if (geoTheKnife.domicilioEsistente(domicilio)) {break;}
                    else {pulisci(); System.out.println("Errore. Domicilio non esistente.");}
                }
                user = new Guest(domicilio);pulisci();
                System.out.println("Benvenuto Guest, ti trovi a " + domicilio);
                break;
            case "2":
                while (true) {
                    try {
                        pulisci();
                        System.out.print("Login - The Knife\nInserisci il tuo username: ");
                        String username = sc.nextLine();
                        System.out.print("Inserisci la password: ");
                        String psw = sc.nextLine();

                        user = Utente.login(username, psw);
                        break; // login riuscito allora esce dal ciclo
                    } catch (ErroreLogin e) {
                        pulisci();
                        System.out.println(
                                "Errore. Login non riuscito! Vuoi: \n1 - Tornare al menu' \n2 - Ritentare il login");
                        String scelta = sc.nextLine();
                        if (scelta.equals("1")) {
                            menu_log(); // torna al menu
                            break;
                        }
                        // se 2, viene ripetuto il ciclo
                    }
                }
                break;
            case "3":
                while (true) {
                    String username = "";

                    pulisci();
                    System.out.println("TheKnife - Registrazione");
                    
                    while (true) {
                        System.out.print("Inserisci il tuo username: ");
                        username = sc.nextLine();
                    
                        if (Utente.checkUser(username)) {
                            System.out.println("Errore. Username già esistente. Riprova.");
                        } else {
                            break; // se l'username non esiste allora esce dal ciclo
                        }
                    }
                        pulisci();
                        System.out.print("Inserisci la tua password: ");
                        String psw = sc.nextLine();
                        pulisci();
                        System.out.print("Inserisci il tuo nome: ");
                        String nome = sc.nextLine();                        
                        pulisci();
                        System.out.print("Inserisci il tuo cognome: ");
                        String cognome = sc.nextLine();
                        pulisci();
                        while(true) {
                            System.out.println("Inserisci il tuo domicilio: ");
                            domicilio = sc.nextLine();
                            if (geoTheKnife.domicilioEsistente(domicilio)) {break;}
                            else {pulisci(); System.out.println("Errore. Domicilio non esistente.");}
                        }
                        pulisci();
                        System.out.print("Inserisci il tuo ruolo (utente/ristoratore, default: utente): ");
                        String ruolo = sc.nextLine();
                        pulisci();
                    try {                        
                        if (ruolo.equals("ristoratore")) {
                            user = Ristoratore.register(username, psw, nome, cognome, domicilio, "ristoratore");
                        } else {
                            user = Utente.register(username, psw, nome, cognome, domicilio, "utente");                          
                        }

                    } catch(UserAlreadyExists e) {
                        System.out.println("Errore. L'utente esiste gia'.");
                    }
                    pulisci();
                    System.out.println("Registrazione ok, " + user.getUsername() + " sei un " + user.getRuolo());                    
                    break;
                }
            }

            System.out.println("Benvenuto " + user.getUsername() + " sei un " + user.getRuolo() + " il tuo domicilio e' " + user.getDomicilio() + ", e' valido?? " + geoTheKnife.domicilioEsistente(domicilio));
        }

    public static void main_menu() {

    }   

    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin, InterruptedException {
        System.out.println("Benvenuto in TheKnife.");
        /*
         * System.out.println(Utente.checkUser("konoi"));
         * Utente.register("konod", "Ciao", "mattia", "rotteri", "Via Davide Plesa",
         * "utente");
         * Utente u = Utente.login("konoi", "ciao1234");
         * System.out.println(u.getUsername());
         * System.out.println(Utente.checkRuolo("konoi"));
         */
        //System.out.println(geoTheKnife.getLatitudineLongitudine("Via Cremona 15 Busto Arsizio"));
        menu_log();
        //Ristorante.scriviRistorante("konoi", "Via Cremona", "ahsbas", "€€€", "Italiana", "010291301", 0, "null", "null");
        //user = new Ristoratore("plesa", "null", "null", "null", "null");
        //Ristoratore.aggiungiRistorante("wiz", "Via Cremona", "ahsbas", "€€€", "Italiana", "010291301", 0, "null", "null");
    }
}