package theknife;

import java.io.*;
import java.util.*;

/**
 * Classe principale dell'applicazione TheKnife.
 * <p>
 * TheKnife è un'applicazione console che permette agli utenti di registrarsi,
 * effettuare il login, esplorare ristoranti filtrandoli per vari criteri
 * e gestire il proprio profilo, inclusi utenti guest e ristoratori.
 * </p>
 *
 * <p>
 * Requisiti: Java 23.0.1, codifica UTF-8 attiva nel terminale.
 * </p>
 * 
 * @author Mattia Rotteri, Davide Plesa, Davide Mantovan
 * @version 1.0
 */

public class TheKnife {

    public static Scanner sc = new Scanner(System.in);
    public static GestioneUtenti user;
    public static int raggio = 30;

    /**
     * Menu' di login e registrazione.
     * L'utente può scegliere se accedere come guest, fare login, registrarsi o
     * uscire dall'applicazione.
     *
     * @throws IOException
     * @throws RestaurantAlreadyExists
     * @throws RecensioneAlreadyExists
     */
    public static void menu_log() throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {
        Utility.pulisci();
        Utility.printLogo();
        System.out.println("Benvenuto,\n1 - Entra come Guest - Entra come utente non registrato.");
        System.out.println("2 - Login - Effettua il login.");
        System.out.println("3 - Registrati - Effettua la registrazione.");
        System.out.println("ESCI - Esci dall'applicazione.");

        String domicilio = "";
        String m;

        do {
            m = sc.nextLine();
            if (!(m.equals("1") || m.equals("2") || m.equals("3") || m.equalsIgnoreCase("esci"))) {
                System.out.println("Errore: Inserisci un'opzione valida.");
            }
        } while (!(m.equals("1") || m.equals("2") || m.equals("3") || m.equalsIgnoreCase("esci")));

        switch (m) {
            case "1":
                Utility.pulisci();Utility.printGuest();
                domicilio = Utility.validaInput("Inserisci il tuo domicilio: ", "Errore: Domicilio non esistente, riprova!", "domicilioEsistente");
                user = new Guest(domicilio);
                break;

            case "2":
                while (true) {
                    try {
                        Utility.pulisci();
                        Utility.printLogin();
                        String username = Utility.validaInput(
                                "Inserisci il tuo username: ",
                                "Errore: Lo username non può essere vuoto. Riprova!", "notEmpty");
                        String psw = Utility.validaInput(
                                "Inserisci la password: ", "Errore: La password non può essere vuota. Riprova!", "notEmpty");
                        user = Utente.login(username, psw);
                        break;
                    } catch (ErroreLogin e) {
                        Utility.pulisci();Utility.printError();
                        System.out.println(
                                "Errore: Login non riuscito! Vuoi: \n1 - Tornare al menu' \n2 - Ritentare il login");
                        String scelta = sc.nextLine();
                        if (scelta.equals("1")) {
                            menu_log();
                            break;
                        }
                    }
                }
                break;

            case "3":
                while (true) {
                    Utility.pulisci();
                    Utility.printReg();
                    System.out.println("TheKnife - Registrazione");

                    String username = Utility.validaInput(
                            "Inserisci il tuo username: ", "Errore: Username già esistente o non valido. Riprova!", "usernameUnico");
                    Utility.pulisci();Utility.printReg();
                    String psw = Utility.validaInput("Inserisci la tua password: ", "Errore: La password non può essere vuota. Riprova!", "notEmpty");
                    Utility.pulisci();Utility.printReg();
                    String nome = Utility.validaInput("Inserisci il tuo nome: ", "Errore: Il nome non può essere vuoto. Riprova!", "notEmpty");
                    Utility.pulisci();Utility.printReg();
                    String cognome = Utility.validaInput("Inserisci il tuo cognome: ", "Errore: Il cognome non può essere vuoto. Riprova!", "notEmpty");
                    Utility.pulisci();Utility.printReg();
                    domicilio = Utility.validaInput("Inserisci il tuo domicilio: ", "Errore: Domicilio non esistente. Riprova!", "domicilioEsistente");
                    Utility.pulisci();Utility.printReg();
                    String ruolo;
                    System.out.print("Inserisci il tuo ruolo (utente/ristoratore, default: utente): ");
                    ruolo = sc.nextLine().trim().toLowerCase();
                    if (!ruolo.equals("ristoratore")) {
                        ruolo = "utente";
                    }
                    try {
                        if (ruolo.equals("ristoratore")) {
                            user = Ristoratore.register(username, psw, nome, cognome, domicilio, "ristoratore");
                        } else {
                            user = Utente.register(username, psw, nome, cognome, domicilio, "utente");
                        }
                    } catch (UserAlreadyExists e) {
                        System.out.println("Errore: L'utente esiste già.");
                    }

                    Utility.pulisci();
                    break;
                }
                break;

            case "esci":
                System.exit(0);
        }

        main_menu();
    }

    /**
     * Serve per visualizzare il menu' principale.
     * L'utente può visualizzare ristoranti, modificare il raggio di ricerca,
     * vedere il proprio profilo e, se è un ristoratore, gestire i propri
     * ristoranti.
     *
     * @throws IOException
     * @throws RestaurantAlreadyExists
     * @throws RecensioneAlreadyExists
     */
    public static void main_menu() throws IOException, RestaurantAlreadyExists, RecensioneAlreadyExists {
        Utility.pulisci();Utility.printLogo();
        int opzioneProfilo = 0, opzioneRistorante = 0, opzioneRaggio = 0;
        boolean isProfiloAbilitato = false, isRistoranteAbilitato = false;
        int count = 1;

        System.out.println("Benvenuto, " + user.getUsername() + ". Cosa vuoi fare? ");

        System.out.println(count + " - Visualizza la lista di ristoranti (con filtri)");

        if (user.getRuolo().equals("utente") || user.getRuolo().equals("ristoratore")) {
            opzioneProfilo = ++count;
            isProfiloAbilitato = true;
            System.out.println(opzioneProfilo + " - Visualizza il tuo profilo.");
        }
        opzioneRaggio = ++count;
        System.out.println(opzioneRaggio + " - Imposta raggio di ricerca (Ora " + raggio + " km).");

        if (user.getRuolo().equals("ristoratore")) {
            opzioneRistorante = ++count;
            isRistoranteAbilitato = true;
            System.out.println(opzioneRistorante + " - Visualizza i tuoi ristoranti.");
        }
        System.out.println("ESCI - Chiudi l'applicazione.");

        int scelta = -1000;
        while (scelta < 1 || scelta > count) {
            try {

                String input = sc.nextLine();
                if (input.equalsIgnoreCase("esci")) {
                    System.exit(0);
                }
                scelta = Integer.parseInt(input);
                if (scelta < 1 || scelta > count) {
                    System.out.println("Scelta non valida, riprova.");
                }
            } catch (NumberFormatException e) {
                if ((Integer.toString(scelta).equalsIgnoreCase("esci"))) {
                    System.exit(0);
                }
                System.out.println("Errore: formato errato, perfavore inserisci un'opzione valida!");
            }
        }

        if (scelta == 1) {
            Utility.pulisci();Utility.printFiltri();
            ListaRistorante lista = geoTheKnife.cercaVicinanza(user.getDomicilio(), raggio);
            System.out.println(
                    "Inserisci i filtri desiderati per visualizzare la lista dei ristoranti:\nPer visualizzare TUTTI i ristoranti nelle vicinanze, premi INVIO.\nFORMAT FILTRI: (filtro1, filtro2, filtro3 ....)\nEsempio: 1, 2, 3\n1 - Tipologia di cucina.\n2 - Disponibilità del servizio di delivery.\n3 - Disponibilità del servizio di prenotazione online.\n4 - Fascia di prezzo.\n5 - Per media del numero di stelle.\n");
            String filtriUtente = sc.nextLine();
            String[] filtri = filtriUtente.split(",");
            lista = Utility.cercaFiltri(lista, filtri);
            Utility.stampaRicerca(lista, "filtri", user, "Lista dei ristoranti filtrati", true);
        }

        else if (isProfiloAbilitato && scelta == opzioneProfilo) {
            Utente.visualizzaProfilo((Utente) user);
        } else if (scelta == opzioneRaggio) {
            Utility.pulisci();Utility.printRaggio();
            System.out.println(
                    "Inserisci il raggio di ricerca dalla tua posizione (" + user.getDomicilio().toUpperCase() + ")");

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
            main_menu();

        } else if (isRistoranteAbilitato && scelta == opzioneRistorante) {
            Utility.stampaRicerca(Ristoratore.getRistorantiRistoratore(user.getUsername()), "ristoratore", user,
                    "Lista dei tuoi ristoranti", false);
        }
    }

    public static void main(String[] args) throws IOException, UserAlreadyExists, ErroreLogin, InterruptedException,
            RestaurantAlreadyExists, RecensioneAlreadyExists {
        menu_log();
    }
}