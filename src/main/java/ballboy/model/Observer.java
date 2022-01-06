package ballboy.model;

import ballboy.model.entities.DynamicEntityImpl;

import java.util.ArrayList;

public class Observer {

    private final ArrayList<Entity> observered = new ArrayList<>();
    public void addObserver(Entity entity){
        if(!(entity instanceof DynamicEntityImpl)){
            return;
        }
        observered.add(entity);
    }
    public int Notify(String levelEnemyColor){
        int score = 0;
        for(Entity entity: observered){
            if(entity instanceof DynamicEntityImpl){
                if(((DynamicEntityImpl) entity).checkDead()){
                    if(((DynamicEntityImpl) entity).getColor().equals(levelEnemyColor) || levelEnemyColor == null){
                        score++;
                    }
                }
            }
        }
        return (score);
    }
}
