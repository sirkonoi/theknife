import java.io.*;
import theknife.*;
import java.util.*;

public class TheKnife {

    public static Scanner sc = new Scanner(System.in);
    public static Utente user;

    public static void pulisci() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }    
    public static void menu() throws IOException {
        System.out.println("Menu':");
        System.out.println("1 - Entra come Guest");        
        System.out.println("2 - Login");
        System.out.println("3 - Registrati");  
        
        String m;
        do {
            m = sc.nextLine();
            if (!(m.equals("1") || m.equals("2") || m.equals("3"))) {
                System.out.println("Errore. Inserisci un'opzione valida.");
            }
        } while (!(m.equals("1") || m.equals("2") || m.equals("3")));
        
        switch(m) {
            case "1":
            pulisci();
            Guest u = new Guest();
            System.out.println("Benvenuto Guest");
            //chiama app();
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
                    System.out.println("Errore. Login non riuscito! Vuoi: \n1 - Tornare al menu' \n2 - Ritentare il login");
                    String scelta = sc.nextLine();
                    if (scelta.equals("1")) {
                        menu(); // torna al menu
                        break;
                    }
                    // se 2, viene ripetuto il ciclo
                }
            }
            pulisci();
            System.out.println("loggato.. as " + user.getUsername());
            break;        
            //app();
            case "3":
            try {
            System.out.println("TheKnife - Registrazione\nInserisci il tuo username:");
            String username = sc.nextLine();
            pulisci();
            System.out.print("Inserisci la tua password:");
            String psw = sc.nextLine();            
            pulisci();
            System.out.print("Inserisci il tuo nome:");
            pulisci();
            String nome = sc.nextLine();              
            System.out.print("Inserisci il tuo cognome:");
            String cognome = sc.nextLine();            
            pulisci();
            System.out.print("Inserisci il tuo domicilio:");
            String domicilio = sc.nextLine();        
            pulisci();      
            System.out.print("Inserisci il tuo ruolo:");
            String ruolo = sc.nextLine();
            pulisci();
            if(ruolo.equals("utente")) {
                user = Utente.register(username, psw, nome, cognome, domicilio, "utente");
            }
            else{
                user = Ristoratore.register(username, psw, nome, cognome, domicilio, "ristoratore");
            }

            System.out.println(user.getRuolo());

            } catch(UserAlreadyExists e) {
                System.out.println("L'utente esiste gia'");
            }
            break;

        }
    }    
    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin {
        System.out.println("Benvenuto in TheKnife.");

        /*System.out.println(Utente.checkUser("konoi"));
        Utente.register("konod", "Ciao", "mattia", "rotteri", "Via Davide Plesa", "utente");
        Utente u = Utente.login("konoi", "ciao1234");
        System.out.println(u.getUsername());
        System.out.println(Utente.checkRuolo("konoi"));*/
        //menu();
        //Ristorante.scriviRistorante("konoi", "Via Cremona", "ahsbas", "€€€", "Italiana", "010291301", 0, "null", "null");
        user = new Ristoratore("plesa", "null", "null", "null", "null");
        Ristoratore.aggiungiRistorante("wiz", "Via Cremona", "ahsbas", "€€€", "Italiana", "010291301", 0, "null", "null");
    }
}