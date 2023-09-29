package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.postgresql.util.PSQLException;
import java.sql.SQLException;

/**
 * La classe "DipendenteProgetto" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un dipendente a progetto.
 */
public class DipendenteProgetto {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JTextField nomeTextField;
    private JPanel dipendenteProgettoPanel;
    private JTextField cognomeTextField;
    private JTextField codFiscaleTextField;
    private JTextField matricolaTextField;
    private JTextField indirizzoTextField;
    private JTextField dataNascitaTextField;
    private JTextField dataAssunzioneTextField;
    private JTextField scadenzaTextField;
    private JTextField costoTextField;
    private JComboBox progettoComboBox;
    private JCheckBox indirizzoCheckBox;
    private JCheckBox dataOdiernaAssuzioneCheckBox;
    private JCheckBox dataOdiernaScadenzaCheckBox;
    private JLabel nomeLabel;
    private JLabel cognomeLabel;
    private JLabel codFiscaleLabel;
    private JLabel matricolaLabel;
    private JLabel indirizzoLabel;
    private JLabel dataNascitaLabel;
    private JLabel dataAssunzioneLabel;
    private JLabel scadenzaLabel;
    private JLabel costoLabel;
    private JLabel progettoLabel;
    private JButton okButton;
    private JButton annullaButton;
    private String vecchioNome;
    private String vecchioCognome;
    private String vecchioCodFiscale;
    private String vecchiaMatricola;
    private LocalDate vecchiaDataNascita;
    private String vecchioProgetto;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "DipendenteProgetto".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sui dipendenti a progetto.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public DipendenteProgetto(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {

        frame = new JFrame("Dipendente a progetto");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(dipendenteProgettoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaProgetti());
        progettoComboBox.setModel(model);

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Nasconde la finestra corrente, mostra la finestra chiamante e la elimina
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Azione per la checkBox "Indirizzo", rende il campo testuale abilitato o disabilitato in base al valore di selezione
        indirizzoCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Abilita o disabilita la casella di testo per l'indirizzo in base allo stato della casella di controllo
                if (e.getStateChange() == ItemEvent.SELECTED)
                    indirizzoTextField.setEnabled(true);
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                    indirizzoTextField.setEnabled(false);
            }
        });

        // Azione per la casella di controllo "Data Assunzione Odierna", se selezionata abilita il campo testuale relativo alla data assunzione
        dataOdiernaAssuzioneCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Abilita o disabilita la casella di testo per la data di assunzione in base allo stato della casella di controllo
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataAssunzioneTextField.setEnabled(false);
                    dataAssunzioneTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    dataAssunzioneTextField.setEnabled(true);
                    dataAssunzioneTextField.setText("");
                }
            }
        });

        // Azione per la casella di controllo "Data Scadenza Odierna", se selezionata abilita il campo testuale relativo alla data di scadenza
        dataOdiernaScadenzaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Abilita o disabilita la casella di testo per la data di scadenza in base allo stato della casella di controllo
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    scadenzaTextField.setEnabled(false);
                    scadenzaTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    scadenzaTextField.setEnabled(true);
                    scadenzaTextField.setText("");
                }
            }
        });

        // Gestisce la modifica o l'aggiunta di un dipendente a progetto
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome;
                String cognome;
                String codFiscale;
                String matricola;
                String indirizzo;
                LocalDate dataNascita;
                LocalDate dataAssunzione;
                LocalDate scadenza;
                BigDecimal costo;
                String progettoProprietario = (String) progettoComboBox.getSelectedItem();

                boolean checkedIndirizzo = indirizzoCheckBox.isSelected();

                if ((nome = nomeTextField.getText()).isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il nome del dipendente!");

                } else if ((cognome = cognomeTextField.getText()).isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il cognome del dipendente!");

                }else if ((codFiscale = codFiscaleTextField.getText()).isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il codice fiscale del dipendente!");

                }else if ((codFiscale.length() != 16) || (!controller.isAlphanumeric(codFiscale))){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un codice fiscale di esattamente 16 caratteri alfanumerici!");

                } else if ((matricola = matricolaTextField.getText()).isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire la matricola del dipendente!");

                } else if (matricola.length() != 8){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire una matricola di esattamente 8 caratteri!");

                } else if ((!controller.verifyDate(dataNascitaTextField.getText())) || (!controller.verifyDate(dataAssunzioneTextField.getText())) ||
                        (!controller.verifyDate(scadenzaTextField.getText()))){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci una data con formattazione YYYY-MM-DD!");

                } else if (controller.notCoerenzaDate(LocalDate.parse(dataNascitaTextField.getText()), LocalDate.parse(dataAssunzioneTextField.getText()), LocalDate.parse(scadenzaTextField.getText()))){

                  JOptionPane.showMessageDialog(frame, "Attenzione! Controllare la coerenza tra le date!");

                } else if (!controller.verifyBigDecimal(costoTextField.getText())){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci un costo corretto!");

                    //non lo produce, poiche il metodo di recupero imposta una stringa "" se non riesce a recuperare nulla
                } else if (progettoProprietario == null || progettoProprietario.isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un progetto nel database!");

                } else if (controller.checkIsDipendenteIndeterminato(codFiscale)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Questo dipendente e' gia' un dipendente indeterminato!");

                } else if (controller.coerenzaDateFineProgettoDipendente(LocalDate.parse(scadenzaTextField.getText()), progettoProprietario)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Controllare che la data di assunzione e la scadenza siano precedenti alla data fine del progetto!");

                } else if (modificaMode && !controller.checkMatricolaUnica(vecchiaMatricola, matricola, modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! La matricola inserita e' gia' stata assegnata ad un altro dipendente!");

                } else if (modificaMode && controller.coerenzaCodiceFiscale(vecchioNome, vecchioCognome, vecchioCodFiscale, vecchiaDataNascita,
                        codFiscale, nome, cognome, LocalDate.parse(dataNascitaTextField.getText()), modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Esiste un altro dipendente con lo stesso codice fiscale ma con dati anagrafici diversi!");

                } else if (!modificaMode && controller.isDipendenteProgettoActive(modificaMode, codFiscale, LocalDate.parse(dataAssunzioneTextField.getText()), LocalDate.parse(scadenzaTextField.getText()), vecchiaMatricola)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Questo dipendente a progetto gia' possiede una carriera attiva!");

                } else if (!modificaMode && !controller.checkMatricolaUnica(vecchiaMatricola, matricola, modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! La matricola inserita e' gia' stata assegnata ad un altro dipendente!");

                } else if (!modificaMode && controller.coerenzaCodiceFiscale(vecchioNome, vecchioCognome, vecchioCodFiscale, vecchiaDataNascita,
                        codFiscale, nome, cognome, LocalDate.parse(dataNascitaTextField.getText()), modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Esiste un altro dipendente con lo stesso codice fiscale ma con dati anagrafici diversi!");

                } else if (controller.checkAcquistoDipendenteProgettoHalfBudget(modificaMode, new BigDecimal(costoTextField.getText()), (String) progettoComboBox.getSelectedItem(), vecchiaMatricola)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Il dipendente e' troppo costoso per rientrare nella meta' del budget!");

                } else if (modificaMode && controller.isDipendenteProgettoActive(modificaMode, codFiscale, LocalDate.parse(dataAssunzioneTextField.getText()), LocalDate.parse(scadenzaTextField.getText()), vecchiaMatricola)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Questo dipendente a progetto gia' possiede una carriera attiva!");

                } else {

                    dataNascita = LocalDate.parse(dataNascitaTextField.getText());
                    dataAssunzione = LocalDate.parse(dataAssunzioneTextField.getText());
                    scadenza = LocalDate.parse(scadenzaTextField.getText());
                    costo = new BigDecimal(costoTextField.getText());

                    if (checkedIndirizzo){

                        indirizzo = indirizzoTextField.getText();

                        if (modificaMode){

                            try {
                                controller.modificaDipendenteProgetto(vecchiaMatricola, vecchioProgetto, nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione, scadenza, costo, progettoProprietario);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        } else {

                            try {
                                controller.aggiungiDipendenteProgetto(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione, scadenza, costo, progettoProprietario);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }

                    } else{

                        if (modificaMode){

                            try {
                                controller.modificaDipendenteProgetto(vecchiaMatricola, vecchioProgetto, nome, cognome, codFiscale, matricola, null, dataNascita, dataAssunzione, scadenza, costo, progettoProprietario);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        } else {

                            try {
                                controller.aggiungiDipendenteProgetto(nome, cognome, codFiscale, matricola, dataNascita, dataAssunzione, scadenza, costo, progettoProprietario);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }
                    }

                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaDipendenteProgetto" per aggiornare la tabella
                    callback.aggiornaAggiuntaModificaArea();

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

        // Imposta il campo Nome con il valore dal database
        nomeTextField.setText((String) dati[1]);
        setVecchioNome((String) dati[1]);

        // Imposta il campo Cognome con il valore dal database
        cognomeTextField.setText((String) dati[2]);
        setVecchioCognome((String) dati[2]);

        // Imposta il campo Codice Fiscale con il valore dal database
        codFiscaleTextField.setText((String) dati[3]);
        setVecchioCodFiscale((String) dati[4]);

        // Imposta il campo Matricola con il valore dal database
        matricolaTextField.setText((String) dati[0]);
        setVecchiaMatricola((String) dati[0]);

        // Verifica se l'indirizzo è presente nel database e imposta la casella di controllo Indirizzo
        if (dati[5] == null) {
            indirizzoCheckBox.setSelected(false);
        } else {
            indirizzoCheckBox.setSelected(true);
            indirizzoTextField.setText((String) dati[4]);
        }

        // Imposta il campo Data di Nascita con il valore dal database
        dataNascitaTextField.setText((String) dati[5]);
        setVecchiaDataNascita(LocalDate.parse((String) dati[5]));

        // Imposta il campo Data di Assunzione con il valore dal database
        dataAssunzioneTextField.setText((String) dati[6]);

        // Imposta il campo Costo con il valore dal database
        costoTextField.setText((String) dati[7]);

        // Imposta il campo Scadenza con il valore dal database
        scadenzaTextField.setText((String) dati[8]);

        // Imposta la ComboBox Progetto con il valore dal database
        for (String stored : controller.recuperaProgetti()) {
            if (stored.contains(dati[9].toString())) {
                progettoComboBox.setSelectedItem(stored);
                setVecchioProgetto(dati[9].toString());
                break;
            }
        }
    }


    // Metodi setter per i dati vecchi

    /**
     * Imposta il nome dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioNome  {@link String}  Rappresenta il nome corrente dell'istanza da modificare
     * */
    public void setVecchioNome(String vecchioNome){this.vecchioNome = vecchioNome;}

    /**
     * Imposta il cognome dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioCognome  {@link String}  Rappresenta il cognome corrente dell'istanza da modificare
     * */
    public void setVecchioCognome(String vecchioCognome){
        this.vecchioCognome = vecchioCognome;
    }

    /**
     * Imposta il codice fiscale dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioCodFiscale  {@link String}  Rappresenta il codice fiscale corrente dell'istanza da modificare
     * */
    public void setVecchioCodFiscale(String vecchioCodFiscale){
        this.vecchioCodFiscale = vecchioCodFiscale;
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
     * Imposta la data di nascita dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaDataNascita  {@link LocalDate}  Rappresenta la data di nascita corrente dell'istanza da modificare
     * */
    public void setVecchiaDataNascita(LocalDate vecchiaDataNascita){ this.vecchiaDataNascita = vecchiaDataNascita; }

    /**
     * Imposta il progetto proprietario dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioProgetto  {@link String}  Rappresenta il progetto proprietario corrente dell'istanza da modificare
     * */
    public void setVecchioProgetto(String vecchioProgetto){
        this.vecchioProgetto = vecchioProgetto;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}
