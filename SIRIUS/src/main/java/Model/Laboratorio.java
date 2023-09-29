package Model;

import java.util.ArrayList;

/**
 * La classe ”Laboratorio” rappresenta i laboratori che si trovano
 * attualmente all’interno dell’azienda. In particolare un laboratorio avra'
 * un nome e un topic.
 * */
public class Laboratorio {

    private String nome;
    private String topic;


    //Associazione: Un laboratorio possiede sempre un responsabile scientifico
    private DipendenteIndeterminato responsabileScientifico;

    //Associazione: riporta tutti gli afferenti al laboratorio
    private ArrayList<DipendenteIndeterminato> dipendentiAfferenti = new ArrayList<DipendenteIndeterminato>(); //Creo già l'ArrayList perché l'associazione è di tipo 1..*

    //Associazione: riporta tutte le attrezzature possedute dal laboratorio
    private ArrayList<Attrezzatura> attrezzaturePossedute = null;

    //Associazione: riporta tutti i progetti a cui lavora il laboratorio
    private ArrayList<Progetto> progettiLavorati = null;



    // COSTRUTTORI
    /**
     * Costruisce un nuovo oggetto "Laboratorio" con le infromazioni fornite.
     *
     * @param nome                    {@link String}                    Il nome del laboratorio, unico nel sistema.
     * @param topic                   {@link String}                    Il topic del laboratorio.
     * @param responsabileScientifico {@link DipendenteIndeterminato}   Il responsabile scientifico del laboratorio.
     */
    public Laboratorio(String nome, String topic, DipendenteIndeterminato responsabileScientifico) {
        this.nome = nome;
        this.topic = topic;

        //Associazione (responsabile scientifico)
        this.responsabileScientifico = responsabileScientifico;
    }

    // SETTERS
    //Imposta un nome per il laboratorio
    /**
     * Imposta il nome del laboratorio.
     *
     * @param nome {@link String}   Il nome del laboratorio.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    //Imposta un topic per il laboratorio
    /**
     * Imposta il topic del laboratorio.
     *
     * @param topic {@link String}  Il topic del laboratorio.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    //Imposta il responsabile scientifico del laboratorio
    /**
     * Imposta il responsabile scientifico del laboratorio.
     *
     * @param responsabileScientifico {@link DipendenteIndeterminato}   Il responsabile scientifico del laboratorio.
     */
    public void setResponsabileScientifico(DipendenteIndeterminato responsabileScientifico) {
        this.responsabileScientifico = responsabileScientifico;
    }



    // GETTERS
    //Recupera il nome del laboratorio
    /**
     * Recupera il nome del laboratorio.
     *
     * @return {@link String}   Il nome del laboratorio.
     */
    public String getNome(){
        return nome;
    }

    //Recupera il topic del laboratorio
    /**
     * Recupera il topic del laboratorio.
     *
     * @return {@link String}   Il topic di ricerca del laboratorio.
     */
    public String getTopic(){
        return topic;
    }

    //Recupera il responsabile scientifico del laboratorio
    /**
     * Recupera il responsabile scientifico del laboratorio.
     *
     * @return {@link DipendenteIndeterminato}  Il responsabile scientifico del laboratorio.
     */
    public DipendenteIndeterminato getResponsabileScientifico(){
        return responsabileScientifico;
    }

    //Recupera tutti gli afferenti al laboratorio
    /**
     * Recupera la lista di tutti i dipendenti afferenti al laboratorio.
     *
     * @return {@link ArrayList<DipendenteIndeterminato>}   La lista di tutti i dipendenti afferenti.
     */
    public ArrayList<DipendenteIndeterminato> getAfferenti(){
        return dipendentiAfferenti;
    }

    //Recupera tutte le attrezzature del laboratorio
    /**
     * Recupera la lista di tutte le attrezzature possedute dal laboratorio.
     *
     * @return {@link ArrayList<Attrezzatura>}  La lista di tutte le attrezzature possedute.
     */
    public ArrayList<Attrezzatura> getAttrezzaturePossedute(){
        return attrezzaturePossedute;
    }

    //Recupera tutti i progetti a cui lavora il laboratorio
    /**
     * Recupera la lista di progetti a cui il laboratorio lavora.
     *
     * @return {@link ArrayList<Attrezzatura>}  La lista di progetti a cui il laboratorio lavora.
     */
    public ArrayList<Progetto> getProgettiLavorati(){
        return progettiLavorati;
    }



    // METODI
    //ASSOCIAZIONE: Progetto - Laboratorio (lavorare)
    //Aggiunge un progetto a cui lavora il laboratorio
    /**
     * Aggiunge un progetto alla lista dei progetti lavorati dal laboratorio.
     *
     * @param progetto {@link Progetto} Il progetto da aggiungere al laboratorio.
     * @return true se il progetto è stato aggiunto con successo, false se gia' presente.
     */
    public boolean addProgetto(Progetto progetto){

        boolean aggiunto = false;

        if (this.progettiLavorati == null)
            this.progettiLavorati = new ArrayList<Progetto>();

        if (!this.progettiLavorati.contains(progetto)) {

            this.progettiLavorati.add(progetto);

            aggiunto = true;

        }

        return aggiunto;
    }

    //Rimuove un progetto a cui lavora il laboratorio
    /**
     * Rimuove un progetto dalla lista dei progetti lavorati dal laboratorio.
     *
     * @param progetto {@link Progetto} Il progetto da rimuovere dal laboratorio.
     * @return true se il progetto è stato rimosso con successo, false se non esiste.
     */
    public boolean removeProgetto(Progetto progetto){

        boolean rimosso = false;

        if (this.progettiLavorati != null) {
            if (this.progettiLavorati.contains(progetto)) {

                this.progettiLavorati.remove(progetto);

                rimosso = true;

                if (this.progettiLavorati.isEmpty())
                    this.progettiLavorati = null;
            }
        }

        return rimosso;
    }


    //ASSOCIAZIONE: Attrezzatura - Laboratorio (possedere)
    //Aggiunge una attrezzatura al laboratorio
    /**
     * Aggiunge un'attrezzatura alla lista delle attrezzature possedute dal laboratorio.
     *
     * @param attrezzatura {@link Attrezzatura} L'attrezzatura da aggiungere al laboratorio.
     * @return true se l'attrezzatura è stata aggiunta con successo, false se gia' presente.
     */
    public boolean addAttrezzatura(Attrezzatura attrezzatura) {

        boolean aggiunto = false;

        if (this.attrezzaturePossedute == null)
            this.attrezzaturePossedute = new ArrayList<Attrezzatura>();

        if (!this.attrezzaturePossedute.contains(attrezzatura)) {

            this.attrezzaturePossedute.add(attrezzatura);

            aggiunto = true;

        }

        return aggiunto;
    }

    //Rimuove un'attrezzatura del laboratorio
    /**
     * Rimuove un'attrezzatura dalla lista delle attrezzature possedute dal laboratorio.
     *
     * @param attrezzatura {@link Attrezzatura} L'attrezzatura da rimuovere dal laboratorio.
     * @return true se l'attrezzatura è stata rimossa con successo, false se non esiste.
     */
    public boolean removeAttrezzatura(Attrezzatura attrezzatura){

        boolean rimosso = false;

        if (this.attrezzaturePossedute != null) {
            if (this.attrezzaturePossedute.contains(attrezzatura)) {

                this.attrezzaturePossedute.remove(attrezzatura);

                rimosso = true;

                if (this.attrezzaturePossedute.isEmpty())
                    this.attrezzaturePossedute = null;
            }
        }

        return rimosso;
    }

    //ASSOCIAZIONE: Dipendente indeterminato - Laboratorio (afferire)
    //Aggiunge afferenti al laboratorio
    /**
     * Aggiunge un dipendente afferente al laboratorio.
     *
     * @param afferente {@link DipendenteIndeterminato} Il dipendente afferente da aggiungere al laboratorio.
     * @return true se il dipendente è stato aggiunto con successo, false se gia' registrato.
     */
    public boolean addAfferente(DipendenteIndeterminato afferente) {

        boolean aggiunto = false;

        if (!this.dipendentiAfferenti.contains(afferente)) {

            this.dipendentiAfferenti.add(afferente);

            aggiunto = true;
        }

        return aggiunto;
    }

    //Rimuove un afferente dal laboratorio
    /**
     * Rimuove un dipendente afferente dal laboratorio.
     *
     * @param dipendente {@link DipendenteIndeterminato}    Il dipendente afferente da rimuovere dal laboratorio.
     * @return true se il dipendente è stato rimosso con successo, false se non esiste.
     */
    public boolean removeAfferente(DipendenteIndeterminato dipendente) {

        boolean rimosso = false;

        if (this.dipendentiAfferenti.contains(dipendente)) {

            this.dipendentiAfferenti.remove(dipendente);

            rimosso = true;
        }

        return rimosso;
    }

    //OVERRIDE DI METODI EREDITATI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "Laboratorio" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi "nome", "topic" e "responsabileScientifico".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "Laboratorio" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "Laboratorio" e i campi sono uguali all'istanza di laboratorio considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Laboratorio))
            return false;

        Laboratorio laboratorio = (Laboratorio) obj;

        if (!laboratorio.getNome().equals(this.getNome()))                                          return false;
        if (!laboratorio.getTopic().equals(this.getTopic()))                                        return false;
        if (!laboratorio.getResponsabileScientifico().equals(this.getResponsabileScientifico()))    return false;


        return true;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa del laboratorio invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "Laboratorio" considerato.
     * */
    public String toString(){

        return String.format("Nome: %s%nTopic: %s%nResponsabile scientifico: %s %s%nMatricola: %s",
                nome, topic, responsabileScientifico.getNome(), responsabileScientifico.getCognome(), responsabileScientifico.getMatricola());
    }
}
