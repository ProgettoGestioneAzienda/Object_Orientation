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
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaDipendenteProgetto",
 * e fornisce la possibilita' di visualizzare, aggiungere o modificare dipendenti a progetto.
 */
public class AreaDipendenteProgetto {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JPanel areaDipendenteProgettoPanel;
    private JTable dipendenteProgettoTable;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JButton modificaButton;
    private JTextField cercaTextField;
    private JLabel cercaLabel;
    private TableModel model = null;

    /**
     * Costruisce un'interfaccia utente "AreaDipendenteProgetto", che permette di visualizzare, aggiungere o modificare dipendenti a progetto.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sui dipendenti a progetto.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaDipendenteProgetto(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area dei dipendenti di progetto
        frame = new JFrame("Area dipendente progetto");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaDipendenteProgettoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella dei dipendenti di progetto
        String[] colonne = {"Matricola", "Nome", "Cognome", "Codice fiscale", "Indirizzo", "Data nascita", "Data assunzione", "Costo", "Scadenza", "CUP"};

        // Si verifica che il model sia correttamente recuperabile tramite il metodo apposito
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
                // Crea una nuova finestra per aggiungere un dipendente di progetto
                DipendenteProgetto dipendenteProgetto = null;
                try {
                    dipendenteProgetto = new DipendenteProgetto(controller, frame, false);

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    dipendenteProgetto.setAggiornaArea(new AggiornaArea() {
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

                // Rendi visibile la finestra di DipendenteProgetto
                dipendenteProgetto.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei dipendenti di progetto
                int rigaSelezionata = dipendenteProgettoTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    DipendenteProgetto dipendenteProgetto = null;
                    int numCols = dipendenteProgettoTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupero dei dati dalla tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = dipendenteProgettoTable.getValueAt(rigaSelezionata, i);

                    // Crea una finestra per modificare un dipendente di progetto
                    try {
                        dipendenteProgetto = new DipendenteProgetto(controller, frame, true);

                        // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                        dipendenteProgetto.setAggiornaArea(new AggiornaArea() {
                            @Override
                            public void aggiornaAggiuntaModificaArea() {
                                aggiornaValori(controller, colonne);
                            }
                        });

                        // Imposta i dati nella finestra di modifica di DipendenteProgetto
                        dipendenteProgetto.setField(controller, dati);

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }

                    // Rendi visibile la finestra di modifica di DipendenteProgetto
                    dipendenteProgetto.frame.setVisible(true);

                    // Nascondi la finestra corrente
                    frame.setVisible(false);
                }
            }
        });
    }

    /**
     * Aggiorna la riga selezionata nella tabella dei dipendenti a progetto in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {
        // Ottieni il testo di ricerca dal campo di ricerca e convertilo in minuscolo per una corrispondenza case-insensitive
        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Itera attraverso tutte le righe e colonne della tabella dei dipendenti di progetto
        for (int i = 0; i < dipendenteProgettoTable.getRowCount(); i++) {
            for (int j = 0; j < dipendenteProgettoTable.getColumnCount(); j++) {
                // Ottieni il contenuto della cella corrente
                Object cella = dipendenteProgettoTable.getValueAt(i, j);

                // Se il contenuto non è nullo e contiene il testo di ricerca in minuscolo, seleziona la riga e interrompi la ricerca
                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    dipendenteProgettoTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

                // Se non è stata trovata alcuna corrispondenza, cancella la selezione nella tabella dei dipendenti di progetto
                dipendenteProgettoTable.clearSelection();
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
            // Crea un nuovo modello per la tabella dei dipendenti di progetto
            model = new DefaultTableModel(controller.recuperaObjectDipendentiProgetto(), colonne) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rendi tutte le celle non modificabili
                }
            };

        } catch (NullPointerException exception) {
            // Se il modello non può essere recuperato, chiudi il frame
            frame.setVisible(false);
            frame.dispose();
        }

        // Aggiorna il modello della tabella dei dipendenti di progetto
        dipendenteProgettoTable.setModel(model);
    }
}
