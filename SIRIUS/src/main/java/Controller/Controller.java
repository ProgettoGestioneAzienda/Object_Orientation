package Controller;

import Model.*;
import ImplementazionePostgresDAO.*;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Questa classe funge da controller principale per la gestione delle operazioni dell'azienda.
 * Gestisce la creazione, la modifica e la gestione dei dipendenti, dei progetti, delle attrezzature,
 * dei laboratori e delle altre entità aziendali.
 */
public class Controller {

    private DipendenteIndeterminato dipendenteIndeterminato;
    private DipendenteIndeterminato responsabileScientifico;
    private DipendenteIndeterminato referenteScientifico;
    private DipendenteIndeterminato responsabile;
    private DipendenteProgetto dipendenteProgetto;
    private ScattoCarriera scattoCarriera;
    private ScattoCarriera scattoMiddle;
    private ScattoCarriera scattoSenior;
    private ScattoCarriera scattoPromossoDirigente;
    private ScattoCarriera scattoRimossoDirigente;
    private Laboratorio laboratorio;
    private Progetto progetto;
    private Attrezzatura attrezzatura;

    // ELENCHI
    private ArrayList<DipendenteIndeterminato> elencoAllDipendentiIndeterminati = null;
    private ArrayList<DipendenteProgetto> elencoAllDipendentiProgetto = null;
    private ArrayList<ScattoCarriera> elencoAllScattiCarriera = null;
    private ArrayList<Laboratorio> elencoAllLaboratori = null;
    private ArrayList<Progetto> elencoAllProgetti = null;
    private ArrayList<Attrezzatura> elencoAllAttrezzature = null;


    // METODI AGGIUNTA

    /**
     * Aggiunge un dipendente indeterminato con dati in inserimento (senza dataFine e senza indirizzo).
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param tipo           {@link String}     Il tipo del dipendente (es. "Senior").
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param dirigente                         Indica se il dipendente è un dirigente o meno.
     * @throws IllegalArgumentException Se il dipendente a tempo indeterminato è già registrato.
     */
    public void aggiungiDipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola,
                                                String tipo, LocalDate dataNascita,
                                                LocalDate dataAssunzione, boolean dirigente) throws IllegalArgumentException {

        //in memoria
        dipendenteIndeterminato = new DipendenteIndeterminato(nome, cognome, codFiscale, matricola, TipoDipendente.valueOf(tipo), dataNascita, dataAssunzione, dirigente);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteIndeterminatoToElenco(dipendenteIndeterminato)) {

            throw new IllegalArgumentException("Il dipendente a tempo indeterminato risulta già registrato!");

        } else {

            //nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            insertDipendenteIndeterminatoToDatabase(dipendenteIndeterminato);

            //calcola automaticamente gli scatti di carriera in base alla data di assunzione
            insertAutomaticScatti();
        }
    }

    /**
     * Aggiunge un dipendente indeterminato con dati in inserimento, compresi di indirizzo (senza dataFine).
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param tipo           {@link String}     Il tipo del dipendente (es. "Senior").
     * @param indirizzo      {@link String}     L'indirizzo del dipendente.
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param dirigente                         Indica se il dipendente è un dirigente o meno.
     * @throws IllegalArgumentException Se il dipendente a tempo indeterminato è già registrato.
     */
    public void aggiungiDipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola,
                                                String tipo, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione,
                                                boolean dirigente) throws IllegalArgumentException {
        //in memoria
        dipendenteIndeterminato = new DipendenteIndeterminato(nome, cognome, codFiscale, matricola, TipoDipendente.valueOf(tipo), indirizzo, dataNascita, dataAssunzione, dirigente);
        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteIndeterminatoToElenco(dipendenteIndeterminato)) {

            throw new IllegalArgumentException("Il dipendente a tempo indeterminato risulta già registrato!");

        } else {

            //nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            insertDipendenteIndeterminatoToDatabase(dipendenteIndeterminato);

            //calcola automaticamente gli scatti di carriera in base alla data di assunzione
            insertAutomaticScatti();
        }
    }

    /**
     * Aggiunge un dipendente indeterminato con dati in inserimento, compresi di dataFine (senza indirizzo).
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param tipo           {@link String}     Il tipo del dipendente (es. "Senior").
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param dataFine       {@link LocalDate}  La data di fine rapporto del dipendente.
     * @param dirigente                         Indica se il dipendente è un dirigente o meno.
     * @throws IllegalArgumentException Se il dipendente a tempo indeterminato è già registrato.
     */
    public void aggiungiDipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola,
                                                String tipo, LocalDate dataNascita, LocalDate dataAssunzione,
                                                LocalDate dataFine, boolean dirigente) throws IllegalArgumentException {

        //in memoria
        dipendenteIndeterminato = new DipendenteIndeterminato(nome, cognome, codFiscale, matricola, TipoDipendente.valueOf(tipo), dataNascita, dataAssunzione, dataFine, dirigente);
        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteIndeterminatoToElenco(dipendenteIndeterminato)) {

            throw new IllegalArgumentException("Il dipendente a tempo indeterminato risulta già registrato!");

        } else {

            //nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            insertDipendenteIndeterminatoToDatabase(dipendenteIndeterminato);

            //calcola automaticamente gli scatti di carriera in base alla data di assunzione
            insertAutomaticScatti();
        }
    }

    /**
     * Aggiunge un dipendente indeterminato con dati in inserimento, compresi indirizzo e dataFine.
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param tipo           {@link String}     Il tipo del dipendente (es. "Senior").
     * @param indirizzo      {@link String}     L'indirizzo del dipendente.
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param dataFine       {@link LocalDate}  La data di fine rapporto del dipendente.
     * @param dirigente                         Indica se il dipendente è un dirigente o meno.
     * @throws IllegalArgumentException Se il dipendente a tempo indeterminato è già registrato.
     */
    public void aggiungiDipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola,
                                                String tipo, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione,
                                                LocalDate dataFine, boolean dirigente) throws IllegalArgumentException {

        //in memoria
        dipendenteIndeterminato = new DipendenteIndeterminato(nome, cognome, codFiscale, matricola, TipoDipendente.valueOf(tipo), indirizzo, dataNascita, dataAssunzione, dataFine, dirigente);
        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteIndeterminatoToElenco(dipendenteIndeterminato)) {

            throw new IllegalArgumentException("Il dipendente a tempo indeterminato risulta già registrato!");

        } else {

            //nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            insertDipendenteIndeterminatoToDatabase(dipendenteIndeterminato);

            //calcola automaticamente gli scatti di carriera in base alla data di assunzione
            insertAutomaticScatti();
        }
    }

    /**
     * Aggiunge un dipendente a progetto con i dati in inserimento, compresi di indirizzo.
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param indirizzo      {@link String}     L'indirizzo del dipendente.
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param scadenza       {@link LocalDate}  La data di scadenza contrattuale del dipendente.
     * @param costo          {@link BigDecimal} Il costo dell'ingaggio del dipendente.
     * @param stringProgetto {@link String}     I dati del progetto che ha ingaggiato il dipendente.
     * @throws IllegalArgumentException Se il dipendente a progetto è già registrato.
     */
    public void aggiungiDipendenteProgetto(String nome, String cognome, String codFiscale, String matricola,
                                           String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione,
                                           LocalDate scadenza, BigDecimal costo, String stringProgetto) throws IllegalArgumentException {

        //ricava il progetto che ingaggia dall'elenco di tutti i progetti
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        //istanzia il dipendente a progetto con i dati forniti, per verificarne l'esistenza
        dipendenteProgetto = new DipendenteProgetto(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione, scadenza, costo, progetto);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteProgettoToElenco(dipendenteProgetto)) {

            throw new IllegalArgumentException("Il dipendente a progetto risulta gia' registrato!");

        } else {

            // Aggiunge l'acquisto del dipendente al progetto specifico.
            progetto.addDipendenteIngaggiato(dipendenteProgetto);

            // nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            // si inserisce anche il progetto proprietario, poiche' e' un dipendente a progetto
            insertDipendenteProgettoToDatabase(dipendenteProgetto, progetto);
        }
    }

    /**
     * Aggiunge un dipendente a progetto con dati in inserimento (senza indirizzo).
     *
     * @param nome           {@link String}     Il nome del dipendente.
     * @param cognome        {@link String}     Il cognome del dipendente.
     * @param codFiscale     {@link String}     Il codice fiscale del dipendente.
     * @param matricola      {@link String}     La matricola del dipendente, unica nel sistema.
     * @param dataNascita    {@link LocalDate}  La data di nascita del dipendente.
     * @param dataAssunzione {@link LocalDate}  La data di assunzione del dipendente.
     * @param scadenza       {@link LocalDate}  La data di scadenza contrattuale del dipendente.
     * @param costo          {@link BigDecimal} Il costo dell'ingaggio del dipendente.
     * @param stringProgetto {@link String}     I dati del progetto che ha ingaggiato il dipendente.
     * @throws IllegalArgumentException Se il dipendente a progetto è già registrato.
     */
    public void aggiungiDipendenteProgetto(String nome, String cognome, String codFiscale, String matricola,
                                           LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza,
                                           BigDecimal costo, String stringProgetto) throws IllegalArgumentException {

        // Ricava il progetto che ingaggia dall'elenco di tutti i progetti
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        //istanzia il dipendente a progetto con i dati forniti, per verificarne l'esistenza
        dipendenteProgetto = new DipendenteProgetto(nome, cognome, codFiscale, matricola, dataNascita, dataAssunzione, scadenza, costo, progetto);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addDipendenteProgettoToElenco(dipendenteProgetto)) {

            throw new IllegalArgumentException("Il dipendente a progetto risulta gia' registrato!");

        } else {

            // Aggiunge l'acquisto del dipendente al progetto specifico.
            progetto.addDipendenteIngaggiato(dipendenteProgetto);

            // nel caso in cui il dipendente non sia gia' registrato, inserisce quest'ultimo nel DB
            // si inserisce anche il progetto proprietario, poiche' e' un dipendente a progetto
            insertDipendenteProgettoToDatabase(dipendenteProgetto, progetto);
        }
    }

    /**
     * Aggiunge un'attrezzatura acquistata da un progetto e posseduta da un laboratorio specifico.
     *
     * @param descrizione       {@link String}      La descrizione dell'attrezzatura.
     * @param costo             {@link BigDecimal}  Il costo dell'attrezzatura.
     * @param stringProgetto    {@link String}      I dati del progetto che acquista l'attrezzatura.
     * @param stringLaboratorio {@link String}      I dati del laboratorio che possiede l'attrezzatura.
     * @throws IllegalArgumentException Se l'attrezzatura è già registrata.
     */
    public void aggiungiAttrezzatura(String descrizione, BigDecimal costo, String stringProgetto, String stringLaboratorio) throws IllegalArgumentException {

        // Ricava il progetto che acquista l'attrezzatura dall'elenco dei progetti
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Ricava il laboratorio possedente dell'attrezzatura dall'elenco dei laboratori
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratorio);

        // Crea una nuova istanza di attrezzatura, per verificarne l'esistenza
        attrezzatura = new Attrezzatura(idAttrezzaturaPiuGrandePlusOne(), descrizione, costo, progetto, laboratorio);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addAttrezzaturaToElenco(attrezzatura)) {

            throw new IllegalArgumentException("L'attrezzatura risulta gia' registrata!");

        } else {

            // Imposta l'acquisto dell'attrezzatura da parte del progetto
            progetto.addAttrezzaturaAcquistata(attrezzatura);

            // Imposta il possedimento dell'attrezzatura da parte del laboratorio
            laboratorio.addAttrezzatura(attrezzatura);

            // Registra la nuova attrezzatura acquistata nel database
            AttrezzaturaPostgresDAO attrezzaturaDB = new AttrezzaturaPostgresDAO();
            attrezzaturaDB.addAttrezzatura(descrizione, costo, progetto.getCup(), laboratorio.getNome());
        }
    }

    /**
     * Aggiunge un'attrezzatura acquistata da un progetto, senza specificare un laboratorio possedente.
     *
     * @param descrizione       {@link String}      La descrizione dell'attrezzatura.
     * @param costo             {@link BigDecimal}  Il costo dell'attrezzatura.
     * @param stringProgetto    {@link String}      I dati del progetto che acquista l'attrezzatura.
     * @throws IllegalArgumentException Se l'attrezzatura è già registrata.
     */
    public void aggiungiAttrezzatura(String descrizione, BigDecimal costo, String stringProgetto) throws IllegalArgumentException {

        // Ricava il progetto che acquista l'attrezzatura dall'elenco dei progetti
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Crea una nuova istanza di attrezzatura, per verificarne l'esistenza
        attrezzatura = new Attrezzatura(idAttrezzaturaPiuGrandePlusOne(), descrizione, costo, progetto);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addAttrezzaturaToElenco(attrezzatura)) {

            throw new IllegalArgumentException("L'attrezzatura risulta gia' registrata!");

        } else {

            // Imposta l'acquisto dell'attrezzatura da parte del progetto
            progetto.addAttrezzaturaAcquistata(attrezzatura);

            // Registra la nuova attrezzatura acquistata nel database
            AttrezzaturaPostgresDAO attrezzaturaDB = new AttrezzaturaPostgresDAO();
            attrezzaturaDB.addAttrezzatura(descrizione, costo, progetto.getCup(), null);
        }
    }

    /**
     * Calcola un nuovo ID per l'aggiunta di una nuova attrezzatura.
     * Il nuovo ID è uguale all'ID maggiore recuperato dall'elenco, aumentato di un'unita'.
     *
     * @return {@link Integer}  Il nuovo ID per l'attrezzatura.
     */
    public int idAttrezzaturaPiuGrandePlusOne() {

        int max = 0;

        // Scansione di tutte le attrezzature per trovare l'ID più grande.
        for (Attrezzatura attrezzatura : getElencoAllAttrezzature()) {
            if (attrezzatura.getIdAttrezzatura() > max)
                max = attrezzatura.getIdAttrezzatura();
        }

        // Restituisci il nuovo ID incrementato di uno rispetto all'ID più grande presente.
        return max + 1;
    }


    /**
     * Aggiunge un laboratorio con il nome, il topic e il responsabile scientifico specificati.
     *
     * @param nome                          {@link String}  Il nome del laboratorio.
     * @param topic                         {@link String}  Il topic del laboratorio.
     * @param stringResponsabileScientifico {@link String}  I dati del responsabile scientifico del laboratorio.
     * @throws IllegalArgumentException Se il laboratorio è già registrato.
     */
    public void aggiungiLaboratorio(String nome, String topic, String stringResponsabileScientifico) throws IllegalArgumentException {

        //  Ricava il responsabile scientifico selezionato tra tutti i dipendenti a tempo indeterminato (deve esistere).
        responsabileScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabileScientifico);

        // Crea una nuova istanza di laboratorio, per verificarne l'esistenza.
        laboratorio = new Laboratorio(nome, topic, responsabileScientifico);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addLaboratorioToElenco(laboratorio)) {

            throw new IllegalArgumentException("Il laboratorio e' gia stato registrato!");

        } else {

            // Imposta la responsabilita' del dipendente per il nuovo laboratorio
            responsabileScientifico.addLaboratorioResponsabileScientifico(laboratorio);

            // Registra l'afferenza
            laboratorio.addAfferente(responsabileScientifico);
            responsabileScientifico.addAfferenzaLaboratorio(laboratorio);

            // Registra il nuovo laboratorio nel database
            LaboratorioPostgresDAO laboratorioDB = new LaboratorioPostgresDAO();
            laboratorioDB.addLaboratorio(nome, topic, responsabileScientifico.getMatricola());

            // Registra il responsabile scientifico come nuovo afferente al laboratorio.
            AfferirePostgresDAO afferenzaDB = new AfferirePostgresDAO();
            afferenzaDB.addAfferenza(responsabileScientifico.getMatricola(), laboratorio.getNome());
        }
    }

    /**
     * Aggiunge un progetto con i dati in inserimento, inclusa la data di fine.
     *
     * @param nome                       {@link String}     Il nome del progetto, unico nel sistema.
     * @param cup                        {@link String}     Il CUP del progetto, unico nel sistema.
     * @param budget                     {@link BigDecimal} Il budget del progetto.
     * @param dataInizio                 {@link LocalDate}  La data di inizio del progetto.
     * @param dataFine                   {@link LocalDate}  La data di fine del progetto.
     * @param stringReferenteScientifico {@link String}     I dati del referente scientifico associato al progetto.
     * @param stringResponsabile         {@link String}     I dati del responsabile del progetto.
     * @throws IllegalArgumentException Se il progetto e' gia' registrato.
     */
    public void aggiungiProgetto(String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, String stringReferenteScientifico, String stringResponsabile) throws IllegalArgumentException {

        //  Ricava il referente scientifico selezionato tra tutti i dipendenti a tempo indeterminato (deve esistere).
        referenteScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringReferenteScientifico);

        //  Ricava il responsabile selezionato tra tutti i dipendenti a tempo indeterminato (deve esistere).
        responsabile = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabile);

        // Crea una nuova istanza di progetto, per verificarne l'esistenza.
        progetto = new Progetto(cup, nome, budget, dataInizio, dataFine, referenteScientifico, responsabile);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addProgettoToElenco(progetto)) {

            throw new IllegalArgumentException("Il progetto e' gia' stato registrato!");

        } else {

            // Imposta le responsabilita' del referente scientifico e del responsabile
            referenteScientifico.addProgettoReferenteScientifico(progetto);
            responsabile.addProgettoResponsabile(progetto);

            // Registra il nuovo progetto nel database
            ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
            progettoDB.addProgetto(nome, cup, budget, dataInizio, null, referenteScientifico.getMatricola(), responsabile.getMatricola());
        }
    }

    /**
     * Aggiunge un nuovo progetto con i dati in inserimento, senza specificare la data di fine.
     *
     * @param nome                       {@link String}     Il nome del progetto, unico nel sistema.
     * @param cup                        {@link String}     Il CUP del progetto, unico nel sistema.
     * @param budget                     {@link BigDecimal} Il budget del progetto.
     * @param dataInizio                 {@link LocalDate}  La data di inizio del progetto.
     * @param stringReferenteScientifico {@link String}     I dati del referente scientifico associato al progetto.
     * @param stringResponsabile         {@link String}     I dati del responsabile del progetto.
     * @throws IllegalArgumentException Se il progetto e' gia' registrato.
     */
    public void aggiungiProgetto(String nome, String cup, BigDecimal budget, LocalDate dataInizio, String stringReferenteScientifico, String stringResponsabile) throws IllegalArgumentException {

        //  Ricava il referente scientifico selezionato tra tutti i dipendenti a tempo indeterminato (deve esistere).
        referenteScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringReferenteScientifico);

        //  Ricava il responsabile selezionato tra tutti i dipendenti a tempo indeterminato (deve esistere).
        responsabile = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabile);

        // Crea una nuova istanza di progetto, per verificarne l'esistenza.
        progetto = new Progetto(cup, nome, budget, dataInizio, referenteScientifico, responsabile);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addProgettoToElenco(progetto)) {

            throw new IllegalArgumentException("Il progetto e' gia' stato registrato!");

        } else {

            // Imposta le responsabilita' del referente scientifico e del responsabile
            referenteScientifico.addProgettoReferenteScientifico(progetto);
            responsabile.addProgettoResponsabile(progetto);

            // Registra il nuovo progetto nel database
            ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
            progettoDB.addProgetto(nome, cup, budget, dataInizio, null, referenteScientifico.getMatricola(), responsabile.getMatricola());
        }
    }

    /**
     * Aggiunge uno scatto di carriera con i dati in inserimento.
     *
     * @param tipoScatto {@link String}     Il tipo di scatto di carriera (Middle, Senior, Promosso_a_dirigente, Rimosso_da_dirigente).
     * @param dipendente {@link String}     Il nome del dipendente coinvolto nello scatto.
     * @param data       {@link LocalDate}  La data in cui si verifica lo scatto di carriera.
     * @throws IllegalArgumentException Se lo scatto di carriera è già registrato.
     */
    public void aggiungiScattoCarriera(String tipoScatto, String dipendente, LocalDate data) throws IllegalArgumentException {

        //Ricava il dipendente che effettua lo scatto di carriera tra tutti i dipendenti indeterminati (deve esistere).
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), dipendente);

        // Crea una nuova istanza di scatto di carriera, per verificarne l'esistenza.
        scattoCarriera = new ScattoCarriera(data, TipoScatto.valueOf(tipoScatto), dipendenteIndeterminato);

        // Verifica che l'istanza non sia già presente nell'elenco, se non e' presente la aggiunge.
        if (!addScattoCarrieraToElenco(scattoCarriera)) {

            throw new IllegalArgumentException("Lo scatto di carriera e' gia' stato registrato!");

        } else {

            // Imposta l'aggiunta di un nuovo scatto di carriera
            dipendenteIndeterminato.addScattoCarriera(scattoCarriera);

            // Aggiorna lo stato dirigenziale del dipendente in memoria.
            if (tipoScatto.equals(TipoScatto.Promosso_a_dirigente)) {
                dipendenteIndeterminato.setDirigente(true);
            } else
                dipendenteIndeterminato.setDirigente(false);

            // Registra il nuovo scatto di carriera nel database
            ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
            scattoCarrieraDB.addScattoCarriera(scattoCarriera.getTipoScatto().toString(), dipendenteIndeterminato.getMatricola(), data);

            //Deve aggiornare lo stato dirigenziale del dipendente nel database
            DipendenteIndeterminatoPostgresDAO dipendenteDB = new DipendenteIndeterminatoPostgresDAO();

            switch (TipoScatto.valueOf(tipoScatto)) {

                case Promosso_a_dirigente:
                    dipendenteDB.updateStatoDirigente(dipendenteIndeterminato.getMatricola(), true);
                    break;

                case Rimosso_da_dirigente:
                    dipendenteDB.updateStatoDirigente(dipendenteIndeterminato.getMatricola(), false);
                    break;

                //Aggiorna i valori del tipo di dipendente indeterminato a Middle oppure Senior.
                default:
                    dipendenteDB.updateTipoDipendente(dipendenteIndeterminato.getMatricola(), tipoScatto);
            }
        }
    }

    /**
     * Aggiunge un'istanza di lavoro tra un progetto ed un laboratorio.
     *
     * @param stringProgetto    {@link String}  I dati del progetto coinvolto.
     * @param stringLaboratorio {@link String}  I dati del laboratorio coinvolto.
     * @throws IllegalArgumentException Se l'istanza di lavoro è già registrata.
     */
    public void aggiungiLavorare(String stringProgetto, String stringLaboratorio) throws IllegalArgumentException {

        // Ricava il progetto per cui si vuole registrare l'istanza (deve esistere).
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Ricava il laboratorio per cui si vuole registrare l'istanza (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratorio);

        // Verifica che l'istanza di lavoro non sia gia' presente nel laboratorio e nel progetto.
        // Se non presente, registra l'istanza di lavoro tra il progetto ed il laboratorio
        if (!progetto.addLaboratorioLavorante(laboratorio) || !laboratorio.addProgetto(progetto)) {

            throw new IllegalArgumentException("L'istanza di lavoro e' gia' stata registrata!");

        } else {

            // Registra la nuova istanza di lavoro nel database
            LavorarePostgresDAO lavorareDB = new LavorarePostgresDAO();
            lavorareDB.addLavorare(progetto.getCup(), laboratorio.getNome());
        }
    }

    /**
     * Aggiunge un'afferenza tra un dipendente a tempo indeterminato ed un laboratorio.
     *
     * @param stringDipendente  {@link String}  I dati del dipendente coinvolto nell'afferenza.
     * @param stringLaboratorio {@link String}  I dati del laboratorio coinvolto nell'afferenza.
     * @throws IllegalArgumentException Se l'afferenza e' gia' registrata.
     */
    public void aggiungiAfferenza(String stringDipendente, String stringLaboratorio) throws IllegalArgumentException {

        // Ricava il dipendente indeterminato per cui si vuole registrare l'afferenza (deve esistere).
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringDipendente);

        // Ricava il laboratorio per cui si vuole registrare l'afferenza (deve esistere).
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratorio);

        // Verifica che l'afferenza non sia gia' presente nel dipendente indeterminato e nel laboratorio.
        // Se non presente, registra l'afferenza tra questi ultimi
        if (!dipendenteIndeterminato.addAfferenzaLaboratorio(laboratorio) || !laboratorio.addAfferente(dipendenteIndeterminato)) {

            throw new IllegalArgumentException("L'afferenza e' gia' stata registrata!");

        } else {

            // Viene aggiunta l'afferenza al database.
            AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
            afferireDB.addAfferenza(dipendenteIndeterminato.getMatricola(), laboratorio.getNome());
        }
    }

    //METODI MODIFICA

    /**
     * Modifica i dettagli di un dipendente a tempo indeterminato nel sistema.
     *
     * @param vecchiaMatricola  {@link String}  La vecchia matricola del dipendente da aggiornare.
     * @param vecchioDirigente                  Il vecchio stato dirigenziale del dipendente da aggiornare.
     * @param nome              {@link String}  Il nuovo nome del dipendente.
     * @param cognome           {@link String}  Il nuovo cognome del dipendente.
     * @param codFiscale        {@link String}  Il nuovo codice fiscale del dipendente.
     * @param matricola         {@link String}  La nuova matricola identificativa del dipendente da modificare, unica nel sistema.
     * @param tipo              {@link String}  Il nuovo tipo di dipendente (Middle, Senior, Promosso_a_dirigente, Rimosso_da_dirigente).
     * @param dataNascita       {@link String}  La nuova data di nascita del dipendente.
     * @param dataAssunzione    {@link String}  La nuova data di assunzione del dipendente.
     * @param dirigente                     Indica se il dipendente è un dirigente (true) o no (false).
     * @throws IllegalArgumentException Se la modifica e' gia' registrata.
     */
    public void modificaDipendenteIndeterminato(String vecchiaMatricola, boolean vecchioDirigente, String nome, String cognome,
                                                String codFiscale, String matricola, String tipo, String indirizzo, LocalDate dataNascita,
                                                LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente) throws IllegalArgumentException {

        // Crea una nuova istanza di scatto di carriera, per verificarne l'esistenza.
        dipendenteIndeterminato = new DipendenteIndeterminato(nome, cognome, codFiscale, matricola, TipoDipendente.valueOf(tipo), indirizzo, dataNascita, dataAssunzione, dataFine, dirigente);

        if (getElencoAllDipendentiIndeterminati().contains(dipendenteIndeterminato))
            throw new IllegalArgumentException("Il dipendente indeterminato e' gia' stato registrato!");

        // Ricava il dipendente indeterminato da modificare, in base alla matricola corrente
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), vecchiaMatricola);

        // Imposta i nuovi campi del dipendente indeterminato
        dipendenteIndeterminato.setNome(nome);
        dipendenteIndeterminato.setCognome(cognome);
        dipendenteIndeterminato.setTipoDipendente(TipoDipendente.valueOf(tipo));
        dipendenteIndeterminato.setIndirizzo(indirizzo);
        dipendenteIndeterminato.setDataNascita(dataNascita);
        dipendenteIndeterminato.setDataFineRapporto(dataFine);
        dipendenteIndeterminato.setDirigente(dirigente);

        // Se il codice fiscale e' cambiato, aggiorna i dati anagrafici del dipendente.
        if (!codFiscale.equals(dipendenteIndeterminato.getCodFiscale())) {

            String vecchioCodFiscale = dipendenteIndeterminato.getCodFiscale();
            dipendenteIndeterminato.setCodFiscale(codFiscale);

            updateDatiAnagraficiDipendenteIndeterminatoInElenco(vecchioCodFiscale, dipendenteIndeterminato);
            updateDatiAnagraficiDipendenteDatabase(dipendenteIndeterminato, vecchioCodFiscale);
        }

        // Se la data di assunzione e' cambiata, verranno ricalcolati gli scatti di carriera, eliminando i vecchi ed aggiungendone di nuovi
        if (!dataAssunzione.equals(dipendenteIndeterminato.getDataAssunzione())) {

            ricalcolaScattiCarriera(dataAssunzione);
        }

        // Viene aggiornato lo stato dirigenziale in maniera appropriata
        if (!vecchioDirigente && dirigente)
            insertScattoPromossoDirigenteNow();

        if (vecchioDirigente && !dirigente)
            insertScattoRimossoDirigenteNow(vecchioDirigente);

        // Viene impostata la nuova matricola
        dipendenteIndeterminato.setMatricola(matricola);

        // Aggiornamento del dipendente indeterminato nel database
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
        dipendenteIndeterminatoDB.updateDipendenteIndeterminato(vecchiaMatricola, dipendenteIndeterminato.getNome(), dipendenteIndeterminato.getCognome(),
                dipendenteIndeterminato.getCodFiscale(), dipendenteIndeterminato.getMatricola(), dipendenteIndeterminato.getTipoDipendente().toString(), dipendenteIndeterminato.getIndirizzo(),
                dipendenteIndeterminato.getDataNascita(), dipendenteIndeterminato.getDataAssunzione(), dipendenteIndeterminato.getDataFineRapporto(), dipendenteIndeterminato.getDirigente());
    }

    /**
     * Verifica se un dipendente a tempo indeterminato e' un responsabile scientifico, un referente scientifico oppure un responsabile.
     *
     * @param matricola {@link String}  La matricola del dipendente da verificare.
     * @return True se il dipendente ha responsabilità, altrimenti false.
     */
    public boolean checkResponsabilitaDipendente(String matricola) {

        // Inizialmente, impostiamo il test su false, per cui il dipendente non ha nessuna responsabilita'.
        boolean test = false;

        // Ricava l'istanza del dipendente indeterminato per cui si vuole effettuare la verifica (deve esiste)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), matricola);

        // Verifica se il dipendente ha responsabilita' di qualunque tipo
        if (dipendenteIndeterminato.getLaboratoriResponsabileScientifico() != null)
            test = true;
        else if (dipendenteIndeterminato.getProgettiReferenteScientifico() != null)
            test = true;
        else if (dipendenteIndeterminato.getProgettiResponsabile() != null)
            test = true;

        return test;
    }

    /**
     * Verifica se un dipendente a tempo indeterminato e' responsabile scientifico di qualche laboratorio.
     *
     * @param matricola {@link String} La matricola del dipendente da verificare.
     * @return True se il dipendente ha responsabilità, altrimenti false.
     */
    public boolean checkResponsabileScientificoLaboratorioLetture(String matricola){

        // Inizialmente, il dipendente non e' responsabile scientifico di alcun laboratorio
        boolean test = false;

        if (getElencoAllLaboratori() != null) {

            for (Laboratorio laboratorio : getElencoAllLaboratori()){

                // E' stato trovato un laboratorio di cui il dipendente e' responsabile scientifico
                if (laboratorio.getResponsabileScientifico().getMatricola().equals(matricola)) {
                    test = true;
                    break;
                }
            }

        }

        return test;
    }

    /**
     * Verifica se un dipendente a tempo indeterminato e' responsabile scientifico del laboratorio specificato.
     *
     * @param matricola {@link String}  La matricola del dipendente da verificare.
     * @param nomeLab   {@link String}  Il nome del laboratorio da verificare.
     * @return True se il dipendente e' responsabile scientifico, altrimenti false.
     */
    public boolean checkResponsabileScientificoLaboratorio(String matricola, String nomeLab){

        // Inizialmente, imposta il test su false, ovvero il dipendente non e' responsabile scientifico del laboratorio specificato
        boolean test = false;

        // Ricava l'istanza del laboratorio per cui si vuole effettuare la verifica (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLab);

        // Ricava l'istanza del dipendente indeterminato per cui si vuole effettuare la verifica (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), matricola);

        if (laboratorio.getResponsabileScientifico().equals(dipendenteIndeterminato))
            test = true;

        return test;
    }

    /**
     * Ricalcola gli scatti di carriera di un dipendente a tempo indetermianato in base alla nuova data di assunzione, rimpiazzando i vecchi scatti con degli scatti coerenti.
     *
     * @param dataAssunzione {@link LocalDate}  La nuova data di assunzione del dipendente.
     */
    public void ricalcolaScattiCarriera(LocalDate dataAssunzione) {

        // Aggiorna la data di assunzione del dipendente nel model.
        dipendenteIndeterminato.setDataAssunzione(dataAssunzione);

        // Verifica se il dipendente ha effettuato degli scatti di carriera in passato, se sì, rimuovili.
        if (dipendenteIndeterminato.getScattiEffettuati() != null)
            removeAllScattiCarrieraDipendente();

        // Esegui l'inserimento automatico degli scatti di carriera in base alla nuova data di assunzione, sia nel database, sia in memoria.
        insertAutomaticScatti();

        // Aggiorna la data di assunzione del dipendente nel database.
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
        dipendenteIndeterminatoDB.updateDataAssunzione(dipendenteIndeterminato.getMatricola(), dipendenteIndeterminato.getDataAssunzione());
    }

    /**
     * Rimuove tutti gli scatti di carriera relativi ad un dipendente a tempo indeterminato.
     */
    public void removeAllScattiCarrieraDipendente() {

        // Rimuovi tutti gli scatti di carriera in memoria.
        removeAllScattiCarrieraDipendente(dipendenteIndeterminato);

        // Imposta la lista degli scatti di carriera relativi al dipendente a null.
        dipendenteIndeterminato.setScattiEffettuati(null);

        // Rimuovi tutti gli scatti di carriera del dipendente dal database.
        ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
        scattoCarrieraDB.removeAllScattiCarrieraDipendente(dipendenteIndeterminato.getMatricola());
    }

    /**
     * Rimuove tutti gli scatti di carriera, di un certo tipo specificato, di un dipendente a tempo indeterminato.
     *
     * @param vecchioDipendente {@link DipendenteIndeterminato}  Il dipendente a cui rimuovere tutti gli scatti di carreira.
     * @param tipoScatto        {@link TipoScatto}  Il tipo di scatto di carriera da rimuovere.
     */
    public void removeAllTipoScattiCarrieraDipendente(DipendenteIndeterminato vecchioDipendente, TipoScatto tipoScatto) {

        // Rimuovi tutti gli scatti di carriera di un tipo specifico dalla memoria.
        removeAllTipoScattiDipendente(vecchioDipendente, tipoScatto);

        // Rimuovi tutti gli scatti di carriera di quel tipo dal database.
        ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
        scattoCarrieraDB.removeScattoCarriera(tipoScatto.toString(), vecchioDipendente.getMatricola());
    }

    /**
     * Aggiornamento polimorfico dei dati anagrafici di un dipendente nel database.
     *
     * @param dipendente        {@link Dipendente}  Il dipendente da aggiornare.
     * @param vecchioCodFiscale {@link String}      Il codice fiscale corrente del dipendente.
     */
    public void updateDatiAnagraficiDipendenteDatabase(Dipendente dipendente, String vecchioCodFiscale) {

        // Viene sfruttato il polimorfismo (assegnata una'istanza di tipo "DipendenteIndeterminato" o "DipendenteProgetto" ad una variabile di tipo "Dipendente"),
        // per applicare i dovuti aggiornamenti nel database

        if (dipendente instanceof DipendenteIndeterminato) {

            // Aggiorna il dipendente indeterminato con i nuovi dati
            DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteIndeterminatoDB.updateDatiAnagraficiDipendente(vecchioCodFiscale, dipendente.getNome(), dipendente.getCognome(),
                    dipendente.getCodFiscale(), dipendente.getIndirizzo(), dipendente.getDataNascita());

        } else if (dipendente instanceof DipendenteProgetto) {

            // Aggiorna il dipendente indeterminato con i nuovi dati
            DipendenteProgettoPostgresDAO dipendenteProgettoDB = new DipendenteProgettoPostgresDAO();
            dipendenteProgettoDB.updateDatiAnagraficiDipendente(vecchioCodFiscale, dipendente.getNome(), dipendente.getCognome(),
                    dipendente.getCodFiscale(), dipendente.getIndirizzo(), dipendente.getDataNascita());
        }
    }

    /**
     * Modifica un dipendente a progetto con i dati in inserimento.
     *
     * @param vecchiaMatricola      {@link String}      La matricola identificativa della carriera del dipendente in aggiornamento.
     * @param stringVecchioProgetto {@link String}      I dati del vecchio progetto che ha acquistato l'attrezzatura.
     * @param nome                  {@link String}      Il nuovo nome del dipendente.
     * @param cognome               {@link String}      Il nuovo cognome del dipendente.
     * @param codFiscale            {@link String}      Il nuovo codice fiscale del dipendente.
     * @param matricola             {@link String}      La nuova matricola identificativa del dipendente, unica nel sistema.
     * @param indirizzo             {@link String}      Il nuovo indirizzo del dipendente.
     * @param dataNascita           {@link LocalDate}   La nuova data di nascita del dipendente.
     * @param dataAssunzione        {@link LocalDate}   La nuova data di assunzione del dipendente.
     * @param scadenza              {@link LocalDate}   La nuova data di scadenza del contratto del dipendente.
     * @param costo                 {@link BigDecimal}  Il nuovo costo del contratto del dipendente.
     * @param stringProgetto        {@link String}      Il nuovo progetto che ha ingaggiato il dipendente.
     * @throws IllegalArgumentException Se il dipendente a progetto e' gia' registrato.
     */
    public void modificaDipendenteProgetto(String vecchiaMatricola, String stringVecchioProgetto, String nome, String cognome, String codFiscale, String matricola,
                                           String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione,
                                           LocalDate scadenza, BigDecimal costo, String stringProgetto) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo progetto che, eventualmente, ingaggia il dipendente a progetto (deve esistere)
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Crea una nuova istanza di dipendente a progetto, per verificarne l'esistenza.
        dipendenteProgetto = new DipendenteProgetto(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione, scadenza, costo, progetto);

        if (getElencoAllDipendentiProgetto().contains(dipendenteProgetto))
            throw new IllegalArgumentException("Il dipendente a progetto e' gia' stato registrato!");

        // Se il dipendente a progetto non esiste gia', allora si modifica la vecchia istanza del dipendente con i nuovi dati
        // Non viene modificata la matricola, siccome bisogna aggiornare le associazioni del dipendente  con il vecchio ed il nuovo progetto proprietario

        // Ricava l'istanza del vecchio progetto che in precedenza ha ingaggiato il dipendente a progetto (deve esistere)
        Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), stringVecchioProgetto);

        // Recupera la vecchia istanza di dipendente a progetto, modificandone i campi
        dipendenteProgetto = getIstanceFromArrayList(getElencoAllDipendentiProgetto(), vecchiaMatricola);
        dipendenteProgetto.setNome(nome);
        dipendenteProgetto.setCognome(cognome);
        dipendenteProgetto.setCodFiscale(codFiscale);
        dipendenteProgetto.setIndirizzo(indirizzo);
        dipendenteProgetto.setDataNascita(dataNascita);
        dipendenteProgetto.setDataAssunzione(dataAssunzione);
        dipendenteProgetto.setScadenza(scadenza);
        dipendenteProgetto.setCosto(costo);

        // Nel caso cambia il progetto che ingaggia il dipendente a progetto, deve aggiornare l'associazione
        if (!vecchioProgetto.equals(progetto)){

            // Elimina l'ingaggio del dipendente dal progetto rimosso
            vecchioProgetto.removeDipendenteIngaggiato(dipendenteProgetto);

            // Aggiunge l'ingaggio del dipendente al progetto impostato
            progetto.addDipendenteIngaggiato(dipendenteProgetto);

            // Imposta il nuovo progetto proprietario del dipendente a progetto modificato
            dipendenteProgetto.setProgettoProprietario(progetto);
        }

        // Imposta la nuova matricola del dipendente a progetto
        dipendenteProgetto.setMatricola(matricola);

        //Aggiornamento del dipendente a progetto nel database
        DipendenteProgettoPostgresDAO dipendenteProgettoDB = new DipendenteProgettoPostgresDAO();
        dipendenteProgettoDB.updateDipendenteProgetto(vecchiaMatricola, dipendenteProgetto.getNome(), dipendenteProgetto.getCognome(),
                dipendenteProgetto.getCodFiscale(), dipendenteProgetto.getMatricola(), dipendenteProgetto.getIndirizzo(),
                dipendenteProgetto.getDataNascita(), dipendenteProgetto.getDataAssunzione(), dipendenteProgetto.getScadenza(), dipendenteProgetto.getProgettoProprietario().getCup(), dipendenteProgetto.getCosto());
    }

    /**
     * Modifica un'attrezzatura acquistata da un progetto, non specificando il laboratorio possedente.
     *
     * @param vecchioId                 {@link Integer}     L'identificativo numerico dell'attrezzatura da modificare.
     * @param stringVecchioProgetto     {@link String}      I dati del vecchio progetto che ha acquistato l'attrezzatura.
     * @param stringVecchioLaboratorio  {@link String}      I dati del vecchio laboratorio che possiede l'attrezzatura.
     * @param descrizione               {@link String}      La nuova descrizione dell'attrezzatura.
     * @param costo                     {@link BigDecimal}  Il nuovo costo dell'attrezzatura.
     * @param stringProgetto            {@link String}      I dati del nuovo progetto che ha acquistato l'attrezzatura.
     * @param stringLaboratorio         {@link String}      I dati del nuovo laboratorio che possiede l'attrezzatura.
     * @throws IllegalArgumentException Se l'attrezzatura e' gia registrata.
     */
    public void modificaAttrezzatura(String vecchioId, String stringVecchioProgetto, String stringVecchioLaboratorio, String descrizione, BigDecimal costo, String stringProgetto, String stringLaboratorio) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo progetto che, eventualmente, acquista l'attrezzatura (deve esistere)
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Ricava l'istanza del nuovo laboratorio che, eventualmente, possiede l'attrezzatura (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratorio);

        // Crea una nuova istanza dell'attrezzatura, per verificarne l'esistenza.
        attrezzatura = new Attrezzatura(Integer.parseInt(vecchioId), descrizione, costo, progetto, laboratorio);

        // Verifica se l'attrezzatura creata e' gia' esistente
        for (Attrezzatura currAttrezzatura : progetto.getAttrezzature()) {

            if (currAttrezzatura.equals(attrezzatura) && currAttrezzatura.getLaboratorioPossedente() != null &&
                    currAttrezzatura.getLaboratorioPossedente().equals(laboratorio))
                throw new IllegalArgumentException("L'attrezzatura risulta gia' registrata");
        }

        // Ricava l'istanza del vecchio che ha acquistato l'attrezzatura (deve esistere)
        Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), stringVecchioProgetto);

        // Ricava, se specificata, l'istanza del vecchio laboratorio che possiede l'attrezzatura (deve esistere)
        Laboratorio vecchioLaboratorio = null;
        if (stringVecchioLaboratorio != null)
            vecchioLaboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringVecchioLaboratorio);

        // Viene recuperata l'istanza dell'attrezzatura modificata, e vengono aggiornati i capi di descrizione e costo
        attrezzatura = getIstanceFromArrayList(getElencoAllAttrezzature(), vecchioId);
        attrezzatura.setDescrizione(descrizione);
        attrezzatura.setCosto(costo);

        // Nel caso venga modificato il progetto che ha acquistato l'attrezzaura, viene modificata l'associazione ingaggiare
        if (!progetto.equals(vecchioProgetto)) {

            // Rimuove l'acquisto dell'attrezzatura dal progetto modificato e registra il nuovo acquisto al progetto in modifica
            vecchioProgetto.removeAttrezzaturaAcquistata(attrezzatura);
            progetto.addAttrezzaturaAcquistata(attrezzatura);

            // Imposta il nuovo progetto come proprietario dell'attrezzatura
            attrezzatura.setProgettoProprietario(progetto);
        }

        // Nel caso venga modificato il laboratorio che possiede l'attrezzaura, viene modificata l'associazione possedere
        if (vecchioLaboratorio != null && !laboratorio.equals(vecchioLaboratorio)) {

            // Rimuove il possedimento dell'attrezzatura dal laboratorio modificato e lo aggiunge al laboratorio in modifica
            vecchioLaboratorio.removeAttrezzatura(attrezzatura);
            laboratorio.addAttrezzatura(attrezzatura);

            // Imposta il nuovo laboratorio possedente dell'attrezzatura
            attrezzatura.setLaboratorioPossedente(laboratorio);

        } else if (vecchioLaboratorio == null){

            // Aggiunge l'attrezzatura al nuovo laboratorio
            laboratorio.addAttrezzatura(attrezzatura);

            // Imposta il nuovo laboratorio possedente dell'attrezzatura
            attrezzatura.setLaboratorioPossedente(laboratorio);
        }

        // Aggiornamento dell'attrezzatura nel database
        AttrezzaturaPostgresDAO attrezzaturaDB = new AttrezzaturaPostgresDAO();
        attrezzaturaDB.updateAttrezzatura(Integer.parseInt(vecchioId),
                                            attrezzatura.getDescrizione(),
                                            attrezzatura.getCosto(),
                                            progetto.getCup(),
                                            laboratorio.getNome());
    }

    /**
     * Modifica un'attrezzatura acquistata da un progetto, non specificando il laboratorio possedente.
     *
     * @param vecchioId                 {@link Integer}     L'identificativo numerico dell'attrezzatura da modificare.
     * @param stringVecchioProgetto     {@link String}      I dati del vecchio progetto che ha acquistato l'attrezzatura.
     * @param stringVecchioLaboratorio  {@link String}      I dati del vecchio laboratorio che possiede l'attrezzatura.
     * @param descrizione               {@link String}      La nuova descrizione dell'attrezzatura.
     * @param costo                     {@link BigDecimal}  Il nuovo costo dell'attrezzatura.
     * @param stringProgetto            {@link String}      I dati del nuovo progetto che ha acquistato l'attrezzatura.
     * @throws IllegalArgumentException Se l'attrezzatura e' gia registrata.
     */
    public void modificaAttrezzatura(String vecchioId, String stringVecchioProgetto, String stringVecchioLaboratorio, String descrizione, BigDecimal costo, String stringProgetto) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo progetto che, eventualmente, acquista l'attrezzatura (deve esistere)
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Crea una nuova istanza dell'attrezzatura, per verificarne l'esistenza.
        attrezzatura = new Attrezzatura(Integer.parseInt(vecchioId), descrizione, costo, progetto);

        // Ricava, se specificata, l'istanza del vecchio laboratorio che possiede l'attrezzatura (deve esistere)
        Laboratorio vecchioLaboratorio = null;
        if (stringVecchioLaboratorio != null)
            vecchioLaboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringVecchioLaboratorio);

        // Verifica l'esistenza dell'attrezzatura
        if (vecchioLaboratorio == null && progetto.getAttrezzature().contains(attrezzatura)){

            throw new IllegalArgumentException("L'attrezzatura risulta gia' registrata");
        }

        // Ricava l'istanza del vecchio che ha acquistato l'attrezzatura (deve esistere)
        Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), stringVecchioProgetto);

        // Viene recuperata l'istanza dell'attrezzatura modificata, e vengono aggiornati i capi di descrizione e costo
        attrezzatura = getIstanceFromArrayList(getElencoAllAttrezzature(), vecchioId);
        attrezzatura.setDescrizione(descrizione);
        attrezzatura.setCosto(costo);

        // Nel caso venga modificato il progetto che ha acquistato l'attrezzaura, viene modificata l'associazione ingaggiare
        if (!progetto.equals(vecchioProgetto)) {

            // Rimuove l'acquisto dell'attrezzatura dal progetto modificato e registra il nuovo acquisto al progetto in modifica
            vecchioProgetto.removeAttrezzaturaAcquistata(attrezzatura);
            progetto.addAttrezzaturaAcquistata(attrezzatura);

            // Imposta il nuovo progetto come proprietario dell'attrezzatura
            attrezzatura.setProgettoProprietario(progetto);
        }

        // Aggiornamento dell'attrezzatura nel database
        AttrezzaturaPostgresDAO attrezzaturaDB = new AttrezzaturaPostgresDAO();
        attrezzaturaDB.updateAttrezzatura(Integer.parseInt(vecchioId),
                attrezzatura.getDescrizione(),
                attrezzatura.getCosto(),
                progetto.getCup(),
                null);
    }

    /**
     * Aggiorna un laboratorio esistente con i dati forniti.
     *
     * @param vecchioNome                          {@link String}   Il nome del laboratorio da aggiornare.
     * @param nome                                 {@link String}   Il nuovo nome del laboratorio, unico nel sistema.
     * @param topic                                {@link String}   Il nuovo topic del laboratorio.
     * @param stringResponsabileScientifico        {@link String}   I dati del nuovo responsabile scientifico del laboratorio.
     * @throws IllegalArgumentException Se il laboratorio e' gia' registrato.
     */
    public void modificaLaboratorio(String vecchioNome, String nome, String topic, String stringResponsabileScientifico) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo responsabile scientifico del nuovo laboratorio (deve esistere)
        responsabileScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabileScientifico);

        // Crea una nuova istanza del laboratorio, per verificarne l'esistenza.
        laboratorio = new Laboratorio(nome, topic, responsabileScientifico);

        if (getElencoAllLaboratori().contains(laboratorio)) {
            throw new IllegalArgumentException("Il laboratorio e' gia' stato registrato!");
        }

        // Ricava l'istanza del vecchio laboratorio da aggiornare (deve esistere)
        Laboratorio vecchioLaboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), vecchioNome);

        // Modifica le associazioni nel caso in cui sia stato cambiato il responsabile scientifico
        if (!vecchioLaboratorio.getResponsabileScientifico().equals(responsabileScientifico)){

            // Elimina la responsabilita' dal vecchio responsabile scientifico
            vecchioLaboratorio.getResponsabileScientifico().removeLaboratorioResponsabileScientifico(vecchioLaboratorio);

            // Aggiunge la responsabilita' al nuovo responsabile scientifico
            responsabileScientifico.addLaboratorioResponsabileScientifico(vecchioLaboratorio);

            // Registra il nuovo responsabile scientifico del laboratorio
            vecchioLaboratorio.setResponsabileScientifico(responsabileScientifico);

            // Aggiunge l'afferenza del nuovo responsabile scientifico, non rimuovendo l'afferenza del vecchio responsabile scientifico
            AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
            afferireDB.addAfferenza(responsabileScientifico.getMatricola(), vecchioLaboratorio.getNome());
        }

        // Aggiornamento del laboratorio nel database
        LaboratorioPostgresDAO laboratorioDB = new LaboratorioPostgresDAO();
        laboratorioDB.updateLaboratorio(vecchioLaboratorio.getNome(), laboratorio.getNome(), laboratorio.getTopic(), responsabileScientifico.getMatricola());

        // Aggiornamento dei campi del laboratorio nella memoria
        vecchioLaboratorio.setNome(laboratorio.getNome());
        vecchioLaboratorio.setTopic(laboratorio.getTopic());
    }

    /**
     * Aggiorna un progetto esistente con i dati forniti.
     *
     * @param vecchioCup                 {@link String} Il vecchio CUP unico del progetto da aggiornare.
     * @param nome                       {@link String} Il nuovo nome del progetto, unico nel sistema.
     * @param cup                        {@link String} Il nuovo CUP unico del progetto.
     * @param budget                     {@link BigDecimal} Il nuovo budget del progetto.
     * @param dataInizio                 {@link LocalDate} La nuova data di inizio del progetto.
     * @param dataFine                   {@link LocalDate} La nuova data di fine del progetto.
     * @param stringReferenteScientifico {@link String} Il nuovo referente scientifico del progetto.
     * @param stringResponsabile         {@link String} Il nuovo responsabile del progetto.
     * @throws IllegalArgumentException Se il progetto e' gia' registrato.
     */
    public void modificaProgetto(String vecchioCup, String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine,
                                 String stringReferenteScientifico, String stringResponsabile) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo referente scientifico del nuovo progetto (deve esistere)
        referenteScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringReferenteScientifico);

        // Ricava l'istanza del nuovo responsabile del nuovo progetto (deve esistere)
        responsabile = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabile);

        // Crea una nuova istanza del progetto, per verificarne l'esistenza.
        progetto = new Progetto(cup, nome, budget, dataInizio, dataFine, referenteScientifico, responsabile);

        if (getElencoAllProgetti().contains(progetto)) {

            throw new IllegalArgumentException("Il progetto e' gia' stato registrato!");
        }

        // Ricava l'istanza del vecchio progetto da aggiornare (deve esistere)
        Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), vecchioCup);

        // Se viene modificato il referente scientifico, modifica le associazioni
        if (!vecchioProgetto.getReferenteScientifico().equals(referenteScientifico)){

            // Vengono modificate le responsabilita' dei dipendenti

            // Rimossa responsabilita' dal vecchio referente scientifico
            vecchioProgetto.getReferenteScientifico().removeProgettoReferenteScientifico(vecchioProgetto);

            // Aggiunta responsabilita' al nuovo referente scientifico
            referenteScientifico.addProgettoReferenteScientifico(vecchioProgetto);

            // Aggiornato il referente scientifico del progetto
            vecchioProgetto.setReferenteScientifico(referenteScientifico);
        }

        // Se viene modificato il responsabile, modifica le associazioni
        if (!vecchioProgetto.getResponsabile().equals(responsabile)){
            // Vengono modificate le responsabilita' dei dipendenti

            // Rimossa responsabilita' dal vecchio responsabile
            vecchioProgetto.getResponsabile().removeProgettoResponsabile(vecchioProgetto);

            // Aggiunta responsabilita' al nuovo referente scientifico
            responsabile.addProgettoResponsabile(vecchioProgetto);

            // Aggiornato il responsabile del progetto
            vecchioProgetto.setResponsabile(responsabile);
        }

        // Aggiorna il progetto nel database.
        ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
        progettoDB.updateProgetto(vecchioProgetto.getCup(), progetto.getNome(), progetto.getCup(), progetto.getBudget(),
                progetto.getDataInizio(), progetto.getDataFine(),referenteScientifico.getMatricola(), responsabile.getMatricola());

    }

    /**
     * Aggiorna un'afferenza tra un dipendente a tempo indeterminato ed un laboratorio con i dati forniti.
     *
     * @param vecchiaMatricola  {@link String}  La matricola del dipendente da aggiornare.
     * @param vecchioNomeLab    {@link String}  Il nome del laboratorio da aggiornare.
     * @param matricola         {@link String}  La matricola del nuovo dipendente.
     * @param nomeLab           {@link String}  Il nome del nuovo laboratorio.
     * @throws IllegalArgumentException Se il laboratorio e' gia' registrato.
     * */
    public void modificaAfferenza(String vecchiaMatricola, String vecchioNomeLab, String matricola, String nomeLab) throws IllegalArgumentException{

        // Ricava l'istanza del nuovo laboratorio (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLab);

        // Ricava l'istanza del nuovo dipendente indeterminato (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), matricola);

        // Verifica se l'afferenza e' gia' presente
        if (laboratorio.getAfferenti().contains(dipendenteIndeterminato))
            throw new IllegalArgumentException("L'afferenza del dipendente al laboratorio specificato e' gia' registrata!");

        // Ricava l'istanza del laboratorio da modificare (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), vecchioNomeLab);

        // Ricava l'istanza del dipendente indeterminato da modificare (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), vecchiaMatricola);

        // Elimina la vecchia afferenza dal laboratorio
        laboratorio.removeAfferente(dipendenteIndeterminato);
        dipendenteIndeterminato.removeAfferenzaLaboratorio(laboratorio);

        // Ricava l'istanza del nuovo laboratorio (deve esistere)
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLab);

        // Ricava l'istanza del nuovo dipendente indeterminato (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), matricola);

        // Aggiunge la nuova afferenza al nuovo laboratorio
        laboratorio.addAfferente(dipendenteIndeterminato);
        dipendenteIndeterminato.addAfferenzaLaboratorio(laboratorio);

        // Aggiorna l'afferenza nel database
        AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
        afferireDB.updateAfferenza(vecchiaMatricola, vecchioNomeLab, dipendenteIndeterminato.getMatricola(), laboratorio.getNome());

    }

    /**
     * Verifica se la data di fine del progetto è successiva a tutte le scadenze dei dipendenti del progetto ingaggiati da quest'ultimo.
     *
     * @param dataFineProgetto {@link LocalDate}    La data di fine del progetto da verificare.
     * @param stringProgetto   {@link String}       I dati del progetto da verificare.
     * @return True se la data di fine del progetto è successiva a tutte le scadenze dei dipendenti, altrimenti false.
     */
    public boolean checkDataFineIsAfterAllScadenzaDipProgetto(LocalDate dataFineProgetto, String stringProgetto) {

        boolean test = true;

        // Recupera l'istanza del progetto da verificare dalla lista di tutti i progetti.
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Effettua un ciclo su tutte le scadenze dei dipendenti ingaggiati nel progetto.
        for (DipendenteProgetto dipendenteProgetto : progetto.getDipendentiIngaggiati()) {

            // Se la data di fine del progetto è prima della scadenza di un dipendente, imposta il flag "test" su false.
            if (dataFineProgetto.isBefore(dipendenteProgetto.getScadenza()))
                test = false;
        }

        return test;
    }

    /**
     * Modifica uno scatto di carriera esistente con i dati forniti.
     *
     * @param vecchioTipoScatto {@link String}      Il tipo di scatto di carriera da aggiornare (Middle, Senior, Promosso_a_dirigente, Rimosso_da_dirigente).
     * @param vecchiaMatricola  {@link String}      La matricola da aggiornare del dipendente che ha effettutato lo scatto di carriera.
     * @param vecchiaData       {@link LocalDate}   La vecchia data dello scatto di carriera da aggiornare.
     * @param tipoScatto        {@link String}      Il nuovo tipo di scatto di carriera (Middle, Senior, Promosso_a_dirigente, Rimosso_da_dirigente).
     * @param dipendente        {@link String}      Il nuovo dipendente associato allo scatto di carriera.
     * @param data              {@link LocalDate}   La nuova data dello scatto di carriera.
     * @throws IllegalArgumentException Se lo scatto di carriera e' gia' registrato.
     */
    public void modificaScattoCarriera(String vecchioTipoScatto, String vecchiaMatricola, LocalDate vecchiaData, String tipoScatto, String dipendente, LocalDate data) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo dipendente indeterminato (deve esistere).
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), dipendente);

        // Crea una nuova istanza dello scatto di carriera, per verificarne l'esistenza.
        scattoCarriera = new ScattoCarriera(data, TipoScatto.valueOf(tipoScatto), dipendenteIndeterminato);

        if (getElencoAllScattiCarriera().contains(scattoCarriera)) {

            throw new IllegalArgumentException("Lo scatto di carriera e' gia' stato registrato!");

        } else {

            // Ricava l'istanza del vecchio dipendente indeterminato (deve esistere).
            DipendenteIndeterminato vecchioDipendente = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), vecchiaMatricola);

            ScattoCarriera vecchioScattoCarriera = null;

            //cerca l'istanza del vecchio scatto carriera (non utilizzabile il metodo statico getIstanceFromArrayList(...) poiche' uno scatto e' identificato contemporaneamente dalla data, dalla matricola del dipendente e dal tipo dello scatto)
            for (ScattoCarriera scatto : vecchioDipendente.getScattiEffettuati()) {

                if (scatto.getTipoScatto().equals(TipoScatto.valueOf(vecchioTipoScatto)) && scatto.getData().equals(vecchiaData)) {
                    vecchioScattoCarriera = scatto;
                    break;
                }
            }

            //viene rimosso il vecchio scatto di carriera dal vecchio dipendente
            //viene aggiunto il nuovo scatto di carriera al nuovo dipendente

            //vecchio dipendente
            //dalle associazioni
            vecchioDipendente.removeScattoCarriera(vecchioScattoCarriera);
            vecchioScattoCarriera.setDipendente(null);
            //dall'elenco
            getElencoAllScattiCarriera().remove(vecchioScattoCarriera);

            //nuovo dipendente
            //nelle associazioni
            dipendenteIndeterminato.addScattoCarriera(scattoCarriera);
            scattoCarriera.setDipendente(dipendenteIndeterminato);
            //nell'elenco
            addScattoCarrieraToElenco(scattoCarriera);

            //aggiornamento del DB
            //scatti carriera
            ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
            scattoCarrieraDB.updateScattoCarriera(vecchioScattoCarriera.getTipoScatto().toString(), vecchioScattoCarriera.getDipendente().getMatricola(), vecchioScattoCarriera.getData(),
                    scattoCarriera.getTipoScatto().toString(), scattoCarriera.getDipendente().getMatricola(), scattoCarriera.getData());

            //aggiornamento valori dirigenziali
            if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente))
                dipendenteIndeterminato.setDirigente(true);

            if (scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                dipendenteIndeterminato.setDirigente(false);

            if (vecchioScattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente))
                vecchioDipendente.setDirigente(false);

            if (vecchioScattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                vecchioDipendente.setDirigente(true);

            //Deve aggiornare i valori nella tabella del dipendente
            DipendenteIndeterminatoPostgresDAO dipendenteDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteDB.updateStatoDirigente(dipendenteIndeterminato.getMatricola(), dipendenteIndeterminato.getDirigente());
            dipendenteDB.updateStatoDirigente(vecchioDipendente.getMatricola(), vecchioDipendente.getDirigente());
        }
    }

    /**
     * Verifica la coerenza tra la data di assunzione e la data dello scatto di carriera del dipendente a tempo indeterminato specificato
     *
     * @param dipendente {@link String}     Il dipendente di cui verificare lo scatto di carriera.
     * @param dataScatto {@link LocalDate}  La data dello scatto di carriera da verificare.
     * @return True se le date non sono coerenti, false altrimenti.
     * */
    public boolean checkNotCoerenzaDataAssunzioneDataScatto(String dipendente, LocalDate dataScatto){

        // Ricava l'istanza del dipendente indeterminato da verificare (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), dipendente);

        return notCoerenzaDate(dipendenteIndeterminato.getDataAssunzione(), dataScatto, null);
    }

    /**
     * Aggiorna un'istanza di lavoro tra un progetto ed un laboratorio con i dati forniti.
     *
     * @param stringVecchioLaboratorio  {@link String}  I dati del vecchio laboratorio da aggiornare.
     * @param stringVecchioProgetto     {@link String}  I dati del vecchio progetto da aggiornare.
     * @param stringLaboratorio         {@link String}  I dati del nuovo laboratorio.
     * @param stringProgetto            {@link String}  I dati del nuovo progetto.
     * @throws IllegalArgumentException Se l'istanza di lavoro risulta gia' registrata.
     * */
    public void modificaLavoro(String stringVecchioLaboratorio, String stringVecchioProgetto, String stringLaboratorio, String stringProgetto) throws IllegalArgumentException {

        // Ricava l'istanza del nuovo laboratorio (deve esistere).
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratorio);

        // Ricava l'istanza del nuovo progetto (deve esistere).
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        // Verifica l'esistenza di un'istanza di lavoro tra il progetto ed il laboratorio specificato
        if (!laboratorio.addProgetto(progetto) && !progetto.addLaboratorioLavorante(laboratorio))
            throw new IllegalArgumentException("L'istanza di lavoro e' gia' stata registrata!");

        // Ricava l'istanza del vecchio laboratorio da aggiornare (deve esistere).
        Laboratorio vecchioLaboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringVecchioLaboratorio);

        // Ricava l'istanza del vecchio progetto da aggiornare (deve esistere).
        Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), stringVecchioProgetto);

        // Rimuove l'istanza di lavoro del vecchio progetto dal laboratorio
        vecchioLaboratorio.removeProgetto(vecchioProgetto);

        // Rimuove l'istanza di lavoro del vecchio laboratorio dal progetto
        vecchioProgetto.removeLaboratorioLavorante(vecchioLaboratorio);

        // Aggiunge l'istanza di lavoro del nuovo progetto al nuovo laboratorio
        laboratorio.addProgetto(progetto);

        // Aggiunge l'istanza di lavoro del nuovo laboratorio al nuovo progetto
        progetto.addLaboratorioLavorante(laboratorio);

        //Aggiorna l'istanza di lavoro nel database
        LavorarePostgresDAO lavorareDB = new LavorarePostgresDAO();
        lavorareDB.updateLavorare(vecchioProgetto.getCup(), vecchioLaboratorio.getNome(), progetto.getCup(), laboratorio.getNome());
    }

    // METODI DI ELIMINAZIONE

    /**
     * Effettua l'eliminazione di un laboratorio, in base al nome specificato.
     * @param nomeLaboratorio {@link String} Il nome del laboratorio, unico nel sistema.
     */
    public void eliminaLaboratorio(String nomeLaboratorio){

        // Ricava l'istanza del laboratorio da eliminare (deve esistere).
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLaboratorio);

        // Rimuove la responsabilita' del responsabile scientifico
        laboratorio.getResponsabileScientifico().removeLaboratorioResponsabileScientifico(laboratorio);

        // Rimuove le afferenze di tutti i dipendenti
        for (DipendenteIndeterminato dipendente : laboratorio.getAfferenti())
            dipendente.removeAfferenzaLaboratorio(laboratorio);

        // Rimuove i rapporti lavorativi con ogni progetto, se presenti
        if (laboratorio.getProgettiLavorati() != null) {
            for (Progetto progetto : laboratorio.getProgettiLavorati())
                progetto.removeLaboratorioLavorante(laboratorio);
        }

        // Rimuove tutte le attrezzature possedute
        if (laboratorio.getAttrezzaturePossedute() != null) {
            for (Attrezzatura attrezzatura : laboratorio.getAttrezzaturePossedute())
                attrezzatura.setLaboratorioPossedente(null);
        }

        // Rimuove il laboratorio dall'elenco
        elencoAllLaboratori.remove(laboratorio);

        // Se l'elenco dei laboratori e' vuoto, impostalo a null
        if (elencoAllLaboratori.isEmpty())
            elencoAllLaboratori = null;

        // Effettua la rimozione del laboratorio dal database
        LaboratorioPostgresDAO laboratorioDB = new LaboratorioPostgresDAO();
        laboratorioDB.removeLaboratorio(laboratorio.getNome());
    }

    /**
     * Effettua l'eliminazione di un'istanza di lavoro tra un progetto ed un laboratorio.
     * @param cupProgetto       {@link String}  Il CUP unico del progetto.
     * @param nomeLaboratorio   {@link String}  Il nome del laboratorio, unico nel sistema.
     */
    public void eliminaLavoro(String cupProgetto, String nomeLaboratorio){

        // Ricava l'istanza del progetto di cui eliminare l'istanza di lavoro (deve esistere).
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), cupProgetto);

        // Ricava l'istanza del laboratorio di cui eliminare l'istanza di lavoro (deve esistere).
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLaboratorio);

        // Elimina il laboratorio lavorante al progetto
        progetto.removeLaboratorioLavorante(laboratorio);

        // Elimina il progetto dalla lista dei lavori del laboratorio
        laboratorio.removeProgetto(progetto);

        // Elimina la relazione di lavoro dal database
        LavorarePostgresDAO lavorareDB = new LavorarePostgresDAO();
        lavorareDB.removeLavorare(progetto.getCup(), laboratorio.getNome());

    }

    /**
     * Effettua l'eliminazione di un'afferenza tra un dipendente indeterminato ed un laboratorio.
     * @param matricolaDipendente       {@link String}  La matricola del dipendente, unica nel sistema.
     * @param nomeLaboratorio           {@link String}  Il nome del laboratorio, unico nel sistema.
     */
    public void eliminaAfferenza(String matricolaDipendente, String nomeLaboratorio) throws IllegalArgumentException {

        // Ricava l'istanza del dipendente indeterminato di cui eliminare l'afferenza (deve esistere).
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), matricolaDipendente);

        // Ricava l'istanza del laboratorio di cui eliminare l'afferenza (deve esistere).
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLaboratorio);

        //Verifica se il dipendente e' responsabile scientifico del laboratorio, nel caso impedisce l'eliminazione
        if (laboratorio.getResponsabileScientifico().equals(dipendenteIndeterminato))
            throw new IllegalArgumentException("Non e' possibile rimuovere l'afferenza di un responsabile scientifico! Modificare prima quest'ultimo nell'area laboratori!");

        // Rimuove l'afferenza del dipendente dal laboratorio
        dipendenteIndeterminato.removeAfferenzaLaboratorio(laboratorio);

        // Elimina l'afferente al laboratorio
        laboratorio.removeAfferente(dipendenteIndeterminato);

        // Elimina l'afferenza dal database
        AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
        afferireDB.removeAfferenza(dipendenteIndeterminato.getMatricola(), laboratorio.getNome());

    }

    //METODI DI SUPPLEMENTO

    /**
     * Ottiene un'istanza dall'elenco in base ad un campo specificato, generalmente l'identificativo.
     *
     * @param elenco    {@link ArrayList<T>} L'elenco da cui ottenere l'istanza.
     * @param istanza   {@link String}      Il campo dell'istanza da cercare.
     * @return Restituisce l'istanza corrispondente o null se non trovata.
     */
    public static <T> T getIstanceFromArrayList(ArrayList<T> elenco, String istanza) {

        // Stringa per memorizzare il nome o l'identificatore reale.
        StringBuilder realInstance = new StringBuilder();

        if (elenco == null) {
            return null;
        } else {
            // Estrai il nome o l'identificatore reale da istanza.
            for (char c : istanza.toCharArray()) {
                if (c == ' ')
                    break;
                realInstance.append(c);
            }

            for (T t : elenco) {
                // Controlla se l'istanza contiene il nome o l'identificatore reale.
                if (t.toString().contains(realInstance)) {
                    return t;  // Restituisce l'istanza interessata.
                }
            }

            return null;  // Restituisce null se non trova nessuna istanza.
        }
    }

    /**
     * Inserisce nel database il dipendente a tempo indeterminato specificato.
     *
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato} Il dipendente in insermento.
     */
    public void insertDipendenteIndeterminatoToDatabase(DipendenteIndeterminato dipendenteIndeterminato) {

        // Registra il dipendente a tempo indeterminato nel database.
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
        dipendenteIndeterminatoDB.addDipendenteIndeterminato(dipendenteIndeterminato.getNome(), dipendenteIndeterminato.getCognome(),
                dipendenteIndeterminato.getCodFiscale(), dipendenteIndeterminato.getMatricola(), dipendenteIndeterminato.getTipoDipendente().toString(),
                dipendenteIndeterminato.getIndirizzo(), dipendenteIndeterminato.getDataNascita(), dipendenteIndeterminato.getDataAssunzione(),
                dipendenteIndeterminato.getDataFineRapporto(), dipendenteIndeterminato.getDirigente());
    }

    /**
     * Inserisce nel database il dipendente a progetto specificato.
     *
     * @param dipendenteProgetto {@link DipendenteProgetto} Rappresenta il dipendente in inserimento.
     * @param progetto           {@link Progetto}           Rappresenta il progetto proprietario del dipendente.
     */
    public void insertDipendenteProgettoToDatabase(DipendenteProgetto dipendenteProgetto, Progetto progetto) {

        // Registra il dipendente a progetto nel database.
        DipendenteProgettoPostgresDAO dipendenteProgettoDB = new DipendenteProgettoPostgresDAO();
        dipendenteProgettoDB.addDipendenteProgetto(dipendenteProgetto.getNome(), dipendenteProgetto.getCognome(),
                dipendenteProgetto.getCodFiscale(), dipendenteProgetto.getMatricola(),
                dipendenteProgetto.getIndirizzo(), dipendenteProgetto.getDataNascita(),
                dipendenteProgetto.getDataAssunzione(), dipendenteProgetto.getScadenza(),
                progetto.getCup(), dipendenteProgetto.getCosto());
    }

    /**
     * Inserisce automaticamente gli scatti di carriera nel database, nel momento di aggiunta di un nuovo dipendente a tempo indeterminato.
     */
    public void insertAutomaticScatti() {

        // Dichiarazione di una possibile connessione al database per l'inserimento degli scatti di carriera.
        ScattoCarrieraPostgresDAO scattoCarrieraDB;
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB;

        // Controllo di coerenza tra dataAssunzione, tipo e data corrente per gli scatti di carriera.
        // Se i controlli passano, si registrano gli scatti di carriera.

        switch (dipendenteIndeterminato.getTipoDipendente()) {

            case Middle:

                // Data minima per lo scatto di carriera "Middle".
                LocalDate dataAssunzioneInfLimit = dipendenteIndeterminato.getDataAssunzione().plusYears(3);

                scattoMiddle = new ScattoCarriera(dataAssunzioneInfLimit, TipoScatto.Middle, dipendenteIndeterminato);

                // Aggiungi lo scatto "Middle" al model.
                addScattoCarrieraToElenco(scattoMiddle);

                // Aggiungi lo scatto "Middle" alle associazioni del model.
                dipendenteIndeterminato.addScattoCarriera(scattoMiddle);
                scattoMiddle.setDipendente(dipendenteIndeterminato);

                dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Middle);

                // Crea una connessione al database per l'inserimento dello scatto "Middle".
                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
                scattoCarrieraDB.addScattoCarriera(scattoMiddle.getTipoScatto().toString(), scattoMiddle.getDipendente().getMatricola(), scattoMiddle.getData());

                dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
                dipendenteIndeterminatoDB.updateTipoDipendente(dipendenteIndeterminato.getMatricola(), TipoDipendente.Middle.toString());

                // Verifica se il dipendente è dirigente e, in tal caso, registra lo scatto di carriera "Promosso_a_dirigente".
                insertScattoPromossoDirigenteNow();

                break;

            case Senior:

                // Data minima per lo scatto di carriera "Senior".
                LocalDate dataAssunzioneSeniorLimit = dipendenteIndeterminato.getDataAssunzione().plusYears(7);

                // Data minima per lo scatto di carriera "Middle".
                LocalDate dataAssunzioneMiddleLimit = dipendenteIndeterminato.getDataAssunzione().plusYears(3);

                // Crea gli scatti di carriera "Senior" e "Middle".
                scattoMiddle = new ScattoCarriera(dataAssunzioneMiddleLimit, TipoScatto.Middle, dipendenteIndeterminato);

                // Aggiungi scatto "Middle" al model.
                addScattoCarrieraToElenco(scattoMiddle);

                // Aggiungi lo scatto "Middle" alle associazioni del model.
                dipendenteIndeterminato.addScattoCarriera(scattoMiddle);
                scattoMiddle.setDipendente(dipendenteIndeterminato);

                // Crea una connessione al database per l'inserimento degli scatti "Middle" e "Senior".
                dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();

                // Inserisci lo scatto "Middle" nel database.
                scattoCarrieraDB.addScattoCarriera(scattoMiddle.getTipoScatto().toString(), scattoMiddle.getDipendente().getMatricola(), scattoMiddle.getData());
                dipendenteIndeterminatoDB.updateTipoDipendente(dipendenteIndeterminato.getMatricola(), TipoDipendente.Middle.toString());

                scattoSenior = new ScattoCarriera(dataAssunzioneSeniorLimit, TipoScatto.Senior, dipendenteIndeterminato);

                // Aggiungi scatto "Senior" al model.
                addScattoCarrieraToElenco(scattoSenior);

                // Aggiungi lo scatto "Senior" alle associazioni del model.
                dipendenteIndeterminato.addScattoCarriera(scattoSenior);
                scattoSenior.setDipendente(dipendenteIndeterminato);

                // Inserisci lo scatto "Senior" nel database.
                scattoCarrieraDB.addScattoCarriera(scattoSenior.getTipoScatto().toString(), scattoSenior.getDipendente().getMatricola(), scattoSenior.getData());
                dipendenteIndeterminatoDB.updateTipoDipendente(dipendenteIndeterminato.getMatricola(), TipoDipendente.Senior.toString());

                // Verifica se il dipendente è dirigente e, in tal caso, registra lo scatto di carriera "Promosso_a_dirigente".
                insertScattoPromossoDirigenteNow();

                break;

            default:

                // Se il tipo è "Junior", verifica solo lo stato dirigenziale.
                insertScattoPromossoDirigenteNow();
        }
    }

    /**
     * Inserisce uno scatto "Promosso_a_dirigente" con data odierna se il dipendente è stato promosso a dirigente.
     */
    public void insertScattoPromossoDirigenteNow() {

        if (dipendenteIndeterminato.getDirigente()) {

            ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
            DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();

            // Crea uno scatto "Promosso_a_dirigente" con la data odierna.
            scattoPromossoDirigente = new ScattoCarriera(LocalDate.now(), TipoScatto.Promosso_a_dirigente, dipendenteIndeterminato);

            // Aggiungi lo scatto "Promosso_a_dirigente" al model.
            addScattoCarrieraToElenco(scattoPromossoDirigente);

            // Aggiungi lo scatto "Promosso_a_dirigente" alle associazioni del model.
            dipendenteIndeterminato.addScattoCarriera(scattoPromossoDirigente);
            scattoPromossoDirigente.setDipendente(dipendenteIndeterminato);
            dipendenteIndeterminato.setDirigente(true);

            // Inserisci lo scatto "Promosso_a_dirigente" nel database.
            scattoCarrieraDB.addScattoCarriera(scattoPromossoDirigente.getTipoScatto().toString(), scattoPromossoDirigente.getDipendente().getMatricola(), scattoPromossoDirigente.getData());

            // Aggiorna lo stato di dirigente nel database.
            dipendenteIndeterminatoDB.updateStatoDirigente(dipendenteIndeterminato.getMatricola(), true);

        }
    }

    /**
     * Inserisce uno scatto "Rimosso_da_dirigente" con data odierna se il dipendente era un dirigente prima dell'operazione.
     *
     * @param vecchioDirigente true se il dipendente era un dirigente prima dell'operazione, altrimenti false.
     */
    public void insertScattoRimossoDirigenteNow(boolean vecchioDirigente) {

        if (vecchioDirigente && !dipendenteIndeterminato.getDirigente()) {

            ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
            DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();

            // Crea uno scatto "Rimosso_da_dirigente" con la data odierna.
            scattoRimossoDirigente = new ScattoCarriera(LocalDate.now(), TipoScatto.Rimosso_da_dirigente, dipendenteIndeterminato);

            // Aggiungi lo scatto "Rimosso_da_dirigente" al model.
            addScattoCarrieraToElenco(scattoRimossoDirigente);

            // Aggiungi lo scatto "Rimosso_da_dirigente" alle associazioni del model.
            dipendenteIndeterminato.addScattoCarriera(scattoRimossoDirigente);
            scattoRimossoDirigente.setDipendente(dipendenteIndeterminato);
            dipendenteIndeterminato.setDirigente(false);

            // Inserisci lo scatto "Rimosso_da_dirigente" nel database.
            scattoCarrieraDB.addScattoCarriera(scattoRimossoDirigente.getTipoScatto().toString(), scattoRimossoDirigente.getDipendente().getMatricola(), scattoRimossoDirigente.getData());

            // Aggiorna lo stato di dirigente nel database.
            dipendenteIndeterminatoDB.updateStatoDirigente(dipendenteIndeterminato.getMatricola(), false);

        }
    }

    /**
     * Inserisce automaticamente gli scatti di carriera nel database, caricando anche evenutali promozioni o rimozioni dirigenziali.
     * Viene utilizzata durante la creazione degli oggetti in memoria, ma non in fase di aggiunta di un nuovo dipedente a tempo indeterminato.
     *
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato} Il dipendente da inserire.
     * @param dataScattoPiuRecente      {@link LocalDate}   Lo scatto piu' recente di rimozione o promozione dirigenziale
     */

    public void insertAutomaticScatti(DipendenteIndeterminato dipendenteIndeterminato, LocalDate dataScattoPiuRecente) throws IllegalArgumentException {

        // Dichiarazione di una possibile connessione al database per l'inserimento degli scatti di carriera.
        ScattoCarrieraPostgresDAO scattoCarrieraDB;
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB;

        // Controllo di coerenza tra dataAssunzione, tipo e data corrente per gli scatti di carriera.
        // Se i controlli passano, si registrano gli scatti di carriera.
        ArrayList<ScattoCarriera> listaScatti = dipendenteIndeterminato.getScattiEffettuati();

        LocalDate dataAssunzioneInfLimit = dipendenteIndeterminato.getDataAssunzione().plusYears(3);
        LocalDate dataAssunzioneSupLimit = dipendenteIndeterminato.getDataAssunzione().plusYears(7);


        if (LocalDate.now().isBefore(dataAssunzioneInfLimit)) {

            //per questo dipendente junior non ci devono essere scatti carriera, per cui i presenti nel database verranno eliminati in quanto non coerenti
            removeAllTipoScattiCarrieraDipendente(dipendenteIndeterminato, TipoScatto.Senior);
            removeAllTipoScattiCarrieraDipendente(dipendenteIndeterminato, TipoScatto.Middle);

            //viene impostato il tipo dipendente a Junior
            dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Junior);

            //crea una connessione al database per aggiornare il tipo dipendente nella tabella apposita del database
            dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteIndeterminatoDB.updateTipoDipendente(dipendenteIndeterminato.getMatricola(), dipendenteIndeterminato.getTipoDipendente().toString());

        } else if (LocalDate.now().isAfter(dataAssunzioneInfLimit) && LocalDate.now().isBefore(dataAssunzioneSupLimit)) {

            //il dipendente e' un dipendente middle, per cui si verifica se gli scatti registrati nel database sono validi, se non lo sono vengono rimossi ed inseriti i nuovi
            scattoMiddle = new ScattoCarriera(dataAssunzioneInfLimit, TipoScatto.Middle, dipendenteIndeterminato);

            if (listaScatti == null || !listaScatti.contains(scattoMiddle)) {

                //crea le associazioni
                dipendenteIndeterminato.addScattoCarriera(scattoMiddle);
                scattoMiddle.setDipendente(dipendenteIndeterminato);

                //rimuove lo scatto di carriera non coerente eventualmente presente nel database
                removeAllTipoScattiCarrieraDipendente(scattoMiddle.getDipendente(), scattoMiddle.getTipoScatto());

                //crea una connessione al database per inserire lo scatto di carriera coerente
                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
                scattoCarrieraDB.addScattoCarriera(scattoMiddle.getTipoScatto().toString(), scattoMiddle.getDipendente().getMatricola(), scattoMiddle.getData());

                addScattoCarrieraToElenco(scattoMiddle);
            }

            //imposta il tipo di dipendente adeguato
            dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Middle);

            //crea una connessione al database per aggiornare il tipo dipendente nella tabella apposita del database
            dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteIndeterminatoDB.updateTipoDipendente(scattoMiddle.getDipendente().getMatricola(), scattoMiddle.getDipendente().getTipoDipendente().toString());

        } else if (LocalDate.now().isAfter(dataAssunzioneSupLimit)) {

            //il dipendente e' un dipendente middle, per cui si verifica se gli scatti registrati nel database sono validi, se non lo sono vengono rimossi ed inseriti i nuovi
            scattoMiddle = new ScattoCarriera(dataAssunzioneInfLimit, TipoScatto.Middle, dipendenteIndeterminato);

            if (listaScatti == null || !listaScatti.contains(scattoMiddle)) {

                //crea le associazioni
                dipendenteIndeterminato.addScattoCarriera(scattoMiddle);
                scattoMiddle.setDipendente(dipendenteIndeterminato);

                //rimuove lo scatto di carriera non coerente eventualmente presente nel database
                removeAllTipoScattiCarrieraDipendente(scattoMiddle.getDipendente(), scattoMiddle.getTipoScatto());

                //crea una connessione al database per inserire lo scatto di carriera coerente
                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
                scattoCarrieraDB.addScattoCarriera(scattoMiddle.getTipoScatto().toString(), scattoMiddle.getDipendente().getMatricola(), scattoMiddle.getData());

                addScattoCarrieraToElenco(scattoMiddle);
            }

            //il dipendente e' un dipendente senior, per cui si verifica se gli scatti registrati nel database sono validi, se non lo sono vengono rimossi ed inseriti i nuovi
            scattoSenior = new ScattoCarriera(dataAssunzioneSupLimit, TipoScatto.Senior, dipendenteIndeterminato);

            if (listaScatti == null || !listaScatti.contains(scattoSenior)) {

                //crea le associazioni
                dipendenteIndeterminato.addScattoCarriera(scattoSenior);
                scattoSenior.setDipendente(dipendenteIndeterminato);

                //rimuove lo scatto di carriera non coerente eventualmente presente nel database
                removeAllTipoScattiCarrieraDipendente(scattoSenior.getDipendente(), scattoSenior.getTipoScatto());

                //crea una connessione al database per inserire lo scatto di carriera coerente
                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
                scattoCarrieraDB.addScattoCarriera(scattoSenior.getTipoScatto().toString(), scattoSenior.getDipendente().getMatricola(), scattoSenior.getData());

                addScattoCarrieraToElenco(scattoSenior);
            }

            //imposta il tipo di dipendente adeguato
            dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Senior);

            //crea una connessione al database per aggiornare il tipo dipendente nella tabella apposita del database
            dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteIndeterminatoDB.updateTipoDipendente(scattoSenior.getDipendente().getMatricola(), scattoSenior.getDipendente().getTipoDipendente().toString());

        }

        //se un dipendente viene letto come dirigente, si controlla che non ci sia lo scatto ad esso associato, nel caso non ci sia, lo si aggiunge
        if (dipendenteIndeterminato.getDirigente()) {

            //recupera la data di promozione piu' recente del dipendente e le confronta
            LocalDate dataPromozionePiuRecente = recuperaDataScattoPiuRecente(dipendenteIndeterminato, TipoScatto.Promosso_a_dirigente);

            //osserva il caso in cui lo scatto non e' presente in memoria
            if (dataPromozionePiuRecente == null) {

                scattoPromossoDirigente = new ScattoCarriera(LocalDate.now(), TipoScatto.Promosso_a_dirigente, dipendenteIndeterminato);

                scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
                scattoCarrieraDB.addScattoCarriera(scattoPromossoDirigente.getTipoScatto().toString(), scattoPromossoDirigente.getDipendente().getMatricola(), scattoPromossoDirigente.getDipendente().getDataAssunzione());

            } else
                scattoPromossoDirigente = new ScattoCarriera(dataPromozionePiuRecente, TipoScatto.Promosso_a_dirigente, dipendenteIndeterminato);

            dipendenteIndeterminato.addScattoCarriera(scattoPromossoDirigente);
            scattoPromossoDirigente.setDipendente(dipendenteIndeterminato);

            dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
            dipendenteIndeterminatoDB.updateStatoDirigente(scattoPromossoDirigente.getDipendente().getMatricola(), true);

            addScattoCarrieraToElenco(scattoPromossoDirigente);

        } else {

            if (dataScattoPiuRecente != null) {

                scattoRimossoDirigente = new ScattoCarriera(dataScattoPiuRecente, TipoScatto.Rimosso_da_dirigente, dipendenteIndeterminato);

                dipendenteIndeterminato.addScattoCarriera(scattoRimossoDirigente);
                scattoRimossoDirigente.setDipendente(dipendenteIndeterminato);

                dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
                dipendenteIndeterminatoDB.updateStatoDirigente(scattoRimossoDirigente.getDipendente().getMatricola(), false);

                addScattoCarrieraToElenco(scattoRimossoDirigente);
            }
        }
    }

    // METODI GUI
    /*
     * 0 riuscito
     * 1 caso Middle: data troppo recente
     * 2 caso Middle: data troppo poco recente, passare a senior
     * 3 caso Senior: non c'e' uno scatto middle, registrare prima questo
     * 3 caso Senior: data troppo recente
     * 4 caso Promosso_a_dirigente: data di rimozione piu' recende della promozione
     * 5 caso Rimosso_da_darigente: data di promozione piu' recente della rimozione
     * */

    /**
     * Recupera la data dello scatto di carriera più recente di promozione o rimozione dirigenziale per un dipendente a tempo indeterminato specifico.
     *
     * @param dipendenteIndeterminato   {@link DipendenteIndeterminato}     Il dipendente specificato.
     * @param tipoScatto                {@link TipoScatto}                  Il tipo di scatto di carriera da cercare, tra Promosso_a_dirigente o Rimosso_da_dirigente.
     * @return La data dello scatto di carriera più recente del tipo specificato, o null se non è presente.
     */
    //Se viene ritornata una data null in tre casi: il tipo inserito non e' di promozione o rimozione, oppure la lista degli scatti del dipendente e' null oppure la lista degli scatti e' vuota.
    public LocalDate recuperaDataScattoPiuRecente(DipendenteIndeterminato dipendenteIndeterminato, TipoScatto tipoScatto) {

        LocalDate dataPiuRecente = null;
        LocalDate dataCorrente;
        boolean scattoIniziale = true;
        ArrayList<ScattoCarriera> listaScatti = dipendenteIndeterminato.getScattiEffettuati();

        if (tipoScatto.equals(TipoScatto.Promosso_a_dirigente) && listaScatti != null && !listaScatti.isEmpty()) {

            for (ScattoCarriera scattoCarriera : listaScatti) {

                if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente)) {

                    dataCorrente = scattoCarriera.getData();

                    if (scattoIniziale) {

                        dataPiuRecente = dataCorrente;
                        scattoIniziale = false;

                    } else {

                        if (dataPiuRecente.isBefore(dataCorrente))
                            dataPiuRecente = dataCorrente;

                    }
                }
            }

        } else if (tipoScatto.equals(TipoScatto.Rimosso_da_dirigente) && listaScatti != null && !listaScatti.isEmpty()) {

            for (ScattoCarriera scattoCarriera : listaScatti) {

                if (scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente)) {

                    dataCorrente = scattoCarriera.getData();

                    if (scattoIniziale) {

                        dataPiuRecente = dataCorrente;
                        scattoIniziale = false;

                    } else {

                        if (dataPiuRecente.isBefore(dataCorrente))
                            dataPiuRecente = dataCorrente;

                    }
                }
            }
        }

        return dataPiuRecente;

    }

    /**
     * Verifica la coerenza tra tre date, controllando se la data inferiore è antecedente alla data mediana
     * e, se specificata, la data superiore e' successiva alla data mediana.
     *
     * @param dataInf     {@link LocalDate}    La data inferiore.
     * @param dataMediana {@link LocalDate}    La data mediana.
     * @param dataSup     {@link LocalDate}    La data superiore.
     * @return true se le date non sono coerenti, false altrimenti.
     */
    public boolean notCoerenzaDate(LocalDate dataInf, LocalDate dataMediana, LocalDate dataSup) {

        boolean test = false;

        // Se la data superiore è nulla, verifica che la data mediana sia antecedente alla data inferiore.
        if (dataSup == null) {

            if (dataMediana.isBefore(dataInf))
                test = true;

        } else if (dataMediana != null) {

            // Se la data superiore non è nulla, verifica che sia antecedente alla data mediana
            // e che la data mediana sia precedente alla data superiore.
            if (dataInf.isAfter(dataMediana) || dataMediana.isAfter(dataSup))
                test = true;

        }

        return test;
    }

    /**
     * Verifica la coerenza tra le date contrattuali di un dipendente a progetto rispetto al progetto proprietario.
     *
     * @param scadenzaDipendente       {@link LocalDate}    Data di scadenza del contratto del dipendente a progetto.
     * @param progettoCup              {@link String}       Il codice CUP del progetto che ha ingaggiato il dipendente.
     * @return True se le date non sono coerenti con il progetto, false altrimenti.
     */
    public boolean coerenzaDateFineProgettoDipendente(LocalDate scadenzaDipendente, String progettoCup) {

        boolean test = false;

        // Recupera l'istanza del progetto da verificare (deve esistere)
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), progettoCup);

        // Verifica se il progetto ha una data di fine definita e se la data di scadenza contrattuale del dipendente e' successiva alla data di fine del progetto.
        if (progetto.getDataFine() != null && scadenzaDipendente.isAfter(progetto.getDataFine())) {
            test = true;
        }

        return test;
    }

    /**
     * Verifica se un dipendente è un dipendente a tempo indeterminato attivo.
     *
     * @param codFiscale {@link String} Codice fiscale del dipendente da verificare.
     * @return True se e' un dipendente a tempo indeterminato attivo, false altrimenti.
     */
    public boolean checkIsDipendenteIndeterminato(String codFiscale) {

        boolean test = false;

        if (getElencoAllDipendentiIndeterminati() != null) {

            for (DipendenteIndeterminato dip : getElencoAllDipendentiIndeterminati()) {

                if (dip.getCodFiscale().equals(codFiscale)) {

                    // Verifica se il dipendente è a tempo indeterminato e se il suo rapporto è attivo.
                    if (dip.getDataFineRapporto() == null || dip.getDataFineRapporto().isAfter(LocalDate.now())) {
                        test = true;
                        break;
                    }
                }
            }
        }

        return test;
    }

    /**
     * Verifica se un dipendente è un dipendente a progetto attivo.
     *
     * @param codFiscale {@link String} Codice fiscale del dipendente da verificare.
     * @return True se e' un dipendente a progetto attivo, false altrimenti.
     */
    public boolean checkIsDipendenteProgetto(String codFiscale) {

        boolean test = false;

        if (getElencoAllDipendentiProgetto() != null) {

            for (DipendenteProgetto dip : getElencoAllDipendentiProgetto()) {

                if (dip.getCodFiscale().equals(codFiscale)) {

                    // Verifica se il dipendente a progetto ha una data di scadenza futura.
                    if (dip.getScadenza().isAfter(LocalDate.now())) {
                        test = true;
                        break;
                    }
                }
            }

        }

        return test;
    }

    /**
     * Verifica l'unicità di una matricola nel sistema, sia in modifica sia in inserimento.
     *
     * @param vecchiaMatricola {@link String}   La matricola attuale del dipendente, se in inserimento e' nulla.
     * @param matricola        {@link String}   La nuova matricola del dipendente.
     * @param modificatoMode   True se la verifica è in modalità modifica, false altrimenti.
     * @return True se la matricola è unica, false altrimenti.
     */
    public boolean checkMatricolaUnica(String vecchiaMatricola, String matricola, boolean modificatoMode) {

        boolean test = true;

        if (getElencoAllDipendentiIndeterminati() != null || getElencoAllDipendentiProgetto() != null) {

            if (getElencoAllDipendentiIndeterminati() != null) {

                if (modificatoMode) {

                    DipendenteIndeterminato dipAttuale = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), vecchiaMatricola);

                    for (DipendenteIndeterminato dip : getElencoAllDipendentiIndeterminati()) {

                        if (dip.getMatricola().equals(matricola) && !dip.equals(dipAttuale)) {
                            test = false;
                            break;
                        }
                    }

                } else {

                    for (DipendenteIndeterminato dip : getElencoAllDipendentiIndeterminati()) {

                        if (dip.getMatricola().equals(matricola)) {
                            test = false;
                            break;
                        }
                    }
                }

            } else {

                if (test) {

                    if (modificatoMode) {

                        DipendenteProgetto dipAttuale = getIstanceFromArrayList(getElencoAllDipendentiProgetto(), vecchiaMatricola);

                        for (DipendenteProgetto dip : getElencoAllDipendentiProgetto()) {

                            if (dip.getMatricola().equals(matricola) && !dip.equals(dipAttuale)) {
                                test = false;
                                break;
                            }
                        }

                    } else {

                        for (DipendenteProgetto dip : getElencoAllDipendentiProgetto()) {

                            if (dip.getMatricola().equals(matricola)) {
                                test = false;
                                break;
                            }
                        }
                    }
                }

            }
        }

        return test;
    }

    /**
     * Verifica se il nuovo CUP è unico rispetto gli altri progetti.
     *
     * @param vecchioCup {@link String} Il CUP del progetto precedente, se in inserimento e' nullo.
     * @param nuovoCup   {@link String} Il nuovo CUP da verificare.
     * @return True se il CUP è unico, altrimenti False.
     */
    public boolean checkCupUnico(String vecchioCup, String nuovoCup) {

        boolean test = true;

        // Se c'è un CUP precedente, otteniamo l'istanza del progetto corrispondente.
        if (vecchioCup != null)
            progetto = getIstanceFromArrayList(getElencoAllProgetti(), vecchioCup);

        // Scansione di tutti i progetti per verificare l'unicità del nuovo CUP.
        for (Progetto currentProgetto : getElencoAllProgetti()) {

            if (vecchioCup != null) {

                // Se stiamo modificando un progetto esistente, ignoriamo il suo stesso CUP.
                if (!currentProgetto.equals(progetto) && currentProgetto.getCup().equals(nuovoCup))
                    test = false;

            } else {

                // Se stiamo creando un nuovo progetto, verifichiamo che il CUP sia unico.
                if (currentProgetto.getCup().equals(nuovoCup))
                    test = false;
            }
        }

        return test;
    }

    /**
     * Verifica se il nome del laboratorio è unico rispetto gli altri laboratori.
     *
     * @param nomeLab {@link String}    Il nome del laboratorio da verificare.
     * @return True se il nome del laboratorio è unico, altrimenti False.
     */
    public boolean checkNomeLabUnico(String nomeLab) {

        boolean test = true;

        // Se trova un laboratorio con nell'elenco, allora nella verifica salta quel laboratorio, altrimenti li ispeziona tutti

        // Otteniamo l'istanza del laboratorio corrispondente al nome.
        laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), nomeLab);

        if (laboratorio == null){

            // Ispeziona tutti i laboratori per cercarne uno con lo stesso nome inserito
            for (Laboratorio laboratorio : getElencoAllLaboratori()){

                if (laboratorio.getNome().equals(nomeLab)){
                    test = false;
                    break;
                }
            }

        } else {

            // Ispeziona tutti i laboratori tranne il laboratorio trovato
            for (Laboratorio currentLaboratorio : getElencoAllLaboratori()){

                if (currentLaboratorio.equals(laboratorio))
                    continue;

                if (currentLaboratorio.getNome().equals(nomeLab)){

                    test = false;
                    break;
                }
            }
        }

        return test;
    }

    /**
     * Verifica se un dipendente indeterminato è attualmente attivo (con data di fine rapporto non nulla), sia in modifica sia in inserimento.
     *
     * @param modificaMode                      True se in modifica, false se in inserimento.
     * @param codFiscale        {@link String}  Il codice fiscale del dipendente.
     * @param vecchiaMatricola  {@link String}  La matricola del vecchio dipendente, se in inserimento e' nulla.
     * @return True se il dipendente è attivo, altrimenti False.
     */
    public boolean isDipendenteIndeterminatoActive(boolean modificaMode, String codFiscale, String vecchiaMatricola) {

        boolean test = false;

        if (modificaMode) {
            // Otteniamo l'istanza del dipendente indeterminato corrispondente alla matricola.

            if (getElencoAllDipendentiIndeterminati() != null) {
                // Scansione di tutti i dipendenti indeterminati per verificare l'attività.
                for (DipendenteIndeterminato dipendenteRicercato : getElencoAllDipendentiIndeterminati()) {

                    if (dipendenteRicercato.getDataFineRapporto() != null && !dipendenteRicercato.getMatricola().equals(vecchiaMatricola) && dipendenteRicercato.getCodFiscale().equals(codFiscale)) {
                        test = true;
                        break;
                    }
                }
            }

        } else {

            if (getElencoAllDipendentiProgetto() != null) {
                for (DipendenteIndeterminato dipendenteRicercato : getElencoAllDipendentiIndeterminati()) {

                    if (dipendenteRicercato.getDataFineRapporto() != null && dipendenteRicercato.getCodFiscale().equals(codFiscale)) {
                        test = true;
                        break;
                    }
                }
            }
        }

        return test;
    }

    /**
     * Verifica se un dipendente a progetto è attualmente attivo (con data di fine rapporto non nulla), sia in modifica sia in inserimento.
     *
     * @param modificaMode                              True se in modifica, false se in inserimento.
     * @param codFiscale            {@link String}      Il nuovo codice fiscale del dipendente.
     * @param nuovaDataAssunzione   {@link LocalDate}   La nuova data di assunzione del dipendente.
     * @param nuovaScadenza         {@link LocalDate}   La nuova data di scadenza del dipendente.
     * @param vecchiaMatricola      {@link String}      La vecchia matricola del dipendente, se in inserimento e' nulla.
     * @return True se il dipendente è attivo, altrimenti False.
     */
    public boolean isDipendenteProgettoActive(boolean modificaMode, String codFiscale, LocalDate nuovaDataAssunzione, LocalDate nuovaScadenza, String vecchiaMatricola) {

        boolean test = false;

        if (getElencoAllDipendentiProgetto() != null) {

            if (modificaMode) {

                // Scansione di tutti i dipendenti indeterminati per verificare l'attività.
                for (DipendenteProgetto dipendenteRicercato : getElencoAllDipendentiProgetto()) {

                    if (!dipendenteRicercato.getMatricola().equals(vecchiaMatricola) && dipendenteRicercato.getCodFiscale().equals(codFiscale) && ( (dipendenteRicercato.getDataAssunzione().isBefore(nuovaDataAssunzione) && dipendenteRicercato.getScadenza().isAfter(nuovaDataAssunzione)) || (dipendenteRicercato.getDataAssunzione().isBefore(nuovaScadenza) && dipendenteRicercato.getScadenza().isAfter(nuovaScadenza))) ) {
                        test = true;
                        break;
                    }
                }
            } else {

                for (DipendenteProgetto dipendenteRicercato : getElencoAllDipendentiProgetto()) {

                    if (dipendenteRicercato.getCodFiscale().equals(codFiscale) && ( (dipendenteRicercato.getDataAssunzione().isBefore(nuovaDataAssunzione) && dipendenteRicercato.getScadenza().isAfter(nuovaDataAssunzione)) || (dipendenteRicercato.getDataAssunzione().isBefore(nuovaScadenza) && dipendenteRicercato.getScadenza().isAfter(nuovaScadenza)))) {
                        test = true;
                        break;
                    }
                }
            }
        }

        return test;
    }

    /**
     * Verifica la coerenza dei dati anagrafici di un dipendente con lo stesso codice fiscale, sia in modifica sia in inserimento.
     *
     * @param vecchioNome           {@link String}      Il vecchio nome del dipendeente, se in inserimento e' nullo.
     * @param vecchioCognome        {@link String}      Il vecchio cognome del dipendente, se in inserimento e' nullo.
     * @param vecchioCodFiscale     {@link String}      Il vecchio codice fiscale del dipendente, se in inserimento e' nullo.
     * @param vecchiaDataNascita    {@link String}      La vecchia data di nascita del dipendente, se in inserimento e' nulla.
     * @param codFiscale            {@link String}      Il nuovo codice fiscale da verificare.
     * @param nome                  {@link String}      Il nome associato al codice fiscale.
     * @param cognome               {@link String}      Il cognome associato al codice fiscale.
     * @param dataNascita           {@link LocalDate}   La data di nascita associata al codice fiscale.
     * @param modificatoMode    True se la verifica è in modalità di modifica, altrimenti False.
     * @return True se i dati anagrafici non sono coerenti, false altrimenti.
     */
    public boolean coerenzaCodiceFiscale(String vecchioNome, String vecchioCognome, String vecchioCodFiscale, LocalDate vecchiaDataNascita,
                                         String codFiscale, String nome, String cognome, LocalDate dataNascita, boolean modificatoMode) {

        boolean test = false;

        if (getElencoAllDipendentiIndeterminati() != null || getElencoAllDipendentiProgetto() != null) {

            if (getElencoAllDipendentiIndeterminati() != null) {

                if (modificatoMode) {

                    //distinguo due situazioni:
                    //1. se il codice fiscale non cambia, controllo che i dati anagrafici in modifica siano uguali ai dati anagrafici precedenti
                    //2. se cambia il codice fiscale, controllo che non ci sia un altro dipendente con il nuovo codice fiscale ma con dati anagrafici diversi

                    if (vecchioCodFiscale.equals(codFiscale)) {

                        // il codice fiscale non cambia, controllo che i dati anagrafici in modifica siano uguali ai dati anagrafici precedenti

                        if (!vecchioNome.equals(nome) || !vecchioCognome.equals(cognome) || !vecchiaDataNascita.equals(dataNascita))
                            test = true;

                    } else {

                        // cambia il codice fiscale, controllo che non ci sia un altro dipendente con il nuovo codice fiscale ma con dati anagrafici diversi

                        //cerco un dipendente a tempo indeterminato con il nuovo codice fiscale
                        for (DipendenteIndeterminato dipNuovoCodFiscale : getElencoAllDipendentiIndeterminati()) {

                            if (dipNuovoCodFiscale.getCodFiscale().equals(codFiscale) &&
                                    (!dipNuovoCodFiscale.getNome().equals(nome) || !dipNuovoCodFiscale.getCognome().equals(cognome) || !dipNuovoCodFiscale.getDataNascita().equals(dataNascita))) {

                                test = true;
                                break;
                            }
                        }
                    }

                } else {

                    // Ricerca di un dipendente indeterminato con lo stesso codice fiscale.
                    for (DipendenteIndeterminato dip : getElencoAllDipendentiIndeterminati()) {

                        if (dip.getCodFiscale().equals(codFiscale) &&
                                (!dip.getNome().equals(nome) || !dip.getCognome().equals(cognome) || !dip.getDataNascita().equals(dataNascita))) {

                            test = true;
                            break;
                        }
                    }
                }

            } else {

                if (!test) {

                    if (modificatoMode) {

                        //distinguo due situazioni:
                        //1. se il codice fiscale non cambia, controllo che i dati anagrafici in modifica siano uguali ai dati anagrafici precedenti
                        //2. se cambia il codice fiscale, controllo che non ci sia un altro dipendente con il nuovo codice fiscale ma con dati anagrafici diversi

                        if (vecchioCodFiscale.equals(codFiscale)) {

                            // il codice fiscale non cambia, controllo che i dati anagrafici in modifica siano uguali ai dati anagrafici precedenti

                            if (!vecchioNome.equals(nome) || !vecchioCognome.equals(cognome) || !vecchiaDataNascita.equals(dataNascita))
                                test = true;

                        } else {

                            // cambia il codice fiscale, controllo che non ci sia un altro dipendente con il nuovo codice fiscale ma con dati anagrafici diversi

                            //cerco un dipendente a tempo indeterminato con il nuovo codice fiscale
                            for (DipendenteProgetto dipNuovoCodFiscale : getElencoAllDipendentiProgetto()) {

                                if (dipNuovoCodFiscale.getCodFiscale().equals(codFiscale) &&
                                        (!dipNuovoCodFiscale.getNome().equals(nome) || !dipNuovoCodFiscale.getCognome().equals(cognome) || !dipNuovoCodFiscale.getDataNascita().equals(dataNascita))) {

                                    test = true;
                                    break;
                                }
                            }
                        }

                    } else {

                        // Ricerca di un dipendente a progetto con lo stesso codice fiscale.
                        for (DipendenteIndeterminato dip : getElencoAllDipendentiIndeterminati()) {

                            if (dip.getCodFiscale().equals(codFiscale) &&
                                    (!dip.getNome().equals(nome) || !dip.getCognome().equals(cognome) || !dip.getDataNascita().equals(dataNascita))) {

                                test = true;
                                break;
                            }
                        }

                    }
                }

            }

        }

        return test;
    }

    /**
     * Verifica se il numero di laboratori lavoranti su un progetto supera il limite consentito, ovvero 3, in modifica o in inserimento.
     *
     * @param modificaMode                          True se in modifica, false se in inserimento.
     * @param stringProgetto        {@link String}  I dati del nuovo progetto.
     * @param stringVecchioProgetto {@link String}  I dati del vecchio progetto.
     *
     * @return True se il numero di laboratori lavoranti supera il limite, false altrimenti.
     */
    public boolean checkMaxLaboratoriLavoranti(boolean modificaMode, String stringProgetto, String stringVecchioProgetto) {

        boolean test = false;

        // Recupera l'istanza del progetto per cui effettuare la verifica, se specificato rappresenta il nuovo progetto (in modifica)
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);

        if (!modificaMode) {

            // Se il progetto ha più di 3 laboratori lavoranti, segnala l'errore
            if (progetto.getLaboratoriLavoranti().size() >= 3) {
                test = true;
            }
        } else {

            // Recupera l'istanza del vecchio progetto per cui effettuare la verifica
            Progetto vecchioProgetto = getIstanceFromArrayList(getElencoAllProgetti(), stringVecchioProgetto);

            if (!vecchioProgetto.equals(progetto)){

                // Se il progetto ha più di 3 laboratori lavoranti, segnala l'errore
                if (progetto.getLaboratoriLavoranti().size() >= 3) {
                    test = true;
                }
            }
        }

        return test;
    }

    /**
     * Verifica se l'acquisto di un'attrezzatura supera la metà del budget assegnato a un progetto, in modifica o in inserimento.
     *
     * @param modificaMode                              True se in modifica, false se in inserimento.
     * @param costo             {@link BigDecimal}      Il nuovo costo da verificare.
     * @param stringProgetto    {@link String}          I dati del nuovo progetto da verificare.
     * @param vecchioId         {@link Integer}         Il vecchio identificativo numerico dell'attrezzatura.
     * @return  True se il costo supera la metà del budget, false altrimenti.
     */
    public boolean checkAcquistoAttrezzaturaHalfBudget(boolean modificaMode, BigDecimal costo, String stringProgetto, String vecchioId) {

        boolean test = false;

        //recupero il budget del progetto
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);
        BigDecimal halfBudgetProgetto = progetto.getBudget().divide(BigDecimal.valueOf(2));

        //recupero il costo totale delle attrezzature associato a quel progetto
        BigDecimal costoTotaleAttrezzature = getCostoTotaleAttrezzatureProgetto(progetto.getCup());

        if (!modificaMode) {

            costoTotaleAttrezzature = costoTotaleAttrezzature.add(costo);

            if (costoTotaleAttrezzature.compareTo(halfBudgetProgetto) > 0)
                test = true;

        } else if (!(attrezzatura = getIstanceFromArrayList(getElencoAllAttrezzature(), vecchioId)).getCosto().equals(costo)){

            BigDecimal vecchioCosto = attrezzatura.getCosto();

            // Sottrae il costo dell'attrezzatura da modificare al costo totale
            costoTotaleAttrezzature = costoTotaleAttrezzature.subtract(vecchioCosto);

            // Aggiunge
            costoTotaleAttrezzature = costoTotaleAttrezzature.add(costo);

            if (costoTotaleAttrezzature.compareTo(halfBudgetProgetto) > 0)
                test = true;
        }

        return test;
    }

    /**
     * Verifica se l'ingaggio di un dipendente a progetto supera la metà del budget assegnato a un progetto, in modifica o in inserimento.
     *
     * @param modificaMode                          True se in modifica, false se in inserimento.
     * @param costo             {@link BigDecimal}  Il nuovo costo dell'ingaggio del dipendente a progetto.
     * @param stringProgetto    {@link String}      I dati del nuovo progetto che ingaggia.
     * @param vecchiaMatricola  {@link String}      La matricola del vecchio dipendente a progetto, se in inserimento e' nulla.
     * @return  True se il costo supera la metà del budget, false altrimenti.
     */
    public boolean checkAcquistoDipendenteProgettoHalfBudget(boolean modificaMode, BigDecimal costo, String stringProgetto, String vecchiaMatricola) {

        boolean test = true;

        // Recupera il budget del progetto.
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);
        BigDecimal halfBudgetProgetto = progetto.getBudget().divide(BigDecimal.valueOf(2));

        // Recupera il costo totale dei dipendenti associati a quel progetto.
        BigDecimal costoTotaleDipendentiProgetto = getCostoTotaleDipendentiProgettoProgetto(progetto.getCup());

        if (!modificaMode) {

            costoTotaleDipendentiProgetto = costoTotaleDipendentiProgetto.add(costo);

            if (costoTotaleDipendentiProgetto.compareTo(halfBudgetProgetto) > 0)
                test = true;

        } else if (!(dipendenteProgetto = getIstanceFromArrayList(getElencoAllDipendentiProgetto(), vecchiaMatricola)).getCosto().equals(costo)){

            BigDecimal vecchioCosto = dipendenteProgetto.getCosto();

            // Sottrae il costo dell'attrezzatura da modificare al costo totale
            costoTotaleDipendentiProgetto = costoTotaleDipendentiProgetto.subtract(vecchioCosto);

            // Aggiunge
            costoTotaleDipendentiProgetto = costoTotaleDipendentiProgetto.add(costo);

            if (costoTotaleDipendentiProgetto.compareTo(halfBudgetProgetto) > 0)
                test = true;
        }

        return test;
    }

    /**
     * Verifica se il nuovo budget proposto per un progetto e' leggittimo, considerate le spese del progetto.
     *
     * @param budget         {@link BigDecimal} Il nuovo budget.
     * @param stringProgetto {@link String}     I dati del progetto.
     * @return True se il budget è legittimo, altrimenti False.
     */
    public boolean checkNewBudgetIsLegit(BigDecimal budget, String stringProgetto) {

        boolean test = true;

        // Recupera il budget del progetto.
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);
        BigDecimal halfBudgetProgetto = budget.divide(BigDecimal.valueOf(2));

        // Recupera il costo totale delle attrezzature e dei dipendenti associati a quel progetto.
        BigDecimal costoTotaleAttrezzature = getCostoTotaleAttrezzatureProgetto(progetto.getCup());
        BigDecimal costoTotaleDipendentiProgetto = getCostoTotaleDipendentiProgettoProgetto(progetto.getCup());

        // Verifica se il costo totale supera la metà del budget del progetto.
        if (costoTotaleAttrezzature.compareTo(halfBudgetProgetto) > 0 || costoTotaleDipendentiProgetto.compareTo(halfBudgetProgetto) > 0)
            test = false;

        return test;
    }

    /**
     * Ottieni il costo totale delle attrezzature acquistate da un progetto specificato.
     *
     * @param progettoCup {@link String}        Il CUP del progetto, unico nel sistema.
     * @return            {@link BigDecimal}    Il costo totale delle attrezzature acquistate dal progetto.
     */
    public BigDecimal getCostoTotaleAttrezzatureProgetto(String progettoCup) {
        ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
        return progettoDB.getCostoTotaleAttrezzature(progettoCup);
    }

    /**
     * Ottieni il costo totale dei dipendenti ingaggiati da un progetto specificato.
     *
     * @param progettoCup {@link String}       Il CUP del progetto, unico nel sistema.
     * @return            {@link BigDecimal}   Il costo totale dei dipendenti ingaggiati dal progetto.
     */
    public BigDecimal getCostoTotaleDipendentiProgettoProgetto(String progettoCup) {
        ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
        return progettoDB.getCostoTotaleDipendentiProgetto(progettoCup);
    }

    /**
     * Verifica se una stringa è composta da caratteri alfanumerici.
     *
     * @param string {@link String} La stringa da verificare.
     * @return True se è alfanumerica, false altrimenti.
     */
    public boolean isAlphanumeric(String string) {
        boolean flag = true;

        // Verifica se la stringa contiene solo caratteri alfanumerici
        for (char c : string.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    /**
     * Verifica se una stringa può essere convertita in un oggetto LocalDate.
     *
     * @param stringDate {@link String} La stringa da verificare come data.
     * @return True se la conversione è possibile, false altrimenti.
     */
    public boolean verifyDate(String stringDate) {
        boolean test = true;

        try {
            LocalDate date = LocalDate.parse(stringDate);
        } catch (DateTimeParseException dateTimeParseException) {
            test = false;
        }

        return test;
    }

    /**
     * Verifica se una stringa può essere convertita in un oggetto BigDecimal.
     *
     * @param stringBigDecimal {@link String} La stringa da verificare come valore BigDecimal.
     * @return True se la conversione è possibile, false altrimenti.
     */
    public boolean verifyBigDecimal(String stringBigDecimal) {
        boolean test = true;

        try {
            BigDecimal costo = new BigDecimal(stringBigDecimal);
        } catch (NumberFormatException numberFormatException) {
            test = false;
        }

        return test;
    }

    /**
     * Verifica la coerenza della data di assunzione rispetto al tipo di dipendente a tempo indeterminato fornito.
     *
     * @param dataAssunzione {@link LocalDate}      La data di assunzione del dipendente.
     * @param tipoDipendente {@link TipoDipendente} Il tipo di dipendente.
     *
     * @return 0 se il test è superato, 1 se la data specificata e' troppo poco recente per l'inserimento di un dipendente "Junior".
     * 2 se la data odierna è troppo recente per considerare lo scatto "Middle" valido, 3 se la data odierna è troppo vecchia per considerare lo scatto "Middle" valido,
     * 4 se la data odierna è troppo recente per considerare lo scatto "Senior" valido.
     */
    public int verifyDateInterval(LocalDate dataAssunzione, String tipoDipendente) {

        //quando ritorna 0 non c'e alcun problema
        int test = 0;

        if (TipoDipendente.valueOf(tipoDipendente).equals(TipoDipendente.Junior)) {

            LocalDate dateSupLimit = dataAssunzione.plusYears(3);

            if (LocalDate.now().isAfter(dateSupLimit))
                test = 1;

        } else if (TipoDipendente.valueOf(tipoDipendente).equals(TipoDipendente.Middle)) {

            LocalDate dateInfLimit = dataAssunzione.plusYears(3);
            LocalDate dateSupLimit = dataAssunzione.plusYears(7);

            if (LocalDate.now().isBefore(dateInfLimit))
                test = 2;
            else if (LocalDate.now().isAfter(dateSupLimit))
                test = 3;

        } else if (TipoDipendente.valueOf(tipoDipendente).equals(TipoDipendente.Senior)) {

            LocalDate dateInfLimit = dataAssunzione.plusYears(7);

            if (LocalDate.now().isBefore(dateInfLimit))
                test = 4;

        }

        return test;
    }

    //METODI LETTURA
    //Il ruolo di tali metodi e' quello di creare adeguatamente gli oggetti in memoria a partire dalle informazioni contenute nel database

    /**
     * Legge tutti i dipendenti indeterminati dal database, crea oggetti in memoria e li aggiunge all'elenco dei dipendenti a tempo indeterminato.
     *
     * @return True se i dati non presentano incoerenze irrisolvibili automaticamente, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiDipendentiIndeterminati() throws SQLException{

        boolean coerenzaDatiLettura;

        // Creazione di liste per memorizzare i dati dei dipendenti indeterminati dal database.
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> codFiscali = new ArrayList<>();
        ArrayList<String> matricole = new ArrayList<>();
        ArrayList<String> tipiDipendente = new ArrayList<>();
        ArrayList<String> indirizzi = new ArrayList<>();
        ArrayList<LocalDate> dateNascita = new ArrayList<>();
        ArrayList<LocalDate> dateAssunzioni = new ArrayList<>();
        ArrayList<LocalDate> dateFine = new ArrayList<>();
        ArrayList<Boolean> dirigenti = new ArrayList<>();

        // Estrae i dati dei dipendenti indeterminati dal database.
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoDB = new DipendenteIndeterminatoPostgresDAO();
        dipendenteIndeterminatoDB.obtainDipendentiIndeterminati(nomi, cognomi, codFiscali, matricole, tipiDipendente, indirizzi, dateNascita, dateAssunzioni, dateFine, dirigenti);

        // Loop attraverso i dati estratti per creare oggetti DipendenteIndeterminato.
        for (int i = 0; i < nomi.size(); i++) {

            dipendenteIndeterminato = new DipendenteIndeterminato(nomi.get(i), cognomi.get(i), codFiscali.get(i), matricole.get(i),
                    TipoDipendente.valueOf(tipiDipendente.get(i)), indirizzi.get(i), dateNascita.get(i), dateAssunzioni.get(i), dateFine.get(i), dirigenti.get(i));

            // Controlla che il dipendente non sia gia' un dipendente a progetto
            if (checkIsDipendenteProgetto(dipendenteIndeterminato.getCodFiscale()))
                return false;

            // Controlla che non ci siano dipendenti con lo stesso codice fiscale ma con dati anagrafici diversi
            if (coerenzaCodiceFiscale(null, null, null, null, dipendenteIndeterminato.getCodFiscale(),
                                        dipendenteIndeterminato.getNome(), dipendenteIndeterminato.getCognome(), dipendenteIndeterminato.getDataNascita(), false))
                return false;

            // Controlla se il dipedente a tempo indeterminato sia gia' attivo
            if (isDipendenteIndeterminatoActive(false, dipendenteIndeterminato.getCodFiscale(), null))
                return false;

            // Controlla se il dipendente abbia una dataFine impostata ma ricopra ancora il ruolo di responsabile scientifico
            if (dipendenteIndeterminato.getDataFineRapporto() != null && checkResponsabileScientificoLaboratorioLetture(dipendenteIndeterminato.getMatricola()))
                return false;

            // Controlla se il dipendente abbia una dataFine impostata ma ricopra il ruolo di responsabile scientifico senza avere il giusto tipo
            if (dipendenteIndeterminato.getDataFineRapporto() != null && !dipendenteIndeterminato.getTipoDipendente().equals(TipoDipendente.Senior) && checkResponsabileScientificoLaboratorioLetture(dipendenteIndeterminato.getMatricola()))
                return false;

            // Aggiunge il dipendente indeterminato all'elenco.
            addDipendenteIndeterminatoToElenco(dipendenteIndeterminato);
        }

        // Legge gli scatti di carriera, per poter successivamente correggere o registrare nel database e nella memoria eventuali scatti mancanti o incoerenti
        coerenzaDatiLettura = leggiScattiCarriera();

        if (coerenzaDatiLettura) {

            LocalDate dataScattoDirigente;
            LocalDate dataRimozioneDirigente;

            //inserisce tutti gli scatti carriera
            if (getElencoAllDipendentiIndeterminati() != null) {

                for (DipendenteIndeterminato dipendente : getElencoAllDipendentiIndeterminati()) {

                    // Recupera le date di promozione e rimozione piu' recenti, recupera valori null se non esistono.
                    dataScattoDirigente = recuperaDataScattoPiuRecente(dipendente, TipoScatto.Promosso_a_dirigente);
                    dataRimozioneDirigente = recuperaDataScattoPiuRecente(dipendente, TipoScatto.Rimosso_da_dirigente);

                    // Inserimento automatico di scatti di carriera corretti
                    if (dipendente.getDirigente()) {
                        insertAutomaticScatti(dipendente, dataScattoDirigente);
                    } else {
                        insertAutomaticScatti(dipendente, dataRimozioneDirigente);
                    }

                }
            }
        }

        return coerenzaDatiLettura;
    }

    /**
     * Legge tutti gli scatti di carriera dal database, crea oggetti in memoria e li aggiunge all'elenco degli scatti di carriera.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiScattiCarriera() throws SQLException{

        boolean coerenzaDati = true;

        // Creazione di liste per memorizzare i dati degli scatti di carriera dal database.
        ArrayList<String> tipiScatto = new ArrayList<>();
        ArrayList<String> stringDipendenti = new ArrayList<>();
        ArrayList<LocalDate> date = new ArrayList<>();

        // Estrae i dati degli scatti di carriera dal database.
        ScattoCarrieraPostgresDAO scattoCarrieraDB = new ScattoCarrieraPostgresDAO();
        scattoCarrieraDB.obtainScattiCarriera(tipiScatto, stringDipendenti, date);

        // Loop attraverso i dati estratti e crea oggetti ScattoCarriera.
        for (int i = 0; i < stringDipendenti.size(); i++) {

            // Ottiene un'istanza di DipendenteIndeterminato dal nome del dipendente.
            dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringDipendenti.get(i));

            // Crea un oggetto ScattoCarriera.
            scattoCarriera = new ScattoCarriera(date.get(i), TipoScatto.valueOf(tipiScatto.get(i)), dipendenteIndeterminato);

            // Si verifica che la data dello scatto sia precedente rispetto la data assunzione del dipendente.
            if (checkNotCoerenzaDataAssunzioneDataScatto(scattoCarriera.getDipendente().getMatricola(), scattoCarriera.getData()))
                return false;

            // Aggiunge lo scatto di carriera al dipendente
            dipendenteIndeterminato.addScattoCarriera(scattoCarriera);

            if (scattoCarriera.getTipoScatto().equals(TipoScatto.Middle))
                dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Middle);
            else if (scattoCarriera.getTipoScatto().equals(TipoScatto.Senior))
                dipendenteIndeterminato.setTipoDipendente(TipoDipendente.Senior);
            else if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente))
                dipendenteIndeterminato.setDirigente(true);
            else if (scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                dipendenteIndeterminato.setDirigente(false);

            // Aggiunge lo scatto di carriera all'elenco.
            addScattoCarrieraToElenco(scattoCarriera);
        }

        //verifica che non ci siano incongruenze nell'alternanza di promozioni e rimozioni a ruoli dirigenziali
        if (getElencoAllScattiCarriera() != null)
            coerenzaDati = verificaIntegritaAllPromozioniRimozioni();

        return coerenzaDati;
    }

    /**
     * Legge tutti i dipendenti a progetto dal database, crea oggetti in memoria e li aggiunge all'elenco dei dipendenti a progetto.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiDipendentiProgetto() throws SQLException{

        boolean integro = true;

        // Creazione di liste per memorizzare i dati dei dipendenti di progetto dal database.
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> codFiscali = new ArrayList<>();
        ArrayList<String> matricole = new ArrayList<>();
        ArrayList<String> indirizzi = new ArrayList<>();
        ArrayList<LocalDate> dateNascita = new ArrayList<>();
        ArrayList<LocalDate> dateAssunzioni = new ArrayList<>();
        ArrayList<LocalDate> scadenze = new ArrayList<>();
        ArrayList<BigDecimal> costiContratti = new ArrayList<>();
        ArrayList<String> stringProgetti = new ArrayList<>();

        // Estrae i dati dei dipendenti di progetto dal database.
        DipendenteProgettoPostgresDAO dipendenteProgettoDB = new DipendenteProgettoPostgresDAO();
        dipendenteProgettoDB.obtainDipendentiProgetto(nomi, cognomi, codFiscali, matricole, indirizzi, dateNascita, dateAssunzioni, scadenze, costiContratti, stringProgetti);

        // Loop attraverso i dati estratti e crea oggetti DipendenteProgetto.
        for (int i = 0; i < nomi.size(); i++) {

            // Ottieni un'istanza di Progetto dal nome del progetto.
            progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetti.get(i));

            // Crea un oggetto DipendenteProgetto.
            dipendenteProgetto = new DipendenteProgetto(nomi.get(i), cognomi.get(i), codFiscali.get(i), matricole.get(i),
                    indirizzi.get(i), dateNascita.get(i), dateAssunzioni.get(i), scadenze.get(i), costiContratti.get(i), progetto);

            // Controlla che il dipendente non sia gia' un dipendente indeterminato
            if (checkIsDipendenteIndeterminato(dipendenteProgetto.getCodFiscale()))
                return false;

            // Controlla che non ci siano dipendenti con lo stesso codice fiscale ma con dati anagrafici diversi
            if (coerenzaCodiceFiscale(null, null, null, null, dipendenteProgetto.getCodFiscale(),
                    dipendenteProgetto.getNome(), dipendenteProgetto.getCognome(), dipendenteProgetto.getDataNascita(), false))
                return false;

            // Controlla che la data di scadenza di un dipendente a progetto superi la data di fine del progetto
            if (coerenzaDateFineProgettoDipendente(dipendenteProgetto.getScadenza(), progetto.getCup()))
                return false;

            // Controlla se il dipedente a progetto abbia gia' una carriera attiva
            if (isDipendenteProgettoActive(false, dipendenteProgetto.getCodFiscale(), dipendenteProgetto.getDataAssunzione(), dipendenteProgetto.getScadenza(), null))
                return false;

            // Aggiunge l'acquisto al progetto
            progetto.addDipendenteIngaggiato(dipendenteProgetto);

            // Aggiungi il dipendente a progetto all'elenco.
            addDipendenteProgettoToElenco(dipendenteProgetto);
        }

        BigDecimal costoTotaleDipendentiProgetto = new BigDecimal(0);

        //Verifica se il costo totale, per ogni progetto, dei dipendenti a progetto non supera la meta' del budget destinato al progetto
        if (elencoAllProgetti != null) {
            for (Progetto currentProgetto : getElencoAllProgetti()) {

                if (currentProgetto.getDipendentiIngaggiati() != null) {
                    for (DipendenteProgetto currentDipendenteProgetto : currentProgetto.getDipendentiIngaggiati())
                        costoTotaleDipendentiProgetto = costoTotaleDipendentiProgetto.add(currentDipendenteProgetto.getCosto());

                    if (currentProgetto.getBudget().divide(BigDecimal.valueOf(2)).compareTo(costoTotaleDipendentiProgetto) < 0)
                        return false;
                    costoTotaleDipendentiProgetto = new BigDecimal(0);
                }
            }
        }

        return integro;
    }


    /**
     * Legge tutte le attrezzature dal database, crea oggetti in memoria e li aggiunge all'elenco delle attrezzature.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiAttrezzature() throws SQLException{

        boolean integro = true;

        // Creazione di liste per memorizzare i dati delle attrezzature dal database.
        ArrayList<Integer> idAttrezzature = new ArrayList<Integer>();
        ArrayList<String> descrizioni = new ArrayList<>();
        ArrayList<BigDecimal> costi = new ArrayList<>();
        ArrayList<String> stringProgetti = new ArrayList<>();
        ArrayList<String> stringLaboratori = new ArrayList<>();

        Progetto progetto;
        Laboratorio laboratorio = null;

        // Estrae i dati delle attrezzature dal database.
        AttrezzaturaPostgresDAO attrezzaturaDB = new AttrezzaturaPostgresDAO();
        attrezzaturaDB.obtainAttrezzature(idAttrezzature, descrizioni, costi, stringProgetti, stringLaboratori);

        // Loop attraverso i dati estratti e crea oggetti Attrezzatura.
        for (int i = 0; i < idAttrezzature.size(); i++) {

            // Ottiene un'istanza di Progetto dal nome del progetto.
            progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetti.get(i));

            // Crea un oggetto Attrezzatura.
            attrezzatura = new Attrezzatura(idAttrezzature.get(i), descrizioni.get(i), costi.get(i), progetto, laboratorio);

            if (stringLaboratori.get(i) != null) {

                laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), stringLaboratori.get(i));

                // Controlla se il laboratorio non lavora al progetto
                if (progetto.getLaboratoriLavoranti().contains(laboratorio))
                    return false;

                // Aggiunge il possedimento dell'attrezzatura al laboratorio
                laboratorio.addAttrezzatura(attrezzatura);

            }

            // Aggiunge l'acquisto dell'attrezzatura al progetto
            progetto.addAttrezzaturaAcquistata(attrezzatura);

            // Aggiunge l'attrezzatura all'elenco.
            addAttrezzaturaToElenco(attrezzatura);
        }

        BigDecimal costoTotaleAttrezzature = new BigDecimal(0);

        //Verifica se il costo totale, per ogni progetto, delle attrezzature acquistate non supera la meta' del budget destinato al progetto
        if (getElencoAllProgetti() != null) {
            for (Progetto currentProgetto : getElencoAllProgetti()) {

                if (currentProgetto.getAttrezzature() != null) {
                    for (Attrezzatura currentAttrezzatura : currentProgetto.getAttrezzature())
                        costoTotaleAttrezzature = costoTotaleAttrezzature.add(currentAttrezzatura.getCosto());

                    if (currentProgetto.getBudget().divide(BigDecimal.valueOf(2)).compareTo(costoTotaleAttrezzature) < 0)
                        return false;

                    costoTotaleAttrezzature = new BigDecimal(0);
                }
            }
        }

        return integro;
    }


    /**
     * Legge tutti i laboratori dal database, crea oggetti in memoria e li aggiunge all'elenco dei laboratori.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiLaboratori() throws SQLException{

        boolean integro = true;

        // Creazione di liste per memorizzare i dati dei laboratori dal database.
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> topics = new ArrayList<>();
        ArrayList<String> stringResponsabiliScientifici = new ArrayList<>();

        // Estrae i dati dei laboratori dal database.
        LaboratorioPostgresDAO laboratorioDB = new LaboratorioPostgresDAO();
        laboratorioDB.obtainLaboratori(nomi, topics, stringResponsabiliScientifici);

        // Loop attraverso i dati estratti e crea oggetti Laboratorio.
        for (int i = 0; i < nomi.size(); i++) {

            // Ottiene un'istanza di DipendenteIndeterminato dal nome del responsabile scientifico.
            responsabileScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabiliScientifici.get(i));

            // Crea un oggetto Laboratorio.
            laboratorio = new Laboratorio(nomi.get(i), topics.get(i), responsabileScientifico);

            // Verifica che, per il responsabile selezionato, siano coerenti il tipo e la data fine (non impostata)
            if (!responsabileScientifico.getTipoDipendente().equals(TipoDipendente.Senior) || responsabileScientifico.getDataFineRapporto() != null)
                return false;

            // Aggiunge la responsabilita tra responsabile scientifico e il laboratorio.
            responsabileScientifico.addLaboratorioResponsabileScientifico(laboratorio);

            // Aggiunge il laboratorio all'elenco.
            addLaboratorioToElenco(laboratorio);
        }

        //aggiunge le afferenze dei responsabili scientifici
        leggiAfferenze();

        if (getElencoAllLaboratori() != null) {

            for (Laboratorio lab : getElencoAllLaboratori()) {

                responsabileScientifico = lab.getResponsabileScientifico();

                if (!lab.getAfferenti().contains(responsabileScientifico)) {

                    lab.addAfferente(responsabileScientifico);
                    responsabileScientifico.addAfferenzaLaboratorio(lab);

                    AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
                    afferireDB.addAfferenza(responsabileScientifico.getMatricola(), lab.getNome());
                }
            }
        }

        return integro;
    }


    /**
     * Legge tutti i progetti dal database, crea oggetti in memoria e li aggiunge all'elenco dei progetti.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiProgetti() throws SQLException{

        boolean integro = true;

        // Creazione di liste per memorizzare i dati dei progetti dal database.
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cups = new ArrayList<>();
        ArrayList<BigDecimal> budgets = new ArrayList<>();
        ArrayList<LocalDate> dateInizio = new ArrayList<>();
        ArrayList<LocalDate> dateFine = new ArrayList<>();
        ArrayList<String> stringReferentiScientifici = new ArrayList<>();
        ArrayList<String> stringResponsabili = new ArrayList<>();

        // Estrae i dati dei progetti dal database.
        ProgettoPostgresDAO progettoDB = new ProgettoPostgresDAO();
        progettoDB.obtainProgetti(nomi, cups, budgets, dateInizio, dateFine, stringReferentiScientifici, stringResponsabili);

        // Loop attraverso i dati estratti e crea oggetti Progetto.
        for (int i = 0; i < nomi.size(); i++) {

            // Ottiene un'istanza di DipendenteIndeterminato dal nome del referente scientifico e del responsabile.
            referenteScientifico = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringReferentiScientifici.get(i));
            responsabile = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringResponsabili.get(i));

            // Crea un oggetto Progetto.
            progetto = new Progetto(cups.get(i), nomi.get(i), budgets.get(i), dateInizio.get(i), dateFine.get(i), referenteScientifico, responsabile);

            // Controlla che la data fine del progetto rientra nel periodo lavorativo del referente scientifico
            if (checkPeriodoLavorativoCoerenteReferenteScientifico(referenteScientifico.getDataAssunzione(), referenteScientifico.getDataFineRapporto(), progetto.getDataInizio(), progetto.getDataFine()))
                return false;

            // Controlla che la data fine del progetto rientra nel periodo lavorativo del referente scientifico
            if (checkPeriodoLavorativoCoerenteResponsabile(responsabile.getDirigente(), responsabile.getDataAssunzione(), responsabile.getDataFineRapporto(), progetto.getDataInizio(), progetto.getDataFine()))
                return false;

            // Aggiunge le responsabilita' dei dipendenti rispetto al progetto
            referenteScientifico.addProgettoReferenteScientifico(progetto);
            responsabile.addProgettoResponsabile(progetto);

            // Aggiunge il progetto all'elenco.
            addProgettoToElenco(progetto);
        }

        return integro;
    }

    /**
     * Verifica che il nome del progetto specificato sia unico nel sistema.
     *
     * @param nomeProgetto {@link String}   Il nome del progetto da verificare.
     * @return True se il nome e' unico, false altrimenti.
     * */
    public boolean checkNomeProgettoUnico(String nomeProgetto){

        boolean test = true;

        // Se trova un progetto con il nome specificato nell'elenco, allora nella verifica salta quel progetto, altrimenti li ispeziona tutti

        // Otteniamo l'istanza del progetto corrispondente al nome.
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), nomeProgetto);

        if (progetto == null){

            // Ispeziona tutti i progetti per cercarne uno con lo stesso nome inserito
            for (Progetto progetto : getElencoAllProgetti()){

                if (progetto.getNome().equals(nomeProgetto)){
                    test = false;
                    break;
                }
            }

        } else{

            // Ispeziona tutti i progetti tranne il progetto trovato
            for (Progetto currentProgetto : getElencoAllProgetti()){

                if (currentProgetto.equals(progetto))
                    continue;

                if (currentProgetto.getNome().equals(nomeProgetto)){

                    test = false;
                    break;
                }
            }
        }

        return test;
    }

    /**
     * Verifica che il periodo lavorativo del referente scientifico specificato sia coerente con la data di fine del progetto specificato.
     *
     * @param dataAssunzioneDipendente  {@link LocalDate}   La data di assunzione del referente scientifico.
     * @param dataFineDipendente        {@link LocalDate}   La data di fine rapporto del referente scientifico.
     * @param dataInizioProgetto        {@link LocalDate}   La data di inizio del progetto.
     * @param dataFineProgetto          {@link LocalDate}   La data di fine del progetto.
     *
     * @return True se il periodo lavorativo e' coerente, altrimenti false.
     * */
    public boolean checkPeriodoLavorativoCoerenteReferenteScientifico(LocalDate dataAssunzioneDipendente, LocalDate dataFineDipendente, LocalDate dataInizioProgetto, LocalDate dataFineProgetto){

        boolean test = false;

        LocalDate dataScattoSenior = dataAssunzioneDipendente.plusYears(7);

        if (dataFineProgetto == null){
            if (!( ( dataScattoSenior.isBefore(dataInizioProgetto) || dataScattoSenior.isEqual(dataInizioProgetto) ) && dataFineDipendente == null ))
                test = true;
        } else {

            if (! ( ( dataScattoSenior.isBefore(dataInizioProgetto) || dataScattoSenior.isEqual(dataInizioProgetto) ) && ( dataFineDipendente == null || ( (dataFineProgetto.isBefore(dataFineDipendente) || dataFineProgetto.isEqual(dataFineDipendente)) && LocalDate.now().isAfter(dataFineDipendente)) ) ) )
                test = true;
        }

        return test;
    }

    /**
     * Verifica che il periodo lavorativo del responsabile specificato sia coerente con la data di fine del progetto specificato.
     *
     * @param dirigente                                     True se il dipendente e' dirigente, false altrimenti.
     * @param dataAssunzioneDipendente  {@link LocalDate}   La data di assunzione del responsabile.
     * @param dataFineDipendente        {@link LocalDate}   La data di fine rapporto del responsabile.
     * @param dataInizioProgetto        {@link LocalDate}   La data di inizio del progetto.
     * @param dataFineProgetto          {@link LocalDate}   La data di fine del progetto.
     *
     * @return True se il periodo lavorativo e' coerente, altrimenti false.
     * */
    public boolean checkPeriodoLavorativoCoerenteResponsabile(boolean dirigente, LocalDate dataAssunzioneDipendente, LocalDate dataFineDipendente, LocalDate dataInizioProgetto, LocalDate dataFineProgetto){

        boolean test = false;

        if (dataFineProgetto == null){
            if (!( dirigente && ( dataAssunzioneDipendente.isBefore(dataInizioProgetto) || dataAssunzioneDipendente.isEqual(dataInizioProgetto) ) && dataFineDipendente == null ))
                test = true;
        } else {

            if (! ( dirigente && ( dataAssunzioneDipendente.isBefore(dataInizioProgetto) || dataAssunzioneDipendente.isEqual(dataInizioProgetto) ) && ( dataFineDipendente == null || ( (dataFineProgetto.isBefore(dataFineDipendente) || dataFineProgetto.isEqual(dataFineDipendente)) && LocalDate.now().isAfter(dataFineDipendente)) ) ) )
                test = true;
        }

        return test;
    }

    /**
     * Legge tutte le istanze di lavoro, tra progetti e laboratori, dal database, crea oggetti in memoria e li aggiunge alle associazioni corrispettive.
     *
     * @return True se i dati non presentano incoerenze, false altrimenti.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public boolean leggiLavorare() throws SQLException{

        boolean integro = true;

        // Creazione di liste per memorizzare i dati delle associazioni Lavorare dal database.
        ArrayList<String> progetti = new ArrayList<>();
        ArrayList<String> laboratori = new ArrayList<>();

        // Estrae i dati delle associazioni Lavorare dal database.
        LavorarePostgresDAO lavorareDB = new LavorarePostgresDAO();
        lavorareDB.obtainLavorare(progetti, laboratori);

        // Loop attraverso i dati estratti e crea oggetti Lavorare.
        for (int i = 0; i < progetti.size(); i++) {

            // Ottiene un'istanza di Progetto e Laboratorio dal nome.
            progetto = getIstanceFromArrayList(getElencoAllProgetti(), progetti.get(i));
            laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), laboratori.get(i));

            // Verifica se il progetto non e' attivo, nell'eventualita' elimina l'istanza di lavoro dal database e prosegue con la lettura
            if (checkProgettoIsNotActive(progetto.getDataFine())) {

                lavorareDB.removeLavorare(progetto.getCup(), laboratorio.getNome());

            } else {

                // Aggiunge il laboratorio come lavorante al progetto e viceversa.
                if (checkMaxLaboratoriLavoranti(false, progetto.getCup(), null))
                    return false;

                // Aggiunge la relazione di lavoro tra laboratorio e progetto
                progetto.addLaboratorioLavorante(laboratorio);
                laboratorio.addProgetto(progetto);

            }

        }

        return integro;
    }

    /**
     * Verifica che il progetto specificato non sia piu' attivo.
     *
     * @param dataFineProgetto {@link LocalDate}    La data di fine del progetto.
     *
     * @return True se il progetto non e' attivo, false altrimenti.
     * */
    public boolean checkProgettoIsNotActive(LocalDate dataFineProgetto){

        boolean test = false;

        if (dataFineProgetto != null && dataFineProgetto.isBefore(LocalDate.now()))
            test = true;

        return test;
    }

    /**
     * Legge tutte le afferenze tra dipendenti a tempo indeterminato e laboratori dal database, crea oggetti in memoria e li aggiunge alle associazioni corrispettive.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    public void leggiAfferenze() throws SQLException{

        // Creazione di liste per memorizzare i dati delle associazioni Afferenze dal database.
        ArrayList<String> dipendenti = new ArrayList<>();
        ArrayList<String> laboratori = new ArrayList<>();

        // Estrae i dati delle associazioni Afferenze dal database.
        AfferirePostgresDAO afferireDB = new AfferirePostgresDAO();
        afferireDB.obtainAfferenze(dipendenti, laboratori);

        // Loop attraverso i dati estratti e crea oggetti Afferenza.
        for (int i = 0; i < dipendenti.size(); i++) {

            // Ottiene un'istanza di DipendenteIndeterminato e Laboratorio dal nome.
            dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), dipendenti.get(i));
            laboratorio = getIstanceFromArrayList(getElencoAllLaboratori(), laboratori.get(i));

            // Verifica che il dipendente abbia una data di fine precedente alla data attuale, in tal caso lo rimuove.
            // Infatti, un dipendente in dirittura di licenziamento, mantiene le afferenze, ma non puo' registrarne di nuove.
            if (checkInvalidAfferenza(dipendenteIndeterminato.getMatricola())){

                afferireDB.removeAfferenza(dipendenteIndeterminato.getMatricola(), laboratorio.getNome());

            } else {

                // Aggiunge l' afferenza del dipendente al laboratorio
                laboratorio.addAfferente(dipendenteIndeterminato);
                dipendenteIndeterminato.addAfferenzaLaboratorio(laboratorio);
            }
        }
    }

    /**
     * Verifica se l'afferenza e' invalida, ovvero se il dipendente a tempo indeterminato specificato presenta una data di fine rapporto precedente alla data attuale.
     *
     * @param stringDipendenteIndeterminato {@link String}  I dati del dipendente da verificare.
     *
     * @return True se presenta una data di fine invalida, altrimenti false.
     * */
    public boolean checkInvalidAfferenza(String stringDipendenteIndeterminato){

        boolean test = false;

        // Recupera l'istanza del dipendente indeterminato da verificare (deve esistere)
        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringDipendenteIndeterminato);

        if (dipendenteIndeterminato.getDataFineRapporto() != null && dipendenteIndeterminato.getDataFineRapporto().isBefore(LocalDate.now())) {
                test = true;
        }

        return test;
    }

    //METODI PER DISPLAY
    /**
     * Recupera una lista di dati dei dipendenti a tempo indeterminato presenti nel database.
     *
     * @return {@link ArrayList}    Un array di stringhe contenente i dati dei dipendenti indeterminati.
     */
    public String[] recuperaDipendentiIndeterminati() {
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoPostgresDAO = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(dipendenteIndeterminatoPostgresDAO.getDipendentiIndeterminati(), new String[]{"3", "4", "5", "1", "2", "6", "7", "8", "9", "10"});
    }

    /**
     * Recupera una lista abbreviata di dati dei dipendenti a tempo indeterminato presenti nel database.
     *
     * @return {@link ArrayList}    Un array di stringhe contenente dati abbreviati dei dipendenti indeterminati.
     */
    public String[] recuperaDipendentiIndeterminatiBreve() {
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoPostgresDAO = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(dipendenteIndeterminatoPostgresDAO.getDipendentiIndeterminati(), new String[]{"1", "3", "4"});
    }

    /**
     * Recupera una lista di dati dei progetti presenti nel database.
     *
     * @return {@link ArrayList} Un array di stringhe contenente i dati dei progetti.
     */
    public String[] recuperaProgetti() {
        ProgettoPostgresDAO progettoPostgresDAO = new ProgettoPostgresDAO();
        return stringArrayListToStringArray(progettoPostgresDAO.getProgetti(), new String[]{"1", "2"});
    }

    /**
     * Recupera una lista di dati dei progetti non terminati presenti nel database.
     *
     * @return {@link ArrayList} Un array di stringhe contenente i dati dei progetti.
     */
    public String[] recuperaProgettiNonTerminati() {
        ProgettoPostgresDAO progettoPostgresDAO = new ProgettoPostgresDAO();
        return stringArrayListToStringArray(progettoPostgresDAO.getProgettiNonTerminati(), new String[]{"1", "2"});
    }

    /**
     * Recupera una lista di dati dei laboratori presenti nel database.
     *
     * @return {@link ArrayList} Un array di stringhe contenente i dati dei laboratori.
     */
    public String[] recuperaLaboratori() {
        LaboratorioPostgresDAO laboratorioPostgresDAO = new LaboratorioPostgresDAO();
        return stringArrayListToStringArray(laboratorioPostgresDAO.getLaboratori(), new String[]{"1", "2"});
    }

    /**
     * Recupera una lista di dati dei laboratori lavoranti per un progetto specifico presenti nel databse.
     *
     * @param stringProgetto    {@link String}    I dati del progetto.
     * @return                  {@link ArrayList}  Un array di stringhe contenente i dati dei laboratori lavoranti per il progetto specificato.
     */
    public String[] recuperaLaboratoriLavoranti(String stringProgetto) {

        LaboratorioPostgresDAO laboratorioPostgresDAO = new LaboratorioPostgresDAO();

        // Recupera l'istanza del progetto interessato
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);
        return stringArrayListToStringArray(laboratorioPostgresDAO.getLaboratoriLavoranti(progetto.getCup()), new String[]{"1", "2"});
    }

    /**
     * Recupera una lista di dati dei laboratori candidati ad una istanza di lavoro per un progetto specifico presente nel database.
     *
     * @param stringProgetto {@link String}     I dati del progetto.
     * @return               {@link ArrayList}  Un array di stringhe contenente i dati dei laboratori candidati per il progetto specificato.
     */
    public String[] recuperaLaboratoriCandidati(String stringProgetto) {

        LaboratorioPostgresDAO laboratorioPostgresDAO = new LaboratorioPostgresDAO();

        // Recupera l'istanza del progetto interessato
        progetto = getIstanceFromArrayList(getElencoAllProgetti(), stringProgetto);
        return stringArrayListToStringArray(laboratorioPostgresDAO.getLaboratoriCandidati(progetto.getCup()), new String[]{"1", "2"});
    }

    /**
     * Recupera una lista di dati dei dipendenti candidati al ruolo di responsabile scientifico per un laboratorio presenti nel database.
     *
     * @return {@link ArrayList} Un array di stringhe contenente i dati dei dipendenti candidati per il ruolo specifico.
     */
    public String[] recuperaAllDipendentiCandidatiResponsabileScientifico() {
        DipendenteIndeterminatoPostgresDAO responsabiliScientificiDB = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(responsabiliScientificiDB.getAllDipendentiCandidatiResponsabileScientifico(), new String[]{"1", "3", "4"});
    }

    /**
     * Recupera una lista di dati dei dipendenti candidati al ruolo di referente scientifico per un progetto nel database.
     * @param dataInizioProgetto {@link LocalDate}  La data di inizio del progetto.
     * @param dataFineProgetto   {@link LocalDate}  La data di fine del progetto.
     *
     * @return {@link ArrayList} Un array di stringhe contenente i dati dei dipendenti candidati per il ruolo specifico.
     */
    public String[] recuperaAllDipendentiCandidatiReferenteScientifico(LocalDate dataInizioProgetto, LocalDate dataFineProgetto) {
        DipendenteIndeterminatoPostgresDAO referentiScientificiDB = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(referentiScientificiDB.getAllDipendentiCandidatiReferenteScientifico(dataInizioProgetto, dataFineProgetto), new String[]{"1", "3", "4"});
    }

    /**
     * Recupera una lista di dati dei dipendenti candidati al ruolo di dirigente per un progetto presente nel database.
     * @param dataInizioProgetto {@link LocalDate}  La data di inizio del progetto.
     * @param dataFineProgetto   {@link LocalDate}  La data di fine del progetto.
     *
     * @return {@link ArrayList}    Un array di stringhe contenente i dati dei dipendenti candidati per il ruolo specifico.
     */
    public String[] recuperaAllDipendentiCandidatiDirigente(LocalDate dataInizioProgetto, LocalDate dataFineProgetto) {
        DipendenteIndeterminatoPostgresDAO dirigentiDB = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(dirigentiDB.getAllDipendentiCandidatiDirigente(dataInizioProgetto, dataFineProgetto), new String[]{"1", "3", "4"});
    }

    /**
     * Recupera la lista di dati dei dipendenti che possono effettuare uno scatto di carriera del tipo specificato.
     *
     * @param tipoScatto {@link String}     Il tipo di scatto specificato.
     * @return           {@link ArrayList}  Un array di stringhe contenente i dati dei dipendenti candidati a ruoli specifici.
     */
    public String[] recuperaDipendentiCandidati(String tipoScatto) {
        DipendenteIndeterminatoPostgresDAO dipendentiCandidatiPostgresDAO = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToStringArray(dipendentiCandidatiPostgresDAO.getDipendentiCandidatiScatto(tipoScatto), new String[]{"1", "3", "4"});
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutti i dipendenti indeterminati presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutti i dipendenti indeterminati.
     */
    public Object[][] recuperaObjectDipendentiIndeterminati() {
        DipendenteIndeterminatoPostgresDAO dipendenteIndeterminatoPostgresDAO = new DipendenteIndeterminatoPostgresDAO();
        return stringArrayListToObjectMatrix(dipendenteIndeterminatoPostgresDAO.getDipendentiIndeterminati());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutti i dipendenti a progetto presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutti i dipendenti a progetto.
     */
    public Object[][] recuperaObjectDipendentiProgetto() {
        DipendenteProgettoPostgresDAO dipendenteProgettoPostgresDAO = new DipendenteProgettoPostgresDAO();
        return stringArrayListToObjectMatrix(dipendenteProgettoPostgresDAO.getDipendentiProgetto());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutte le attrezzature presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutte le attrezzature.
     */
    public Object[][] recuperaObjectAttrezzature() {
        AttrezzaturaPostgresDAO attrezzaturaPostgresDAO = new AttrezzaturaPostgresDAO();
        return stringArrayListToObjectMatrix(attrezzaturaPostgresDAO.getAttrezzature());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutti i laboratori presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutti i laboratori.
     */
    public Object[][] recuperaObjectLaboratori() {
        LaboratorioPostgresDAO laboratorioPostgresDAO = new LaboratorioPostgresDAO();
        return stringArrayListToObjectMatrix(laboratorioPostgresDAO.getLaboratori());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutti gli scatti di carriera presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutti gli scatti di carriera.
     */
    public Object[][] recuperaObjectScatti() {
        ScattoCarrieraPostgresDAO scattoPostgresDAO = new ScattoCarrieraPostgresDAO();
        return stringArrayListToObjectMatrix(scattoPostgresDAO.getScattiCarriera());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutti i progetti presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutti i progetti.
     */
    public Object[][] recuperaObjectProgetti() {
        ProgettoPostgresDAO progettoPostgresDAO = new ProgettoPostgresDAO();
        return stringArrayListToObjectMatrix(progettoPostgresDAO.getProgetti());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutte le istanze di lavoro presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutte le istanze di lavoro.
     */
    public Object[][] recuperaObjectLavori() {
        LavorarePostgresDAO lavorarePostgresDAO = new LavorarePostgresDAO();
        return stringArrayListToObjectMatrix(lavorarePostgresDAO.getLavorare());
    }

    /**
     * Recupera una matrice di oggetti contenente tutti i dati di tutte le afferenze presenti nel database.
     *
     * @return Una matrice di oggetti contenente tutti i dati di tutte le afferenze.
     */
    public Object[][] recuperaObjectAfferenze() {
        AfferirePostgresDAO afferirePostgresDAO = new AfferirePostgresDAO();
        return stringArrayListToObjectMatrix(afferirePostgresDAO.getAfferenze());
    }

    /**
     * Converte un ArrayList di stringhe in una matrice di oggetti per l'uso in una JTable, al fine di visualizzarne a video i valori.
     *
     * @param rawDatabase {@link ArrayList} ArrayList contenente i dati del database sottoforma di array di stringhe.
     * @return Una matrice di oggetti contenente i dati del database.
     */
    public Object[][] stringArrayListToObjectMatrix(ArrayList<String[]> rawDatabase) {

        Object[][] risultato;

        if (rawDatabase.isEmpty())
            risultato = null;
        else {
            // La prima riga dell'ArrayList contiene il numero di colonne
            int columnNumber = Integer.parseInt(rawDatabase.get(0)[0]);
            risultato = new Object[rawDatabase.size()][columnNumber - 1];

            for (int i = 0; i < rawDatabase.size(); i++) {
                for (int j = 0; j < columnNumber - 1; j++) {
                    // Ogni elemento dell'ArrayList è una riga di dati, quindi si salta il primo elemento (numero di colonne)
                    risultato[i][j] = rawDatabase.get(i)[j + 1];
                }
            }
        }
        return risultato;
    }


    /**
     * Converte una matrice di stringhe in un array di stringhe, concatenando le stringhe di ogni array in un unico array di campi separati dal simbolo "-".
     * Questo metodo è utilizzato per convertire i dati ottenuti dalle implementazioni DAO in un formato leggibile dalle componenti GUI JComboBox.
     *
     * @param rawDatabase Matrice di array di stringhe che rappresentano i dati di un dipendente.
     * @param columns     Array di caratteri che rappresentano la numerazione delle colonne, a partire da 1, del database.
     *                    Devono essere inseriti in modo tale che persista una relazione di ordine tra i valori numerici.
     * @return Un array di stringhe, ognuna delle quali contiene tutte le colonne della matrice con i campi separati da un carattere "-".
     */
    public String[] stringArrayListToStringArray(ArrayList<String[]> rawDatabase, String[] columns) {

        String[] risultato;

        if (rawDatabase.isEmpty()) {
            // Se la matrice è vuota, restituisci un array di stringa vuoto.
            risultato = new String[]{""};
        } else {

            risultato = new String[rawDatabase.size()];
            int columnNumber = Integer.parseInt(rawDatabase.get(0)[0]); // Numero totale di colonne nei dati del database

            boolean find = false; // Variabile per verificare se la colonna corrente deve essere inclusa nell'output

            for (int i = 0; i < rawDatabase.size(); i++) {

                risultato[i] = ""; // Inizializza l'elemento dell'array risultato per l'indice corrente

                for (int j = 1; j < columnNumber; j++) { // Inizia da 1 poiché il primo elemento contiene il numero di colonne

                    for (String string : columns) {
                        if (string.equals(Integer.toString(j))) {
                            find = true; // La colonna corrente deve essere inclusa nell'output
                            break;
                        }
                    }

                    if (find) {
                        // Se la colonna corrente deve essere inclusa, aggiungi il suo valore all'elemento corrente dell'array risultato.
                        if (!(risultato[i].isEmpty())) {
                            risultato[i] = risultato[i] + " - " + rawDatabase.get(i)[j];
                        } else {
                            risultato[i] = rawDatabase.get(i)[j];
                        }
                    }

                    find = false; // Reimposta la variabile di controllo per la prossima colonna
                }
            }
        }

        return risultato;
    }

    /**
     * Verifica l'integrità di tutti gli scatti di carriera, di tipo promozione e rimozione, dei dipendenti a tempo indeterminato che li hanno effettuati, presenti nell'elenco.
     * Per compiere il controllo, ispeziona la corretta alternanza, a due a due, degli scatti.
     *
     * @return True se l'alternanza di scatti è coerente per tutti i dipendenti, altrimenti False.
     */
    public boolean verificaIntegritaAllPromozioniRimozioni() {

        boolean integro = true;

        LocalDate dataPromozioneMenoRecente;
        LocalDate dataRimozioneMenoRecente;

        for (DipendenteIndeterminato dipendenteIndeterminato : getElencoAllDipendentiIndeterminati()) {

            if (integro) {

                if (dipendenteIndeterminato.getScattiEffettuati() != null) {

                    DipendenteIndeterminato dipendenteCopia = new DipendenteIndeterminato(
                            dipendenteIndeterminato.getNome(),
                            dipendenteIndeterminato.getCognome(),
                            dipendenteIndeterminato.getCodFiscale(),
                            dipendenteIndeterminato.getMatricola(),
                            dipendenteIndeterminato.getTipoDipendente(),
                            dipendenteIndeterminato.getIndirizzo(),
                            dipendenteIndeterminato.getDataNascita(),
                            dipendenteIndeterminato.getDataAssunzione(),
                            dipendenteIndeterminato.getDataFineRapporto(),
                            dipendenteIndeterminato.getDirigente());

                    // Crea una copia dei dati di scatti per il dipendente.
                    for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                        ScattoCarriera copiaScatto = new ScattoCarriera(scattoCarriera.getData(), scattoCarriera.getTipoScatto(), dipendenteCopia);

                        if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente) || scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                            dipendenteCopia.addScattoCarriera(copiaScatto);
                    }

                    while (dipendenteCopia.getScattiEffettuati() != null && !dipendenteCopia.getScattiEffettuati().isEmpty()) {

                        // Ottieni la data della promozione meno recente e la data della rimozione meno recente.
                        dataPromozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Promosso_a_dirigente);
                        dataRimozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Rimosso_da_dirigente);

                        if (dataPromozioneMenoRecente == null && dataRimozioneMenoRecente != null) {

                            integro = false;
                            break;

                        } else if (dataPromozioneMenoRecente != null && dataRimozioneMenoRecente != null && dataRimozioneMenoRecente.isBefore(dataPromozioneMenoRecente)) {

                            integro = false;
                            break;

                        } else {

                            // Rimuovi gli scatti di carriera già controllati dalla copia.
                            ScattoCarriera scattoPromozione = new ScattoCarriera(dataPromozioneMenoRecente, TipoScatto.Promosso_a_dirigente, dipendenteCopia);
                            ScattoCarriera scattoRimozione = new ScattoCarriera(dataRimozioneMenoRecente, TipoScatto.Rimosso_da_dirigente, dipendenteCopia);

                            dipendenteCopia.removeScattoCarriera(scattoPromozione);
                            dipendenteCopia.removeScattoCarriera(scattoRimozione);
                        }
                    }
                }
            } else break;
        }

        return integro;
    }

    /**
     * Verifica l'integrità degli scatti di carriera, di tipo promozione e rimozione, per un singolo dipedente a tempo indeterminato, per testare l'aggiunta di uno scatto di questo tipo.
     * Per compiere il controllo, ispeziona la corretta alternanza, a due a due, degli scatti.
     *
     * @param tipoScatto       {@link String}   Il tipo di scatto da aggiungere (Promosso_a_dirigente o Rimosso_da_dirigente).
     * @param stringDipendente {@link String}   I dati del dipendente indeterminato che ha effettuato lo scatto.
     * @param dataScatto       {@link String}   La data dello scatto da verificare.
     * @return True se l'integrità è verificata, altrimenti False.
    */
    public boolean verificaIntegritaAggiuntaPromozioniRimozioniDipendente(String tipoScatto, String stringDipendente, LocalDate dataScatto) {

        boolean integro = true;

        LocalDate dataPromozioneMenoRecente;
        LocalDate dataRimozioneMenoRecente;

        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringDipendente);

        if (integro) {

            if (dipendenteIndeterminato.getScattiEffettuati() != null) {

                DipendenteIndeterminato dipendenteCopia = new DipendenteIndeterminato(
                        dipendenteIndeterminato.getNome(),
                        dipendenteIndeterminato.getCognome(),
                        dipendenteIndeterminato.getCodFiscale(),
                        dipendenteIndeterminato.getMatricola(),
                        dipendenteIndeterminato.getTipoDipendente(),
                        dipendenteIndeterminato.getIndirizzo(),
                        dipendenteIndeterminato.getDataNascita(),
                        dipendenteIndeterminato.getDataAssunzione(),
                        dipendenteIndeterminato.getDataFineRapporto(),
                        dipendenteIndeterminato.getDirigente());

                // Crea una copia dei dati di scatti per il dipendente.
                for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                    ScattoCarriera copiaScatto = new ScattoCarriera(scattoCarriera.getData(), scattoCarriera.getTipoScatto(), dipendenteCopia);

                    if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente) || scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                        dipendenteCopia.addScattoCarriera(copiaScatto);
                }

                // Inserisci lo scatto da testare nella copia.
                scattoCarriera = new ScattoCarriera(dataScatto, TipoScatto.valueOf(tipoScatto), dipendenteCopia);
                dipendenteCopia.addScattoCarriera(scattoCarriera);

                while (dipendenteCopia.getScattiEffettuati() != null && !dipendenteCopia.getScattiEffettuati().isEmpty()) {

                    // Ottieni la data della promozione meno recente e la data della rimozione meno recente.
                    dataPromozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Promosso_a_dirigente);
                    dataRimozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Rimosso_da_dirigente);

                    if (dataPromozioneMenoRecente == null && dataRimozioneMenoRecente != null) {

                        integro = false;
                        break;

                    } else if (dataPromozioneMenoRecente != null && dataRimozioneMenoRecente != null && dataRimozioneMenoRecente.isBefore(dataPromozioneMenoRecente)) {

                        integro = false;
                        break;

                    } else {

                        // Rimuovi gli scatti di carriera già controllati dalla copia.
                        ScattoCarriera scattoPromozione = new ScattoCarriera(dataPromozioneMenoRecente, TipoScatto.Promosso_a_dirigente, dipendenteCopia);
                        ScattoCarriera scattoRimozione = new ScattoCarriera(dataRimozioneMenoRecente, TipoScatto.Rimosso_da_dirigente, dipendenteCopia);

                        dipendenteCopia.removeScattoCarriera(scattoPromozione);
                        dipendenteCopia.removeScattoCarriera(scattoRimozione);
                    }
                }
            }
        }

        return integro;
    }

    /**
    * Verifica l'integrità degli scatti di carriera, di tipo promozione e rimozione, per un singolo dipedente a tempo indeterminato, per testare la modifica di uno scatto di questo tipo.
     * Per compiere il controllo, ispeziona la corretta alternanza, a due a due, degli scatti.
     *
     * @param vecchioTipoScatto         {@link String}      Il tipo di scatto da modificare.
     * @param stringVecchioDipendente   {@link String}      I dati del dipendente da modificare.
     * @param vecchiaDataScatto         {@link LocalDate}   La data dello scatto da modificare.
     * @param tipoScatto                {@link String}      Il nuovo tipo di scatto (Promosso_a_dirigente o Rimosso_da_dirigente).
     * @param dipendente                {@link String}      I nuovi dati del dipendente.
     * @param dataScatto                {@link LocalDate}   La nuova data dello scatto da verificare.
     * @return True se l'integrità è verificata, altrimenti False.
    */
    public boolean verificaIntegritaModificaPromozioniRimozioniDipendente(String vecchioTipoScatto, String stringVecchioDipendente, LocalDate vecchiaDataScatto, String tipoScatto, String dipendente, LocalDate dataScatto){

        boolean integro = true;

        LocalDate dataPromozioneMenoRecente;
        LocalDate dataRimozioneMenoRecente;

        dipendenteIndeterminato = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), dipendente);
        DipendenteIndeterminato vecchioDipendente = getIstanceFromArrayList(getElencoAllDipendentiIndeterminati(), stringVecchioDipendente);

        if (dipendenteIndeterminato.getMatricola().equals(vecchioDipendente.getMatricola())){

            //quando il dipendente non cambia, si verifica che l'aggiornamento del vecchio scatto di carriera non porti ad una successione di scatti non coerente

            if (integro) {

                if (dipendenteIndeterminato.getScattiEffettuati() != null) {

                    DipendenteIndeterminato dipendenteCopia = new DipendenteIndeterminato(
                            dipendenteIndeterminato.getNome(),
                            dipendenteIndeterminato.getCognome(),
                            dipendenteIndeterminato.getCodFiscale(),
                            dipendenteIndeterminato.getMatricola(),
                            dipendenteIndeterminato.getTipoDipendente(),
                            dipendenteIndeterminato.getIndirizzo(),
                            dipendenteIndeterminato.getDataNascita(),
                            dipendenteIndeterminato.getDataAssunzione(),
                            dipendenteIndeterminato.getDataFineRapporto(),
                            dipendenteIndeterminato.getDirigente());

                    //crea un altro arraylist con una copia degli elementi, ma con riferimento diverso
                    for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                        ScattoCarriera copiaScatto = new ScattoCarriera(scattoCarriera.getData(), scattoCarriera.getTipoScatto(), dipendenteCopia);

                        if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente) || scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                            dipendenteCopia.addScattoCarriera(copiaScatto);
                    }


                    //cerca l'istanza del vecchio scatto carriera (non utilizzabile il metodo statico getIstanceFromArrayList(...) poiche' uno scatto e' identificato contemporaneamente dalla data, dalla matricola del dipendente e dal tipo dello scatto)
                    if (dipendenteCopia.getScattiEffettuati() != null) {
                        for (ScattoCarriera vecchioScatto : dipendenteCopia.getScattiEffettuati()) {

                            if (vecchioScatto.getTipoScatto().equals(TipoScatto.valueOf(vecchioTipoScatto)) &&
                                    vecchioScatto.getData().equals(vecchiaDataScatto)) {

                                vecchioScatto.setTipoScatto(TipoScatto.valueOf(tipoScatto));
                                vecchioScatto.setData(dataScatto);
                                break;
                            }
                        }
                    }

                    while (dipendenteCopia.getScattiEffettuati() != null && !dipendenteCopia.getScattiEffettuati().isEmpty()) {

                        dataPromozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Promosso_a_dirigente);
                        dataRimozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Rimosso_da_dirigente);

                        if (dataPromozioneMenoRecente == null && dataRimozioneMenoRecente != null) {

                            integro = false;
                            break;

                        } else if (dataPromozioneMenoRecente != null && dataRimozioneMenoRecente != null && dataRimozioneMenoRecente.isBefore(dataPromozioneMenoRecente)) {

                            integro = false;
                            break;

                        } else {

                            //prima elimina dall'arrayList copia gli scatti gia' controllati
                            ScattoCarriera scattoPromozione = new ScattoCarriera(dataPromozioneMenoRecente, TipoScatto.Promosso_a_dirigente, dipendenteCopia);
                            ScattoCarriera scattoRimozione = new ScattoCarriera(dataRimozioneMenoRecente, TipoScatto.Rimosso_da_dirigente, dipendenteCopia);

                            dipendenteCopia.removeScattoCarriera(scattoPromozione);
                            dipendenteCopia.removeScattoCarriera(scattoRimozione);
                        }
                    }
                }
            }

        } else {

            //altrimenti, quando il dipendente associato allo scatto cambia, si verificano:
            //1) Se viene rimosso lo scatto del vecchio dipendente, allora la successione degli scatti resti coerente
            //2) Se viene inserito lo scatto al nuovo dipendente, allora la successione degli scatti resti coerente

            //caso 1: rimozione scatto al vecchio dipendente
            if (integro) {

                if (vecchioDipendente.getScattiEffettuati() != null) {

                    DipendenteIndeterminato dipendenteCopia = new DipendenteIndeterminato(
                            vecchioDipendente.getNome(),
                            vecchioDipendente.getCognome(),
                            vecchioDipendente.getCodFiscale(),
                            vecchioDipendente.getMatricola(),
                            vecchioDipendente.getTipoDipendente(),
                            vecchioDipendente.getIndirizzo(),
                            vecchioDipendente.getDataNascita(),
                            vecchioDipendente.getDataAssunzione(),
                            vecchioDipendente.getDataFineRapporto(),
                            vecchioDipendente.getDirigente());

                    //crea un altro arraylist con una copia degli elementi, ma con riferimento diverso
                    for (ScattoCarriera scattoCarriera : vecchioDipendente.getScattiEffettuati()) {

                        ScattoCarriera copiaScatto = new ScattoCarriera(scattoCarriera.getData(), scattoCarriera.getTipoScatto(), dipendenteCopia);

                        if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente) || scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                            dipendenteCopia.addScattoCarriera(copiaScatto);
                    }


                    //cerca l'istanza del vecchio scatto carriera (non utilizzabile il metodo statico getIstanceFromArrayList(...) poiche' uno scatto e' identificato contemporaneamente dalla data, dalla matricola del dipendente e dal tipo dello scatto)
                    if (dipendenteCopia.getScattiEffettuati() != null) {
                        for (ScattoCarriera vecchioScatto : dipendenteCopia.getScattiEffettuati()) {

                            if (vecchioScatto.getTipoScatto().equals(TipoScatto.valueOf(vecchioTipoScatto)) &&
                                    vecchioScatto.getData().equals(vecchiaDataScatto)) {

                                //viene rimosso lo scatto
                                dipendenteCopia.removeScattoCarriera(vecchioScatto);
                                break;
                            }
                        }
                    }

                    //si verifica la coerenza
                    while (dipendenteCopia.getScattiEffettuati() != null && !dipendenteCopia.getScattiEffettuati().isEmpty()) {

                        dataPromozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Promosso_a_dirigente);
                        dataRimozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Rimosso_da_dirigente);

                        if (dataPromozioneMenoRecente == null && dataRimozioneMenoRecente != null) {

                            integro = false;
                            break;

                        } else if (dataPromozioneMenoRecente != null && dataRimozioneMenoRecente != null && dataRimozioneMenoRecente.isBefore(dataPromozioneMenoRecente)) {

                            integro = false;
                            break;

                        } else {

                            //prima elimina dall'arrayList copia gli scatti gia' controllati
                            ScattoCarriera scattoPromozione = new ScattoCarriera(dataPromozioneMenoRecente, TipoScatto.Promosso_a_dirigente, dipendenteCopia);
                            ScattoCarriera scattoRimozione = new ScattoCarriera(dataRimozioneMenoRecente, TipoScatto.Rimosso_da_dirigente, dipendenteCopia);

                            dipendenteCopia.removeScattoCarriera(scattoPromozione);
                            dipendenteCopia.removeScattoCarriera(scattoRimozione);
                        }
                    }
                }
            }

            //caso 2: aggiunta scatto al nuovo dipendente
            if (integro) {

                if (dipendenteIndeterminato.getScattiEffettuati() != null) {

                    DipendenteIndeterminato dipendenteCopia = new DipendenteIndeterminato(
                            dipendenteIndeterminato.getNome(),
                            dipendenteIndeterminato.getCognome(),
                            dipendenteIndeterminato.getCodFiscale(),
                            dipendenteIndeterminato.getMatricola(),
                            dipendenteIndeterminato.getTipoDipendente(),
                            dipendenteIndeterminato.getIndirizzo(),
                            dipendenteIndeterminato.getDataNascita(),
                            dipendenteIndeterminato.getDataAssunzione(),
                            dipendenteIndeterminato.getDataFineRapporto(),
                            dipendenteIndeterminato.getDirigente());

                    //crea un altro arraylist con una copia degli elementi, ma con riferimento diverso
                    for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                        ScattoCarriera copiaScatto = new ScattoCarriera(scattoCarriera.getData(), scattoCarriera.getTipoScatto(), dipendenteCopia);

                        if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente) || scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente))
                            dipendenteCopia.addScattoCarriera(copiaScatto);
                    }


                    //cerca l'istanza del vecchio scatto carriera (non utilizzabile il metodo statico getIstanceFromArrayList(...) poiche' uno scatto e' identificato contemporaneamente dalla data, dalla matricola del dipendente e dal tipo dello scatto)
                    if (dipendenteCopia.getScattiEffettuati() != null) {
                        for (ScattoCarriera vecchioScatto : dipendenteCopia.getScattiEffettuati()) {

                            if (vecchioScatto.getTipoScatto().equals(TipoScatto.valueOf(vecchioTipoScatto)) &&
                                    vecchioScatto.getData().equals(vecchiaDataScatto)) {

                                //viene aggiunto lo scatto
                                dipendenteCopia.getScattiEffettuati().add(vecchioScatto);
                                break;
                            }
                        }
                    }

                    //si verifica la coerenza
                    while (dipendenteCopia.getScattiEffettuati() != null && !dipendenteCopia.getScattiEffettuati().isEmpty()) {

                        dataPromozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Promosso_a_dirigente);
                        dataRimozioneMenoRecente = recuperaDataScattoMenoRecente(dipendenteCopia, TipoScatto.Rimosso_da_dirigente);

                        if (dataPromozioneMenoRecente == null && dataRimozioneMenoRecente != null) {

                            integro = false;
                            break;

                        } else if (dataPromozioneMenoRecente != null && dataRimozioneMenoRecente != null && dataRimozioneMenoRecente.isBefore(dataPromozioneMenoRecente)) {

                            integro = false;
                            break;

                        } else {

                            //prima elimina dall'arrayList copia gli scatti gia' controllati
                            ScattoCarriera scattoPromozione = new ScattoCarriera(dataPromozioneMenoRecente, TipoScatto.Promosso_a_dirigente, dipendenteCopia);
                            ScattoCarriera scattoRimozione = new ScattoCarriera(dataRimozioneMenoRecente, TipoScatto.Rimosso_da_dirigente, dipendenteCopia);

                            dipendenteCopia.removeScattoCarriera(scattoPromozione);
                            dipendenteCopia.removeScattoCarriera(scattoRimozione);
                        }
                    }
                }
            }
        }

        return integro;
    }

    /**
    * Restituisce la data del tipo di scatto dirigenziale meno recente per il dipendente indeterminato specificato.
     *
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato}   Il dipendente indeterminato.
     * @param tipoScatto              {@link TipoScatto}                Il tipo di scatto da cercare (Promosso_a_dirigente o Rimosso_da_dirigente).
     * @return                        {@link LocalDate}                 La data del tipo di scatto meno recente, o null se non trovato.
    */
    public LocalDate recuperaDataScattoMenoRecente(DipendenteIndeterminato dipendenteIndeterminato, TipoScatto tipoScatto) {

        LocalDate dataMenoRecente = null;
        LocalDate dataCorrente;
        boolean scattoIniziale = true;

        if (tipoScatto.equals(TipoScatto.Promosso_a_dirigente) && dipendenteIndeterminato.getScattiEffettuati() != null && !dipendenteIndeterminato.getScattiEffettuati().isEmpty()) {

            for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                if (scattoCarriera.getTipoScatto().equals(TipoScatto.Promosso_a_dirigente)) {

                    dataCorrente = scattoCarriera.getData();

                    if (scattoIniziale) {

                        dataMenoRecente = dataCorrente;
                        scattoIniziale = false;

                    } else {

                        if (dataMenoRecente.isAfter(dataCorrente))
                            dataMenoRecente = dataCorrente;

                    }
                }
            }

        } else if (tipoScatto.equals(TipoScatto.Rimosso_da_dirigente) && dipendenteIndeterminato.getScattiEffettuati() != null && !dipendenteIndeterminato.getScattiEffettuati().isEmpty()) {

            for (ScattoCarriera scattoCarriera : dipendenteIndeterminato.getScattiEffettuati()) {

                if (scattoCarriera.getTipoScatto().equals(TipoScatto.Rimosso_da_dirigente)) {

                    dataCorrente = scattoCarriera.getData();

                    if (scattoIniziale) {

                        dataMenoRecente = dataCorrente;
                        scattoIniziale = false;

                    } else {

                        if (dataMenoRecente.isAfter(dataCorrente))
                            dataMenoRecente = dataCorrente;

                    }
                }

            }
        }

        return dataMenoRecente;
    }

    // METODI ELENCHI
    // SETTERS
    /**
     * Imposta un elenco, passato come parametro, di tutti i dipendenti a tempo indeterminato in memoria.
     * @param elencoAllDipendentiIndeterminati {@link ArrayList<DipendenteIndeterminato>} Elenco di dipendenti a tempo indeterminato.
    * */
    public void setElencoAllDipendentiIndeterminati(ArrayList<DipendenteIndeterminato> elencoAllDipendentiIndeterminati) {
        this.elencoAllDipendentiIndeterminati = elencoAllDipendentiIndeterminati;
    }

    /**
     * Imposta un elenco, passato come parametro, di tutti i dipendenti a progetto in memoria.
     * @param elencoAllDipendentiProgetto {@link ArrayList<DipendenteProgetto>} Elenco di dipendenti a progetto.
    * */
    public void setElencoAllDipendentiProgetto(ArrayList<DipendenteProgetto> elencoAllDipendentiProgetto){
        this.elencoAllDipendentiProgetto = elencoAllDipendentiProgetto;
    }

    /**
     * Imposta un elenco, passato come parametro, di tutti gli scatti di carriera in memoria.
     * @param elencoAllScattiCarriera {@link ArrayList<ScattoCarriera>} Elenco di scatti carriera.
    */
    public void setElencoAllScattiCarriera(ArrayList<ScattoCarriera> elencoAllScattiCarriera){
        this.elencoAllScattiCarriera = elencoAllScattiCarriera;
    }

    /**
     * Imposta un elenco, passato come parametro, di tutti i laboratori in memoria.
     * @param elencoAllLaboratori {@link ArrayList<Laboratorio>} Elenco di laboratori.
    */
    public void setElencoAllLaboratori(ArrayList<Laboratorio> elencoAllLaboratori){
        this.elencoAllLaboratori = elencoAllLaboratori;
    }

    /**
     * Imposta un elenco, passato come parametro, di tutte le attrezzature in memoria.
     * @param elencoAllAttrezzature {@link ArrayList<Attrezzatura>} Elenco di attrezzature.
    */
    public void setElencoAllAttrezzature(ArrayList<Attrezzatura> elencoAllAttrezzature){
        this.elencoAllAttrezzature = elencoAllAttrezzature;
    }

    /**
     * Imposta un elenco, passato come parametro, di tutti i progetti in memoria.
     * @param elencoAllProgetti {@link ArrayList<Progetto>} Elenco di progetti.
    */
    public void setElencoAllProgetti(ArrayList<Progetto> elencoAllProgetti){
        this.elencoAllProgetti = elencoAllProgetti;
    }

    //GETTERS
    /**
     * Recupera l'elenco di tutti i dipendenti a tempo indeterminato presenti in memoria.
     * @return {@link ArrayList<DipendenteIndeterminato>} Tutti i dipendenti a tempo indeterminato presenti in memoria.
     */
    public ArrayList<DipendenteIndeterminato> getElencoAllDipendentiIndeterminati() {
        return elencoAllDipendentiIndeterminati;
    }

    /**
     * Recupera l'elenco di tutti i dipendenti a progetto presenti in memoria.
     * @return {@link ArrayList<DipendenteProgetto>} Tutti i dipendenti a progetto presenti in memoria.
     */
    public ArrayList<DipendenteProgetto> getElencoAllDipendentiProgetto(){
        return elencoAllDipendentiProgetto;
    }

    /**
     * Recupera l'elenco di tutti gli scatti di carriera presenti in memoria.
     * @return {@link ArrayList<ScattoCarriera>} Elenco di tutti gli scatti di carriera presenti in memoria.
     */
    public ArrayList<ScattoCarriera> getElencoAllScattiCarriera(){
        return elencoAllScattiCarriera;
    }

    /**
     * Recupera l'elenco di tutti i laboratori presenti in memoria.
     * @return {@link ArrayList<Laboratorio>} Elenco di tutti i laboratori presenti in memoria.
     */
    public ArrayList<Laboratorio> getElencoAllLaboratori(){
        return elencoAllLaboratori;
    }

    /**
     * Recupera l'elenco di tutte le attrezzature presenti in memoria.
     * @return {@link ArrayList<Attrezzatura>} Elenco di tutte le attrezzature presenti in memoria.
     */
    public ArrayList<Attrezzatura> getElencoAllAttrezzature(){
        return elencoAllAttrezzature;
    }

    /**
     * Recupera l'elenco di tutti i progetti presenti in memoria.
     * @return {@link ArrayList<Progetto>} Elenco di tutti i progetti presenti in memoria.
     */
    public ArrayList<Progetto> getElencoAllProgetti(){
        return elencoAllProgetti;
    }

    // ALTRI METODI

    // METODI ADD
    /**
     * Aggiunge un dipendente a tempo indeterminato all'elenco di tutti i dipendenti a tempo indeterminato in memoria.
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato} Il dipendente da aggiungere.
     * 
     * @return True se il dipendente e' stato aggiunto con successo, altrimenti false se gia' presente.
     */
    public boolean addDipendenteIndeterminatoToElenco(DipendenteIndeterminato dipendenteIndeterminato){

        boolean test = false;

        if (elencoAllDipendentiIndeterminati == null)
            elencoAllDipendentiIndeterminati = new ArrayList<>();

        if (!elencoAllDipendentiIndeterminati.contains(dipendenteIndeterminato)) {
            elencoAllDipendentiIndeterminati.add(dipendenteIndeterminato);
            test = true;
        }

        return test;
    }

    /**
     * Aggiunge un dipendente a progetto all'elenco di tutti i dipendenti a progetto in memoria.
     * @param dipendenteProgetto {@link DipendenteProgetto} Il dipendente da aggiungere.
     * 
     * @return True se il dipendente e' stato aggiunto con successo, altrimenti false se gia' presente
     */
    public boolean addDipendenteProgettoToElenco(DipendenteProgetto dipendenteProgetto){

        boolean test = false;

        if (elencoAllDipendentiProgetto == null)
            elencoAllDipendentiProgetto = new ArrayList<>();

        if (!elencoAllDipendentiProgetto.contains(dipendenteProgetto)) {
            elencoAllDipendentiProgetto.add(dipendenteProgetto);
            test = true;
        }

        return test;
    }

    /**
     * Aggiunge uno scatto di carriera all'elenco di tutti gli scatti di carriera in memoria.
     * @param scattoCarriera {@link ScattoCarriera} Lo scatto di carriera da aggiungere.
     * 
     * @return True se lo scatto di carriera e' stato aggiunto con successo, altrimenti false se gia' presente.
     */
    public boolean addScattoCarrieraToElenco(ScattoCarriera scattoCarriera){

        boolean test = false;

        if (elencoAllScattiCarriera == null)
            elencoAllScattiCarriera = new ArrayList<>();

        if (!elencoAllScattiCarriera.contains(scattoCarriera)) {
            elencoAllScattiCarriera.add(scattoCarriera);
            test = true;
        }

        return test;
    }

    /**
     * Aggiunge un laboratorio all'elenco di tutti i laboratori in memoria.
     * @param laboratorio {@link Laboratorio} Il laboratorio da aggiungere.
     * 
     * @return True se il laboratorio e' stato aggiunto con successo, altrimenti false se gia' presente.
     */
    public boolean addLaboratorioToElenco(Laboratorio laboratorio){

        boolean test = false;

        if (elencoAllLaboratori == null)
            elencoAllLaboratori = new ArrayList<>();

        if (!elencoAllLaboratori.contains(laboratorio)) {
            elencoAllLaboratori.add(laboratorio);
            test = true;
        }

        return test;
    }

    /**
     * Aggiunge l'attrezzatura acquistata da un progetto all'elenco di tutte le attrezzature in memoria.
     * @param attrezzatura {@link Attrezzatura} L'attrezzatura da aggiungere.
     * 
     * @return True se l'attrezzatura e' stata aggiunta con successo, altrimenti false se gia' presente.
     */
    public boolean addAttrezzaturaToElenco(Attrezzatura attrezzatura){

        boolean test = false;

        if (elencoAllAttrezzature == null)
            elencoAllAttrezzature = new ArrayList<>();

        if (!elencoAllAttrezzature.contains(attrezzatura)) {
            elencoAllAttrezzature.add(attrezzatura);
            test = true;
        }

        return test;
    }

    /**
     * Aggiunge un progetto all'elenco di tutti i progetti in memoria.
     * @param progetto {@link Progetto} Il progetto da aggiungere.
     * 
     * @return True se il progetto e' stato aggiunto con successo, altrimenti false se gia' presente.
     */
    public boolean addProgettoToElenco(Progetto progetto){

        boolean test = false;

        if (elencoAllProgetti == null)
            elencoAllProgetti = new ArrayList<>();

        if (!elencoAllProgetti.contains(progetto)) {
            elencoAllProgetti.add(progetto);
            test = true;
        }

        return test;
    }

    // METODI UPDATE
    /**
     * Aggiorna i dati anagrafici di un dipendente a tempo indeterminato nell'elenco di tutti i dipendenti a tempo indeterminato in memoria.
     * @param vecchioCodFiscale {@link String}                  Il codice fiscale del dipendente da aggiornare.
     * @param nuovoDipendente   {@link DipendenteIndeterminato} I nuovi dati anagrafici del dipendente.
     */
    public void updateDatiAnagraficiDipendenteIndeterminatoInElenco(String vecchioCodFiscale, DipendenteIndeterminato nuovoDipendente){

        for (int i = 0; i < elencoAllDipendentiIndeterminati.size(); i++) {

            if (vecchioCodFiscale.equals(elencoAllDipendentiIndeterminati.get(i).getCodFiscale())) {
                //aggiorna tutti i campi del vecchio dipendente con i dati del nuovo
                elencoAllDipendentiIndeterminati.get(i).setNome(nuovoDipendente.getNome());
                elencoAllDipendentiIndeterminati.get(i).setCognome(nuovoDipendente.getCognome());
                elencoAllDipendentiIndeterminati.get(i).setCodFiscale(nuovoDipendente.getCodFiscale());
                elencoAllDipendentiIndeterminati.get(i).setDataNascita(nuovoDipendente.getDataNascita());

                //non viene inserito il break; poiche aggiorna i dati anagrafici di ogni contratto del dipendente.
            }
        }
    }

    // METODI REMOVE
    /**
     * Rimuove tutti gli scatti di carriera di un dipendente a tempo indeterminato, presenti nell'elenco di tutti gli scatti di carriera in memoria.
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato} Il dipendente a cui rimuovere tutti gli scatti di carriera.
     */
    public void removeAllScattiCarrieraDipendente(DipendenteIndeterminato dipendenteIndeterminato){

        String matricola = dipendenteIndeterminato.getMatricola();

        if (dipendenteIndeterminato.getScattiEffettuati() != null) {

            for (int i = 0; i < elencoAllScattiCarriera.size(); i++) {

                if (elencoAllScattiCarriera.get(i).getDipendente().getMatricola().equals(matricola)) {
                    elencoAllScattiCarriera.remove(i);
                }
            }
        }
    }

    /**
     * Rimuove tutti gli scatti di carriera, di un tipo specificato, di un dipendente a tempo indeterminato, presenti nell'elenco di tutti gli scatti di carriera in memoria.
     * @param dipendenteIndeterminato {@link DipendenteIndeterminato} Il dipendente a cui rimuovere gli scatti del tipo specificato.
     * @param tipoScatto              {@link TipoScatto}              Il tipo di scatto di tutti gli scatti di carriera da rimuovere.
     */
    public void removeAllTipoScattiDipendente(DipendenteIndeterminato dipendenteIndeterminato, TipoScatto tipoScatto){

        String matricola = dipendenteIndeterminato.getMatricola();
        ArrayList<ScattoCarriera> scattiDipendente = dipendenteIndeterminato.getScattiEffettuati();

        if (dipendenteIndeterminato.getScattiEffettuati() != null){

            for (int i = 0; i < scattiDipendente.size(); i++) {

                if (scattiDipendente.get(i).getTipoScatto().equals(tipoScatto)){
                    //rimuovo lo scatto dall'associazione
                    scattiDipendente.remove(i);
                    //rimuovo lo scatto dall'elenco
                    elencoAllScattiCarriera.remove(i);
                }
            }
        }
    }
}
