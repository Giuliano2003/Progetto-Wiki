package model;

import java.util.ArrayList;

public class Frase {
    /**
     * Le parole della frase
     */
    public ArrayList<String> parole = new ArrayList<>();

    /**
     * La pagina di destinazione del collegamento
     */

    public Pagina paginaDestinazione;//dalla parte della rel collegamento 0..1

    /**
     * Il testo di appartenenza della frase
     */

    public ArrayList<Testo> testoAppartenenza = new ArrayList<>();

    /**
     * Il costruttore di frase
     */

    public Frase(){
        paginaDestinazione =null;
    }

    /**
     * Il costruttore di frase che aggiunge una parola
     * @param Parola la parola da aggiungere
     */


    public Frase(String Parola)
    {
        parole.add(Parola);
    }

    /**
     * Il metodo aggiungi parola
     * @param parola la parola da aggiungere
     */

    public void aggiungiParola(String parola){
            parole.add(parola);
    }

    /**
     * Il metodo stampafrase
     * @param frase la frase da stampare
     */

    public void stampaFrase(Frase frase)
    {
        ArrayList<String>parole=frase.getParole();
        for (String s:
             parole) {
            System.out.print(" "+s);
        }
    }

    /**
     * Ritorna le parole della frase
     * @return un array list che contiene  le parole della frase
     */
    public ArrayList<String> getParole() {
        return parole;
    }

    /**
     * Un metodo che restituisce true se la frase è vuota,false altrimenti
     * @return a boolean che indica il risultato del confronto
     */

    public boolean isEmpty() {
        return parole.isEmpty();
    }

    /**
     * un metodo che mi indica se la frase è vuota oppure no
     * @param frase la frase
     * @return un boolean che restituisce true se la frase è vuota,false altrimenti
     */

    public boolean isEmpty(Frase frase)
    {
        ArrayList<String> parole=frase.getParole();
        if(parole.isEmpty()){
            return true;
        }
        return false;
    }


}
