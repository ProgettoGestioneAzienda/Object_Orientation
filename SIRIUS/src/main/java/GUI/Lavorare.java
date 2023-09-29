package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

/**
 * La classe "Lavorare" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un'istanza di lavoro fra un progetto ed un laboratorio.
 */
public class Lavorare {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JComboBox progettoComboBox;
    private JPanel lavorarePanel;
    private JComboBox laboratorioComboBox;
    private JButton annullaButton;
    private JButton okButton;
    private JLabel progettoLabel;
    private JLabel laboratorioLabel;
    private JButton impostaButton;
    private String vecchioLaboratorio;
    private String vecchioProgetto;
    private boolean modificaMode;
    private AggiornaArea callback;

    /**
     * Costruisce un'interfaccia grafica di finalizzazione "Lavorare".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sulle istanze di lavoro.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public Lavorare(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {

        frame = new JFrame("Lavorare");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(lavorarePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaProgettiNonTerminati());
        progettoComboBox.setModel(model);

        model = new DefaultComboBoxModel<>(controller.recuperaLaboratori());
        laboratorioComboBox.setModel(model);


        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Il bottone imposta aggiorna i laboratori visualizzati nella Combobox, mostrando solo quelli che non lavorano attualmente al progetto specificato
        impostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel<String> modelAggiornato = null;

                try {
                    modelAggiornato = new DefaultComboBoxModel<>(controller.recuperaLaboratoriCandidati((String) progettoComboBox.getSelectedItem()));
                    laboratorioComboBox.setModel(modelAggiornato);
                } catch (NullPointerException nullPointerException){
                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono progetti nel database!");
                }
            }
        });

        // Gestisce la modifica o l'aggiunta di un'istanza di lavoro tra un progetto ed un laboratorio
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println((String) laboratorioComboBox.getSelectedItem());

                String progetto = (String) progettoComboBox.getSelectedItem();
                String laboratorio = (String) laboratorioComboBox.getSelectedItem();

                if (progetto == null || progetto.isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un progetto nel database!");

                } else if (laboratorio == null || laboratorio.isEmpty()){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Non ci sono laboratori candidati per il progetto selezionato!");

                } else if (controller.checkMaxLaboratoriLavoranti(modificaMode, progetto, vecchioProgetto)){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Il progetto ha raggiunto il massimo di laboratori lavoranti, ovvero 3!");

                } else {

                    if (modificaMode){

                        try {
                            controller.modificaLavoro(vecchioLaboratorio, vecchioProgetto, laboratorio, progetto);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }

                    } else {

                        try {
                            controller.aggiungiLavorare(progetto, laboratorio);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    }
                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaLavorare" per aggiornare la tabella
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
        // Imposta la selezione nel campo del progetto
        for (String stored : controller.recuperaProgettiNonTerminati()) {
            if (stored.contains(dati[0].toString())) {
                progettoComboBox.setSelectedItem(stored);
                setVecchioProgetto(dati[0].toString());
                break;
            }
        }

        // Imposta la selezione nel campo del laboratorio
        for (String stored : controller.recuperaLaboratori()) {
            if (stored.contains(dati[1].toString())) {
                laboratorioComboBox.setSelectedItem(stored);
                setVecchioLaboratorio(dati[1].toString());
                break;
            }
        }
    }

    /**
     * Imposta il laboratorio dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioLaboratorio  {@link String}  Rappresenta il laboratorio corrente dell'istanza da modificare
     * */
    public void setVecchioLaboratorio(String vecchioLaboratorio){ this.vecchioLaboratorio = vecchioLaboratorio; }

    /**
     * Imposta il progetto dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioProgetto  {@link String}  Rappresenta il progetto corrente dell'istanza da modificare
     * */
    public void setVecchioProgetto(String vecchioProgetto) { this.vecchioProgetto = vecchioProgetto; }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){ this.callback = callback; }
}
