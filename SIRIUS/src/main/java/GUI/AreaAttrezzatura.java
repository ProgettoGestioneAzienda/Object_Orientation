package GUI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

/**
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaAttrezzatura",
 * e fornisce la possibilita' di visualizzare, aggiungere o modificare le attrezzature.
 */
public class AreaAttrezzatura {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JPanel areaAttrezzaturaPanel;
    private JTable attrezzaturaTable;
    private JTextField cercaTextField;
    private JButton modificaButton;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JLabel cercaLabel;
    private JScrollPane scroll;
    private TableModel model = null;

    /**
     * Costruisce un'interfaccia utente "AreaAttrezzatura", che permette di visualizzare, aggiungere o modificare attrezzature.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle attrezzature.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaAttrezzatura(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area dell'attrezzatura
        frame = new JFrame("Area attrezzatura");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaAttrezzaturaPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella dell'attrezzatura
        String[] colonne = {"id", "Descrizione", "Costo", "Laboratorio", "Progetto"};

        aggiornaValori(controller, colonne);

        // Gestione del campo di ricerca per le attrezzature
        cercaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aggiornaSelezione();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aggiornaSelezione();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aggiornaSelezione();
            }
        });

        // Gestione dell'azione del pulsante "Indietro"
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Nascondi la finestra corrente
                frame.setVisible(false);

                // Rendi visibile la finestra chiamante
                frameChiamante.setVisible(true);

                // Rilascia le risorse della finestra corrente
                frame.dispose();
            }
        });

        // Gestione dell'azione del pulsante "Aggiungi"
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crea una nuova istanza di Attrezzatura
                Attrezzatura attrezzatura = null;
                try {
                    attrezzatura = new Attrezzatura(controller, frame, false);

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    attrezzatura.setAggiornaArea(new AggiornaArea() {
                        @Override
                        public void aggiornaAggiuntaModificaArea() {
                            aggiornaValori(controller, colonne);
                        }
                    });
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }

                // Nascondi la finestra corrente
                frame.setVisible(false);

                // Rendi visibile la finestra di Attrezzatura
                attrezzatura.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella delle attrezzature
                int rigaSelezionata = attrezzaturaTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    Attrezzatura attrezzatura = null;
                    int numCols = attrezzaturaTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupera i dati dalla riga selezionata nella tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = attrezzaturaTable.getValueAt(rigaSelezionata, i);

                    // Crea una nuova finestra per modificare un'attrezzatura
                    try {
                        attrezzatura = new Attrezzatura(controller, frame, true);

                        // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                        attrezzatura.setAggiornaArea(new AggiornaArea() {
                            @Override
                            public void aggiornaAggiuntaModificaArea() {
                                aggiornaValori(controller, colonne);
                            }
                        });

                        // Imposta i dati nella finestra di modifica di Attrezzatura
                        attrezzatura.setField(controller, dati);

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }

                    // Rendi visibile la finestra di modifica di Attrezzatura
                    attrezzatura.frame.setVisible(true);

                    // Nascondi la finestra corrente
                    frame.setVisible(false);
                }
            }
        });
    }

    /**
     * Aggiorna la riga selezionata nella tabella delle attrezzature in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {

        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Trova la prima cella della tabella che contiene quanto scritto nel campo testuale di ricerca
        for (int i = 0; i < attrezzaturaTable.getRowCount(); i++) {
            for (int j = 0; j < attrezzaturaTable.getColumnCount(); j++) {
                Object cella = attrezzaturaTable.getValueAt(i, j);

                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    attrezzaturaTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

                attrezzaturaTable.clearSelection();
            }
        }
    }

    /**
     * Aggiorna i risultati visualizzabili a video, nella tabella JTable apposita.
     *
     * @param controller {@link Controller} Fornisce i metodi per il recupero di informazioni dal database.
     * @param colonne                       Array di stringhe, rappresenta le intestazioni dei dati forniti dal controller.
     * */
    public void aggiornaValori(Controller controller, String[] colonne){
        try {
            // Crea un nuovo modello per la tabella delle attrezzature
            model = new DefaultTableModel(controller.recuperaObjectAttrezzature(), colonne) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rendi tutte le celle non modificabili
                }
            };
        } catch (NullPointerException exception) {
            // Se il modello non può essere recuperato, chiudi la finestra corrente
            frame.setVisible(false);
            frame.dispose();
        }

        // Aggiorna il modello della tabella delle attrezzature
        attrezzaturaTable.setModel(model);
    }
}
