package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.utilities.Vector2D;

public class SquareCatBehaviourStratergy implements BehaviourStrategy{
    private Level level;
    private double XOFFSET;
    private double YOFFSET;
    private static final double HorizontalAcceleration = 100;
    private static final double VerticalAcceleration = 100;

    public SquareCatBehaviourStratergy (Level level) {
        this.level = level;
        this.XOFFSET = 0;
        this.YOFFSET = 0;
    }

    public void behave(DynamicEntity entity, double frameDurationMilli) {
//        if(entity.getPosition().getX() > level.getHeroX() - 30){
//            entity.getPosition().setX(level.getHeroX() - 30);
//        }
        entity.setPosition(new Vector2D(level.getHeroX() - level.getHeroWidth() + XOFFSET, level.getHeroY() - level.getHeroHeight() + YOFFSET));
        if(entity.getPosition().getX() >= level.getHeroX() - level.getHeroWidth() - 1 && entity.getPosition().getX() < level.getHeroX() + level.getHeroWidth() && entity.getPosition().getY() <= level.getHeroY() - level.getHeroHeight()){
            XOFFSET+= 1;
        }
        if(entity.getPosition().getX() > level.getHeroX() + level.getHeroWidth() && entity.getPosition().getY() < level.getHeroY() + level.getHeroHeight()){
            YOFFSET += 1;
        }
        if(entity.getPosition().getX() + level.getHeroWidth() >= level.getHeroX() && level.getHeroY() + level.getHeroHeight() <= entity.getPosition().getY()){
            XOFFSET -= 1;
        }
        if(entity.getPosition().getX() + level.getHeroWidth() <= level.getHeroX() && level.getHeroY() <= entity.getPosition().getY() + level.getHeroHeight()){
            YOFFSET -= 1;
        }
    }
}
