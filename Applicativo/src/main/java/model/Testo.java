package model;

import java.lang.reflect.AnnotatedArrayType;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Classe per conservare i testi che creano gli utenti
 */

public class Testo {
    /**
     * Il contenuto del testo
     */
    public String contenuto;

    /**
     * La data ultima modifica
     */
    public Date dataUltimaModifica;

    /**
     * L'ora ultima modifica
     */

    public Time oraUltimaModifica;

    /**
     * La pagina di riferimento del testo
     */
    public Pagina pagina;

    /**
     * le frasi del testo
     */

    public ArrayList<Frase> frasiTesto = new ArrayList<>();

    /**
     * I testi passati
     */
    ArrayList<CronologiaTesto> testiPassati = new ArrayList<>();

    /**
     * Le modifiche proposte a quel testo
     */
    ArrayList<ModificaTesto> testiModificati = new ArrayList<>();


    /**
     * Metodo che aggiunge frasi al testo
     * @param frase le frasi da aggiungere
     */
    public void aggiungiFrasi(ArrayList<Frase> frase)
    {
        for (Frase f: frase) {
            frasiTesto.add(f);
        }
    }

    /**
     * Costruttore che mi istanzia e crea un testo con delle frasi
     * @param frasi le frasi che voglio aggiungere al testo
     */

    public Testo(ArrayList<String> frasi)
    {
        Frase frase = new Frase();
        for (String s : frasi) {
            if (s.equals(".")) {
                frase.aggiungiParola(s);
                frasiTesto.add(frase);
                frase = new Frase();
            }
            else {
                frase.aggiungiParola(s);
            }
        }
        if (!frase.getParole().isEmpty()) {
            frasiTesto.add(frase);
        }

        Iterator<Frase> iterator = frasiTesto.iterator();
        while (iterator.hasNext()) {
            Frase f = iterator.next();
            if (f.isEmpty(f)) {
                iterator.remove();
            }
        }
    }

    /**
     * Costruttore che mi istanzia un testo vuoto
     */

    public Testo()
    {
            // inializza un testo vuoto
    }

    /**
     * Costruttore che mi istanzia un testo e setta la pagina
     * @param pagina la pagina
     */

    public Testo(Pagina pagina)
    {
        this.pagina = pagina;
    }

    /**
     * Metodo che mi ritorna le frasi del testo
     * @return un array list che contiene le frasi del testo
     */

    public ArrayList<Frase> getFrasiTesto() {
        return frasiTesto;
    }
}
