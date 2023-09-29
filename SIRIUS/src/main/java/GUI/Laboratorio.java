package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

/**
 * La classe "Laboratorio" comprende l'interfaccia grafica per la finalizzazione di una registrazione o di una modifica di un laboratorio.
 */
public class Laboratorio {
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");

    public JFrame frame;
    private JTextField nomeTextField;
    private JTextField topicTextField;
    private JLabel nomeLable;
    private JComboBox responsabileScientificoComboBox;
    private JLabel topicLable;
    private JLabel responsabileScientificoLable;
    private JPanel laboratorioPanel;
    private JButton annullaButton;
    private JButton okButton;
    private String vecchioNome;
    private boolean modificaMode;
    private AggiornaArea callback;


    /**
     * Costruisce un'interfaccia grafica di finalizzazione "Laboratorio".
     *
     * @param controller     {@link Controller} Il controller per gestire le operazioni sui laboratori.
     * @param frameChiamante {@link JFrame}     Il frame chiamante da cui è stato aperto questo frame.
     * @param modificaMode                      True se si è in modalità modifica, False se si è in modalità aggiunta.
     * @throws SQLException                     Se si verifica un errore durante l'interazione con il database.
     */
    public Laboratorio(Controller controller, JFrame frameChiamante, boolean modificaMode) throws SQLException {

        frame = new JFrame("Laboratorio");
        frame.setIconImage(img_logo_lr.getImage());
        frame.setContentPane(laboratorioPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        this.modificaMode = modificaMode;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.recuperaAllDipendentiCandidatiResponsabileScientifico());
        responsabileScientificoComboBox.setModel(model);

        // Nasconde la finestra di interazione corrente, per rendere visibile la finestra chiamante relativa all'apposita area
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chiude la finestra corrente, riporta la finestra chiamante in primo piano e rilascia la risorsa della finestra corrente
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Gestisce la modifica o l'aggiunta di un laboratorio
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottiene i dati dai campi di input
                String nome;
                String topic;
                String responsabileScientifico = (String) responsabileScientificoComboBox.getSelectedItem();

                // Verifica e gestisce gli errori di input
                if ((nome = nomeTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un nome!");

                } else if (!controller.checkNomeLabUnico(nome)) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Il nome inserito è già stato registrato per un altro laboratorio!");

                } else if ((topic = topicTextField.getText()).isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserire un topic!");

                } else if (responsabileScientifico == null || responsabileScientifico.isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Attenzione! Inserisci prima un dipendente indeterminato di tipo \"Senior\"!");

                } else {

                    // Se tutto è corretto, aggiunge o modifica il laboratorio
                    if (modificaMode) {

                        try {
                            controller.modificaLaboratorio(vecchioNome, nome, topic, responsabileScientifico);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }

                    } else {

                        try {
                            controller.aggiungiLaboratorio(nome, topic, responsabileScientifico);
                        } catch (IllegalArgumentException illegalArgumentException){
                            JOptionPane.showMessageDialog(frame, "Attenzione! " + illegalArgumentException);
                        }
                    }
                    // Chiude la finestra corrente, riporta la finestra chiamante in primo piano e rilascia la risorsa della finestra corrente
                    frame.setVisible(false);
                    frameChiamante.setVisible(true);

                    // Permette di invocare il metodo sovrascritto dell'interfaccia, che a sua volta chiama un metodo di "AreaLaboratorio" per aggiornare la tabella
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
        nomeTextField.setText((String) dati[0]);
        setVecchioNome((String) dati[0]);

        // Imposta il campo Topic con il valore dal database
        topicTextField.setText((String) dati[1]);

        // Imposta la ComboBox Responsabile Scientifico con il valore dal database
        for (String stored : controller.recuperaAllDipendentiCandidatiResponsabileScientifico()) {
            if (stored.contains(dati[2].toString())) {
                responsabileScientificoComboBox.setSelectedItem(stored);
                break;
            }
        }
    }

    /**
     * Imposta il nome dell'istanza da modificare, selezionata nell'area.
     *
     * @param vecchioNome  {@link String}  Rappresenta il nome corrente dell'istanza da modificare
     * */
    public void setVecchioNome(String vecchioNome){
        this.vecchioNome = vecchioNome;
    }

    /**
     * Imposta il riferimento ad un ogetto, che permette di invocare un metodo nella GUI chiamante
     *
     * @param callback  {@link AggiornaArea}  Rappresenta il riferimento ad un ogetto anonimo, creato nell'apposita area.
     * */
    public void setAggiornaArea(AggiornaArea callback){
            this.callback = callback;
    }
}
