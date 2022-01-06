package ballboy.model.factories;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.behaviour.PassiveEntityBehaviourStrategy;
import ballboy.model.entities.behaviour.SquareCatBehaviourStratergy;
import ballboy.model.entities.collision.BallboyCollisionStrategy;
import ballboy.model.entities.collision.PassiveCollisionStrategy;
import ballboy.model.entities.utilities.*;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

public class SquareCatFactory implements EntityFactory{
    public Entity createEntity(Level level, JSONObject config) {
        try{
            double startX = ((Number) config.get("startX")).doubleValue();
            double startY = ((Number) config.get("startX")).doubleValue();
            String imageName = (String) config.getOrDefault(("image"), "zucc.png");
            String size = (String) config.get("size");

            double height;
            if (size.equals("small")) {
                height = 25.0;
            } else if (size.equals("medium")) {
                height = 35;
            } else if (size.equals("large")) {
                height = 45;
            } else {
                throw new ConfigurationParseException(String.format("Invalid SquareCat size %s", size));
            }

            Image image = new Image(imageName);
            // preserve image ratio
            double width = height * image.getWidth() / image.getHeight();

            Vector2D startingPosition = new Vector2D(startX, startY);

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .build();

            AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                    startingPosition,
                    height,
                    width
            );

            return new DynamicEntityImpl(
                    kinematicState,
                    volume,
                    Entity.Layer.FOREGROUND,
                    new Image(imageName),
                    new String(),
                    new PassiveCollisionStrategy(),
                    new SquareCatBehaviourStratergy(level)
            );
        }catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid squareCat entity configuration | %s | %s", config, e));
        }
    }
}
