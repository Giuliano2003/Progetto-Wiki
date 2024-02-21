package model;

import java.util.ArrayList;

public class Frase {
    public ArrayList<String> parole = new ArrayList<>();

     public Pagina pagina_destinazione;//dalla parte della rel collegamento 0..1
            public ArrayList<Testo> testo_appartenente=new ArrayList<>();



             //se avessi fatto il costruttore di default mi avrebbe settato a null anche il vettore
    //e avrei dovuto fare un altra volta new perdendo quel blocco di memoria precedentemente creato
            public Frase(){
                pagina_destinazione=null;
            }//dopo cio puoi utilizzare i tuoi metodi con cui o fai una frase normale o un collegamento




            //metodo per creare il collegamento(ovvero una frase normale che è anche un collegaemento) quindi o crei la pagina di destinazione o te la prendi dai parametri
            //sempre in un testo, che dovra prendere in input(nei parametri)
    /*
            public void crea_collegamento() {
                pagina_destinazione=new Pagina(this);//vai ad aggiungere questo "collegamento" al vettore frasilinkate

                testo_appartenente.add(new Testo());//qui devo per forza crearmi un nuovo costruttore** ,//che mi istanzia un Testo che mi crea una nuova Pagina pk la pagina destinazione non è la pagina dove si trova la frase//
                // ,**di Testo senza parametro pk quello che ho attualmente io ovvero public Testo(Pagina p) non lo posso usare pk non ho nessun oggetto Pagina
*/

            //metodo per creare una frase normale
    /*
            public void crea_frasenormale(){
                testo_appartenente.add(new Testo(this));
            }
*/


    public Frase(String Parole)
    {
        parole.add(Parole);
    }

    public void aggiungiParola(String parola){
            parole.add(parola);
    }

    public void stampaFrase(Frase frase)
    {
        ArrayList<String>parole=frase.getParole();
        for (String s:
             parole) {
            System.out.print(" "+s);
        }
        System.out.println("Fine");
    }

    public ArrayList<String> getParole() {
        return parole;
    }

    public boolean isEmpty() {
        return parole.isEmpty();
    }

    public boolean isEmpty(Frase frase)
    {
        ArrayList<String> parole=frase.getParole();
        if(parole.isEmpty()){
            return true;
        }
        return false;
    }

    ArrayList<Testo> testoAppartenenza = new ArrayList<>();

    public Pagina paginaDestinazione;
}
