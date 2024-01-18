package PoolGame.Observer;

import PoolGame.Game;
import PoolGame.Items.Ball;

/** The BallObserver observing the game */
public class BallObserver implements Observer {

    /** The score increment associated with the ball */
    private int score;

    /** The ball object to observe*/
    private Ball ball;

    /**
     * Initialise the ball observer and attach it to the ball
     * @param b The ball object
     */
    public BallObserver(Ball b) {
        this.ball = b;
        this.score = 0;
        b.attach(this);
    }

    /**
     * Implementing the ball's update method
     * @param game The game object
     * @return The returned int value
     */
    public int update(Game game) {
        this.score = game.getColorToScore().get(this.ball.getColour());
        return this.score;
    }
}
