package PoolGame.Memento;

import PoolGame.Game;
import PoolGame.Items.Ball;
import PoolGame.Singleton.Level;

import java.util.List;

/** The Originator managing the game state */
public class Originator {
    private List<Ball> balls;
    private int score;
    private double durationTime;
    private Level level;

    /**
     * Initialise the Originator and supply the balls
     * @param game The game object to get the balls from
     */
    public Originator(Game game){
        this.balls = game.getPoolTable().getBalls();
    }

    /**
     * Saves the state of Originator into a memento object and returning it
     * @return The new memento object containing the state
     */
    public Memento saveState(){
        return new Memento(balls, score, durationTime, level);}

    /**
     * Recover the state from the supplied memento object to the Originator
     * @param memento The Memento object to extract the state from
     */
    public void RecoverState(Memento memento){
        this.balls = memento.getBalls();
        this.score = memento.getScore();
        this.durationTime = memento.getDurationTime();
        this.level = memento.getLevel();
    }

    /**
     * Update the state of Originator with the current game's state.
     * @param score The current score in the game
     * @param durationTime The time elapsed in the game
     * @param level The difficulty level of the game
     */
    public void updateState(int score, double durationTime, Level level){

        for (Ball b : this.balls){
            b.setPreviousXPos(b.getXPos());
            b.setPreviousYPos(b.getYPos());
            b.setWasDisabled(b.isDisabled());
        }

        this.score = score;
        this.durationTime = durationTime;
        this.level = level;
    }

    /**
     * Update the state of Originator to the actual game.
     * @param game The game to which the state is to be implemented to
     */
    public void updateStateToGame(Game game){
        game.setScore(this.score);

        long difference = (long) (game.getDurationTime() - this.durationTime);
        game.adjustStartTimeBy(difference);

        for (Ball b : this.balls){
            b.setXPos(b.getPreviousXPos());
            b.setYPos(b.getPreviousYPos());
            b.setXVel(0.0);
            b.setYVel(0.0);
            if (b.wasDisabled()){b.disable();}
            else { b.enable();}
        }

        if (game.getConfigReader().getLevel() != this.level){
            if (this.level==Level.easy){
                game.loadNewEasyGame();
            } else if (this.level==Level.normal){
                game.loadNewNormalGame();
            } else if (this.level==Level.hard){
                game.loadNewHardGame();
            }
        }
    }
}
