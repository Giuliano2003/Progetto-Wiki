package model;

import java.util.ArrayList;

public class Utente {
    public String username;

    public String email;

    private String password;

    public int numeroModifiche;

    public Utente(String nomeUtente, String password, String email)
    {
        this.username=nomeUtente;
        this.password=password;
        this.email=email;
    }

    public Utente(String nome)
    {
        this.username=nome;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    ArrayList<String> modificheTesto = new ArrayList<>();

    public String getNomeUtente() {
        return username;
    }
}
