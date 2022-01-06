package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntityImpl;

/**
 * Collision logic for enemies.
 */
public class EnemyCollisionStrategy implements CollisionStrategy {
    private final Level level;

    public EnemyCollisionStrategy(Level level) {
        this.level = level;
    }

    @Override
    public void collideWith(
            Entity enemy,
            Entity hitEntity) {
        DynamicEntityImpl enemyD = (DynamicEntityImpl) enemy;
        if (level.isHero(hitEntity)) {
            if(enemyD.checkDead()){
                return;
            }
            level.resetHero();
        }
        if(level.isSquareCat(hitEntity)) {
//            enemyD.Notify();
            enemyD.isDead(true);
        }
    }
}
