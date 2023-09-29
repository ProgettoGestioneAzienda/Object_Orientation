package Model;

import java.time.LocalDate;

/**
 * La classe ”Dipendente” e' una classe astratta, nonche' generalizzazione di ”Dipendente a tempo
 * indeterminato” e ”Dipendente a progetto”. Queste ultime erediteranno le variabili di istanza ed i metodi della classe ”Dipendente”:
 * la matricola, il nome, il cognome, il codice fiscale, l’indirizzo di residenza, la data di nascita, la data di assunzione.
 * In quanto classe astratta, non potranno essere istanziati oggetti di tipo "Dipendente",
 * pertanto non e' prevista la presenza di dipendenti che non siano ne' dipendenti a tempo indeterminato, ne' dipendenti a progetto.
 * */
public abstract class Dipendente {
    private String matricola;
    private String nome;
    private String cognome;
    private String codFiscale;
    private String indirizzo = null;
    private LocalDate dataNascita;
    private LocalDate dataAssunzione;

    // COSTRUTTORI
    //no indirizzo
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" o "DipendenteProgetto", con le infromazioni fornite, non specificando l'indirizzo, che sara' nullo.
     *
     * @param nome              {@link String}      Rappresenta il nome del dipendente.
     * @param cognome           {@link String}      Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}      Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}      Rappresenta la matricola del dipendente, unica nel sistema.
     * @param dataNascita       {@link LocalDate}   Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}   Rappresenta la data di assunzione del dipendente.
     * */
    protected Dipendente(String nome, String cognome, String codFiscale, String matricola, LocalDate dataNascita, LocalDate dataAssunzione) {
        this.nome = nome;
        this.cognome = cognome;
        this.codFiscale = codFiscale;
        this.matricola = matricola;
        this.dataNascita = dataNascita;
        this.dataAssunzione = dataAssunzione;
    }

    //si indirizzo
    /**
     * Costruisce un nuovo oggetto "DipendenteIndeterminato" o "DipendenteProgetto", con le infromazioni fornite, specificando l'indirizzo.
     *
     * @param nome              {@link String}      Rappresenta il nome del dipendente.
     * @param cognome           {@link String}      Rappresenta il cognome del dipendente.
     * @param codFiscale        {@link String}      Rappresenta il codice fiscale del dipendente.
     * @param matricola         {@link String}      Rappresenta la matricola del dipendente, unica nel sistema.
     * @param indirizzo         {@link String}      Rappresenta l'indirizzo del dipendente.
     * @param dataNascita       {@link LocalDate}   Rappresenta la data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}   Rappresenta la data di assunzione del dipendente.
     * */
    protected Dipendente(String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione){
        this.nome = nome;
        this.cognome = cognome;
        this.codFiscale = codFiscale;
        this.matricola = matricola;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.dataAssunzione = dataAssunzione;
    }

    // SETTERS
    /**
     * Imposta la matricola, unica nel sistema, del dipendente. Simboleggia il particolare vincolo contrattuale stabilito con l'azienda, ed identifica il dipendente.
     *
     * @param matricola {@link String}  Stringa di 8 caratteri alfanumerici, che rappresenta la matricola identificante della carriera del dipendente, unica nel sistema.
     * */
    public void setMatricola (String matricola) {
        this.matricola = matricola;
    }

    /**
     * Imposta il nome del dipendente.
     *
     * @param nome  {@link String}  Stringa che rappresenta il nome del dipendente.
     * */
    public void setNome (String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome del dipendente.
     *
     * @param cognome   {@link String}  Stringa che rappresenta il cognome del dipendente.
     * */
    public void setCognome (String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta il codice fiscale del dipendente.
     *
     * @param codFiscale    {@link String}  Codice alfanumerico di 16 caratteri, che rappresenta il codice fiscale del dipendente.
     * */
    public void setCodFiscale (String codFiscale) {
        this.codFiscale = codFiscale;
    }

    /**
     * Imposta la data di nascita del dipendente.
     *
     * @param dataNascita   {@link LocalDate}   Data con formattazione (YYYY-MM-DD), che rappresenta la data di nascita del dipendente.
     * */
    public void setDataNascita (LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Imposta la data di assunzione del dipendente.
     *
     * @param dataAssunzione    {@link LocalDate}   Data con formattazione (YYYY-MM-DD), che rappresenta la data di assunzione del dipendente.
     * */
    public void setDataAssunzione (LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    /**
     * Imposta l'indirizzo del dipendente.
     *
     * @param indirizzo {@link String}  Stringa che rappresenta l'indirizzo del dipendente.
     * */
    public void setIndirizzo (String indirizzo) {
        this.indirizzo = indirizzo;
    }

    // GETTERS
    /**
     * Restituisce l'identificativo della carriera del dipendente, ovvero la matricola, unica nel sistema.
     *
     * @return {@link String}   Stringa di 8 caratteri alfanumerici, che rappresenta la matricola identificante della carriera del dipendente, unica nel sistema.
     * */
    public String getMatricola(){
        return matricola;
    }

    /**
     * Restituisce il nome del dipendente.
     *
     * @return {@link String}   Stringa che rappresenta il nome del dipendente.
     * */
    public String getNome(){
        return nome;
    }

    /**
     * Restituisce il cognome del dipendente.
     *
     * @return {@link String}   Stringa che rappresenta il cognome del dipendente.
     * */
    public String getCognome(){
        return cognome;
    }

    /**
     * Restituisce il codice fiscale del dipendente.
     *
     * @return {@link String}   Stringa di 16 caratteri alfanumerici, che rappresenta il codice fiscale del dipendente.
     * */
    public String getCodFiscale(){
        return codFiscale;
    }

    /**
     * Restituisce la data di nascita del dipendente.
     *
     * @return {@link LocalDate}    Data con formattazione (YYYY-MM-DD), che rappresenta la data di nascita del dipendente.
     * */
    public LocalDate getDataNascita(){
        return dataNascita;
    }

    /**
     * Restituisce la data di assunzione del dipendente.
     *
     * @return {@link String}   Data con formattazione (YYYY-MM-DD), che rappresenta la data di assunzione del dipendente.
     * */
    public LocalDate getDataAssunzione(){
        return dataAssunzione;
    }

    /**
     * Restituisce l'indirizzo del dipendente.
     *
     * @return {@link String}   Stringa che rappresenta l'indirizzo del dipendente.
     * */
    public String getIndirizzo() {
        return indirizzo;
    }

    //METODI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "DipendenteIndeterminato" e "DipendenteProgetto"
     * ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi "matricola", "nome", "cognome", "codFiscale", "dataNascita", "dataAssunzione".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "DipendenteIndeterminato" o "DipendenteProgetto" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "DipendenteIndeterminato" o "DipendenteProgetto" e i campi sono uguali all'istanza considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Dipendente))
            return false;

        Dipendente dipendente = (Dipendente) obj;

        if (!dipendente.getMatricola().equals(this.getMatricola()))                 return false;
        if (!dipendente.getNome().equals(this.getNome()))                           return false;
        if (!dipendente.getCognome().equals(this.getCognome()))                     return false;
        if (!dipendente.getCodFiscale().equals(this.getCodFiscale()))               return false;
        if (!dipendente.getDataNascita().equals(this.getDataNascita()))             return false;
        if (!dipendente.getDataAssunzione().equals(this.getDataAssunzione()))       return false;


        if (!(dipendente.getIndirizzo() == null && this.getIndirizzo() == null)) {
            if (this.getIndirizzo() == null) {
                if (!dipendente.getIndirizzo().equals(this.getIndirizzo()))
                    return false;
            } else {
                if (!this.getIndirizzo().equals(dipendente.getIndirizzo()))
                    return false;
            }
        }

        return true;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa del dipendente invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "DipendenteIndeterminato" o "DipendenteProgetto" considerato.
     * */
    @Override
    public String toString() {
        return String.format("Nome: %s%nCognome: %s%nMatricola: %s%nCodice Fiscale: %s%nIndirizzo: %s%nData di Nascita: %s%nData Assunzione: %s",
                nome, cognome, matricola, codFiscale, indirizzo, dataNascita, dataAssunzione);
    }
}
