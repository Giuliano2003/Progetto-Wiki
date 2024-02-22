package model;
import java.sql.Time;
import java.util.Date;
public class ModificaTesto {
    public String testoRichiestoUtente;

    public boolean esito;

    public Date dataCreazione;

    public Time oraCreazione;

    public Utente utente;

    public Testo testoRiferimento;

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getUtente() {
        return utente;
    }

    public ModificaTesto()
    {

    }
}
