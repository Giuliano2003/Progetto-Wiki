package model;

import java.util.ArrayList;

public class Autore extends Utente{
        public int H_index;

        public Autore(String nome,String password,String email)
        {
                super(nome,password, email);
        }

        public Autore(String nome)
        {
                super(nome);
        }

        @Override
        public String getUsername() {
                return super.getUsername();
        }

        ArrayList<Pagina> pagineCreate = new ArrayList<>();


}
