package ballboy.model.levels;

import ballboy.ConfigurationParseException;
import ballboy.model.*;
import ballboy.model.Observer;
import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.StaticEntity;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.model.factories.EntityFactory;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Level logic, with abstract factor methods.
 */
public class LevelImpl implements Level {

    private List<Entity> entities = new ArrayList<>();
    private final PhysicsEngine engine;
    private final EntityFactory entityFactory;
    private ControllableDynamicEntity<DynamicEntity> hero;
    private Entity squareCat;
    private Entity finish;
    private double levelHeight;
    private double levelWidth;
    private double levelGravity;
    private double floorHeight;
    private Color floorColor;
    public boolean finished;
    private final Observer observer = new Observer();
    private int levelScore;
    private String levelEnemeyColor;
    private JSONObject levelConfiguration;

    private final double frameDurationMilli;

    /**
     * A callback queue for post-update jobs. This is specifically useful for scheduling jobs mid-update
     * that require the level to be in a valid state.
     */
    private final Queue<Runnable> afterUpdateJobQueue = new ArrayDeque<>();

    public LevelImpl(
            JSONObject levelConfiguration,
            PhysicsEngine engine,
            EntityFactory entityFactory,
            double frameDurationMilli) {
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.frameDurationMilli = frameDurationMilli;
        initLevel(levelConfiguration);
        this.levelConfiguration = levelConfiguration;
        this.finished =false;
    }

    /**
     * Instantiates a level from the level configuration.
     *
     * @param levelConfiguration The configuration for the level.
     */
    private void initLevel(JSONObject levelConfiguration) {
        this.levelWidth = ((Number) levelConfiguration.get("levelWidth")).doubleValue();
        this.levelHeight = ((Number) levelConfiguration.get("levelHeight")).doubleValue();
        this.levelGravity = ((Number) levelConfiguration.get("levelGravity")).doubleValue();
        this.levelEnemeyColor = (String) levelConfiguration.get("levelEnemyColor");
        JSONObject floorJson = (JSONObject) levelConfiguration.get("floor");
        this.floorHeight = ((Number) floorJson.get("height")).doubleValue();
        String floorColorWeb = (String) floorJson.get("color");
        this.floorColor = Color.web(floorColorWeb);


        JSONArray generalEntities = (JSONArray) levelConfiguration.get("genericEntities");
            for (Object o : generalEntities) {
                Entity entity = entityFactory.createEntity(this, (JSONObject) o);
                this.entities.add(entity);
                this.observer.addObserver(entity);
            }

            //hero stuff
            JSONObject heroConfig = (JSONObject) levelConfiguration.get("hero");
            double maxVelX = ((Number) levelConfiguration.get("maxHeroVelocityX")).doubleValue();
            if (maxVelX == 0) {
                maxVelX = 50;
            }

            Object hero = entityFactory.createEntity(this, heroConfig);
            if (!(hero instanceof DynamicEntity)) {
                throw new ConfigurationParseException("hero must be a dynamic entity");
            }

            DynamicEntity dynamicHero = (DynamicEntity) hero;
            Vector2D heroStartingPosition = dynamicHero.getPosition();
            this.hero = new ControllableDynamicEntity<>(dynamicHero, heroStartingPosition, maxVelX, floorHeight,
                    levelGravity);
            this.entities.add(this.hero);

            //squareCatConfigStuff
            JSONObject squareCatConfig = (JSONObject) levelConfiguration.get("squarecat");
            this.squareCat = entityFactory.createEntity(this, squareCatConfig);
            entities.add(squareCat);


            JSONObject finishConfig = (JSONObject) levelConfiguration.get("finish");
            this.finish = entityFactory.createEntity(this, finishConfig);
            this.entities.add(finish);
    }

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    private List<DynamicEntity> getDynamicEntities() {
        return entities.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return entities.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public double getLevelHeight() {
        return this.levelHeight;
    }

    @Override
    public double getLevelWidth() {
        return this.levelWidth;
    }

    @Override
    public double getHeroHeight() {
        return hero.getHeight();
    }

    @Override
    public double getHeroWidth() {
        return hero.getWidth();
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public Color getFloorColor() {
        return floorColor;
    }

    @Override
    public double getGravity() {
        return levelGravity;
    }

    @Override
    public void update() {
        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        dynamicEntities.stream().forEach(e -> {
            e.update(frameDurationMilli, levelGravity);
        });

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB)) {
                    dynamicEntityA.collideWith(dynamicEntityB);
                    dynamicEntityB.collideWith(dynamicEntityA);
                    if (!isHero(dynamicEntityA) && !isHero(dynamicEntityB)) {
                        engine.resolveCollision(dynamicEntityA, dynamicEntityB);
                    }
                }
            }

            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(staticEntity);
                    engine.resolveCollision(dynamicEntityA, staticEntity, this);
                }
            }
        }

        dynamicEntities.stream().forEach(e -> engine.enforceWorldLimits(e, this));

        afterUpdateJobQueue.forEach(j -> j.run());
        afterUpdateJobQueue.clear();
        levelScore = observer.Notify(levelEnemeyColor);

//        int i = 0;
//        for(Entity entity: entities){
//            if(entity instanceof ControllableDynamicEntity){
//                i++;
//                System.out.println(i);
//            }
//       }
//        System.out.println("Level hero position");
//        System.out.println(hero.getPosition().getX());
//        System.out.println(hero.getPosition().getY());
//        System.out.println("");

    }

    @Override
    public double getHeroX() {
        return hero.getPosition().getX();
    }

    @Override
    public double getHeroY() {
        return hero.getPosition().getY();
    }

    @Override
    public boolean boostHeight() {
        return hero.boostHeight();
    }

    @Override
    public boolean dropHeight() {
        return hero.dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return hero.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return hero.moveRight();
    }

    @Override
    public boolean isHero(Entity entity) {
        return entity == hero;
    }

    @Override
    public boolean isFinish(Entity entity) {
        return this.finish == entity;
    }

    @Override
    public boolean isSquareCat(Entity entity){return this.squareCat == entity;}

    @Override
    public void resetHero() {
        afterUpdateJobQueue.add(() -> this.hero.reset());
    }

    public int getLevelScore(){
        return observer.Notify(levelEnemeyColor);
    }
    public String getLevelEnemeyColor(){
        return levelEnemeyColor;
    }
//    public void deleteAllEntities(){
//        for(int i = 0; i < entities.size(); i++){
//            if(entities.get(i) instanceof ControllableDynamicEntity){
//                System.out.println("YES");
//            }
//            entities.remove(i);
//        }
//    }
//    public void addNewEntities(ArrayList<Entity> newEntities){
//        for(int i = 0; i < newEntities.size(); i++){
//            entities.add(i, newEntities.get(i));
//        }
//    }

    public Level copy(){
        LevelImpl newLevel = new LevelImpl(levelConfiguration, engine, entityFactory,frameDurationMilli);
        ArrayList<Vector2D> currentPositions = this.getPositions();
        newLevel.setPosition(currentPositions);
        return newLevel;

    }

    public void setPosition(ArrayList<Vector2D> position){
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i) instanceof DynamicEntity){
                ((DynamicEntity) entities.get(i)).setPosition(position.get(i));
            }
        }
    }

    public ArrayList<Vector2D> getPositions(){
        ArrayList<Vector2D> positions = new ArrayList<>();
        for(Entity entitiy: entities){
            positions.add(entitiy.getPosition());
        }
        return positions;
    }

    public ControllableDynamicEntity getHero(){
        return hero;
    }

    public void setHero(ControllableDynamicEntity newHero){
        hero = newHero;
    }

    @Override
    public void finish() {
        finished = true;
    }
    public boolean isFinished(){
        return finished;
    }

}
