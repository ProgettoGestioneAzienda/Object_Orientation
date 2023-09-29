package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.sql.SQLException;

/**
 * La classe "ScattoCarriera" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di uno scatto di carriera.
 */
public class ScattoCarriera {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JCheckBox dataOdiernaCheckBox;
    private JButton annullaButton;
    private JButton okButton;
    private JComboBox tipoScattoComboBox;
    private JTextField dataTextField;
    private JLabel tipoScattoLable;
    private JLabel dipendenteLable;
    private JLabel dataLable;
    private JPanel scattoCarrieraPanel;
    private JComboBox dipendenteComboBox;
    private JButton impostaButton;
    private String vecchioTipoScatto;
    private String vecchiaMatricola;
    private LocalDate vecchiaData;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "ScattoCarriera".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sugli scatti di carriera.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public ScattoCarriera(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {
        // Inizializzazione dell'interfaccia grafica e dei gestori di eventi
        frame = new JFrame("Scatto di carriera");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(scattoCarrieraPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaDipendentiIndeterminatiBreve());
        dipendenteComboBox.setModel(model);

        // Il bottone imposta aggiorna i dipendenti visualizzati nella Combobox, mostrando solo i dipendenti candidati allo scatto, in base al tipo selezionato
        impostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel<String> modelAggiornato = null;
                try {
                    modelAggiornato = new DefaultComboBoxModel<>(controller.recuperaDipendentiCandidati((String) tipoScattoComboBox.getSelectedItem()));
                    dipendenteComboBox.setModel(modelAggiornato);
                } catch (NullPointerException nullPointerException){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono dipendenti candidati per lo scatto specificato nel database!");
                }
            }
        });

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Gestisce la modifica o l'aggiunta di uno scatto di carriera
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoScatto = (String) tipoScattoComboBox.getSelectedItem();
                String dipendente = (String) dipendenteComboBox.getSelectedItem();
                LocalDate data;

                // Verifica che la data inserita sia nel formato corretto (YYYY-MM-DD).
                if (!controller.verifyDate(dataTextField.getText())) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci una data con formattazione YYYY-MM-DD!");
                }
                // Verifica che la data di scatto sia successiva alla data di assunzione del dipendente.
                else if (controller.checkNotCoerenzaDataAssunzioneDataScatto(dipendente, LocalDate.parse(dataTextField.getText()))){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci una data di scatto posteriore alla data di assunzione del dipendente!");
                }
                // Verifica che sia stato selezionato un tipo di scatto valido.
                else if (tipoScatto == null || tipoScatto.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono dipendenti candidati per lo scatto selezionato!");
                }
                // Verifica che sia stato selezionato un dipendente valido.
                else if (dipendente == null || dipendente.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono dipendenti candidati per lo scatto selezionato!");
                }
                // Verifica l'integrità degli scatti di responsabilità in base alla modalità (aggiunta o modifica).
                else if (!modificaMode && !controller.verificaIntegritaAggiuntaPromozioniRimozioniDipendente(tipoScatto, dipendente, LocalDate.parse(dataTextField.getText()))) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Lo scatto inserito rende non coerente la successione degli scatti di responsabilità!");
                }
                // Verifica l'integrità degli scatti di responsabilità in base alla modalità (aggiunta o modifica).
                else if (modificaMode && !controller.verificaIntegritaModificaPromozioniRimozioniDipendente(vecchioTipoScatto, vecchiaMatricola, vecchiaData, tipoScatto, dipendente, LocalDate.parse(dataTextField.getText()))){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Lo scatto modificato rende non coerente la successione degli scatti di responsabilità!");
                }
                // Se tutte le verifiche hanno successo, esegue l'aggiunta o la modifica dello scatto di carriera.
                else {
                    data = LocalDate.parse(dataTextField.getText());
                    if (modificaMode){
                        try{
                            controller.modificaScattoCarriera(vecchioTipoScatto, vecchiaMatricola, vecchiaData, tipoScatto, dipendente, data);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    } else {
                        try {
                            controller.aggiungiScattoCarriera(tipoScatto, dipendente, data);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    }
                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaScattoCarriera" per aggiornare la tabella
                    callback.aggiornaAggiuntaModificaArea();

                    frame.dispose();
                }
            }
        });

        // Abilita oppure disabilita il campo relativo all'inserimento della data, in base ad una selezione grafica.
        dataOdiernaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataTextField.setEnabled(false);
                    dataTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    dataTextField.setEnabled(true);
                    dataTextField.setText("");
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
        // Imposta il tipo di scatto selezionato nella ComboBox
        for (String stored : new String[]{"Promosso_a_dirigente", "Rimosso_da_dirigente"}) {
            if (stored.contains(dati[1].toString())) {
                tipoScattoComboBox.setSelectedItem(stored);
                setVecchioTipoScatto(dati[1].toString());
                break;
            }
        }

        // Imposta il dipendente selezionato nella ComboBox
        for (String stored : controller.recuperaDipendentiIndeterminatiBreve()) {
            if (stored.contains(dati[0].toString())) {
                dipendenteComboBox.setSelectedItem(stored);
                setVecchiaMatricola(dati[0].toString());
                break;
            }
        }

        // Imposta la data nello scatto di carriera
        dataTextField.setText(dati[2].toString());
        setVecchiaData(LocalDate.parse(dati[2].toString()));
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
     * Imposta la data dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaData  {@link String}  Rappresenta la data corrente dell'istanza da modificare
     * */
    public void setVecchiaData(LocalDate vecchiaData){
        this.vecchiaData = vecchiaData;
    }

    /**
     * Imposta il tipo di scatto dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioTipoScatto  {@link String}  Rappresenta il tipo di scatto corrente dell'istanza da modificare
     * */
    public void setVecchioTipoScatto(String vecchioTipoScatto){
        this.vecchioTipoScatto = vecchioTipoScatto;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}
