package PoolGame.Memento;

import PoolGame.Items.Ball;
import PoolGame.Singleton.Level;
import java.util.List;

/**
 * The memento class for saving the state of the game
 */
public class Memento {
    private List<Ball> balls; //PoolTable.getBalls()
    private int score; //Game.getScore()
    private double durationTime; //Game.getDurationTime()
    private Level level; //ConfigReader.getLevel()

    /**
     * Initialise the Memento object
     * @param balls The list of balls
     * @param score The score of the game
     * @param durationTime The time elapsed in the game
     * @param level The difficulty level of the game
     */
    public Memento(List<Ball> balls, int score, double durationTime, Level level){
        this.balls = balls;
        this.score = score;
        this.durationTime = durationTime;
        this.level = level;
    }

    /**
     * Returns the time elapsed
     * @return The duration time saved
     */
    public double getDurationTime() {
        return durationTime;
    }

    /**
     * Returns the score
     * @return The score saved
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the difficulty level of the game
     * @return The level saved
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Returns the list of balls on the table
     * @return The list of balls
     */
    public List<Ball> getBalls(){
        return this.balls;
    }

    /**
     * Set the List of Balls
     * @param balls The List of Balls
     */
    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

}
