package Model;

import java.math.BigDecimal;

/**
 *  La classe ”Attrezzatura” rappresenta le attrezzature acquistate tramite i
 *  fondi di progetti, che possono o meno trovarsi all’interno di un
 *  laboratorio. In particolare, un’attrezzatura e' un oggetto che avra' un
 *  costo e una descrizione (che appunto descrive l’oggetto in questione).
 * */
public class Attrezzatura {

    private Integer idAttrezzatura;
    private String descrizione;
    private BigDecimal costo;

    //Associazione: laboratorio possedente
    private Laboratorio laboratorioPossedente = null;

    //Associazione: progetto proprietario
    private Progetto progettoProprietario;

    // COSTRUTTORI
    /**
     * Costruisce un nuovo oggetto "Attrezzatura" con le infromazioni fornite, non specificando il laboratorio che la possiede, che sara' nullo.
     *
     * @param idAttrezzatura        {@link Integer}         Rappresenta l'identificativo unico dell'attrezzatura.
     * @param descrizione           {@link String}          Rappresenta la descrizione dell'attrezzatura.
     * @param costo                 {@link BigDecimal}      Rappresenta il prezzo di acquisto dell'attrezzatura.
     * @param progettoProprietario  {@link Progetto}        Rappresenta il progetto che acquista l'attrezzatura, ovvero il progetto proprietario.
     * */
    public Attrezzatura(Integer idAttrezzatura, String descrizione, BigDecimal costo, Progetto progettoProprietario) {
        this.idAttrezzatura = idAttrezzatura;
        this.descrizione = descrizione;
        this.costo = costo;

        //Associazione (acquistare)
        this.progettoProprietario = progettoProprietario;
    }

    /**
     * Costruisce un nuovo oggetto "Attrezzatura" con le infromazioni fornite, specificando il laboratorio che la possiede.
     *
     * @param idAttrezzatura        {@link Integer}         Rappresenta l'identificativo unico dell'attrezzatura.
     * @param descrizione           {@link String}          Rappresenta la descrizione dell'attrezzatura.
     * @param costo                 {@link BigDecimal}      Rappresenta il prezzo di acquisto dell'attrezzatura.
     * @param progettoProprietario  {@link Progetto}        Rappresenta il progetto che acquista l'attrezzatura, ovvero il progetto proprietario.
     * @param laboratorioPossedente {@link Laboratorio}     Rappresenta il laboratorio che possiede l'attrezzatura.
     * */
    public Attrezzatura(Integer idAttrezzatura, String descrizione, BigDecimal costo, Progetto progettoProprietario, Laboratorio laboratorioPossedente) {
        this.idAttrezzatura = idAttrezzatura;
        this.descrizione = descrizione;
        this.costo = costo;

        //Associazione (acquistare)
        this.progettoProprietario = progettoProprietario;

        //Associazione (possedere)
        this.laboratorioPossedente = laboratorioPossedente;
    }

    // SETTERS
    /**
     * Imposta l'identificativo unico dell'attrezzatura.
     *
     * @param idAttrezzatura {@link Integer}    Numero intero che rappresentera' l'identificativo unico dell'attrezzatura.
     * */
    public void setIdAttrezzatura(Integer idAttrezzatura){
        this.idAttrezzatura = idAttrezzatura;
    }

    /**
     * Imposta la descrizione dell'attrezzatura.
     *
     * @param descrizione  {@link String}   Stringa che rappresentera' la descrizione dell'attrezzatura.
     * */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Imposta il costo dell'attrezzatura.
     *
     * @param costo {@link BigDecimal}  Valore monetario che rappresentera' i prezzo di acquisto dell'attrezzatura.
     * */
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    /**
     * Imposta il laboratorio che possiede l'attrezzatura.
     *
     * @param laboratorioPossedente {@link Laboratorio} Rappresenta il laboratorio che possiedera' l'attrezzatura.
     * */
    public void setLaboratorioPossedente(Laboratorio laboratorioPossedente){
        this.laboratorioPossedente = laboratorioPossedente;
    }

    /**
     * Imposta il progetto proprietario dell'attrezzatura, ovvero il progetto che ne ha pagato il prezzo di acquisto.
     *
     * @param progettoProprietario  {@link Progetto}    Rappresenta il progetto che acquista l'attrezzatura, nonche' il progetto proprietario.
     * */
    public void setProgettoProprietario(Progetto progettoProprietario) {
        this.progettoProprietario = progettoProprietario;
    }

    // GETTERS
    /**
     * Restituisce l'identificativo dell'attrezzatura.
     *
     * @return {@link Integer}  Numero intero che rappresenta l'identificativo dell'attrezzatura.
     * */
    public Integer getIdAttrezzatura(){
        return idAttrezzatura;
    }

    /**
     * Restituisce la descrizione dell'attrezzatura.
     *
     * @return {@link String}   Stringa che rappresenta la descrizione dell'attrezzatura.
     * */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce il costo dell'attrezzatura, nonche' il suo prezzo di acquisto.
     *
     * @return {@link BigDecimal}   Il prezzo di acquisto pagato dal progetto proprietario.
     * */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * Restituisce, se esiste, il laboratorio che possiede l'attrezzatura.
     *
     * @return  {@link Laboratorio} Il laboratorio che, eventualemte, possiede l'attrezzatura. Puo' essere nullo.
     * */
    public Laboratorio getLaboratorioPossedente() {
        return laboratorioPossedente;
    }

    /**
     * Restituisce il progetto che ha acquistato l'attrezzatura, nonche' il progetto proprietario.
     *
     * @return {@link Progetto} Il progetto proprietario dell'attrezzatura.
     * */
    public Progetto getProgettoProprietario() {
        return progettoProprietario;
    }

    // METODI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "Attrezzatura" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi "descrizione" e "costo".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "Attrezzatura" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "Attrezzatura" e i campi sono uguali all'istanza di attrezzatura considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Attrezzatura))
            return false;

        Attrezzatura attrezzatura = (Attrezzatura) obj;

        if (!attrezzatura.getDescrizione().equals(this.getDescrizione()))       return false;
        if (!attrezzatura.getCosto().equals(this.getCosto()))                   return false;

        return true;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa dell'attrezzatura invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "Attrezzatura" considerato.
     * */
    @Override
    public String toString() {
        if (laboratorioPossedente != null)
            return String.format("idAttrezzatura: %s%nDescrizione: %s%nCosto: %.2f%nProgetto: %s%nLaboratorio: %s", idAttrezzatura, descrizione, costo, progettoProprietario.getNome(), laboratorioPossedente.getNome());
        else
            return String.format("idAttrezzatura: %s%nDescrizione: %s%nCosto: %.2f%nProgetto: %s", idAttrezzatura, descrizione, costo, progettoProprietario.getNome());
    }
}
