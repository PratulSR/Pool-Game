package PoolGame.Memento;

/** The Caretaker safeguarding the Momento object */
public class Caretaker {

    /** The Memento object containing the state */
    private Memento memento;

    /**
     * Get the stored memento object
     * @return The memento object
     */
    public Memento getMemento(){ return this.memento;}

    /**
     * Set the memento object
     * @param m The new memento object to be saved
     */
    public void setMemento(Memento m){ this.memento = m;}

}