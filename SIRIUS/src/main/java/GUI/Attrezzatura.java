package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

import java.sql.SQLException;

/**
 * La classe "Attrezzatura" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un'attrezzatura.
 */
public class Attrezzatura {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JTextField descrizioneTextField;
    private JTextField costoTextField;
    private JCheckBox laboratorioCheckBox;
    private JPanel attrezzaturaPanel;
    private JLabel descrizioneLable;
    private JLabel costoLable;
    private JLabel progettoLable;
    private JLabel laboratorioLable;
    private JButton annullaButton;
    private JButton okButton;
    private JComboBox<String> laboratorioComboBox;
    private JComboBox<String> progettoComboBox;
    private JButton impostaButton;
    private String vecchioId;
    private String vecchioProgetto;
    private String vecchioLaboratorio = null;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "Attrezzatura".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle attrezzature.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public Attrezzatura(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {

        frame = new JFrame("Attrezzatura");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(attrezzaturaPanel);
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
                // Nasconde la finestra corrente
                frame.setVisible(false);

                // Rende visibile la finestra chiamante
                frameChiamante.setVisible(true);

                // Rilascia le risorse della finestra corrente
                frame.dispose();
            }
        });

        // Gestione dell'azione del pulsante "Imposta"
        impostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultComboBoxModel<String> modelAggiornato = null;

                try {
                    // Recupera e imposta il modello aggiornato per la ComboBox dei laboratori basato sulla selezione del progetto
                    modelAggiornato = new DefaultComboBoxModel<>(controller.recuperaLaboratoriLavoranti((String) progettoComboBox.getSelectedItem()));
                    laboratorioComboBox.setModel(modelAggiornato);
                } catch (NullPointerException nullPointerException){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono laboratori lavoranti al progetto specificato nel database!");
                }
            }
        });

        // Gestisce la modifica o l'aggiunta di un'attrezzatura
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String descrizione;
                BigDecimal costo;
                String progettoProprietario = (String) progettoComboBox.getSelectedItem();
                String laboratorio = (String) laboratorioComboBox.getSelectedItem();

                boolean checkedLaboratorio = laboratorioCheckBox.isSelected();

                if ((descrizione = descrizioneTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire una descrizione!");

                } else if (!controller.verifyBigDecimal(costoTextField.getText())) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un costo corretto!");

                } else if ((costo = new BigDecimal(costoTextField.getText())).compareTo(BigDecimal.valueOf(0)) < 0){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un costo positivo o nullo!");

                } else if (progettoProprietario == null || progettoProprietario.isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire prima un progetto nel database!");

                } else if (checkedLaboratorio && (laboratorio == null || laboratorio.isEmpty())){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono laboratori candidati che lavorano al progetto selezionato!");

                } else if (controller.checkAcquistoAttrezzaturaHalfBudget(modificaMode, costo, progettoProprietario, vecchioId)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! L'attrezzatura è troppo costosa per rientrare nella metà del budget!");

                } else {

                    if (laboratorioCheckBox.isSelected()){

                        if (modificaMode){

                            try {
                                // Modifica l'attrezzatura con i nuovi dati forniti
                                controller.modificaAttrezzatura(vecchioId, vecchioProgetto, vecchioLaboratorio, descrizione, costo, progettoProprietario, laboratorio);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        } else {
                            // Aggiunge una nuova attrezzatura con i dati forniti
                            controller.aggiungiAttrezzatura(descrizione, costo, progettoProprietario, laboratorio);
                        }

                    } else {

                        if (modificaMode){

                            try {
                                // Modifica l'attrezzatura con i nuovi dati forniti (senza specificare il laboratorio)
                                controller.modificaAttrezzatura(vecchioId, vecchioProgetto, vecchioLaboratorio, descrizione, costo, progettoProprietario);
                            } catch (IllegalArgumentException illegalArgumentException){
                                JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                            }

                        }else {
                            // Aggiunge una nuova attrezzatura con i dati forniti (senza specificare il laboratorio)
                            controller.aggiungiAttrezzatura(descrizione, costo, progettoProprietario);
                        }

                    }

                    // Nasconde la finestra corrente
                    frame.setVisible(false);

                    // Rende visibile la finestra chiamante
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaAttrezzatura" per aggiornare la tabella
                    callback.aggiornaAggiuntaModificaArea();

                    // Rilascia le risorse della finestra corrente
                    frame.dispose();
                }
            }
        });

        //maschera il campo laboratorio quando non e' spuntata la checkbox
        laboratorioCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    laboratorioComboBox.setEnabled(true);
                    impostaButton.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    laboratorioComboBox.setEnabled(false);
                    DefaultComboBoxModel<String> modelAggiornato = new DefaultComboBoxModel<>(new String[]{null});
                    laboratorioComboBox.setModel(modelAggiornato);
                    impostaButton.setEnabled(false);
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

        // Imposta il vecchio ID dell'oggetto
        setVecchioId((String) dati[0]);

        // Imposta la descrizione nel campo di testo
        descrizioneTextField.setText((String) dati[1]);

        // Imposta il costo nel campo di testo
        costoTextField.setText((String) dati[2]);

        // Itera attraverso i progetti disponibili e seleziona quello corrispondente al dato preesistente
        for (String stored : controller.recuperaProgetti()){
            if (stored.contains(dati[4].toString())) {
                progettoComboBox.setSelectedItem(stored);
                setVecchioProgetto(dati[4].toString());
            }
        }

        // Se il campo dati[3] (laboratorio) non è nullo, abilita la casella di controllo "laboratorioCheckBox"
        if (dati[3] != null) {

            laboratorioCheckBox.setSelected(true);

            // Simula un click sul pulsante "Imposta" per caricare i laboratori disponibili per il progetto selezionato
            impostaButton.doClick();

            // Itera attraverso i laboratori disponibili e seleziona quello corrispondente al dato preesistente
            for (String stored : controller.recuperaLaboratori()) {
                if (stored.contains(dati[3].toString())) {
                    laboratorioComboBox.setSelectedItem(stored);
                    setVecchioLaboratorio(dati[3].toString());
                }
            }
        }
    }

    /**
     * Imposta l'identificativo dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioId  {@link Integer}  Rappresenta l'identificativo corrente dell'istanza da modificare
     * */
    public void setVecchioId(String vecchioId){
        this.vecchioId = vecchioId;
    }

    /**
     * Imposta il progetto proprietario dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioProgetto  {@link String}  Rappresenta il progetto proprietario corrente dell'istanza da modificare
     * */
    public void setVecchioProgetto(String vecchioProgetto){
        this.vecchioProgetto = vecchioProgetto;
    }

    /**
     * Imposta il laboratorio dove risiede l'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioLaboratorio  {@link String}  Rappresenta il laboratorio corrente dove risiede l'istanza da modificare
     * */
    public void setVecchioLaboratorio(String vecchioLaboratorio){
        this.vecchioLaboratorio = vecchioLaboratorio;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}
