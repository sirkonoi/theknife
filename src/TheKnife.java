import java.io.*;
import theknife.*;
import java.util.*;

public class TheKnife {

    Scanner sc = new Scanner(System.in);

    public void menu() {
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
            //app();
            Guest u = new Guest();
            break;            
            case "2":
            //domande bla bla
            //Utente.login(username, psw);
            //app();
            break;
            case "3":
            //chiedi user, pass
            //Utente.register(username, psw, nome, cognome, domicilio, ruolo);
            //app();
            break;

        }
    }    
    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin {
        System.out.println("Benvenuto in TheKnife.");

        System.out.println(Utente.checkUser("konoi"));
        Utente.register("konod", "Ciao", "mattia", "rotteri", "Via Davide Plesa", "utente");
        Utente u = Utente.login("konoi", "ciao1234");
        System.out.println(u.getUsername());
        System.out.println(Utente.checkRuolo("konoi"));
    }
}