package Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * La classe ”Progetto” rappresenta i progetti presi in carico dall’azienda.
 * In particolare il progetto possiedera' un CUP (Codice Unico Progetto),
 * un nome, una data di inizio e di fine esecuzione del progetto e il budget
 * totale (che rappresenta i fondi istanziati per quel progetto)
 * */
public class Progetto {
    private String cup;
    private String nome;
    private BigDecimal budget;
    private LocalDate dataInizio;
    private LocalDate dataFine = null;
    private DipendenteIndeterminato responsabile;
    private DipendenteIndeterminato referenteScientifico;


    //Associazione: rappresenta i dipendenti ingaggiati tramite il progetto
    private ArrayList<DipendenteProgetto> dipendentiIngaggiati = null;

    //Associazione: rappresenta i laboratori che lavorano al progetto
    private ArrayList<Laboratorio> laboratoriLavoranti = new ArrayList<>();

    //Associazione: rappresenta le attrezzature acquistate tramite il progetto
    private ArrayList<Attrezzatura> attrezzatureAcquistate = null;


    // COSTRUTTORI
    //viene specificata una dataFine
    /**
     * Costruisce un nuovo oggetto "Progetto" con le infromazioni fornite.
     *
     * @param cup                  {@link String}                    Il codice CUP del progetto, unico nel sistema.
     * @param nome                 {@link String}                    Il nome del progetto, unico nel sistema.
     * @param budget               {@link BigDecimal}                Il budget del progetto.
     * @param dataInizio           {@link LocalDate}                 La data di inizio del progetto.
     * @param dataFine             {@link LocalDate}                 La data di fine del progetto.
     * @param referenteScientifico {@link DipendenteIndeterminato}   Il referente scientifico del progetto.
     * @param responsabile         {@link DipendenteIndeterminato}   Il responsabile del progetto.
     */
    public Progetto(String cup, String nome, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, DipendenteIndeterminato referenteScientifico, DipendenteIndeterminato responsabile) {
        this.cup = cup;
        this.nome = nome;
        this.budget = budget;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;

        //Associazione (referente scientifico)
        this.referenteScientifico = referenteScientifico;

        //Associazione (responsabile)
        this.responsabile = responsabile;
    }

    //non viene specificata una dataFine
    /**
     * Costruisce un nuovo oggetto "Progetto" con le infromazioni fornite, non specificando una data di fine progetto, che sara' nulla.
     *
     * @param cup                  {@link String}                    Il codice CUP del progetto, unico nel sistema.
     * @param nome                 {@link String}                    Il nome del progetto, unico nel sistema.
     * @param budget               {@link BigDecimal}                Il budget del progetto.
     * @param dataInizio           {@link LocalDate}                 La data di inizio del progetto.
     * @param referenteScientifico {@link DipendenteIndeterminato}   Il referente scientifico del progetto.
     * @param responsabile         {@link DipendenteIndeterminato}   Il responsabile del progetto.
     */
    public Progetto(String cup, String nome, BigDecimal budget, LocalDate dataInizio, DipendenteIndeterminato referenteScientifico, DipendenteIndeterminato responsabile) {
        this.cup = cup;
        this.nome = nome;
        this.budget = budget;
        this.dataInizio = dataInizio;

        //Associazione (referente scientifico)
        this.referenteScientifico = referenteScientifico;

        //Associazione (responsabile)
        this.responsabile = responsabile;
    }

    // SETTERS
    /**
     * Imposta il codice CUP del progetto, unico nel sistema.
     *
     * @param cup {@link String}    Il codice CUP da impostare.
     */
    public void setCup (String cup) {
        this.cup = cup;
    }

    /**
     * Imposta il nome del progetto, unico nel sistema.
     *
     * @param nome {@link String} Il nome da impostare.
     */
    public void setNome (String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il budget del progetto.
     *
     * @param budget {@link BigDecimal} Il budget da impostare.
     */
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    /**
     * Imposta la data di inizio del progetto.
     *
     * @param dataInizio {@link LocalDate}  La data di inizio da impostare.
     */
    public void setDataInizio (LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * Imposta la data di fine del progetto.
     *
     * @param dataFine {@link LocalDate}    La data di fine da impostare.
     */
    public void setDataFine (LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * Imposta il responsabile del progetto.
     *
     * @param responsabile {@link DipendenteIndeterminato}  Il responsabile da impostare.
     */
    public void setResponsabile(DipendenteIndeterminato responsabile) {
        this.responsabile = responsabile;
    }

    /**
     * Imposta il referente scientifico del progetto.
     *
     * @param referenteScientifico  {@link DipendenteIndeterminato} Il referente scientifico da impostare.
     */
    public void setReferenteScientifico(DipendenteIndeterminato referenteScientifico) {
        this.referenteScientifico = referenteScientifico;
    }



    // GETTERS
    /**
     * Restituisce il codice CUP del progetto, unico nel sistema.
     *
     * @return {@link String} Il codice CUP del progetto.
     */
    public String getCup() {
        return cup;
    }

    /**
     * Restituisce il nome del progetto, unico nel sistema.
     *
     * @return {@link String} Il nome del progetto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il budget del progetto.
     *
     * @return {@link BigDecimal} Il budget del progetto.
     */
    public BigDecimal getBudget() {
        return budget;
    }

    /**
     * Restituisce la data di inizio del progetto.
     *
     * @return {@link LocalDate} La data di inizio del progetto.
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    /**
     * Restituisce la data di fine del progetto.
     *
     * @return {@link LocalDate} La data di fine del progetto.
     */
    public LocalDate getDataFine() {
        return dataFine;
    }

    /**
     * Restituisce il responsabile del progetto.
     *
     * @return {@link DipendenteIndeterminato} Il responsabile del progetto.
     */
    public DipendenteIndeterminato getResponsabile() {
        return responsabile;
    }

    /**
     * Restituisce il referente scientifico del progetto.
     *
     * @return {@link DipendenteIndeterminato} Il referente scientifico del progetto.
     */
    public DipendenteIndeterminato getReferenteScientifico() {
        return referenteScientifico;
    }

    /**
     * Restituisce la lista di tutti dipendenti ingaggiati dal progetto.
     *
     * @return {@link ArrayList<DipendenteIndeterminato>} Una lista di tutti i dipendenti ingaggiati.
     */
    public ArrayList<DipendenteProgetto> getDipendentiIngaggiati() {
        return dipendentiIngaggiati;
    }

    /**
     * Restituisce la lista di tutti i laboratori che lavorano attualmente al progetto.
     *
     * @return {@link ArrayList<Laboratorio>} Una lista di tutti i laboratori che lavorano attualmente al progetto.
     */
    public ArrayList<Laboratorio> getLaboratoriLavoranti() {
        return laboratoriLavoranti;
    }

    /**
     * Restituisce la lista di tutte le attrezzature acquistate dal progetto.
     *
     * @return {@link ArrayList<Attrezzatura>} Una lista di tutte le attrezzature acquistate dal progetto.
     */
    public ArrayList<Attrezzatura> getAttrezzature() {
        return attrezzatureAcquistate;
    }

    //ASSOCIAZIONE: Laboratorio - Progetto
    //Aggiunge un laboratorio a quelli che lavorano al progetto
    /**
     * Aggiunge un laboratorio tra quelli che lavorano al progetto.
     *
     * @param laboratorio {@link Laboratorio} Il laboratorio da aggiungere.
     * @return True se il laboratorio è stato aggiunto con successo, altrimenti False se gia' presente.
     */
    public boolean addLaboratorioLavorante(Laboratorio laboratorio) {

        boolean aggiunto = false;

        if (!this.laboratoriLavoranti.contains(laboratorio)) {

            this.laboratoriLavoranti.add(laboratorio);

            aggiunto = true;

        }

        return aggiunto;
    }

    //Rimuove un laboratorio da quelli che lavorano al progetto
    /**
     * Rimuove un laboratorio tra quelli che lavorano al progetto.
     *
     * @param laboratorio {@link Laboratorio}   Il laboratorio da rimuovere.
     * @return True se il laboratorio è stato rimosso con successo, altrimenti False se non presente.
     */
    public boolean removeLaboratorioLavorante(Laboratorio laboratorio) {

        boolean rimosso = false;

        if (this.laboratoriLavoranti.contains(laboratorio)) {

            this.laboratoriLavoranti.remove(laboratorio);

            rimosso = true;

        }

        return rimosso;
    }


    //ASSOCIAZIONE: Attrezzatura - Progetto
    //Aggiunge un'attrezzatura a quelle acquistate tramite progetto
    /**
     * Aggiunge un'attrezzatura tra quelle acquistate dal progetto.
     *
     * @param attrezzatura {@link Attrezzatura} L'attrezzatura da aggiungere.
     * @return True se l'attrezzatura è stata aggiunta con successo, altrimenti False se gia' presente.
     */
    public boolean addAttrezzaturaAcquistata(Attrezzatura attrezzatura) {

        boolean aggiunto = false;

        if (this.attrezzatureAcquistate == null)
            this.attrezzatureAcquistate = new ArrayList<Attrezzatura>();

        if (!this.attrezzatureAcquistate.contains(attrezzatura)) {

            this.attrezzatureAcquistate.add(attrezzatura);

            aggiunto = true;

        }

        return aggiunto;
    }

    //Rimuove un'attrezzatura da quelle acquistate tramite progetto
    /**
     * Rimuove un'attrezzatura tra quelle acquistate tramite il progetto.
     *
     * @param attrezzatura {@link Attrezzatura} L'attrezzatura da rimuovere.
     * @return True se l'attrezzatura è stata rimossa con successo, altrimenti False se non presente.
     */
    public boolean removeAttrezzaturaAcquistata(Attrezzatura attrezzatura) {

        boolean rimosso = false;

        if (this.attrezzatureAcquistate != null) {
            if (this.attrezzatureAcquistate.contains(attrezzatura)) {

                this.attrezzatureAcquistate.remove(attrezzatura); //Il GC deallocherà l'attrezzatura

                rimosso = true;

                if (this.attrezzatureAcquistate.isEmpty())
                    this.attrezzatureAcquistate = null;
            }
        }

        return rimosso;
    }


    //ASSOCIAZIONE: Dipendente a progetto - Progetto
    //Aggiunge un dipendente a quelli ingaggiati tramite progetto
    /**
     * Aggiunge un dipendente a progetto tra quelli ingaggiati dal progetto.
     *
     * @param contratto  {@link DipendenteProgetto}Il dipendente a progetto da aggiungere.
     * @return True se il dipendente è stato aggiunto con successo, altrimenti False se gia' presente.
     */
    public boolean addDipendenteIngaggiato(DipendenteProgetto contratto){

        boolean aggiunto = false;

        if (this.dipendentiIngaggiati == null)
            this.dipendentiIngaggiati = new ArrayList<>();

        if (!this.dipendentiIngaggiati.contains(contratto)) {

            this.dipendentiIngaggiati.add(contratto);

            aggiunto = true;

        }

        return aggiunto;
    }

    //Rimuove un dipendente da quelli ingaggiati tramite progetto
    /**
     * Rimuove un dipendente a progetto tra quelli ingaggiati dal progetto.
     *
     * @param contratto {@link DipendenteProgetto}  Il dpendente a progetto da rimuovere.
     * @return True se il dipendente è stato rimosso con successo, altrimenti False se non presente.
     */
    public boolean removeDipendenteIngaggiato(DipendenteProgetto contratto){

        boolean rimosso = false;

        if (this.dipendentiIngaggiati != null) {
            if (this.dipendentiIngaggiati.contains(contratto)) {

                this.dipendentiIngaggiati.remove(contratto); //Il GC deallocherà il dipendente a progetto

                rimosso = true;

                if (this.dipendentiIngaggiati.isEmpty())
                    this.dipendentiIngaggiati = null;
            }
        }

        return rimosso;
    }


    //OVERRIDE DI METODI EREDITATI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "Progetto" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi "cup", "nome", "budget", "dataInizio", "dataFine", "referenteScientifico", "responsabile".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "Progetto" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "Progetto" e i campi sono uguali all'istanza di progetto considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Progetto))
            return false;

        Progetto progetto = (Progetto) obj;

        if (!progetto.getCup().equals(this.getCup()))                                       return false;
        if (!progetto.getNome().equals(this.getNome()))                                     return false;
        if (!progetto.getBudget().equals(this.getBudget()))                                 return false;
        if (!progetto.getDataInizio().equals(this.getDataInizio()))                         return false;
        if (!progetto.getResponsabile().equals(this.getResponsabile()))                     return false;
        if (!progetto.getReferenteScientifico().equals(this.getReferenteScientifico()))     return false;

        if (!(progetto.getDataFine() == null && this.getDataFine() == null)) {
            if (this.getDataFine() == null) {
                if (!progetto.getDataFine().equals(this.getDataFine()))
                    return false;
            } else {
                if (!this.getDataFine().equals(progetto.getDataFine()))
                    return false;
            }
        }

        return true;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa del progetto invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "Progetto" considerato.
     * */
    @Override
    public String toString(){

        return String.format("Nome: %s%nCUP: %s%nBudget: %.2f%nData inizio: %s%nData fine: %s%nReferente scientifico: %s %s%nMatricola: %s%nResponsabile: %s %s%nMatricola: %s",
                nome, cup, budget, dataInizio, dataFine, referenteScientifico.getNome(), referenteScientifico.getCognome(), referenteScientifico.getMatricola(),
                responsabile.getNome(), responsabile.getCognome(), responsabile.getMatricola());
    }
}
