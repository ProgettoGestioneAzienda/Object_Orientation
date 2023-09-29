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
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaLavorare",
 * e fornisce la possibilita' di visualizzare, aggiungere, modificare o eliminare istanze di lavoro tra progetti e laboratori.
 */
public class AreaLavorare {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JPanel areaLavorarePanel;
    private JTable lavorareTable;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JButton modificaButton;
    private JTextField cercaTextField;
    private JLabel cercaLabel;
    private JButton eliminaButton;
    private TableModel model;

    /**
     * Costruisce un'interfaccia utente "AreaLavorare", che permette di visualizzare, aggiungere, modificare o rimuovere istanze di lavoro tra laboratori e progetti.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle istanze di lavoro.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaLavorare(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area "Lavorare"
        frame = new JFrame("Area lavorare");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaLavorarePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella dei lavori
        String[] colonne = {"Progetto", "Laboratorio"};

        aggiornaValori(controller, colonne);

        // Gestione del campo di ricerca
        cercaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Aggiorna la selezione quando viene inserito del testo
                aggiornaSelezione();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Aggiorna la selezione quando viene rimosso del testo
                aggiornaSelezione();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Aggiorna la selezione quando ci sono cambiamenti (non usato nel tuo codice)
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
                // Crea una nuova finestra per aggiungere un lavoro
                Lavorare lavorare = null;
                try {
                    lavorare = new Lavorare(controller, frame, false);

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    lavorare.setAggiornaArea(new AggiornaArea() {
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

                // Rendi visibile la finestra di Lavorare
                lavorare.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei lavori
                int rigaSelezionata = lavorareTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    Lavorare lavorare = null;
                    int numCols = lavorareTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupero dei dati dalla tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = lavorareTable.getValueAt(rigaSelezionata, i);

                    try {
                        // Creazione di un'istanza della classe Lavorare in modalità modifica
                        lavorare = new Lavorare(controller, frame, true);

                        // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                        lavorare.setAggiornaArea(new AggiornaArea() {
                            @Override
                            public void aggiornaAggiuntaModificaArea() {
                                aggiornaValori(controller, colonne);
                            }
                        });

                        if (lavorare != null) {

                            // Imposta i dati nella finestra di modifica di Lavorare
                            lavorare.setField(controller, dati);

                            // Rendi visibile la finestra di modifica di Lavorare
                            lavorare.frame.setVisible(true);

                            // Nascondi la finestra corrente
                            frame.setVisible(false);
                        }

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });

        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei laboratori
                int rigaSelezionata = lavorareTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    String selectedCupProgetto = (String) lavorareTable.getValueAt(rigaSelezionata, 0);
                    String selectedNomeLab = (String) lavorareTable.getValueAt(rigaSelezionata, 1);

                    controller.eliminaLavoro(selectedCupProgetto, selectedNomeLab);

                    aggiornaValori(controller, colonne);
                }
            }
        });
    }

    /**
     * Aggiorna la riga selezionata nella tabella delle istanze di lavoro in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {
        // Ottieni il testo di ricerca dal campo di ricerca e convertilo in minuscolo per una corrispondenza case-insensitive
        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Itera attraverso tutte le righe e colonne della tabella dei lavori
        for (int i = 0; i < lavorareTable.getRowCount(); i++) {
            for (int j = 0; j < lavorareTable.getColumnCount(); j++) {
                // Ottieni il contenuto della cella corrente
                Object cella = lavorareTable.getValueAt(i, j);

                // Se il contenuto non è nullo e contiene il testo di ricerca in minuscolo, seleziona la riga e interrompi la ricerca
                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    lavorareTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

                // Se non è stata trovata alcuna corrispondenza, cancella la selezione nella tabella dei lavori
                lavorareTable.clearSelection();
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
            // Crea un nuovo modello per la tabella dei lavori
            model = new DefaultTableModel(controller.recuperaObjectLavori(), colonne) {
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

        // Aggiorna il modello della tabella dei lavori
        lavorareTable.setModel(model);
    }
}