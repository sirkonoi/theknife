import java.io.IOException;

import theknife.*;

public class TheKnife {
    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin {
        System.out.println(Utente.checkUser("konoi"));
        //Utente.register("konoe", "Ciao", "mattia", "rotteri", "Via Davide Plesa", "utente");
        Utente u = Utente.login("konoi", "ciao1234");
        System.out.println(u.getUsername());
    }
}