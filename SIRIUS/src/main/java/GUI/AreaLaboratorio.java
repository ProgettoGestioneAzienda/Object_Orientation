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
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaLaboratorio",
 * e fornisce la possibilita' di visualizzare, aggiungere, modificare o eliminare laboratori.
 */
public class AreaLaboratorio {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JTable laboratorioTable;
    private JPanel areaLaboratorioPanel;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JButton modificaButton;
    private JTextField cercaTextField;
    private JButton eliminaButton;
    private JLabel cercaLabel;
    private TableModel model = null;

    /**
     * Costruisce un'interfaccia utente "AreaLaboratorio", che permette di visualizzare, aggiungere, modificare o eliminare laboratori.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sui laboratori.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaLaboratorio(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area dei laboratori
        frame = new JFrame("Area laboratorio");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaLaboratorioPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella dei laboratori
        String[] colonne = {"Nome", "Topic", "Responsabile scientifico", "Numero afferenti"};

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
                // Crea una nuova finestra per aggiungere un laboratorio
                Laboratorio laboratorio = null;
                try {
                    laboratorio = new Laboratorio(controller, frame, false); // Passa null come datiLaboratorioDaModificare

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    laboratorio.setAggiornaArea(new AggiornaArea() {
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

                // Rendi visibile la finestra di Laboratorio
                laboratorio.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei laboratori
                int rigaSelezionata = laboratorioTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    Laboratorio laboratorio = null;
                    int numCols = laboratorioTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupero dei dati dalla tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = laboratorioTable.getValueAt(rigaSelezionata, i);

                    try {
                        // Creazione di un'istanza della classe Laboratorio in modalità modifica
                        laboratorio = new Laboratorio(controller, frame, true);

                        // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                        laboratorio.setAggiornaArea(new AggiornaArea() {
                            @Override
                            public void aggiornaAggiuntaModificaArea() {
                                aggiornaValori(controller, colonne);
                            }
                        });

                        if (laboratorio != null) {
                            // Imposta i dati nella finestra di modifica di Laboratorio
                            laboratorio.setField(controller, dati);

                            // Rendi visibile la finestra di modifica di Laboratorio
                            laboratorio.frame.setVisible(true);
                            // Nascondi la finestra corrente
                            frame.setVisible(false);

                        }

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });

        // Gestione dell'azione dep pulsante "Elimina"
        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni l'indice della riga selezionata nella tabella dei laboratori
                int rigaSelezionata = laboratorioTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    String selectedNomeLab = (String) laboratorioTable.getValueAt(rigaSelezionata, 0);
                    controller.eliminaLaboratorio(selectedNomeLab);
                }

                aggiornaValori(controller, colonne);
            }
        });
    }

    /**
     * Aggiorna la riga selezionata nella tabella dei laboratori in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {
        // Ottieni il testo di ricerca dal campo di ricerca e convertilo in minuscolo per una corrispondenza case-insensitive
        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Itera attraverso tutte le righe e colonne della tabella dei laboratori
        for (int i = 0; i < laboratorioTable.getRowCount(); i++) {
            for (int j = 0; j < laboratorioTable.getColumnCount(); j++) {
                // Ottieni il contenuto della cella corrente
                Object cella = laboratorioTable.getValueAt(i, j);

                // Se il contenuto non è nullo e contiene il testo di ricerca in minuscolo, seleziona la riga e interrompi la ricerca
                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    laboratorioTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

                // Se non è stata trovata alcuna corrispondenza, cancella la selezione nella tabella dei laboratori
                laboratorioTable.clearSelection();
            }
        }
    }

    /**
     * Aggiorna i risultati visualizzabili a video, nella tabella JTable apposita.
     *
     * @param controller {@link Controller} Fornisce i metodi per il recupero di informazioni dal database.
     * @param colonne                       Array di stringhe, rappresenta le intestazioni dei dati forniti dal controller.
     * */
    public void aggiornaValori(Controller controller, String[] colonne) {
        try {
            // Crea un nuovo modello per la tabella dei laboratori
            model = new DefaultTableModel(controller.recuperaObjectLaboratori(), colonne) {
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

        // Aggiorna il modello della tabella dei laboratori
        laboratorioTable.setModel(model);
    }
}