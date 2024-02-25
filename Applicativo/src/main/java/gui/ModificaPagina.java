package gui;

import controller.Controller;
import model.Frase;
import model.Testo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import static gui.VisualizzaNotifiche.isEqual;

/**
 *  Classe che permette di attuare modifiche alle pagine.
 */
public class ModificaPagina {
    /**
     * frame attuale della pagina
     */
    private JFrame frame;

    /**
     * JtextPane dove scriverò il testo
     */

    private JTextPane textPane;

    /**
     * Il controller che mi permette di inserire la modifica nel DB
     */
    public Controller controller = new Controller();

    /**
     * Le Frasi del testo nuovo.
     */
    ArrayList<Frase> frasiTestoNuovo = new ArrayList<>();

    /**
     * La Hash map che mantiene i collegamenti fra le parole e le pagine di destinazione.
     */
    public HashMap<String,String> hashMap = new HashMap<>();

    /**
     * Instantiates a new Modifica pagina.
     *
     * @param frameChiamante Il frame chiamante
     * @param controller     Il controller
     * @throws SQLException the sql exception
     */
    public ModificaPagina(JFrame frameChiamante, Controller controller) throws SQLException {
        this.controller=controller;
        frame = new JFrame("Modifica il tuo testo");
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(frameChiamante);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                frameChiamante.setEnabled(true);
                frameChiamante.setVisible(true);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.setLayout(new BorderLayout());

        // Etichetta in alto a sinistra
        JLabel titleLabel = new JLabel("Modifica il tuo testo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Creazione del TextPane per inserire il testo
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);

        // Creazione dei bottoni
        JButton salvaButton = new JButton("Salva");
        JButton annullaButton = new JButton("Indietro");

        // Pannello per i bottoni
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);

        // Aggiunta dei componenti al layout della finestra
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        aggiuntaTesto();

        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //quando l'utente clicca su salva noi andiamo a inserire una riga nella tabella Modifiche_testo
                String contenuto = textPane.getText();
                String [] paroleContenuto = contenuto.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
                ArrayList<String> frasiCliccabiliAppoggio = new ArrayList<>();
                try {
                    for (String s: controller.getFrasiLinkate(controller.getTitolo())) {
                        if(s.contains(" ") || s.contains("-") || s.contains("'")){
                            String [] parole = s.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
                            for (String s1: parole) {
                                frasiCliccabiliAppoggio.add(s1);
                            }
                        }
                        else
                        {
                                frasiCliccabiliAppoggio.add(s);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il salvataggio");
                }
                riempiFrasiContenuto(frasiCliccabiliAppoggio,frasiTestoNuovo,paroleContenuto);
                String autore = controller.getNomeUtente();
                String titolo = controller.getTitolo();
                try {
                    controller.addModifichetesto(contenuto,autore,titolo);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore nel salvataggio del testo");
                }
                try {
                    // logica per implementare se la modifica è scritta dall'autore del testo
                    if(autore.equals(controller.getAutorePagina(titolo))){
                        // setto la hash map
                        for (String s: controller.getFrasiLinkate(controller.getTitolo())) {
                            hashMap.put(s,controller.getPaginaDestinazione(controller.getTitolo(),s));
                        }
                        Date dataCreazione = controller.getDataCreazione(contenuto, titolo);
                        Timestamp oraCreazione = controller.getOraCreazione(contenuto, titolo);
                        ArrayList<String> frasiLinkateTesto = (ArrayList<String>) controller.getFrasiLinkate(controller.getTitolo());
                        controller.setEsito(contenuto,dataCreazione,oraCreazione);
                        controller.addFrasi(frasiTestoNuovo,frasiLinkateTesto,hashMap);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore nella modifica");
                }
                salvaButton.setEnabled(false);
            }
        });

        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frameChiamante.setEnabled(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }

    /**
     * Mostra finestra.
     */
    public void mostraFinestra()
    {
        frame.setVisible(true);
    }

    /**
     * Nascondi finestra.
     *
     * @param framechiamante Il framechiamante
     */
    public void nascondiFinestra(JFrame framechiamante)
    {
        frame.setVisible(false);
        framechiamante.setEnabled(true);
    }

    /**
     * Metodo che aggiunge il testo precedente al nuovo panel.
     *
     */
    public void aggiuntaTesto() {
        StringBuilder risultato = new StringBuilder();
        Testo testo = controller.getPagina().getTesto();
        if (testo != null) {
            ArrayList<Frase> frasitesto = testo.getFrasiTesto();
            String testoDaModificare = null;
            for (Frase f : frasitesto) {
                f.stampaFrase(f);
                ArrayList<String> parolefrase = f.getParole();
                testoDaModificare = String.join(" ", parolefrase);
                risultato.append(testoDaModificare).append(" ");
            }
            textPane.setText(String.valueOf(risultato));
        }
    }

    /**
     * Metodo che riempe le frasi in ingresso.
     *
     * @param paroleLinkate the parole linkate
     * @param frasi         the frasi
     * @param paroleTesto   the parole testo
     */
    public void riempiFrasiContenuto(ArrayList<String> paroleLinkate,ArrayList<Frase> frasi,String [] paroleTesto)
    {
        Frase frase = new Frase(); // inizio con una frase vuota
        for (int i = 0; i < paroleTesto.length; i++) {
            if (paroleLinkate.contains(paroleTesto[i])) {
                frasi.add(frase);
                frase = new Frase();
                frase.aggiungiParola(paroleTesto[i]);
                // Aggiungi tutte le parole successive fino al prossimo elemento frasiSottolineate
                while (i + 1 < paroleTesto.length && paroleLinkate.contains(paroleTesto[i+1])) {
                    frase.aggiungiParola(paroleTesto[i + 1]);
                    i++;
                }
                frasi.add(frase);
                frase = new Frase();
            } else if (!isEqual(paroleTesto[i], paroleLinkate)) {
                frase.aggiungiParola(paroleTesto[i]);
                // Se la parola contiene un punto, termina la frase corrente
                if (paroleTesto[i].contains(".")) {
                    frasi.add(frase);
                    frase = new Frase();
                }
            }
        }
        if (!frase.getParole().isEmpty()) {
            frasi.add(frase);
        }

        Iterator<Frase> iterator = frasi.iterator();
        while (iterator.hasNext()) {
            Frase f = iterator.next();
            if (f.isEmpty(f)) {
                iterator.remove();
            }
        }
        //questo lo aggiungo perchè alla fine di tutto mi creerà degli spazi vuoti e io li levo

    }




}
