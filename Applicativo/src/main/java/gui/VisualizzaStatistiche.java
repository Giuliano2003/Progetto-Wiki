package gui;
import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * La classe Visualizza statistiche che permette di visualizzare le stastistiche dell'utente.
 */
public class VisualizzaStatistiche {
    /**
     * Il frame correte
     */
    private JFrame frame;

    /**
     * L'editor pane dove verrano scritti i testi
     */
    public JEditorPane editorPane;

    /**
     * il controller che in questo caso permette di recuperare i dati dell'utente
     */
    private Controller controller;

    /**
     * la tabella
     */
    private DefaultTableModel model;

    /**
     * Un intero che esprime la taglia degli array.
     */
    int size;

    /**
     * Una costante che indica il font che useremo .
     */
    String fontText = "<p style='font-size: 14px; color: #333; font-family: Arial, sans-serif;'>";

    /**
     * Instanzia a new Visualizza statistiche.
     *
     * @param frameChiamante Il frame chiamante
     * @param controller     Il controller
     * @throws SQLException the sql exception
     */
    public VisualizzaStatistiche(JFrame frameChiamante, Controller controller) throws SQLException {
        frame = new JFrame("Visualizza Le Statistiche Di : "+controller.getNomeUtente());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        model = new DefaultTableModel(new String[]{"Modifica Inviata", "Esito", "Autore Pagina", "Titolo"}, 0);
        JTable table = new JTable(model);

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        int pagineCreate=controller.getPagineCreate(controller.getNomeUtente());
        int numeroModifiche = controller.getNumeroModifiche(controller.getNomeUtente());
        String rango = controller.getRango(controller.getNomeUtente());
        editorPane.setContentType("text/html");
        StringBuilder htmlText = new StringBuilder("<html><body style='background-color: #F5F5F5;'>");
        htmlText.append("<h1 style='font-size: 36px; color: #333;'>Statistiche Dell'Utente : ").append(controller.getNomeUtente()).append("</h1>");
        htmlText.append(fontText).append("Hai Creato : ").append(pagineCreate).append(" Pagine").append("</p>");
        htmlText.append(fontText).append("Hai Effettuato : ").append(numeroModifiche).append(" Modifiche").append("</p>");
        htmlText.append(fontText).append("Sei Rango : ").append(rango).append("</p>");
        editorPane.setText(htmlText.toString());
        JScrollPane scrollPane = new JScrollPane(editorPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

// Aggiungi la tabella al pannello della tabella
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

// Aggiungi il pannello della tabella al pannello principale
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        ArrayList<String> testirichiesti = new ArrayList<>();
        ArrayList<String> esito = new ArrayList<>();
        ArrayList<String> autorepagina = new ArrayList<>();
        ArrayList<String> titolopagina = new ArrayList<>();
        controller.getRichieste(controller.getNomeUtente(),testirichiesti,esito,autorepagina,titolopagina);
        size = Math.min(Math.min(testirichiesti.size(), esito.size()), Math.min(autorepagina.size(), titolopagina.size()));
        String esito1 = "L'utente ancora deve accettare o rifiutare";
        try {
            for (int i = 0; i < size; i++) {
                    if(esito.get(i) == null) {
                        Object[] row = {testirichiesti.get(i), esito1, autorepagina.get(i), titolopagina.get(i)};
                        // Controlla se la riga è già presente nella tabella prima di aggiungerla
                        if (!isRowAlreadyAdded(model, row)) {
                            model.addRow(row);
                        }
                    }
                    else {
                        if(esito.get(i).equals("t")) {
                            if(autorepagina.get(i).equals(controller.getNomeUtente())) {
                                Object[] row = {testirichiesti.get(i), "Proposta Accettata","L'Autore Della Pagina Sei Tu", titolopagina.get(i)};
                                // Controlla se la riga è già presente nella tabella prima di aggiungerla
                                if (!isRowAlreadyAdded(model, row)) {
                                    model.addRow(row);
                                }
                            }
                        }
                        else {
                            Object[] row = {testirichiesti.get(i), "Proposta Rifiutata", autorepagina.get(i), titolopagina.get(i)};
                            // Controlla se la riga è già presente nella tabella prima di aggiungerla
                            if (!isRowAlreadyAdded(model, row)) {
                                model.addRow(row);
                            }
                        }
                    }
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
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

    /**
     * Metodo che permette di vedere se una riga è già stata aggiunta
     * @param model la tabella
     * @param row la riga da vedere
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

