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
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaAfferenze",
 * e fornisce la possibilita' di visualizzare, aggiungere, modificare o eliminare un'afferenza tra un dipendente a tempo indeterminato ed un laboratorio'.
 */
public class AreaAfferenze {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JPanel areaAfferenzePanel;
    private JTable afferenzeTable;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JButton modificaButton;
    private JButton eliminaButton;
    private JTextField cercaTextField;
    private JLabel cercaLabel;
    private TableModel model;

    /**
     * Costruisce un'interfaccia utente "AreaAfferenze", che permette di visualizzare, aggiungere, modificare o eliminare un'afferenza'.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle afferenze.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaAfferenze(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area delle afferenze
        frame = new JFrame("Area afferenze");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaAfferenzePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella delle afferenze
        String[] colonne = {"Dipendente", "Laboratorio"};

        // Si verifica che il model sia correttamente recuperabile tramite il metodo del costruttore apposito
        aggiornaValori(controller, colonne);

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

        // Gestione del campo di ricerca
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

        // Gestione dell'azione del pulsante "Aggiungi"
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crea una nuova istanza di Afferenza
                Afferenza afferenza = null;
                try {
                    afferenza = new Afferenza(controller, frame, false);

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    afferenza.setAggiornaArea(new AggiornaArea() {
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

                // Rendi visibile la finestra di Afferenza
                afferenza.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella delle afferenze
                int rigaSelezionata = afferenzeTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    Afferenza afferenza = null;
                    int numCols = afferenzeTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupera i dati dalla riga selezionata nella tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = afferenzeTable.getValueAt(rigaSelezionata, i);

                    // Crea una nuova istanza di Afferenza in modalità di modifica
                    try {
                        afferenza = new Afferenza(controller, frame, true);

                        // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                        afferenza.setAggiornaArea(new AggiornaArea() {
                            @Override
                            public void aggiornaAggiuntaModificaArea() {
                                aggiornaValori(controller, colonne);
                            }
                        });

                        // Imposta i dati nella finestra di modifica di Afferenza
                        afferenza.setField(controller, dati);

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }

                    // Rendi visibile la finestra di modifica di Afferenza
                    afferenza.frame.setVisible(true);

                    // Nascondi la finestra corrente
                    frame.setVisible(false);
                }
            }
        });

        // Gestione dell'azione del pulsante "Elimina"
        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei laboratori
                int rigaSelezionata = afferenzeTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    String selectedMatricola = (String) afferenzeTable.getValueAt(rigaSelezionata, 0);
                    String selectedNomeLab = (String) afferenzeTable.getValueAt(rigaSelezionata, 1);

                    try {
                        controller.eliminaAfferenza(selectedMatricola, selectedNomeLab);
                    } catch (IllegalArgumentException illegalArgumentException){
                        JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                    }
                }

                aggiornaValori(controller, colonne);
            }
        });
    }

    /**
     * Aggiorna la riga selezionata nella tabella delle afferenze in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {
        // Ottieni il testo di ricerca dal campo di ricerca e convertilo in minuscolo per una corrispondenza case-insensitive
        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Itera attraverso tutte le righe e colonne della tabella
        for (int i = 0; i < afferenzeTable.getRowCount(); i++) {
            for (int j = 0; j < afferenzeTable.getColumnCount(); j++) {
                // Ottieni il contenuto della cella corrente
                Object cella = afferenzeTable.getValueAt(i, j);

                // Se il contenuto non è nullo e contiene il testo di ricerca in minuscolo, seleziona la riga e interrompi la ricerca
                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    afferenzeTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

                // Se non è stata trovata alcuna corrispondenza, cancella la selezione
                afferenzeTable.clearSelection();
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
            // Crea un nuovo modello per la tabella delle afferenze
            model = new DefaultTableModel(controller.recuperaObjectAfferenze(), colonne) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rendi tutte le celle non modificabili
                }
            };
        } catch (NullPointerException nullPointerException) {
            // Se il modello non può essere recuperato, chiudi la finestra corrente
            frame.setVisible(false);
            frame.dispose();
        }

        // Aggiorna il modello della tabella delle afferenze
        afferenzeTable.setModel(model);
    }
}
