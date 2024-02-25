package gui;
import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * La classe Cancella pagina che permette all'utente di cancellare una pagina.
 */
public class CancellaPagina {
    private JFrame frame;

    private DefaultTableModel model;

    /**
     * Il controller che recupera le pagine dal db.
     */
    public Controller controller = new Controller();

    /**
     * Instanzia a new Cancella pagina.
     *
     * @param framechiamante Il framechiamante che è quello della home
     * @param controller     Il controller
     */
    public CancellaPagina(JFrame framechiamante, Controller controller) {
        frame = new JFrame("Qui puoi Cancellare Le Tue Pagine !");
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
        JLabel titleLabel = new JLabel("Queste Sono Le Tue Pagine");
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
        initArrayPerTabella(titoli,date,ore);
        int size = Math.min(Math.min(titoli.size(), date.size()),ore.size());
        for (int i = 0; i < size; i++) {
            Object[] row = {titoli.get(i), date.get(i), ore.get(i), "L'autore Della Pagina sei tu"};
            model.addRow(row);
        }
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    String titolo = (String) model.getValueAt(row, 0);
                    try{
                        controller.deletePagina(titolo);
                        JOptionPane.showMessageDialog(null,"Hai cancellato la pagina"+titolo);
                        initArrayPerTabella(titoli,date,ore);
                        int size = Math.min(Math.min(titoli.size(), date.size()),ore.size());
                        for (int i = 0; i < size; i++) {
                            Object[] rowcaricata = {titoli.get(i), date.get(i), ore.get(i), "L'autore Della Pagina sei tu"};
                            if (!isRowAlreadyAdded(model, rowcaricata)) {
                                model.addRow(rowcaricata);
                            }
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Errore non andato a buon fine");
                    }

                }
            }
        });
    }

    /**
     * Metodo che permette di riempire la tabella, riempendo le list in ingresso.
     *
     * @param titoli I titoli delle paginecreate
     * @param date   Le date di creazione
     * @param ore    Le ore di creazone
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
