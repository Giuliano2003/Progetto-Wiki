package model;

import java.util.ArrayList;

public class Pagina {

    public String titolo;

    public Autore autorePagina;

    public Testo testo;

    public String tema;

    public ArrayList<Frase> frasiLinkate = new ArrayList<>();

    public Pagina(String titolo,Testo testo,String nomeAutore)
    {
        this.titolo=titolo;
        this.testo=testo;
        Autore autore = new Autore(nomeAutore);
        this.autorePagina=autore;
    }

    public Pagina()
    {

    }
    public Pagina(Testo testo)
    {
        this.testo=testo;
    }


    public Pagina(String titolo)
    {
        this.titolo=titolo;
    }
    public Pagina(String titolo,Testo testo)
    {
        this.titolo=titolo;
        this.testo=testo;
    }

    public void addFrasiLinkate(String parola)
    {
        Frase frase = new Frase(parola);
        frasiLinkate.add(frase);
    }

    public Testo getTesto() {
        return testo;
    }

    public ArrayList<Frase> getFrasiLinkate()
    {
        return frasiLinkate;
    }
}
