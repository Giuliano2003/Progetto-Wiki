package model;

import java.util.ArrayList;

/**
 * Classe per conservare gli autori delle pagine
 */

public class Autore extends Utente{
        /**
         * L'H index dell'autore
         */
        public int H_index;

        public ArrayList<Pagina> pagineCreate = new ArrayList<>();

        /**
         * Il costruttore dell'autore
         * @param nome il nome dell'autore
         * @param password la password dell'autore
         * @param email l'email dell'autore
         */

        public Autore(String nome,String password,String email)
        {
                super(nome,password, email);
        }

        /**
         * Costruttore vuoto di autore
         */
        public Autore()
        {
                super();
                //costruttore vuoto di autore
        }

        /**
         * Il costruttore dell'autore che prende come parametro solo il nome
         * @param nome il nome
         */

        public Autore(String nome)
        {
                super(nome);
        }

        /**
         * Metodo che restituisce l'username dell'autore
         * @return una stringa che contiene l'username dell'autore
         */

        @Override
        public String getUsername() {
                return super.getUsername();
        }

        /**
         * le pagine create dall'autore
         */

}
