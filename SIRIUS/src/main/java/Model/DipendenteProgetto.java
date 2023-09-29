package Model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * La classe ”Dipendente a progetto” rappresenta i dipendenti assunti per
 * lavorare su un progetto tramite un contratto a tempo determinato.
 * Eredita da ”Dipendente” gli variabili ed i metodi di istanza. Agli attributi di ”Dipendente”, si aggiunge
 * una data di scadenza (ovvero la data in cui scade il contratto a tempo
 * determinato) e un costo.
 * */
public class DipendenteProgetto extends Dipendente {

    private LocalDate scadenza;
    private BigDecimal costo;

    //Associazione: rappresenta il progetto da cui è stato ingaggiato
    private Progetto progettoProprietario;



    // COSTRUTTORI
    //ci saranno 2 possibili costruttori al fine di coprire tutti i casi, in quanto l'indirizzo puo' essere nullabile

    //si indirizzo
    /**
     * Costruisce un nuovo oggetto "DipendenteProgetto" con le infromazioni fornite, con indirizzo specificato.
     *
     * @param nome                  {@link String}      Il nome del dipendente.
     * @param cognome               {@link String}      Il cognome del dipendente.
     * @param codFiscale            {@link String}      Il codice fiscale del dipendente.
     * @param matricola             {@link String}      La matricola del dipendente, unica nel sistema.
     * @param indirizzo             {@link String}      L'indirizzo del dipendente.
     * @param dataNascita           {@link LocalDate}   La data di nascita del dipendente.
     * @param dataAssunzione        {@link LocalDate}   La data di assunzione del dipendente.
     * @param scadenza              {@link LocalDate}   La data di scadenza del contratto di lavoro.
     * @param costo                 {@link BigDecimal}  Il costo dell'ingaggio.
     * @param progettoProprietario  {@link Progetto}    Il progetto da cui il dipendente è stato ingaggiato.
     */
    public DipendenteProgetto (String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, BigDecimal costo, Progetto progettoProprietario) {

        super(nome, cognome, codFiscale, matricola, indirizzo, dataNascita, dataAssunzione);

        this.scadenza = scadenza;
        this.costo = costo;

        //Associazione (ingaggiare)
        this.progettoProprietario = progettoProprietario;
    }

    //no indirizzo
    /**
     * Costruisce un nuovo oggetto "DipendenteProgetto" con le infromazioni fornite, con indirizzo specificato.
     *
     * @param nome                  {@link String}      Il nome del dipendente.
     * @param cognome               {@link String}      Il cognome del dipendente.
     * @param codFiscale            {@link String}      Il codice fiscale del dipendente.
     * @param matricola             {@link String}      La matricola del dipendente, unica nel sistema.
     * @param dataNascita           {@link LocalDate}   La data di nascita del dipendente.
     * @param dataAssunzione        {@link LocalDate}   La data di assunzione del dipendente.
     * @param scadenza              {@link LocalDate}   La data di scadenza del contratto di lavoro.
     * @param costo                 {@link BigDecimal}  Il costo dell'ingaggio.
     * @param progettoProprietario  {@link Progetto}    Il progetto da cui il dipendente è stato ingaggiato.
     */
    public DipendenteProgetto (String matricola, String nome, String cognome, String codFiscale, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, BigDecimal costo, Progetto progettoProprietario) {

        super(nome, cognome, codFiscale, matricola, dataNascita, dataAssunzione);

        this.scadenza = scadenza;
        this.costo = costo;

        //Associazione (ingaggiare)
        this.progettoProprietario = progettoProprietario;
    }


    // SETTERS
    /**
     * Imposta la data di scadenza del contratto di lavoro del dipendente.
     *
     * @param scadenza {@link LocalDate} La data di scadenza del contratto.
     */
    public void setScadenza (LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    /**
     * Imposta il costo dell'ingaggio del dipendente.
     *
     * @param costo {@link BigDecimal}  Il costo dell'ingaggio.
     */
    public void setCosto (BigDecimal costo) {
        this.costo = costo;
    }


    //Associazione: Imposta quale progetto ha ingaggiato il dipendente
    /**
     * Imposta il progetto da cui il dipendente è stato ingaggiato.
     *
     * @param progettoProprietario {@link Progetto} Il progetto da cui il dipendente è stato ingaggiato.
     */
    public void setProgettoProprietario(Progetto progettoProprietario) {
        this.progettoProprietario = progettoProprietario;
    }

    // GETTERS
    /**
     * Restituisce la data di scadenza del contratto di lavoro del dipendente.
     *
     * @return {@link LocalDate}    La data di scadenza del contratto.
     */
    public LocalDate getScadenza(){
        return scadenza;
    }

    /**
     * Restituisce il costo dell'ingaggio del dipendente.
     *
     * @return {@link BigDecimal}   Il costo dell'ingaggio.
     */
    public BigDecimal getCosto(){
        return costo;
    }

    /**
     * Restituisce il progetto da cui il dipendente è stato ingaggiato.
     *
     * @return {@link Progetto}   Il progetto da cui il dipendente è stato ingaggiato.
     */
    public Progetto getProgettoProprietario() {
        return this.progettoProprietario;
    }



    //METODI
    //OVERRIDE DI METODI EREDITATI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "DipendenteProgetto" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi ereditati dalla classe astratta estesa "Dipendente", in piu' ai campi "costo", "ProgettoProprietario" e "scadenza".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "DipendenteProgetto" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "DipendenteProgetto" e i campi sono uguali all'istanza di dipendente a progetto considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        boolean val = super.equals(obj);

        if (val)
        {
            if (obj == this)
                return true;

            if (!(obj instanceof DipendenteProgetto))
                return false;

            DipendenteProgetto dipendente = (DipendenteProgetto) obj;


            if (!dipendente.getCosto().equals(this.getCosto()))         return false;
            if (!dipendente.getProgettoProprietario().equals(this.getProgettoProprietario()))   return false;
            if (!dipendente.getScadenza().equals(this.getScadenza()))   return false;

        }

        return val;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa del dipendente a progetto invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "DipendenteProgetto" considerato.
     * */
    @Override
    public String toString() {
        return String.format("Ruolo: %s%nScadenza: %s%nCosto: %.2f%n%s", this.getClass().getSimpleName(), scadenza, costo, super.toString());
    }

}
