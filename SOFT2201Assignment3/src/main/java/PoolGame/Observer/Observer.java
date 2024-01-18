package PoolGame.Observer;

import PoolGame.Game;

/** The Observer interface for all observers to implement */
public interface Observer {

    /**
     * Implementing the update method
     * @param game The game object
     * @return The returned value
     */
    public int update(Game game);
}