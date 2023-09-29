package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

/**
 * La classe "Afferenza" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un'afferenza tra un dipendente a tempo indeterminato ed un laboratorio'.
 */
public class Afferenza {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JComboBox dipendenteComboBox;
    private JPanel afferenzaPanel;
    private JComboBox laboratorioComboBox;
    private JButton annullaButton;
    private JButton okButton;
    private JLabel dipendenteLabel;
    private JLabel laboratorioLabel;
    private String vecchioNomeLab;
    private String vecchiaMatricola;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "Afferenza".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle afferenze.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public Afferenza(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {
        // Creazione di una nuova finestra JFrame denominata "Afferenza"
        frame = new JFrame("Afferenza");

        // Impostazione dell'icona dell'applicativo
        frame.setIconImage(img_logo_lr.getImage());

        // Impostazione del contenuto della finestra come afferenzaPanel
        frame.setContentPane(afferenzaPanel);

        // Impostazione dell'operazione di chiusura della finestra su "EXIT_ON_CLOSE"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adattamento delle dimensioni della finestra in base al contenuto
        frame.pack();

        // Posizionamento della finestra al centro della finestra chiamante
        frame.setLocationRelativeTo(frameChiamante);

        // Memorizzazione della modalità di modifica
        this.modificaMode = modificaMode;

        // Creazione di un modello per la casella di selezione dipendenteComboBox
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaDipendentiIndeterminatiBreve());

        // Impostazione del modello per la casella di selezione dipendenteComboBox
        dipendenteComboBox.setModel(model);

        // Creazione di un modello per la casella di selezione laboratorioComboBox
        model = new DefaultComboBoxModel<>(controller.recuperaLaboratori());

        // Impostazione del modello per la casella di selezione laboratorioComboBox
        laboratorioComboBox.setModel(model);

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
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

        // Gestisce la modifica o l'aggiunta di un'afferenza tra un laboratorio ed un dipendente a tempo indeterminato
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni il dipendente selezionato dalla casella di selezione
                String dipendente = (String) dipendenteComboBox.getSelectedItem();

                // Ottieni il laboratorio selezionato dalla casella di selezione
                String laboratorio = (String) laboratorioComboBox.getSelectedItem();

                // Verifica se il campo dipendente è vuoto o nullo
                if (dipendente == null || dipendente.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un dipendente a tempo indeterminato nel database!");
                }
                // Verifica se il campo laboratorio è vuoto o nullo
                else if (laboratorio == null || laboratorio.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un laboratorio nel database!");
                }
                // Verifica se si sta modificando l'afferenza di un responsabile scientifico
                else if (modificaMode && controller.checkResponsabileScientificoLaboratorio(vecchiaMatricola, vecchioNomeLab)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non e' possibile modificare l'afferenza di un responsabile scientifico! Cambiare prima il responsabile scientifico del laboratorio coinvolto!");
                }
                else {
                    // Se la modalità di modifica è attiva
                    if (modificaMode) {
                        try {
                            controller.modificaAfferenza(vecchiaMatricola, vecchioNomeLab, dipendente, laboratorio);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    }
                    // Altrimenti, se la modalità di modifica non è attiva
                    else {
                        try {
                            controller.aggiungiAfferenza(dipendente, laboratorio);
                        } catch (IllegalArgumentException illegalArgumentException) {
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    }

                    // Nascondi la finestra corrente
                    frame.setVisible(false);

                    // Rendi visibile la finestra chiamante
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaAfferenza" per aggiornare la tabella
                    callback.aggiornaAggiuntaModificaArea();

                    // Rilascia le risorse della finestra corrente
                    frame.dispose();
                }
            }
        });
    }


    /**
     * Nel caso della modifica, imposta i campi di interazione con i valori dell'istanza da modificare, selezionata precedentemente nell'apposita area.
     *
     * @param controller    {@link Controller}  Il controller per gestire le operazioni di recupero dati da visualizzare nelle ComboBox apposite.
     * @param dati                              Lista di dati appartenenti all'istanza selezionata per la modifica, nella precedente area.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public void setField(Controller controller, Object[] dati) throws SQLException {

        // Itera attraverso i dipendenti indeterminati brevi disponibili e seleziona quello corrispondente al dato preesistente
        for (String stored : controller.recuperaDipendentiIndeterminatiBreve()){
            if (stored.contains(dati[0].toString())) {
                dipendenteComboBox.setSelectedItem(stored);
                setVecchiaMatricola((String) dati[0]);
                break;
            }
        }

        // Itera attraverso i laboratori disponibili e seleziona quello corrispondente al dato pre esistente
        for (String stored : controller.recuperaLaboratori()){
            if (stored.contains(dati[1].toString())) {
                laboratorioComboBox.setSelectedItem(stored);
                setVecchioLaboratorio((String) dati[1]);
                break;
            }
        }
    }

    /**
     * Imposta la matricola dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaMatricola  {@link String}  Rappresenta la matricola corrente dell'istanza da modificare
     * */
    public void setVecchiaMatricola(String vecchiaMatricola){
        this.vecchiaMatricola = vecchiaMatricola;
    }

    /**
     * Imposta il laboratorio dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioNomeLab  {@link String}  Rappresenta il laboratorio corrente dell'istanza da modificare
     * */
    public void setVecchioLaboratorio(String vecchioNomeLab){
        this.vecchioNomeLab = vecchioNomeLab;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}