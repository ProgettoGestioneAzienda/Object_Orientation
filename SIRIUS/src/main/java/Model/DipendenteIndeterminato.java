package Model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * La classe ”Dipendente a tempo indeterminato” rappresenta i dipendenti
 * assunti con contratto a tempo indeterminato dall’azienda. Eredita da
 * ”Dipendente” variabili di istanza e metodi di istanza. Agli attributi di ”Dipendente”, si aggiunge la data di
 * fine rapporto (ovvero la data in cui eventualmente il dipendente smette di prestare
 * servizio nell’azienda), un attributo "tipo" che specifica l’anzianita' del
 * dipendente (ovvero se e' un dipendente Junior, Middle o Senior) e un
 * attributo che indica se il dipendente e' attualmente un dirigente o meno.
 * */
public class DipendenteIndeterminato extends Dipendente {

    private TipoDipendente tipoDipendente = TipoDipendente.Junior;
    private boolean dirigente = false;
    private LocalDate dataFineRapporto = null;


    //Associazione: rappresenta la lista degli scatti effettuati dal dipendente
    private ArrayList<ScattoCarriera> scattiEffettuati = null;

    //Associazione: rappresenta la lista dei laboratori a cui il dipendente afferisce
    private ArrayList<Laboratorio> laboratoriAfferiti = null;

    //Associazione: rappresenta la lista dei laboratori di cui è responsabile scientifico
    private ArrayList<Laboratorio> laboratoriResponsabileScientifico = null;

    //Associazione: rappresenta la lista dei progetti di cui è referente scientifico
    private ArrayList<Progetto> progettiReferenteScientifico = null;

    //Associazione: rappresenta la lista dei progetti di cui è responsabile
    private ArrayList<Progetto> progettiResponsabile = null;



    // COSTRUTTORI
    //ci saranno 4 possibili costruttori al fine di coprire tutti i casi, in quanto l'indirizzo e la dataFine possono essere nullabili.

    //no indirizzo, no dataFine
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" con le infromazioni fornite, non specificando l'indirizzo e la data di fine rapporto, che saranno nulli.
     *
     * @param nome              {@link String}          Rappresenta il nome del dipendente.
     * @param cognome           {@link String}          Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}          Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}          Rappresenta la matricola del dipendente, unica nel sistema.
     * @param tipoDipendente    {@link TipoDipendente}  Rappresenta il tipo del dipendente, in base all'anzianita' di servizio.
     * @param dataNascita       {@link LocalDate}       Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}       Rappresenta la data di assunzione del dipendente.
     * @param dirigente                                 True se e' un dirigente, false altrimenti.
     * */
    public DipendenteIndeterminato (String nome, String cognome, String codFiscale, String matricola, TipoDipendente tipoDipendente, LocalDate dataNascita, LocalDate dataAssunzione, boolean dirigente){

        super(nome, cognome, codFiscale, matricola, dataNascita, dataAssunzione);
        this.tipoDipendente = tipoDipendente;
        this.dirigente = dirigente;
    }

    //si indirizzo, no dataFine
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" con le infromazioni fornite, non specificando la data di fine rapporto, che sara' nulla.
     *
     * @param nome              {@link String}          Rappresenta il nome del dipendente.
     * @param cognome           {@link String}          Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}          Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}          Rappresenta la matricola del dipendente, unica nel sistema.
     * @param tipoDipendente    {@link TipoDipendente}  Rappresenta il tipo del dipendente, in base all'anzianita' di servizio.
     * @param indirizzo         {@link String}          Rappresenta l'indirizzo di residenza del dipendente.
     * @param dataNascita       {@link LocalDate}       Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}       Rappresenta la data di assunzione del dipendente.
     * @param dirigente                                 True se e' un dirigente, false altrimenti.
     * */
    public DipendenteIndeterminato (String nome, String cognome, String codFiscale, String matricola, TipoDipendente tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, boolean dirigente) {

        super(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione);
        this.tipoDipendente = tipoDipendente;
        this.dirigente = dirigente;
    }

    //no indirizzo, si dataFine
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" con le infromazioni fornite, non specificando l'indirizzo, che sara' nullo.
     *
     * @param nome              {@link String}          Rappresenta il nome del dipendente.
     * @param cognome           {@link String}          Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}          Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}          Rappresenta la matricola del dipendente, unica nel sistema.
     * @param tipoDipendente    {@link TipoDipendente}  Rappresenta il tipo del dipendente, in base all'anzianita' di servizio.
     * @param dataNascita       {@link LocalDate}       Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}       Rappresenta la data di assunzione del dipendente.
     * @param dataFineRapporto  {@link LocalDate}       Rappresenta la data di fine rapporto del dipendente.
     * @param dirigente                                 True se e' un dirigente, false altrimenti.
     * */
    public DipendenteIndeterminato (String nome, String cognome, String codFiscale, String matricola, TipoDipendente tipoDipendente, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFineRapporto, boolean dirigente) {

        super(nome, cognome, codFiscale, matricola, dataNascita, dataAssunzione);
        this.tipoDipendente = tipoDipendente;
        this.dataFineRapporto = dataFineRapporto;
        this.dirigente = dirigente;
    }

    //si indirizzo, si dataFine
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" con le infromazioni fornite.
     *
     * @param nome              {@link String}          Rappresenta il nome del dipendente.
     * @param cognome           {@link String}          Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}          Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}          Rappresenta la matricola del dipendente, unica nel sistema.
     * @param tipoDipendente    {@link TipoDipendente}  Rappresenta il tipo del dipendente, in base all'anzianita' di servizio.
     * @param indirizzo         {@link String}          Rappresenta l'indirizzo di residenza del dipendente.
     * @param dataNascita       {@link LocalDate}       Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}       Rappresenta la data di assunzione del dipendente.
     * @param dataFine  {@link LocalDate}               Rappresenta la data di fine rapporto del dipendente.
     * @param dirigente                                 True se e' un dirigente, false altrimenti.
     * */
    public DipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola, TipoDipendente tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente){

        super(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione);
        this.tipoDipendente = tipoDipendente;
        this.dataFineRapporto = dataFine;
        this.dirigente = dirigente;
    }

    // SETTERS
    /**
     * Imposta il tipo del dipendente, in base all'anzianita' di servizio.
     *
     * @param tipoDipendente {@link TipoDipendente}    Puo' essere Junior, Middle o Senior, in base all'anzianita' di servizio.
     * */
    public void setTipoDipendente (TipoDipendente tipoDipendente) { //Posso settare il tipo solo tramite l'aggiunta di uno scatto di carriera
        this.tipoDipendente = tipoDipendente;
    }

    /**
     * Imposta lo stato dirigenziale del dipendente.
     *
     * @param dirigente True se il dipendente e' un dirigente, altrimenti false.
     * */
    public void setDirigente (boolean dirigente) { //Posso settare il dirigente solo tramite l'aggiunta di uno scatto di carriera
        this.dirigente = dirigente;
    }

    /**
     * Imposta la data di fine rapporto del dipendente.
     *
     * @param dataFineRapporto {@link LocalDate}    Rappresenta la data di fine rapporto del dipendente.
     * */
    public void setDataFineRapporto (LocalDate dataFineRapporto) {
        this.dataFineRapporto = dataFineRapporto;
    }

    /**
     * Imposta tutti gli scatti di carriera effettuati dal dipendente.
     *
     * @param scattiEffettuati {@link ArrayList<ScattoCarriera>}    Lista di tutti gli scatti di carriera effettuati dal dipendente.
     * */
    public void setScattiEffettuati(ArrayList<ScattoCarriera> scattiEffettuati) { this.scattiEffettuati = scattiEffettuati; }

    /**
     * Imposta tutti i laboratori afferiti dal dipendente.
     *
     * @param laboratoriAfferiti {@link ArrayList<Laboratorio>}    Lista di tutti i laboratori afferiti dal dipendente.
     * */
    public void setLaboratoriAfferiti(ArrayList<Laboratorio> laboratoriAfferiti) { this.laboratoriAfferiti = laboratoriAfferiti; }

    /**
     * Imposta tutti i laboratori di cui il dipendente e' responsabile scientifico.
     *
     * @param laboratoriResponsabileScientifico {@link ArrayList<Laboratorio>}    Lista di tutti i laboratori di cui il dipendente e' responsabile scientifico.
     * */
    public void setLaboratoriResponsabileScientifico(ArrayList<Laboratorio> laboratoriResponsabileScientifico) {
        this.laboratoriResponsabileScientifico = laboratoriResponsabileScientifico;
    }

    /**
     * Imposta tutti i progetti di cui il dipendente e' referente scientifico.
     *
     * @param progettiReferenteScientifico {@link ArrayList<Progetto>}    Lista di tutti i progetti di cui il dipendente e' referente scientifico.
     * */
    public void setProgettoReferenteScientifico(ArrayList<Progetto> progettiReferenteScientifico) {
        this.progettiReferenteScientifico = progettiReferenteScientifico;
    }

    /**
     * Imposta tutti i progetti di cui il dipendente e' responsabile.
     *
     * @param progettiResponsabile {@link Integer}    Lista di tutti i progetti di cui il dipendente e' responsabile.
     * */
    public void setProgettiResponsabile(ArrayList<Progetto> progettiResponsabile) {

        this.progettiResponsabile = progettiResponsabile;
    }

    // GETTERS
    /**
     * Restituisce il tipo del dipendente, il base all'anzianita' di servizio.
     *
     * @return {@link TipoDipendente}  Il tipo del dipendente.
     * */
    public TipoDipendente getTipoDipendente(){
        return tipoDipendente;
    }

    /**
     * Restituisce la data di fine rapporto del dipendente, vincolata allo specifico contratto.
     *
     * @return {@link LocalDate}  La data di fine rapporto contrattuale del dipendente.
     * */
    public LocalDate getDataFineRapporto(){
        return dataFineRapporto;
    }

    /**
     * Restituisce lo stato dirigenziale del dipendente.
     *
     * @return True se il dipendente e' un dirigente, altrimenti false.
     * */
    public boolean getDirigente(){
        return dirigente;
    }

    /**
     * Restituisce tutti gli scatti di carriera effettuati dal dipendente.
     *
     * @return {@link ArrayList<ScattoCarriera>}  Lista di tutti gli scatti di carriera effettuati dal dipendente.
     * */
    public ArrayList<ScattoCarriera> getScattiEffettuati() {
        return scattiEffettuati;
    }

    /**
     * Restituisce tutti i laboratori afferiti dal dipendente.
     *
     * @return {@link ArrayList<Laboratorio>}  Lista di tutti i laboratori afferiti dal dipendente.
     * */
    public ArrayList<Laboratorio> getLaboratoriAfferiti() {
        return laboratoriAfferiti;
    }

    /**
     * Restituisce tutti i laboratori per i quali il dipendente e' responsabile scientifico.
     *
     * @return {@link ArrayList<Laboratorio>}  Lista di tutti i laboratori per i quali il dipendente e' responsabile scientifico.
     * */
    public ArrayList<Laboratorio> getLaboratoriResponsabileScientifico() {
        return laboratoriResponsabileScientifico;
    }

    /**
     * Restituisce tutti i progetti per i quali il dipendente e' referente scientifico.
     *
     * @return {@link ArrayList<Progetto>}  Lista di tutti i progetti per i quali il dipendente e' referente scientifico.
     * */
    public ArrayList<Progetto> getProgettiReferenteScientifico() {
        return progettiReferenteScientifico;
    }

    /**
     * Restituisce tutti i progetti per i quali il dipendente e' responsabile.
     *
     * @return {@link ArrayList<Progetto>}  Lista di tutti i progetti per i quali il dipendente e' responsabile.
     * */
    public ArrayList<Progetto> getProgettiResponsabile() {
        return progettiResponsabile;
    }

    // METODI
    //ASSOCIAZIONE: Laboratorio - Dipendente indeterminato (Afferire)
    //Aggiunge l'afferenza a un laboratorio
    /**
     * Aggiunge un laboratorio alla lista di tutti i laboratori afferiti dal dipendente.
     *
     * @param lab   {@link Laboratorio}     Il nuovo laboratorio a cui afferira' il dipendente.
     * @return True se l'afferenza viene registrata, altrimenti false se gia' presente.
     * */
    public boolean addAfferenzaLaboratorio (Laboratorio lab) {

        boolean aggiunto = false;

        if (this.laboratoriAfferiti == null)
            this.laboratoriAfferiti = new ArrayList<Laboratorio>();

        if (!this.laboratoriAfferiti.contains(lab)) {

            this.laboratoriAfferiti.add(lab);

            aggiunto = true;
        }

        return aggiunto;
    }

    //Rimuove l'afferenza a un laboratorio
    /**
     * Rimuove un laboratorio alla lista di tutti i laboratori afferiti dal dipendente.
     *
     * @param lab   {@link Laboratorio}     Il laboratorio a cui il dipendente smettera' di afferire.
     * @return True se l'afferenza viene rimossa, altrimenti false se l'afferenza non esiste.
     * */
    public boolean removeAfferenzaLaboratorio (Laboratorio lab) {

        boolean rimosso = false;

        if (this.laboratoriAfferiti != null) {
            if (this.laboratoriAfferiti.contains(lab)) { //Se il laboratorio esiste nell'elenco di laboratori afferiti

                this.laboratoriAfferiti.remove(lab);

                rimosso = true;
            }

            if (this.laboratoriAfferiti.isEmpty())
                this.laboratoriAfferiti = null;
        }

        return rimosso;
    }

    /**
     * Aggiunge uno scatto di carriera alla lista di tutti gli scatti di carriera effettuati dal dipendente.
     *
     * @param scatto   {@link ScattoCarriera}     Il nuovo scatto di carriera effettuato dal dipendente.
     * @return True se lo scatto viene registrata, altrimenti false se gia' presente.
     * */
    public boolean addScattoCarriera (ScattoCarriera scatto) {

        boolean aggiunto = false;

        if (this.scattiEffettuati == null)
            this.scattiEffettuati = new ArrayList<ScattoCarriera>();

        if (!this.scattiEffettuati.contains(scatto)) {

            this.scattiEffettuati.add(scatto);

            aggiunto = true;
        }

        return aggiunto;
    }

    /**
     * Rimuove uno scatto di carriera dalla lista di tutti gli scatti di carriera effettuati dal dipendente.
     *
     * @param scatto   {@link ScattoCarriera}     Lo scatto di carriera da eliminare del dipendente.
     * @return True se lo scatto viene rimosso, altrimenti false se lo scatto non esiste.
     * */
    public boolean removeScattoCarriera (ScattoCarriera scatto) {

        boolean rimosso = false;

        if (this.scattiEffettuati != null) {
            if (this.scattiEffettuati.contains(scatto)) { //Se lo scatto esiste nell'elenco di scatti effettuati

                this.scattiEffettuati.remove(scatto); //Il GC deallocherà lo scatto

                rimosso = true;
            }

            if (this.scattiEffettuati.isEmpty())
                this.scattiEffettuati = null;
        }

        return rimosso;
    }


    //ASSOCIAZIONE: Laboratorio - Dipendente indeterminato (responsabile scientifico)
    //Aggiunge il dipendente come responsabile scientifico
    /**
     * Aggiunge un laboratorio alla lista di tutti i laboratori dei quali il dipendente e' responsabile scientifico.
     *
     * @param laboratorio   {@link Laboratorio}     Il nuovo laboratorio di cui il dipendente sara' responsabile scientifico.
     * @return True se la responsabilita' viene registrata, altrimenti false se gia' presente.
     * */
    public boolean addLaboratorioResponsabileScientifico(Laboratorio laboratorio) {

        boolean aggiunto = false;

        if (this.laboratoriResponsabileScientifico == null)
            this.laboratoriResponsabileScientifico = new ArrayList<Laboratorio>();

        if (!this.laboratoriResponsabileScientifico.contains(laboratorio)) {

            this.laboratoriResponsabileScientifico.add(laboratorio);

            aggiunto = true;
        }

        return aggiunto;
    }

    //Rimuove il dipendente da responsabile scientifico (per usare questo metodo devo prima averlo sostituito con un altro dipendente)
    /**
     * Rimuove un laboratorio dalla lista di tutti i laboratori di cui il dipendente e' responsabile scientifico.
     *
     * @param laboratorio   {@link Laboratorio}     Il laboratorio per cui il dipendente e' responsabile scientifico da eliminare.
     * @return True se la responsabilita' viene rimossa, altrimenti false se la responsabilita' non esiste.
     * */
    public boolean removeLaboratorioResponsabileScientifico(Laboratorio laboratorio) {

        boolean rimosso = false;

        if (this.laboratoriResponsabileScientifico != null) {
            if (this.laboratoriResponsabileScientifico.contains(laboratorio)) { //Se il laboratorio esiste nell'elenco di laboratori di cui sono responsabile scientifico

                this.laboratoriResponsabileScientifico.remove(laboratorio);

                rimosso = true;
            }

            if (this.laboratoriResponsabileScientifico.isEmpty())
                this.laboratoriResponsabileScientifico = null;
        }

        return rimosso;
    }


    //ASSOCIAZIONE: Progetto - Dipendente indeterminato (referente scientifico)
    //Aggiunge il dipendente come responsabile scientifico
    /**
     * Aggiunge un progetto alla lista di tutti i progetti dei quali il dipendente e' referente scientifico.
     *
     * @param progetto   {@link Progetto}     Il nuovo progetto di cui il dipendente sara' referente scientifico.
     * @return True se la responsabilita' viene registrata, altrimenti false se gia' presente.
     * */
    public boolean addProgettoReferenteScientifico(Progetto progetto) {

        boolean aggiunto = false;

        if (this.progettiReferenteScientifico == null)
            this.progettiReferenteScientifico = new ArrayList<Progetto>();

        if (!this.progettiReferenteScientifico.contains(progetto)) {

            this.progettiReferenteScientifico.add(progetto);

            aggiunto = true;
        }

        return aggiunto;
    }

    //Rimuove il dipendente da responsabile scientifico (per usare questo metodo devo prima averlo sostituito con un altro dipendente)
    /**
     * Rimuove un progetto dalla lista di tutti i progetti di cui il dipendente e' referente scientifico.
     *
     * @param progetto   {@link Progetto}     Il Progetto per cui il dipendente e' referente scientifico da eliminare.
     * @return True se la responsabilita' viene rimossa, altrimenti false se la responsabilita' non esiste.
     * */
    public boolean removeProgettoReferenteScientifico(Progetto progetto) {

        boolean rimosso = false;

        if (this.progettiReferenteScientifico != null) {
            if (this.progettiReferenteScientifico.contains(progetto)) { //Se il progetto esiste nell'elenco di progetti di cui sono referente scientifico

                this.progettiReferenteScientifico.remove(progetto);

                rimosso = true;
            }

            if (this.progettiReferenteScientifico.isEmpty())
                this.progettiReferenteScientifico = null;
        }

        return rimosso;
    }


    //ASSOCIAZIONE: Progetto - Dipendente indeterminato (responsabile)
    //Aggiunge il dipendente come responsabile scientifico
    /**
     * Aggiunge un progetto alla lista di tutti i progetti dei quali il dipendente e' responsabile.
     *
     * @param progetto   {@link Progetto}     Il nuovo progetto di cui il dipendente sara' responsabile.
     * @return True se la responsabilita' viene registrata, altrimenti false se gia' presente.
     * */
    public boolean addProgettoResponsabile(Progetto progetto) {

        boolean aggiunto = false;

        if (this.progettiResponsabile == null)
            this.progettiResponsabile = new ArrayList<Progetto>();

        if (!this.progettiResponsabile.contains(progetto)) {

            this.progettiResponsabile.add(progetto);

            aggiunto = true;
        }

        return aggiunto;
    }

    //Rimuove il dipendente da responsabile (per usare questo metodo devo prima averlo sostituito con un altro dipendente)
    /**
     * Rimuove un progetto dalla lista di tutti i progetti di cui il dipendente e' responsabile.
     *
     * @param progetto   {@link Progetto}     Il Progetto per cui il dipendente e' responsabile da eliminare.
     * @return True se la responsabilita' viene rimossa, altrimenti false se la responsabilita' non esiste.
     * */
    public boolean removeProgettoResponsabile(Progetto progetto) {

        boolean rimosso = false;

        if (this.progettiResponsabile != null) {
            if (this.progettiResponsabile.contains(progetto)) { //Se il progetto esiste nell'elenco di progetti di cui sono responsabile

                this.progettiResponsabile.remove(progetto);

                rimosso = true;
            }

            if (this.progettiResponsabile.isEmpty())
                this.progettiResponsabile = null;
        }

        return rimosso;
    }



    //OVERRIDE DI METODI EREDITATI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "DipendenteIndeterminato" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi ereditati dalla classe astratta estesa "Dipendente", in piu' ai campi "tipoDipendente", "dirigente" e "dataFineRapporto".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "DipendenteIndeterminato" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "DipendenteIndeterminato" e i campi sono uguali all'istanza di dipendente indeterminato considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        boolean val = super.equals(obj);

        if (val)
        {
            if (obj == this)
                return true;

            if (!(obj instanceof DipendenteIndeterminato))
                return false;

            DipendenteIndeterminato dipendente = (DipendenteIndeterminato) obj;


            if (!dipendente.getTipoDipendente().equals(this.getTipoDipendente()))       return false;
            if (dipendente.getDirigente() != this.getDirigente())                       return false;

            if (!(dipendente.getDataFineRapporto() == null && this.getDataFineRapporto() == null)) {
                if (this.getDataFineRapporto() == null) {
                    if (!dipendente.getDataFineRapporto().equals(this.getDataFineRapporto()))
                        return false;
                } else {
                    if (!this.getDataFineRapporto().equals(dipendente.getDataFineRapporto()))
                        return false;
                }

            }
        }

        return val;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa del dipendente indeterminato invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "DipendenteIndeterminato" considerato.
     * */
    @Override
    public String toString() {
        return String.format("Ruolo: %s%nTipo: %s%nDirigente: %b%nData fine rapporto: %s%n%s",
                this.getClass().getSimpleName(), tipoDipendente.toString(), dirigente, dataFineRapporto, super.toString());
    }
}
