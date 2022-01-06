package ballboy.model;

import ballboy.model.factories.EntityFactory;
import javafx.application.Platform;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Implementation of the GameEngine interface.
 * This provides a common interface for the entire game.
 */
public class GameEngineImpl implements GameEngine {
    private Level level;
    private ArrayList<Level> levels;
    private int currentLevelIndex;
    private boolean gameFinished;
    public ArrayList<Integer> levelScores = new ArrayList<>();

    public GameEngineImpl(ArrayList<Level> levels) {
        this.levels = levels;
        this.currentLevelIndex = 0;
        this.level = levels.get(currentLevelIndex);
        this.gameFinished = false;
    }

    public Level getCurrentLevel() {
        return level;
    }


    public boolean isGameFinished(){
        return gameFinished;
    }

    @Override
    public GameEngine copy() {
        ArrayList<Level> newLevels = new ArrayList<>();
        for(Level level: levels){
            newLevels.add(level.copy());
        }
        return new GameEngineImpl(newLevels);
    }

    public void startLevel() {
        if(currentLevelIndex == levels.size()){
            gameFinished = true;
            return;
        }
        else{
            currentLevelIndex+=1;
            levelScores.add(level.getLevelScore());
            level = levels.get(currentLevelIndex);
        }
        return;
    }

    public boolean boostHeight() {
        return level.boostHeight();
    }

    public boolean dropHeight() {
        return level.dropHeight();
    }

    public boolean moveLeft() {
        return level.moveLeft();
    }

    public boolean moveRight() {
        return level.moveRight();
    }

    public ArrayList<Integer> getLevelScores(){
        return levelScores;
    }
    public int getCurrentLevelScore() {
        return level.getLevelScore();
    }

    @Override
    public String getLevelEnemeyColor() {
        return level.getLevelEnemeyColor();
    }

    public void tick() {

        level.update();
        if(level.isFinished()){
            startLevel();
        }
    }
}