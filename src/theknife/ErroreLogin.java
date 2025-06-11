package theknife;

/**
 * Eccezione personalizzata legata ad errori durante il login.
 *
 * <p>Viene utilizzata per segnalare credenziali errate inserite durante il login dall'utente.</p>
 */

public class ErroreLogin extends Exception {

    /**
     * Costruttore dell'eccezione {@code ErroreLogin}.
     *
     * @param error Messaggio di errore.
     */    
    public ErroreLogin(String error) {
        super(error);
    }
}