package theknife;

/**
 * Eccezione personalizzata che viene lanciata  quando si tenta di registrare un utente
 * con un username già esistente.
 */

public class UserAlreadyExists extends Exception {
    /**
     * Costruttore dell'eccezione {@code UserAlreadyExists}.
     *
     * @param error Messaggio di errore.
     */        
    public UserAlreadyExists(String error) {
        super(error);
    }
}
