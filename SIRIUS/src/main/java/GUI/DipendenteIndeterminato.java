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
import org.postgresql.util.PSQLException;

/**
 * La classe "DipendenteIndeterminato" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un dipendente indeterminato.
 */
public class DipendenteIndeterminato {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JPanel dipendenteIndeterminatoPanel;
    private JTextField nomeTextField;
    private JTextField cognomeTextField;
    private JTextField matricolaTextField;
    private JComboBox tipoComboBox;
    private JTextField codFiscaleTextField;
    private JTextField indirizzoTextField;
    private JTextField dataNascitaTextField;
    private JTextField dataAssunzioneTextField;
    private JTextField dataFineTextField;
    private JCheckBox indirizzoCheckBox;
    private JCheckBox dirigenteCheckBox;
    private JCheckBox dataFineCheckBox;
    private JLabel nomeLabel;
    private JLabel cognomeLabel;
    private JLabel matricolaLabel;
    private JLabel tipoLabel;
    private JLabel codFiscaleLable;
    private JLabel indirizzoLable;
    private JLabel dataNascitaLable;
    private JLabel dataAssunzioneLable;
    private JLabel dataFineLable;
    private JCheckBox dataAssunzioneOdiernaCheckBox;
    private JCheckBox dataFineOdiernaCheckBox;
    private JButton okButton;
    private JButton annullaButton;
    private String vecchioNome;
    private String vecchioCognome;
    private String vecchioCodFiscale;
    private String vecchiaMatricola;
    private String vecchioTipo;
    private LocalDate vecchiaDataNascita;
    private boolean vecchioDirigente;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "DipendenteIndeterminato".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sui dipendenti indeterminati.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public DipendenteIndeterminato(Controller controller, JFrame frameChiamante, boolean modificaMode){
        // Inizializzazione dell'interfaccia grafica e dei gestori di eventi
        frame = new JFrame("Dipendente indeterminato");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(dipendenteIndeterminatoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Nasconde la finestra corrente, mostra la finestra chiamante e chiudi la finestra corrente
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Gestione del comportamento della checkBox relativa alla data di assunzione odierna
        dataAssunzioneOdiernaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Se la checkbox è selezionata, disabilita il campo data di assunzione e imposta la data odierna
                    dataAssunzioneTextField.setEnabled(false);
                    dataAssunzioneTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    // Se la checkbox è deselezionata, abilita il campo data di assunzione e cancella il testo
                    dataAssunzioneTextField.setEnabled(true);
                    dataAssunzioneTextField.setText("");
                }
            }
        });

        // Gestione del comportamento della checkBox relativa alla data di fine rapporto odierna
        dataFineOdiernaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Se la checkbox è selezionata, disabilita il campo data di fine e imposta la data odierna
                    dataFineTextField.setEnabled(false);
                    dataFineTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    // Se la checkbox è deselezionata, abilita il campo data di fine e cancella il testo
                    dataFineTextField.setEnabled(true);
                    dataFineTextField.setText("");
                }
            }
        });

        // Gestione del comportamento della checkBox relativa all'indirizzo
        indirizzoCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Se la checkbox è selezionata, abilita il campo indirizzo
                    indirizzoTextField.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    // Se la checkbox è deselezionata, disabilita il campo indirizzo e cancella il testo
                    indirizzoTextField.setEnabled(false);
                    indirizzoTextField.setText("");
                }
            }
        });

        // Gestione del comportamento della checkBox relativa alla data di fine rapporto
        dataFineCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Se la checkbox è selezionata, abilita il campo data di fine e la checkbox "Data Fine Odierna"
                    dataFineTextField.setEnabled(true);
                    dataFineOdiernaCheckBox.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    // Se la checkbox è deselezionata, disabilita il campo data di fine e la checkbox "Data Fine Odierna"
                    dataFineTextField.setEnabled(false);
                    dataFineTextField.setText("");
                    dataFineOdiernaCheckBox.setEnabled(false);
                }
            }
        });


        // Gestisce la modifica o l'aggiunta di un dipendente a tempo indeterminato
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validazione e gestione dei dati inseriti
                String nome;
                String cognome;
                String codFiscale;
                String matricola;
                LocalDate dataNascita;
                LocalDate dataAssunzione;
                LocalDate dataFine = null;
                String tipo = (String) tipoComboBox.getSelectedItem();
                String indirizzo = null;

                //ottengono i valori delle checkbox selected/deselected
                boolean checkedDirigente = dirigenteCheckBox.isSelected();
                boolean checkedIndirizzo = indirizzoCheckBox.isSelected();
                boolean checkedDataFine = dataFineCheckBox.isSelected();

                int casoMiddle = 0, casoSenior = 0;

                if ((nome = nomeTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il nome del dipendente!");

                } else if ((cognome = cognomeTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il cognome del dipendente!");

                } else if ((codFiscale = codFiscaleTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire il codice fiscale del dipendente!");

                } else if ((codFiscale.length() != 16) || (!controller.isAlphanumeric(codFiscale))) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un codice fiscale di esattamente 16 caratteri alfanumerici!");

                } else if ((matricola = matricolaTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire la matricola del dipendente!");

                } else if (matricola.length() != 8) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire una matricola di esattamente 8 caratteri!");

                } else if ((!controller.verifyDate(dataNascitaTextField.getText())) || (!controller.verifyDate(dataAssunzioneTextField.getText()))) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci una data con formattazione YYYY-MM-DD!");

                } else if (checkedDataFine && (!controller.verifyDate(dataFineTextField.getText()))) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci una data con formattazione YYYY-MM-DD!");

                } else if (checkedDataFine && controller.notCoerenzaDate(LocalDate.parse(dataNascitaTextField.getText()), LocalDate.parse(dataAssunzioneTextField.getText()), LocalDate.parse(dataFineTextField.getText()))){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Controllare la coerenza tra le date!");

                } else if (!checkedDataFine && controller.notCoerenzaDate(LocalDate.parse(dataNascitaTextField.getText()), LocalDate.parse(dataAssunzioneTextField.getText()), null)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Controllare la coerenza tra le date!");

                } else if ((tipo.equals("Junior")) && controller.verifyDateInterval( LocalDate.parse(dataAssunzioneTextField.getText()), "Junior" ) == 1) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! La data di assunzione specificata e' troppo poco recente per un dipendente di tipo \"Junior\"!");

                } else if (tipo.equals("Middle") &&
                        ((casoMiddle = controller.verifyDateInterval(LocalDate.parse(dataAssunzioneTextField.getText()), "Middle")) == 2 ||
                                (casoSenior = controller.verifyDateInterval(LocalDate.parse(dataAssunzioneTextField.getText()), "Middle")) == 3)) {

                    if (casoMiddle == 2) {

                        JOptionPane.showMessageDialog(frame, "Attenzione! La data di assunzione specificata e' troppo recente per un dipendente di tipo \"Middle\"!");

                    } else {

                        JOptionPane.showMessageDialog(frame, "Attenzione! La data di assunzione specificata e' troppo poco recente per un dipendente di tipo \"Middle\"!");
                    }

                } else if (tipo.equals("Senior") &&
                        (controller.verifyDateInterval(LocalDate.parse(dataAssunzioneTextField.getText()), "Senior") == 4)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! La data di assunzione specificata e' troppo recente per un dipendente di tipo \"Senior\"!");

                } else if (controller.checkIsDipendenteProgetto(codFiscale)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Questo dipendente e' gia' un dipendente a progetto!");

                } else if (!modificaMode && !controller.checkMatricolaUnica(vecchiaMatricola, matricola, modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! La matricola inserita e' gia' stata assegnata ad un altro dipendente!");

                } else if (!modificaMode && controller.coerenzaCodiceFiscale(vecchioNome, vecchioCognome, vecchioCodFiscale, vecchiaDataNascita,
                        codFiscale, nome, cognome, LocalDate.parse(dataNascitaTextField.getText()), modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Esiste un altro dipendente con lo stesso codice fiscale ma con dati anagrafici diversi!");

                } else if (!modificaMode && controller.isDipendenteIndeterminatoActive(modificaMode, codFiscale, vecchiaMatricola)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non e' possibile inserire un dipendente con una carriera attiva!");

                } else if (modificaMode && !controller.checkMatricolaUnica(vecchiaMatricola, matricola, modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! La matricola inserita e' gia' stata assegnata ad un altro dipendente!");

                } else if (modificaMode && controller.coerenzaCodiceFiscale(vecchioNome, vecchioCognome, vecchioCodFiscale, vecchiaDataNascita,
                        codFiscale, nome, cognome, LocalDate.parse(dataNascitaTextField.getText()), modificaMode)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Esiste un altro dipendente con lo stesso codice fiscale ma con dati anagrafici diversi!");

                } else if (modificaMode && checkedDataFine && controller.checkResponsabilitaDipendente(vecchiaMatricola)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non puoi impostare una data di fine rapporto per un dipendente con responsabilita' attive!");

                } else if (modificaMode && vecchioDirigente && !checkedDirigente && controller.checkResponsabilitaDipendente(vecchiaMatricola)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non puoi rimuovere un incarico dirigenziale per un dipendente con responsabilita' attive!");

                } else if (modificaMode && !tipo.equals(vecchioTipo) && controller.checkResponsabilitaDipendente(vecchiaMatricola)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non puoi cambiare il tipo di un dipendente con responsabilita' attive!");

                } else if (modificaMode && !checkedDataFine && controller.isDipendenteIndeterminatoActive(modificaMode, codFiscale, vecchiaMatricola)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Questa carriera non puo' tornare attiva, siccome il dipendente possiede gia' attualmente una carriera attiva!");

                } else {

                    dataNascita = LocalDate.parse(dataNascitaTextField.getText());
                    dataAssunzione = LocalDate.parse(dataAssunzioneTextField.getText());

                    if (!checkedDataFine && !checkedIndirizzo) {

                        //caso in cui si vuole registrare una modifica
                        if (modificaMode) {

                            try{
                                controller.modificaDipendenteIndeterminato(vecchiaMatricola, vecchioDirigente, nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException exception){
                                JOptionPane.showMessageDialog(frame,"Attenzione, " + exception);
                            }

                        } else { //caso di quando si vuole registrare un'aggiunta

                            try {
                                controller.aggiungiDipendenteIndeterminato(nome, cognome, codFiscale, matricola, tipo, dataNascita, dataAssunzione, checkedDirigente);
                            } catch (IllegalArgumentException illegalArgumentException) {
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }
                        }

                    } else if (checkedDataFine && !checkedIndirizzo) {

                        dataFine = LocalDate.parse(dataFineTextField.getText());

                        if (modificaMode) {

                            try{
                                controller.modificaDipendenteIndeterminato(vecchiaMatricola, vecchioDirigente, nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException exception){
                                JOptionPane.showMessageDialog(frame,"Attenzione, " + exception);
                            }

                        } else {

                            try {
                                controller.aggiungiDipendenteIndeterminato(nome, cognome, codFiscale, matricola, tipo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException illegalArgumentException) {
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }

                    } else if (!checkedDataFine && checkedIndirizzo) {

                        indirizzo = indirizzoTextField.getText();

                        if (modificaMode) {

                            try{
                                controller.modificaDipendenteIndeterminato(vecchiaMatricola, vecchioDirigente, nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException exception){
                                JOptionPane.showMessageDialog(frame,"Attenzione, " + exception);
                            }

                        } else {

                            try {
                                controller.aggiungiDipendenteIndeterminato(nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, checkedDirigente);
                            } catch (IllegalArgumentException illegalArgumentException) {
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }


                    } else {

                        dataFine = LocalDate.parse(dataFineTextField.getText());
                        indirizzo = indirizzoTextField.getText();

                        if (modificaMode) {

                            try{
                                controller.modificaDipendenteIndeterminato(vecchiaMatricola, vecchioDirigente, nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException exception){
                                JOptionPane.showMessageDialog(frame,"Attenzione, " + exception);
                            }

                        } else {

                            try {
                                controller.aggiungiDipendenteIndeterminato(nome, cognome, codFiscale, matricola, tipo, indirizzo, dataNascita, dataAssunzione, dataFine, checkedDirigente);
                            } catch (IllegalArgumentException illegalArgumentException) {
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }
                    }
                    // Chiusura del frame
                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaDipendenteIndeterminato" per aggiornare la tabella
                    callback.aggiornaAggiuntaModificaArea();

                    frame.dispose();
                }
            }
        });
    }

    /**
     * Nel caso della modifica, imposta i campi di interazione con i valori dell'istanza da modificare, selezionata precedentemente nell'apposita area.
     *
     * @param dati                              Lista di dati appartenenti all'istanza selezionata per la modifica, nella precedente area.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public void setField(Object[] dati) throws SQLException {
        // Imposta il nome
        nomeTextField.setText((String) dati[2]);
        setVecchioNome((String) dati[2]);

        // Imposta il cognome
        cognomeTextField.setText((String) dati[3]);
        setVecchioCognome((String) dati[3]);

        // Imposta il codice fiscale
        codFiscaleTextField.setText((String) dati[4]);
        setVecchioCodFiscale((String) dati[4]);

        // Imposta la matricola
        matricolaTextField.setText((String) dati[0]);
        setVecchiaMatricola((String) dati[0]);

        // Imposta il tipo
        for (String stored : new String[]{"Junior", "Middle", "Senior"}) {
            if (stored.contains(dati[1].toString())) {
                tipoComboBox.setSelectedItem(stored);
                setVecchioTipo(stored);
                break;
            }
        }

        // Imposta l'indirizzo se presente
        if (dati[5] == null) {
            indirizzoCheckBox.setSelected(false);
        } else {
            indirizzoCheckBox.setSelected(true);
            indirizzoTextField.setText((String) dati[5]);
        }

        // Imposta la data di nascita
        dataNascitaTextField.setText((String) dati[6]);
        setVecchiaDataNascita(LocalDate.parse((String) dati[6]));

        // Imposta la data di assunzione
        dataAssunzioneTextField.setText((String) dati[7]);

        // Imposta la data di fine se presente
        if (dati[8] == null) {
            dataFineCheckBox.setSelected(false);
        } else {
            dataFineCheckBox.setSelected(true);
            dataFineTextField.setText((String) dati[8]);
        }

        // Imposta il flag dirigente
        if (dati[9].toString().equals("t")) {
            dirigenteCheckBox.setSelected(true);
            setVecchioDirigente(true);
        } else {
            dirigenteCheckBox.setSelected(false);
            setVecchioDirigente(false);
        }
    }

    // Metodi setter per i dati vecchi

    /**
     * Imposta il nome dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioNome  {@link String}  Rappresenta il nome corrente dell'istanza da modificare
     * */
    public void setVecchioNome(String vecchioNome){
        this.vecchioNome = vecchioNome;
    }

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
     * Imposta il tipo di dipendente dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioTipo  {@link String}  Rappresenta il tipo di dipendente corrente dell'istanza da modificare
     * */
    public void setVecchioTipo(String vecchioTipo){
        this.vecchioTipo = vecchioTipo;
    }

    /**
     * Imposta la data di nascita dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaDataNascita  {@link LocalDate}  Rappresenta la data di nascita corrente dell'istanza da modificare
     * */
    public void setVecchiaDataNascita(LocalDate vecchiaDataNascita){
        this.vecchiaDataNascita = vecchiaDataNascita;
    }

    /**
     * Imposta lo stato dirigenziale dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioDirigente  {@link String}  Rappresenta lo stato dirigenziale corrente dell'istanza da modificare
     * */
    public void setVecchioDirigente(boolean vecchioDirigente){
        this.vecchioDirigente = vecchioDirigente;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback) { this.callback = callback; }
}
