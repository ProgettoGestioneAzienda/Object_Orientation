package GUI;

import Controller.Controller;
import org.postgresql.util.PSQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;

/**
 * Questa classe rappresenta l'interfaccia primaria con l'utente, e fornisce la possibilita' di selezionare un'area di interesse, in base alle operazioni che l'utente desidera effettuare
 */
public class MainDashboard {

    private static JFrame frame;
    private static ImageIcon img_logo_lr = new ImageIcon(".//src//main//resources//logo_Sirius_lr.png");
    private static Dimension preferredFrameSize = new Dimension(1280,720);
    private JPanel dashboardPanel;
    private JPanel logoPanel;
    private ImageIcon img_logo_hr = new ImageIcon(".//src//main//resources//logo_Sirius_hr.png");
    private JLabel logoIcon;
    private JSplitPane splitPane;


    private JButton dipendentiIndeterminatiButton;
    private JButton dipendentiProgettoButton;
    private JButton scattiButton;
    private JButton progettiButton;
    private JButton laboratoriButton;
    private JButton attrezzatureButton;
    private JButton afferenzeButton;
    private JButton lavorareButton;
    private JMenuBar menuBar;
    private JMenu helpMenu;
    private JMenuItem exitMenuItem;

    /**
     * Costruisce l'interfaccia utente primaria, che visualizzera' un elenco di aree di interesse, disponibili per l'accesso.
     */
    public MainDashboard() throws SQLException{

        Controller controller = new Controller();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Definisco la dashboard laterale
        dashboardPanel = new JPanel(new GridLayout(8, 1, 0, 10));
        dashboardPanel.setBackground(new Color(50, 57, 73));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //dashboardPanel.setSize(new Dimension((int)(preferredFrameSize.getWidth()*0.30),(int) preferredFrameSize.getHeight()));

        //Creazione dei pulsanti nella dashboard laterale
        dipendentiIndeterminatiButton = new JButton("Area dipendenti indeterminati");
        dipendentiIndeterminatiButton.setBackground(new Color(249, 247, 247));
        dipendentiIndeterminatiButton.setBorderPainted(false);
        dipendentiIndeterminatiButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        dipendentiProgettoButton = new JButton("Area dipendenti a progetto");
        dipendentiProgettoButton.setBackground(new Color(249, 247, 247));
        dipendentiProgettoButton.setBorderPainted(false);
        dipendentiProgettoButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        scattiButton = new JButton("Area scatti di carriera");
        scattiButton.setBackground(new Color(249, 247, 247));
        scattiButton.setBorderPainted(false);
        scattiButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        progettiButton = new JButton("Area progetti");
        progettiButton.setBackground(new Color(249, 247, 247));
        progettiButton.setBorderPainted(false);
        progettiButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        laboratoriButton = new JButton("Area laboratori");
        laboratoriButton.setBackground(new Color(249, 247, 247));
        laboratoriButton.setBorderPainted(false);
        laboratoriButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        attrezzatureButton = new JButton("Area attrezzature");
        attrezzatureButton.setBackground(new Color(249, 247, 247));
        attrezzatureButton.setBorderPainted(false);
        attrezzatureButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        afferenzeButton = new JButton("Area afferenze");
        afferenzeButton.setBackground(new Color(249, 247, 247));
        afferenzeButton.setBorderPainted(false);
        afferenzeButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        lavorareButton = new JButton("Area lavori");
        lavorareButton.setBackground(new Color(249, 247, 247));
        lavorareButton.setBorderPainted(false);
        lavorareButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));

        //Aggiunta delle componenti al dashboardPanel
        dashboardPanel.add(dipendentiIndeterminatiButton);
        dashboardPanel.add(dipendentiProgettoButton);
        dashboardPanel.add(scattiButton);
        dashboardPanel.add(progettiButton);
        dashboardPanel.add(laboratoriButton);
        dashboardPanel.add(attrezzatureButton);
        dashboardPanel.add(afferenzeButton);
        dashboardPanel.add(lavorareButton);


        //Definisco la sezione del logo
        logoPanel = new JPanel();
        logoPanel.setBackground(new Color(27, 27, 30));
        logoPanel.setLayout(new BorderLayout());
        //logoPanel.setSize(new Dimension((int) preferredFrameSize.getWidth()-dashboardPanel.getWidth(), (int) preferredFrameSize.getHeight()-dashboardPanel.getHeight()));

        //Definisco l'immagine
        logoIcon = new JLabel("");
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        logoIcon.setIcon(new ImageIcon(img_logo_hr.getImage()));
        logoPanel.add(logoIcon, BorderLayout.CENTER);


        //Split panel
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dashboardPanel, logoPanel);
        splitPane.setDividerSize(2);
        splitPane.setDividerLocation((int)(frame.getWidth()*0.30));

        splitPane.setOneTouchExpandable(false);
        splitPane.setContinuousLayout(true);
        dashboardPanel.setMinimumSize(new Dimension((int)(frame.getWidth()*0.30), frame.getHeight()));
        logoPanel.setMinimumSize(new Dimension((int)(frame.getWidth()*0.35), frame.getHeight()));


        //Auto-resizable image
        logoPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int imgDimension = (int)(logoPanel.getWidth()*0.5);

                if (imgDimension>600)
                    imgDimension = 600;
                else if (imgDimension<350)
                    imgDimension = 350;

                // Ridimensiona l'immagine
                ImageIcon resizedImageIcon = new ImageIcon(img_logo_hr.getImage().getScaledInstance(imgDimension, imgDimension, Image.SCALE_SMOOTH));
                logoIcon.setIcon(resizedImageIcon);
            }
        });

        //creazione delle istanze del model a partire dal database;
        if (!controller.leggiDipendentiIndeterminati()) {
            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati degli scatti di carriera registrati nel database!");
            throw new IllegalArgumentException();
        }

        if (!controller.leggiLaboratori()) {

            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati dei laboratori registrati nel database!");
            throw new IllegalArgumentException();
        }

        if (!controller.leggiProgetti()) {

            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati dei progetti registrati nel database!");
            throw new IllegalArgumentException();
        }

        if (!controller.leggiDipendentiProgetto()) {

            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati dei dipendenti a progetto registrati nel database!");
            throw new IllegalArgumentException();
        }

        if (!controller.leggiAttrezzature()) {

            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati delle attrezzature registrate nel databse!");
            throw new IllegalArgumentException();
        }

        if (!controller.leggiLavorare()) {

            JOptionPane.showMessageDialog(frame, "Attenzione! Problema nella coerenza dei dati delle istanze di lavoro registrate nel database!");
            throw new IllegalArgumentException();

        }

        //configurazione del bottone per accedere alla sezione dei dipendenti indeterminati
        dipendentiIndeterminatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaDipendenteIndeterminato areaDipendenteIndeterminato = null;
                try {
                    areaDipendenteIndeterminato = new AreaDipendenteIndeterminato(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaDipendenteIndeterminato != null) {

                    areaDipendenteIndeterminato.frame.setVisible(true);
                    frame.setVisible(false);

                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        //configurazione del bottone per accedere alla sezione dei dipendenti a progetto
        dipendentiProgettoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaDipendenteProgetto areaDipendenteProgetto = null;

                try {
                    areaDipendenteProgetto = new AreaDipendenteProgetto(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaDipendenteProgetto != null) {

                    areaDipendenteProgetto.frame.setVisible(true);
                    frame.setVisible(false);

                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        scattiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaScattoCarriera areaScattoCarriera = null;

                try {
                    areaScattoCarriera = new AreaScattoCarriera(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaScattoCarriera != null) {

                    areaScattoCarriera.frame.setVisible(true);
                    frame.setVisible(false);

                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        progettiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaProgetto areaProgetto = null;

                try {
                    areaProgetto = new AreaProgetto(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaProgetto != null) {

                    areaProgetto.frame.setVisible(true);
                    frame.setVisible(false);

                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        laboratoriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaLaboratorio areaLaboratorio = null;

                try {
                    areaLaboratorio = new AreaLaboratorio(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaLaboratorio != null) {
                    areaLaboratorio.frame.setVisible(true);
                    frame.setVisible(false);
                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        attrezzatureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaAttrezzatura areaAttrezzatura = null;

                try{
                    areaAttrezzatura = new AreaAttrezzatura(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaAttrezzatura != null) {
                    areaAttrezzatura.frame.setVisible(true);
                    frame.setVisible(false);
                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        afferenzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaAfferenze areaAfferenze = null;

                try{
                    areaAfferenze = new AreaAfferenze(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaAfferenze != null) {
                    areaAfferenze.frame.setVisible(true);
                    frame.setVisible(false);
                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        lavorareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AreaLavorare areaLavorare = null;

                try{
                    areaLavorare = new AreaLavorare(controller, frame);

                } catch(IllegalArgumentException illegalArgumentException){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Autenticazione al database fallita! Controllare le credenziali e riprovare!");
                    illegalArgumentException.printStackTrace();

                } catch(Exception exception){

                    JOptionPane.showMessageDialog(frame, "Attenzione! Si e' verificato un errore, riprovare.");
                    exception.printStackTrace();
                }

                if (areaLavorare != null) {
                    areaLavorare.frame.setVisible(true);
                    frame.setVisible(false);
                } else JOptionPane.showMessageDialog(frame, "Attenzione! problema di inizializzazione interno. Non e' possibile mostrare questa finestra. Riprovare.");
            }
        });

        // Creazione della barra dei menu
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        exitMenuItem = new JMenuItem("Exit");
        helpMenu.add(exitMenuItem);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    /**
     * Rappresenta il punto di ingresso principale dell'applicazione.
     * Il metodo main avvia l'applicazione GUI e gestisce l'inizializzazione.
     * Se sono presenti errori di definizione delle tabelle nel database rispetto a quelle fornite,
     * verra' informato l'utente
     * */
    public static void main(String[] args) {
        frame = new JFrame("Home");
        frame.setBackground(new Color(25, 31, 46));
        frame.setPreferredSize(preferredFrameSize);
        frame.pack();
        try {
            frame.setContentPane(new MainDashboard().splitPane);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setIconImage(img_logo_lr.getImage());
            frame.setVisible(true);

        } catch (SQLException sqlException){
            JOptionPane.showMessageDialog(frame, "Sono presenti errori di definizioni di tabelle nel database sottostante! Utilizzare il materiale fornito!");
        }
    }
}