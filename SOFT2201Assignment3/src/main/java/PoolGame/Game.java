package PoolGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import PoolGame.Builder.BallBuilderDirector;
import PoolGame.Config.BallConfig;
import PoolGame.Config.PocketConfig;
import PoolGame.Config.PocketsConfig;
import PoolGame.Items.Ball;
import PoolGame.Items.Pocket;
import PoolGame.Items.PoolTable;
import PoolGame.Memento.Caretaker;
import PoolGame.Memento.Originator;
import PoolGame.Observer.BallObserver;
import PoolGame.Singleton.ConfigReader;
import PoolGame.Strategy.BallPocketStrategy;
import PoolGame.Strategy.PocketOnce;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static javafx.application.Application.launch;

/** The game class that runs the game */
public class Game {
    private PoolTable table;
    private boolean shownWonText = false;
    private final Text winText = new Text(50, 50, "Win and Bye!!");
    private Text scoreText;
    private int score;
    private HashMap<Color, Integer> colorToScore;
    private long startTime;
    private double durationTime;
    private Text timeText;
    private ConfigReader configReader;
    private Button undoStateButton;
    private Button loadNewNormalGameButton;
    private Button loadNewEasyGameButton;
    private Button loadNewHardGameButton;
    private Originator stateOriginator;
    private Caretaker stateKeep;
    private Group root;
    private Text levelMenuText;
    private Text undoText;
    private Text cheatText;
    private Button removeRedBallsButton;
    private Button removeYellowBallsButton;
    private Button removeGreenBallsButton;
    private Button removeBrownBallsButton;
    private Button removeBlueBallsButton;
    private Button removePurpleBallsButton;
    private Button removeBlackBallsButton;
    private Button removeOrangeBallsButton;
    private HashMap<String, Button> colorToButton;

    /**
     * Initialise the game with the provided config
     * @param config The config parser to load the config from
     */
    public Game(ConfigReader config) {
        this.setup(config);
    }

    /**
     * Setup the game with the details from the provided config
     * @param config The config parser to load the config from
     */
    private void setup(ConfigReader config) {
        this.configReader = config;
        this.table = new PoolTable(config.getConfig().getTableConfig());
        List<BallConfig> ballsConf = config.getConfig().getBallsConfig().getBallConfigs();
        List<Ball> balls = new ArrayList<>();
        BallBuilderDirector builder = new BallBuilderDirector();
        builder.registerDefault();
        for (BallConfig ballConf: ballsConf) {
            Ball ball = builder.construct(ballConf);
            if (ball == null) {
                System.err.println("WARNING: Unknown ball, skipping...");
            } else {
                balls.add(ball);
                if (ball.getBallType().equals(Ball.BallType.NORMALBALL)){
                    new BallObserver(ball);
                }
            }
        }
        PocketsConfig pocketsConfig = config.getConfig().getPocketsConfig();
        for (PocketConfig pc : pocketsConfig.getPocketConfigs()){
            this.table.addPocket(new Pocket(pc.getPositionConfig().getX(), pc.getPositionConfig().getY(), pc.getRadius()));
        }
        this.table.setupBalls(balls);
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);
        this.winText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
        this.score = 0;
        this.scoreText = new Text(800,50,"Score : "+this.score);
        this.scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.undoText = new Text(745,140,"Undo Previous Move ?");
        this.undoText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.cheatText = new Text(820,300,"Cheat ?");
        this.cheatText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.colorToScore = new HashMap<>();
        this.colorToScore.put(Color.valueOf("red"),1);
        this.colorToScore.put(Color.valueOf("yellow"),2);
        this.colorToScore.put(Color.valueOf("green"),3);
        this.colorToScore.put(Color.valueOf("brown"),4);
        this.colorToScore.put(Color.valueOf("blue"),5);
        this.colorToScore.put(Color.valueOf("purple"),6);
        this.colorToScore.put(Color.valueOf("black"),7);
        this.colorToScore.put(Color.valueOf("orange"),8);
        this.startTime = System.currentTimeMillis();
        this.durationTime = (System.currentTimeMillis() - this.startTime)/1000.0;
        this.timeText = new Text(730,80,"Time Elapsed : "+this.durationTime);
        this.timeText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.levelMenuText = new Text(730,220,"Switch to Another Level ?");
        this.levelMenuText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        this.undoStateButton = new Button("Undo");
        this.undoStateButton.setLayoutX(830);
        this.undoStateButton.setLayoutY(150);

        this.colorToButton = new HashMap<>();

        this.removeRedBallsButton = new Button("Red");
        this.removeRedBallsButton.setLayoutX(663);
        this.removeRedBallsButton.setLayoutY(310);
        this.colorToButton.put("red", this.removeRedBallsButton);

        this.removeYellowBallsButton = new Button("Yellow");
        this.removeYellowBallsButton.setLayoutX(705);
        this.removeYellowBallsButton.setLayoutY(310);
        this.colorToButton.put("yellow", this.removeYellowBallsButton);

        this.removeGreenBallsButton = new Button("Green");
        this.removeGreenBallsButton.setLayoutX(760);
        this.removeGreenBallsButton.setLayoutY(310);
        this.colorToButton.put("green", this.removeGreenBallsButton);

        this.removeBrownBallsButton = new Button("Brown");
        this.removeBrownBallsButton.setLayoutX(813);
        this.removeBrownBallsButton.setLayoutY(310);
        this.colorToButton.put("brown", this.removeBrownBallsButton);

        this.removeBlueBallsButton = new Button("Blue");
        this.removeBlueBallsButton.setLayoutX(867);
        this.removeBlueBallsButton.setLayoutY(310);
        this.colorToButton.put("blue", this.removeBlueBallsButton);

        this.removePurpleBallsButton = new Button("Purple");
        this.removePurpleBallsButton.setLayoutX(911);
        this.removePurpleBallsButton.setLayoutY(310);
        this.colorToButton.put("purple", this.removePurpleBallsButton);

        this.removeBlackBallsButton = new Button("Black");
        this.removeBlackBallsButton.setLayoutX(964);
        this.removeBlackBallsButton.setLayoutY(310);
        this.colorToButton.put("black", this.removeBlackBallsButton);

        this.removeOrangeBallsButton = new Button("Orange");
        this.removeOrangeBallsButton.setLayoutX(1011);
        this.removeOrangeBallsButton.setLayoutY(310);
        this.colorToButton.put("orange", this.removeOrangeBallsButton);

        this.loadNewNormalGameButton = new Button("Normal");
        this.loadNewNormalGameButton.setLayoutX(827);
        this.loadNewNormalGameButton.setLayoutY(230);

        this.loadNewEasyGameButton = new Button("Easy");
        this.loadNewEasyGameButton.setLayoutX(780);
        this.loadNewEasyGameButton.setLayoutY(230);

        this.loadNewHardGameButton = new Button("Hard");
        this.loadNewHardGameButton.setLayoutX(890);
        this.loadNewHardGameButton.setLayoutY(230);

        this.stateOriginator = new Originator(this.returnGame());
        this.stateKeep = new Caretaker();
        this.stateOriginator.updateState(this.getScore(),this.getDurationTime(), this.configReader.getLevel());
        this.stateKeep.setMemento(this.stateOriginator.saveState());

        EventHandler<ActionEvent> undoStateHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try{
                    stateOriginator.RecoverState(stateKeep.getMemento());
                    stateOriginator.updateStateToGame(returnGame());
                }
                catch (NullPointerException n){System.out.println("Null Pointer Exception");}

            }
        };

        EventHandler<ActionEvent> loadNewNormalGameHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                loadNewNormalGame();
            }
        };

        EventHandler<ActionEvent> loadNewEasyGameHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                loadNewEasyGame();
            }
        };

        EventHandler<ActionEvent> loadNewHardGameHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                loadNewHardGame();
            }
        };

        EventHandler<ActionEvent> removeRedBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeRedBalls();
            }
        };

        EventHandler<ActionEvent> removeYellowBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeYellowBalls();
            }
        };

        EventHandler<ActionEvent> removeGreenBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeGreenBalls();
            }
        };

        EventHandler<ActionEvent> removeBrownBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeBrownBalls();
            }
        };

        EventHandler<ActionEvent> removeBlueBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeBlueBalls();
            }
        };

        EventHandler<ActionEvent> removePurpleBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removePurpleBalls();
            }
        };

        EventHandler<ActionEvent> removeBlackBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeBlackBalls();
            }
        };

        EventHandler<ActionEvent> removeOrangeBallsHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                removeOrangeBalls();
            }
        };

        undoStateButton.setOnAction(undoStateHandler);
        loadNewNormalGameButton.setOnAction(loadNewNormalGameHandler);
        loadNewEasyGameButton.setOnAction(loadNewEasyGameHandler);
        loadNewHardGameButton.setOnAction(loadNewHardGameHandler);
        removeRedBallsButton.setOnAction(removeRedBallsHandler);
        removeYellowBallsButton.setOnAction(removeYellowBallsHandler);
        removeGreenBallsButton.setOnAction(removeGreenBallsHandler);
        removeBrownBallsButton.setOnAction(removeBrownBallsHandler);
        removeBlueBallsButton.setOnAction(removeBlueBallsHandler);
        removePurpleBallsButton.setOnAction(removePurpleBallsHandler);
        removeBlackBallsButton.setOnAction(removeBlackBallsHandler);
        removeOrangeBallsButton.setOnAction(removeOrangeBallsHandler);
    }

    /**
     * Get the current score of the game
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * Removes all the red balls from the pool table
     */
    private void removeRedBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.RED){
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
            }
        }
    }

    /**
     * Removes all the yellow balls from the pool table
     */
    private void removeYellowBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.YELLOW){
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
            }
        }
    }

    /**
     * Removes all the green balls from the pool table
     */
    private void removeGreenBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.GREEN){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Removes all the brown balls from the pool table
     */
    private void removeBrownBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.BROWN){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Removes all the blue balls from the pool table
     */
    private void removeBlueBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.BLUE){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Removes all the purple balls from the pool table
     */
    private void removePurpleBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.PURPLE){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Removes all the black balls from the pool table
     */
    private void removeBlackBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.BLACK){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Removes all the orange balls from the pool table
     */
    private void removeOrangeBalls(){
        stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
        this.stateKeep.setMemento(stateOriginator.saveState());
        for (Ball b : getPoolTable().getBalls()){
            if (b.getColour() == Color.ORANGE){
                BallPocketStrategy strategy = b.getPocketAction();
                b.setPocketAction(new PocketOnce());
                if (!b.isDisabled()) {
                    b.fallIntoPocket(this.returnGame());
                }
                b.setPocketAction(strategy);
            }
        }
    }

    /**
     * Get the configReader object containing setup info from the JSON file
     * @return The configReader object
     */
    public ConfigReader getConfigReader() {
        return configReader;
    }

    /**
     * Get the Game object
     * @return This current game object
     */
    public Game returnGame(){
        return this;
    }

    /**
     * Get the time elapsed in the game
     * @return The duration time
     */
    public double getDurationTime() {
        return durationTime;
    }

    /**
     * Set the score of the game with the supplied value
     * @param score The new score to be set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Increments the score of the game by the supplied value
     * @param i The value to increment by
     */
    public void incrementScore(int i){
        setScore(getScore()+i);
    }

    /**
     * Get the HashMap that stores the respective score for each colour of ball
     * @return The hashmap
     */
    public HashMap<Color, Integer> getColorToScore() {
        return colorToScore;
    }

    /**
     * Get the window dimension in the x-axis
     * @return The x-axis size of the window dimension
     */
    public double getWindowDimX() {
        return this.table.getDimX();
    }

    /**
     * Get the window dimension in the y-axis
     * @return The y-axis size of the window dimension
     */
    public double getWindowDimY() {
        return this.table.getDimY();
    }

    /**
     * Get the pool table associated with the game
     * @return The pool table instance of the game
     */
    public PoolTable getPoolTable() {
        return this.table;
    }

    /** Add all drawable object to the JavaFX group
     * @param root The JavaFX `Group` instance
    */
    public void addDrawables(Group root) {
        this.root = root;
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);
        groupChildren.add(this.winText);
        groupChildren.add(this.scoreText);
        groupChildren.add(this.timeText);
        groupChildren.add(this.undoStateButton);
        groupChildren.add(this.loadNewNormalGameButton);
        groupChildren.add(this.loadNewEasyGameButton);
        groupChildren.add(this.loadNewHardGameButton);
        groupChildren.add(this.levelMenuText);
        groupChildren.add(this.undoText);
        groupChildren.add(this.cheatText);
        groupChildren.add(this.removeRedBallsButton);
        groupChildren.add(this.removeYellowBallsButton);
        groupChildren.add(this.removeGreenBallsButton);
        groupChildren.add(this.removeBrownBallsButton);
        groupChildren.add(this.removeBlueBallsButton);
        groupChildren.add(this.removePurpleBallsButton);
        groupChildren.add(this.removeBlackBallsButton);
        groupChildren.add(this.removeOrangeBallsButton);
    }

    /** Reset the game */
    public void reset() {
        this.winText.setVisible(false);
        this.shownWonText = false;
        this.table.reset();
        this.score = 0;
        this.resetStartTime();
    }

    /** Code to execute every tick. */
    public void tick() {

        for (Button b : this.colorToButton.values()){
            b.setVisible(false);
        }
        if (table.hasWon() && !this.shownWonText) {
            System.out.println(this.winText.getText());
            this.winText.setVisible(true);
            this.shownWonText = true;
        }
        if (!table.hasWon() && this.shownWonText){
            this.winText.setVisible(false);
            this.shownWonText = false;
        }
        table.checkPocket(this);
        table.handleCollision();
        this.table.applyFrictionToBalls();

        for (Ball ball : this.table.getBalls()) {
            ball.move();
            if (ball.isCueBallShot()){
                stateOriginator.updateState(getScore(),getDurationTime(), configReader.getLevel());
                this.stateKeep.setMemento(stateOriginator.saveState());
                ball.setCueBallShot(false);
            }
            try{
                if (!ball.isDisabled() && ball.getBallType() == Ball.BallType.NORMALBALL) {
                    this.colorToButton.get(ball.getColourAsString().toLowerCase()).setVisible(true);
                }
            } catch (NullPointerException e){
                System.out.println(ball.getColourAsString().toLowerCase());
            }
        }

        this.scoreText.setText("Score : "+this.score);
        this.durationTime = calculateDurationTime();
        this.timeText.setText("Time Elapsed (s) : " + formattedTime(this.durationTime));
    }

    /**
     * Get the time elapsed depending on whether the game is still ongoing
     * If the game is still ongoing, returns the difference between current time and start time
     * If the game has finished, returns the time elapsed when the game was finished
     * @return The duration time
     */
    private double calculateDurationTime(){
        if (table.hasWon()){
            return this.getDurationTime();
        }
        else {
            return (System.currentTimeMillis() - this.startTime)/1000.0;
        }
    }

    /**
     * Loads a new game of normal difficulty level
     */
    public void loadNewNormalGame(){
        
        List<String> args = new ArrayList<>(){{
                add("src/main/resources//config_normal.json");
        }};

        ConfigReader config = App.loadConfig(args);
        this.root.getChildren().remove(1,this.root.getChildren().size());
        this.setup(config);
        addDrawables(this.root);
    }

    /**
     * Loads a new game of easy difficulty level
     */
    public void loadNewEasyGame(){

        List<String> args = new ArrayList<>(){{
            add("src/main/resources//config_easy.json");
        }};

        ConfigReader config = App.loadConfig(args);
        this.root.getChildren().remove(1,this.root.getChildren().size());
        this.setup(config);
        addDrawables(this.root);
    }

    /**
     * Loads a new game of hard difficulty level
     */
    public void loadNewHardGame(){

        List<String> args = new ArrayList<>(){{
            add("src/main/resources//config_hard.json");
        }};

        ConfigReader config = App.loadConfig(args);
        this.root.getChildren().remove(1,this.root.getChildren().size());
        this.setup(config);
        addDrawables(this.root);
    }

    /**
     * Returns the time elapsed in m:ss format
     * @param time
     * @return The formatted time
     */
    private String formattedTime(double time){
        int minutes = (int) Math.floor(time/60);
        int seconds = (int) Math.floor(time%60);
        return String.format("%01d", minutes)+":"+String.format("%02d", seconds);
    }

    /**
     * Resets the start time with the current time
     * That is, effectively restarting the stopwatch
     */
    public void resetStartTime(){
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Adjusts the starting time by adding the supplied number
     * @param l the number in seconds by which to adjust
     */
    public void adjustStartTimeBy(long l){
        this.startTime+=l*1000.0;
    }
}
