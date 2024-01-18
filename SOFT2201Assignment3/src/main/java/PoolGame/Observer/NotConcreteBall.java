package PoolGame.Observer;

import PoolGame.Game;

/** The Subject class for the Ball class to extend */
public abstract class NotConcreteBall {
    private Observer o;

    /**
     * Attaches the observer to a Ball
     * @param o The Observer
     */
    public void attach(Observer o) {
        this.o = o;
    }

    /**
     * Detaches the observer from a Ball
     * @param o The Observer
     */
    public void detach(Observer o) {
        if (this.o == o) {
            this.o = null;
        }
    }

    /**
     * Informs the observer of an update
     * @param game The Game object
     * @return The value of the state updated
     */
    public int inform(Game game) {
        if (o != null) {return this.o.update(game);}
        return 0;
    }

}
