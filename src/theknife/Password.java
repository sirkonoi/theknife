package theknife;

import org.mindrot.jbcrypt.BCrypt;

public class Password {
    private final String hash;

    public Password(String plain) {
        this.hash = BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public String getHash() {
        return hash;
    }

    public boolean verifica(String plain) {
        return BCrypt.checkpw(plain, this.hash);
    }
}
