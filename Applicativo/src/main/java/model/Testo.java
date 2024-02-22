package model;

import java.lang.reflect.AnnotatedArrayType;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Testo {
    public String contenuto;

    public Date dataUltimaModifica;

    public Time oraUltimaModifica;

    public Pagina pagina;

    public ArrayList<Frase> frasiTesto = new ArrayList<>();

    ArrayList<CronologiaTesto> testiPassati = new ArrayList<>();

    ArrayList<ModificaTesto> testiModificati = new ArrayList<>();



    public void aggiungiFrasi(ArrayList<Frase> frase)
    {
        for (Frase f: frase) {
            frasiTesto.add(f);
        }
    }

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

    public Testo()
    {
            // inializza un testo vuoto
    }

    public Testo(Pagina pagina)
    {
        this.pagina = pagina;
    }

    public ArrayList<Frase> getFrasiTesto() {
        return frasiTesto;
    }
}
