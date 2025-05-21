package theknife;

import java.io.*;
import java.util.*;

public class TheKnife {

    public static Scanner sc = new Scanner(System.in);
    public static GestioneUtenti user;
    public static int raggio = 30;

    // loghetto bello
    public static void printLogo() {
        System.out.println("""
                 ______  __ __    ___      __  _  ____   ____  _____  ___
                |      ||  |  |  /  _]    |  |/ ]|    \\ |    ||     |/  _]
                |      ||  |  | /  [_     |  ' / |  _  | |  | |   __/  [_
                |_|  |_||  _  ||    _]    |    \\ |  |  | |  | |  |_|    _]
                  |  |  |  |  ||   [_     |     ||  |  | |  | |   _]   [_
                  |  |  |  |  ||     |    |  .  ||  |  | |  | |  | |     |
                  |__|  |__|__||_____|    |__|\\_||__|__||____||__| |_____|
                """);
    }

    // pulisce il terminale
    public static void pulisci() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // menu registrazione, login, guest
    public static void menu_log() throws IOException {
        pulisci();printLogo();
        System.out.println("\nBenvenuto,\n1 - Entra come Guest");
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
                while (true) {
                    System.out.println("Inserisci il tuo domicilio: ");
                    domicilio = sc.nextLine();
                    if (geoTheKnife.domicilioEsistente(domicilio)) {
                        break;
                    } else {
                        pulisci();
                        System.out.println("Errore. Domicilio non esistente.");
                    }
                }
                user = new Guest(domicilio);
                pulisci();
                System.out.println("Benvenuto Guest, ti trovi a " + domicilio);
                break;
            case "2":
                while (true) {
                    try {
                        pulisci();
                        printLogo();
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

                    pulisci();printLogo();
                    System.out.println("TheKnife - Registrazione");

                    while (true) {
                        System.out.print("Inserisci il tuo username: ");
                        username = sc.nextLine();
                        if (Utente.checkUser(username)) {
                            System.out.println("Errore. Username già esistente. Riprova.");
                        } else {
                            break;
                        }
                    }
                    pulisci();
                    System.out.print("Inserisci la tua password: ");
                    String psw = sc.nextLine().trim();
                    pulisci();
                    System.out.print("Inserisci il tuo nome: ");
                    String nome = sc.nextLine();
                    pulisci();
                    System.out.print("Inserisci il tuo cognome: ");
                    String cognome = sc.nextLine();
                    pulisci();
                    while (true) {
                        System.out.println("Inserisci il tuo domicilio: ");
                        domicilio = sc.nextLine();
                        if (geoTheKnife.domicilioEsistente(domicilio)) {
                            break;
                        } else {
                            pulisci();
                            System.out.println("Errore. Domicilio non esistente. Inserisci nuovamente il tuo domicilio.");
                        }
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

                    } catch (UserAlreadyExists e) {
                        System.out.println("Errore. L'utente esiste gia'.");
                    }
                    pulisci();
                    //System.out.println("Registrazione ok, " + user.getUsername() + " sei un " + user.getRuolo());
                    break;
                }
        }
                main_menu();        
    }

    public static void main_menu() throws IOException {
        pulisci();
        printLogo();
        //menu utente
        if (user.getRuolo().equals("utente")) {
            String sceltaMenu = "";
            System.out.println("Benvenuto, " + user.getUsername() + ". Cosa vuoi fare? ");
            System.out.println(
                    "1 - Cerca un ristorante (con filtri)\n2 - Visualizza il tuo profilo.\n3 - Imposta raggio di ricerca (Ora " + raggio + " km).");
            do {
                sceltaMenu = sc.nextLine();
                if (!((sceltaMenu.equals("1")) || (sceltaMenu.equals("2")) || (sceltaMenu.equalsIgnoreCase("3")))) {
                    System.out.println("Input non valido. Inserisci nuovamente.");
                }
            } while (!((sceltaMenu.equals("1")) || (sceltaMenu.equals("2")) || (sceltaMenu.equalsIgnoreCase("3"))));

            switch (sceltaMenu) {
                case "1":
                //manca check format
                    pulisci();printLogo();
                        Ristorante lista = GestioneUtenti.cercaVicinanza(user.getDomicilio(), raggio);
                        System.out.println( "Inserisci i filtri desiderati per visualizzare la lista dei ristoranti:\nFORMAT: (filtro1, filtro2, filtro3 ....)\nEsempio: 1, 2, 3\n1 - Tipologia di cucina.\n2 - Disponibilità del servizio di delivery.\n3 - Disponibilità del servizio di prenotazione online.\n4 - Fascia di prezzo.\n5 - Per media del numero di stelle.\n");
                        String filtriUtente = sc.nextLine();
                        String[] filtri = filtriUtente.split(",");
                        lista = GestioneUtenti.cercaFiltri(lista, filtri);
                        GestioneUtenti.stampaRicerca(lista);
                    break;
                case "2":
                    Utente.visualizzaProfilo((Utente)user);
                    break;
                case "3":
                    pulisci();printLogo();                
                    System.out.println("Inserisci il raggio di ricerca dalla tua posizione (" + user.getDomicilio().toUpperCase() + ")");

                    do {
                        try {
                            raggio = Integer.parseInt(sc.nextLine());
                            if (raggio <= 0) {
                                System.out.println("Errore. Raggio non valido, inserisci un nuovo valore: ");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore. Inserisci un numero valido: ");
                        }
                    } while (raggio <= 0);

                    System.out.println("Il raggio di ricerca è stato correttamente impostato a " + raggio);
                    main_menu();;

            }
        }

        else if (user.getRuolo().equals("ristoratore")) {
        }

        else {

        }        
    }

    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin, InterruptedException, RestaurantAlreadyExists {
        menu_log();
        //cerca();
        
        /*
        String[] filtri = {"1", "2"};
        Ristorante lista = GestioneUtenti.cercaFiltri(filtri);
        //GestioneUtenti.stampaRicerca(lista);
        
        System.out.println("Che ristorante vuoi visualizzare?????");
        String nomeRistorante = sc.nextLine(); 
        GestioneUtenti.visualizzaRistorante(lista, nomeRistorante);
        */
    }
}