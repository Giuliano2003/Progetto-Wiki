package gui;
import controller.Controller;
import model.Pagina;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * La classe Storia pagine che permette all'utente di visualizzare una tabella e scegliere quale storia delle pagine visionare.
 */
public class StoriaPagine {

    /**
     * il frame della pagina attuale
     */
    private JFrame frame;
    /**
     * la tabella dove andrò a scrivere le pagine
     */

    private DefaultTableModel model;

    /**
     * Il controller che serve a recuperare le pagine dal database.
     */
    public Controller controller = new Controller();

    /**
     * Instanzia a new Storia pagine.
     *
     * @param framechiamante Il framechiamante che è la home
     * @param controller     Il controller
     */
    public StoriaPagine(JFrame framechiamante, Controller controller) {
        frame = new JFrame("Visualizza Le Storie Delle Pagine");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                framechiamante.setVisible(true);
                framechiamante.setEnabled(true);
            }
        });
        this.controller=controller;
        frame.setLocationRelativeTo(null);
        JLabel titleLabel = new JLabel("Visualizza Le Storie Delle Pagine");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Titolo", "Data Creazione", "Ora Creazione", "Autore"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<Date> date = new ArrayList<>();
        ArrayList<Time> ore = new ArrayList<>();
        ArrayList<String> autori = new ArrayList<>(); // serve per dopo
        //funzione che mi inizializza quest'array con la storia delle pagine che l'autore ha creato
        //ma dato che l'utente da traccia può anche vedere la storia dei testi per quali ha proposto una modifica
        //allora serve un altro metodo per settare gli array dei testi in cui l'autore ha proposto una modifica
        initArrayPerTabella(titoli,date,ore);
        int size = Math.min(Math.min(titoli.size(), date.size()),ore.size());
        for (int i = 0; i < size; i++) {
            Object[] row = {titoli.get(i), date.get(i), ore.get(i), "L'autore Della Pagina sei tu"};
            model.addRow(row);
        }
        //quindi adesso dopo averli aggiunti rimetto vuoti gli array
        titoli = new ArrayList<>();
        date= new ArrayList<>();
        ore = new ArrayList<>();
        autori = new ArrayList<>();
        //e mi inizalizzo gli array per i testi in cui l'utente ha proposto una modifica
        initArrayPerTestiModificati(titoli,date,ore,autori);
        size = Math.min(Math.min(titoli.size(), date.size()), Math.min(ore.size(), autori.size()));
        for (int i = 0; i < size; i++) {
                Object[] row = {titoli.get(i), date.get(i), ore.get(i),autori.get(i)};
                if (!isRowAlreadyAdded(model, row)) {
                    model.addRow(row);
                }
        }
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    String titolo = (String) model.getValueAt(row, 0);
                    // Aprire la pagina di destinazione
                    Pagina pagina = new Pagina(titolo);
                    controller.setPagina(pagina);
                    VisualizzaStoria visualizzaStoria = null;
                    try {
                        visualizzaStoria = new VisualizzaStoria(frame,controller);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Errore !");
                    }
                    visualizzaStoria.mostraFinestra();
                }
            }
        });
    }

    /**
     * Riempe gli array list in ingresso dei dati delle pagine che deve visualizzare l'utente
     * cioè o le pagine create o quelle dove ha proposto una modifica
     *
     * @param titoli I titoli delle pagine
     * @param date   Le date di creazione delle pagine
     * @param ore    le ore di creazione delle pagine
     */
    public void initArrayPerTabella(ArrayList<String> titoli,ArrayList<Date> date,ArrayList<Time> ore)
    {
        try{
            controller.addPagineTabella(titoli,date,ore);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Riempe gli array list in ingresso dei dati delle pagine che deve visualizzare l'utente
     * cioè o le pagine create o quelle dove ha proposto una modifica.In questo metodo
     * riempiremo gli array con i dati delle pagine modificate e quindi per completezza
     * scriveremo anche l'autore di quella pagina.
     * @param titoli I titoli delle pagine
     * @param date   Le date delle pagine
     * @param ore    Le ore delle pagine
     * @param autori Gli autori delle pagine
     */
    public void initArrayPerTestiModificati(ArrayList<String> titoli,ArrayList<Date> date,ArrayList<Time> ore, ArrayList<String> autori)
    {
        try{
            controller.addPagineModificateTabella(titoli,date,ore,autori);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
     * @param framechiamante the framechiamante
     */
    public void nascondiFinestra(JFrame framechiamante)
    {
        frame.setVisible(false);
        framechiamante.setEnabled(true);
    }
    //verifico se la riga già è preseente nella tabella

    /**
     * Metodo per verificare se la riga è già presente
     * @param model la tabella
     * @param row la riga
     * @return
     */
    private boolean isRowAlreadyAdded(DefaultTableModel model, Object[] row) {
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isEqual = true;
            for (int j = 0; j < model.getColumnCount(); j++) {
                if (!model.getValueAt(i, j).equals(row[j])) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                return true;
            }
        }
        return false;
    }
}


