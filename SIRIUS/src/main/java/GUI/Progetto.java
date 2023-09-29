package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * La classe "Progetto" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un progetto.
 */
public class Progetto {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JLabel cupLable;
    private JTextField nomeTextField;
    private JTextField cupTextField;
    private JTextField budgetTextField;
    private JTextField dataInizioTextField;
    private JTextField dataFineTextField;
    private JLabel nomeLable;
    private JLabel budgetLable;
    private JLabel dataInizioLable;
    private JLabel dataFineLable;
    private JLabel referenteScientificoLable;
    private JLabel responsabileLable;
    private JPanel progettoPanel;
    private JComboBox referenteScientificoComboBox;
    private JComboBox responsabileComboBox;
    private JButton annullaButton;
    private JButton okButton;
    private JCheckBox dataFineCheckBox;
    private JCheckBox dataFineOdiernaCheckBox;
    private JButton dataFineImpostaButton;
    private String vecchioCup = null;
    private LocalDate vecchiaDataFine;
    private LocalDate vecchiaDataInizio;
    private String vecchioReferenteScientifico;
    private String vecchioResponsabile;
    private boolean modificaMode;
    private AggiornaArea callback;
    private boolean selezionataDataFine = false;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "Progetto".
     *
     * @param controller       Il controller per gestire le operazioni sui progetti.
     * @param frameChiamante   Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode     True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException    Se si verifica un errore durante l'interazione con il database.
     */
    public Progetto(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {

        frame = new JFrame("Progetto");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(progettoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Il bottone imposta recupera referenti scientifici e responsabili il cui periodo lavorativo e' coerente con la data di fine impostata
        dataFineImpostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selezionataDataFine = true;

                if (!controller.verifyDate(dataInizioTextField.getText())){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un formato valido per la data di inizio!");

                } else if(dataFineCheckBox.isSelected()){

                    if (!controller.verifyDate(dataFineTextField.getText())){

                        JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un formato valido per la data di fine!");

                    } else {

                        try {

                            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaAllDipendentiCandidatiReferenteScientifico(LocalDate.parse(dataInizioTextField.getText()), LocalDate.parse(dataFineTextField.getText())));
                            referenteScientificoComboBox.setModel(model);

                            model = new DefaultComboBoxModel<>(controller.recuperaAllDipendentiCandidatiDirigente(LocalDate.parse(dataInizioTextField.getText()), LocalDate.parse(dataFineTextField.getText())));
                            responsabileComboBox.setModel(model);

                            if (modificaMode){
                                // Imposta il referente scientifico del progetto
                                for (String stored : controller.recuperaAllDipendentiCandidatiReferenteScientifico(vecchiaDataInizio, vecchiaDataFine)) {
                                    if (stored.contains(vecchioReferenteScientifico)) {
                                        referenteScientificoComboBox.setSelectedItem(stored);
                                        break;
                                    }
                                }

                                // Imposta il responsabile del progetto
                                for (String stored : controller.recuperaAllDipendentiCandidatiDirigente(vecchiaDataInizio, vecchiaDataFine)) {
                                    if (stored.contains(vecchioResponsabile)) {
                                        responsabileComboBox.setSelectedItem(stored);
                                        break;
                                    }
                                }

                            }

                        } catch (NullPointerException nullPointerException) {
                            JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono dipendenti nel database!");
                        }
                    }

                } else {

                    try {

                        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaAllDipendentiCandidatiReferenteScientifico(LocalDate.parse(dataInizioTextField.getText()),null));
                        referenteScientificoComboBox.setModel(model);

                        model = new DefaultComboBoxModel<>(controller.recuperaAllDipendentiCandidatiDirigente(LocalDate.parse(dataInizioTextField.getText()), null));
                        responsabileComboBox.setModel(model);

                        if (modificaMode) {
                            // Imposta il referente scientifico del progetto
                            for (String stored : controller.recuperaAllDipendentiCandidatiReferenteScientifico(vecchiaDataInizio, null)) {
                                if (stored.contains(vecchioReferenteScientifico)) {
                                    referenteScientificoComboBox.setSelectedItem(stored);
                                    break;
                                }
                            }

                            // Imposta il responsabile del progetto
                            for (String stored : controller.recuperaAllDipendentiCandidatiDirigente(vecchiaDataInizio, null)) {
                                if (stored.contains(vecchioResponsabile)) {
                                    responsabileComboBox.setSelectedItem(stored);
                                    break;
                                }
                            }
                        }

                    } catch (NullPointerException nullPointerException) {
                        JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono dipendenti nel database!");
                    }
                }

            }
        });

        // Abilita o disabilita il campo di inserimento della data di fine del progetto, lo stesso succede per la checkBox per inserire la data odierna
        dataFineCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataFineOdiernaCheckBox.setEnabled(true);
                    dataFineTextField.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    dataFineTextField.setEnabled(false);
                    dataFineOdiernaCheckBox.setEnabled(false);
                    dataFineTextField.setText("");
                }
            }
        });

        // Imposta al valore odierno la datafine, oppure elimina il testo dall'apposito campo di inserimento
        dataFineOdiernaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataFineTextField.setEnabled(false);
                    dataFineTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    dataFineTextField.setEnabled(true);
                    dataFineTextField.setText("");
                }
            }
        });

        // Gestisce la modifica o l'aggiunta di un progetto
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome;
                String cup;
                BigDecimal budget;
                LocalDate dataInizio;
                LocalDate dataFine = null;
                String referenteScientifico = (String) referenteScientificoComboBox.getSelectedItem();
                String resposanbile = (String) responsabileComboBox.getSelectedItem();

                boolean checkedDataFine = dataFineCheckBox.isSelected();

                if (!selezionataDataFine){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Premere almeno una volta il il bottone \"Seleziona\" per non produrre risultati invalidi!");
                    selezionataDataFine = false;
                }
                // Verifica se il campo "Nome" è vuoto
                else if ((nome = nomeTextField.getText()).isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un nome!");
                }
                // Verifica che il campo "Nome" sia unico nel sistema
                else if (!controller.checkNomeProgettoUnico(nome)){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Esiste un altro progetto con lo stesso nome!");
                }
                // Verifica se il campo "CUP" è vuoto
                else if ((cup = cupTextField.getText()).isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un CUP!");
                }
                // Verifica se la lunghezza del CUP è corretta (15 caratteri)
                else if (cup.length() != 15) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Il CUP deve essere di esattamente 15 caratteri!");
                }
                // Verifica se il CUP è alfanumerico
                else if (!controller.isAlphanumeric(cup)) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Il CUP deve essere alfanumerico!");
                }
                // Verifica se il CUP è unico
                else if (!controller.checkCupUnico(vecchioCup, cup)) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Questo CUP è già stato registrato per un altro progetto!");
                }
                // Verifica se è stato selezionato un dipendente senior come referente scientifico e responsabile
                else if (referenteScientifico == null || resposanbile == null || referenteScientifico.isEmpty() || resposanbile.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un dipendente senior nel database!");
                }
                // Verifica se il campo "Budget" contiene un valore numerico valido
                else if (!controller.verifyBigDecimal(budgetTextField.getText())) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un budget valido!");
                }
                // Verifica se il budget è positivo o nullo
                else if ((budget = new BigDecimal(budgetTextField.getText())).compareTo(BigDecimal.valueOf(0)) < 0) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un budget positivo o nullo!");
                }
                // Verifica se il formato della data di inizio è valido
                else if (!controller.verifyDate(dataInizioTextField.getText())) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un formato valido per la data di inizio!");
                }
                // Verifica se il formato della data di fine è valido (se la checkbox "Data Fine" è selezionata)
                else if (checkedDataFine && !controller.verifyDate(dataFineTextField.getText())) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un formato valido per la data di fine!");
                }
                // Verifica se la data di fine è successiva alla data di inizio (se la checkbox "Data Fine" è selezionata)
                else if (checkedDataFine && controller.notCoerenzaDate(LocalDate.parse(dataInizioTextField.getText()), LocalDate.parse(dataFineTextField.getText()), null)) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! La data di fine progetto è più recente rispetto la data di inizio!");
                }
                // Verifica se si è in modalità modifica e il nuovo budget supera la metà del budget attuale
                else if (modificaMode && !controller.checkNewBudgetIsLegit(budget, vecchioCup)) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! Uno dei costi totali degli acquisti supera la metà del nuovo budget!");
                }
                // Verifica se si è in modalità modifica e la nuova data di fine è successiva alle scadenze contrattuali dei dipendenti a progetto
                else if (modificaMode && checkedDataFine && !controller.checkDataFineIsAfterAllScadenzaDipProgetto(LocalDate.parse(dataFineTextField.getText()), vecchioCup)) {
                    JOptionPane.showMessageDialog(frame, "Attenzione! La nuova data di fine del progetto è più recente della scadenza contrattuale di un dipendente a progetto!");
                } else {
                    dataInizio = LocalDate.parse(dataInizioTextField.getText());

                    if (checkedDataFine)
                        dataFine = LocalDate.parse(dataFineTextField.getText());

                    if (modificaMode) {
                        try {
                            controller.modificaProgetto(vecchioCup, nome, cup, budget, dataInizio, dataFine, referenteScientifico, resposanbile);
                        } catch(IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    } else {
                        if (!checkedDataFine) {
                            try {
                                controller.aggiungiProgetto(nome, cup, budget, dataInizio, referenteScientifico, resposanbile);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }
                        } else {
                            try {
                                controller.aggiungiProgetto(nome, cup, budget, dataInizio, dataFine, referenteScientifico, resposanbile);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }
                        }
                    }

                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaProgetto" per aggiornare la tabella
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

        // Imposta il nome del progetto
        nomeTextField.setText((String) dati[1]);

        // Imposta il codice CUP del progetto
        cupTextField.setText((String) dati[0]);
        setVecchioCup((String) dati[0]);

        // Imposta il budget del progetto
        budgetTextField.setText((String) dati[4]);

        // Imposta la data di inizio del progetto
        dataInizioTextField.setText((String) dati[2]);
        setVecchiaDataInizio(LocalDate.parse((String) dati[2]));

        // Verifica se esiste una data di fine per il progetto
        if (dati[3] == null) {
            dataFineCheckBox.setSelected(false);
            setVecchiaDataFine(null);
        } else {
            dataFineCheckBox.setSelected(true);
            dataFineTextField.setText((String) dati[3]);
            setVecchiaDataFine(LocalDate.parse(dati[3].toString()));
        }

        // Imposta il referente scientifico del progetto
        for (String stored : controller.recuperaAllDipendentiCandidatiReferenteScientifico(vecchiaDataInizio, vecchiaDataFine)) {
            if (stored.contains(dati[5].toString())) {
                setVecchioReferenteScientifico(dati[5].toString());
                break;
            }
        }

        // Imposta il responsabile del progetto
        for (String stored : controller.recuperaAllDipendentiCandidatiDirigente(vecchiaDataInizio, vecchiaDataFine)) {
            if (stored.contains(dati[6].toString())) {
                setVecchioResponsabile(dati[6].toString());
                break;
            }
        }

        
        dataFineImpostaButton.doClick();
    }

    /**
     * Imposta il cup dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioCup  {@link String}  Rappresenta la cup corrente dell'istanza da modificare
     * */
    public void setVecchioCup(String vecchioCup){ this.vecchioCup = vecchioCup; }

    /**
     * Imposta la data di fine dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaDataFine  {@link LocalDate}  Rappresenta la data di fine corrente dell'istanza da modificare
     * */
    public void setVecchiaDataFine(LocalDate vecchiaDataFine) { this.vecchiaDataFine = vecchiaDataFine; }

    /**
     * Imposta la data di inizio dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchiaDataInizio {@link LocalDate} Rappresenta la data di inizio corrente dell'istanza da modificare.
     * */
    public void setVecchiaDataInizio(LocalDate vecchiaDataInizio){this.vecchiaDataInizio = vecchiaDataInizio;}

    /**
     * Imposta il referente scientifico da modificare, selezionato nell'area.
     * 
     * @param vecchioReferenteScientifico {@link String} Rappresenta la matricola del referente scientifico dell'istanza da modificare.
     */
    public void setVecchioReferenteScientifico(String vecchioReferenteScientifico){this.vecchioReferenteScientifico = vecchioReferenteScientifico;}

    /**
     * Imposta il responsabile da modificare, selezionato nell'area.
     * 
     * @param vecchioResponsabile {@link String} Rappresenta la matricola del referente scientifico dell'istanza da modificare.
     */
    public void setVecchioResponsabile(String vecchioResponsabile){this.vecchioResponsabile = vecchioResponsabile;}

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}
