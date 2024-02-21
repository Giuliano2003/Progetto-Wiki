package model;

import java.sql.Time;
import java.util.Date;

public class CronologiaTesto {
    public String contenuto;

    public Date dataModifica;

    public Time oraModifica;

    public Testo testoRiferimento;

    public CronologiaTesto(Testo testo,Pagina pagina)
    {
        this.testoRiferimento=testo;
        this.testoRiferimento.pagina=pagina;
    }

    public CronologiaTesto()
    {

    }
}
