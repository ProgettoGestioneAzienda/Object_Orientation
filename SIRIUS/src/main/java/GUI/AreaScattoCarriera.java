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
 * Questa classe rappresenta l'interfaccia utente per l'area "AreaScattoCarriera",
 * e fornisce la possibilita' di visualizzare, aggiungere o modificare scatti di carriera.
 */
public class AreaScattoCarriera {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JTable scattoCarrieraTable;
    private JPanel areaScattoCarrieraPanel;
    private JButton indietroButton;
    private JButton aggiungiButton;
    private JButton modificaButton;
    private JTextField cercaTextField;
    private JLabel cercaLabel;
    private TableModel model = null;

    /**
     * Costruisce un'interfaccia utente "AreaScattoCarriera", che permette di visualizzare, aggiungere o modificare scatti di carriera.
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sugli scatti di carriera.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     */
    public AreaScattoCarriera(Controller controller, JFrame frameChiamante) {

        // Inizializza il frame principale per l'area "Scatto Carriera"
        frame = new JFrame("Area scatto carriera");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(areaScattoCarrieraPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Definizione delle colonne della tabella degli scatti di carriera
        String[] colonne = {"Matricola", "Tipo", "Data"};

        aggiornaValori(controller, colonne);

        // Gestione del campo di ricerca
        cercaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Richiama il metodo per aggiornare la selezione quando viene inserito del testo
                aggiornaSelezione();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Richiama il metodo per aggiornare la selezione quando viene rimosso del testo
                aggiornaSelezione();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Richiama il metodo per aggiornare la selezione quando ci sono cambiamenti (non usato nel tuo codice)
                aggiornaSelezione();
            }
        });

        // Gestione dell'azione del pulsante "Indietro"
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Nasconde la finestra corrente
                frame.setVisible(false);

                // Rende visibile la finestra chiamante
                frameChiamante.setVisible(true);

                // Rilascia le risorse della finestra corrente
                frame.dispose();
            }
        });

        // Gestione dell'azione del pulsante "Aggiungi"
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crea una nuova finestra per aggiungere uno scatto di carriera
                ScattoCarriera scattoCarriera = null;
                try {
                    scattoCarriera = new ScattoCarriera(controller, frame, false);

                    // Viene creato un oggetto che implementa l'interfaccia "AggiornaArea", il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                    scattoCarriera.setAggiornaArea(new AggiornaArea() {
                        @Override
                        public void aggiornaAggiuntaModificaArea() {
                            aggiornaValori(controller, colonne);
                        }
                    });

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }

                // Nasconde la finestra corrente
                frame.setVisible(false);

                // Rende visibile la finestra di ScattoCarriera
                scattoCarriera.frame.setVisible(true);
            }
        });

        // Gestione dell'azione del pulsante "Modifica"
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottiene l'indice della riga selezionata nella tabella degli scatti di carriera
                int rigaSelezionata = scattoCarrieraTable.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Selezionare una riga da modificare!");
                } else {
                    ScattoCarriera scattoCarriera = null;
                    int numCols = scattoCarrieraTable.getColumnCount();
                    Object[] dati = new Object[numCols];

                    // Recupera i dati dalla tabella
                    for (int i = 0; i < numCols; i++)
                        dati[i] = scattoCarrieraTable.getValueAt(rigaSelezionata, i);

                    // Controlla se il tipo di scatto di carriera è "Middle" o "Senior"
                    if (dati[1].equals("Middle") || dati[1].equals("Senior")) {
                        JOptionPane.showMessageDialog(frame, "Attenzione! Non è possibile modificare scatti di carriera basati su vincoli temporali!");
                    } else {
                        // Crea una finestra per modificare uno scatto di carriera
                        try {
                            scattoCarriera = new ScattoCarriera(controller, frame, true);

                            // Viene creato un oggetto che implementa l'interfaccia, il cui riferimento e' assegnato alla variabile di istanza callback nell'apposita GUI
                            scattoCarriera.setAggiornaArea(new AggiornaArea() {
                                @Override
                                public void aggiornaAggiuntaModificaArea() {
                                    aggiornaValori(controller, colonne);
                                }
                            });

                            // Imposta i dati nella finestra di modifica di ScattoCarriera
                            scattoCarriera.setField(controller, dati);

                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }

                        // Rende visibile la finestra di modifica di ScattoCarriera
                        scattoCarriera.frame.setVisible(true);

                        // Nasconde la finestra corrente
                        frame.setVisible(false);
                    }
                }
            }
        });

    }

    /**
     * Aggiorna la riga selezionata nella tabella degli scatti di carriera in base al testo inserito nella casella di ricerca.
     */
    public void aggiornaSelezione() {
        // Ottieni il testo di ricerca dal campo di ricerca e convertilo in minuscolo per una corrispondenza case-insensitive
        String ricercaTesto = cercaTextField.getText().toLowerCase();

        // Itera attraverso tutte le righe e colonne della tabella degli scatti di carriera
        for (int i = 0; i < scattoCarrieraTable.getRowCount(); i++) {
            for (int j = 0; j < scattoCarrieraTable.getColumnCount(); j++) {
                // Ottieni il contenuto della cella corrente
                Object cella = scattoCarrieraTable.getValueAt(i, j);

                // Se il contenuto non è nullo e contiene il testo di ricerca in minuscolo, seleziona la riga e interrompi la ricerca
                if ((cella != null) && cella.toString().toLowerCase().contains(ricercaTesto)) {
                    scattoCarrieraTable.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }
            }
        }

        // Se non viene trovato alcun risultato, cancella la selezione nella tabella degli scatti di carriera
        scattoCarrieraTable.clearSelection();
    }

    /**
     * Aggiorna i risultati visualizzabili a video, nella tabella JTable apposita.
     *
     * @param controller {@link Controller} Fornisce i metodi per il recupero di informazioni dal database.
     * @param colonne                       Array di stringhe, rappresenta le intestazioni dei dati forniti dal controller.
     * */
    public void aggiornaValori(Controller controller, String[] colonne){
        try {
            // Crea un nuovo modello per la tabella degli scatti di carriera
            model = new DefaultTableModel(controller.recuperaObjectScatti(), colonne) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rende tutte le celle non modificabili
                }
            };
        } catch (NullPointerException exception) {
            // Se il modello non può essere recuperato, chiudi la finestra corrente
            frame.setVisible(false);
            frame.dispose();
        }

        // Aggiorna il modello della tabella degli scatti di carriera
        scattoCarrieraTable.setModel(model);
    }
}
