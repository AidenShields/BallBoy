STUDENT README SECTION


- HOW TO RUN CODE
  - Type Gradle Run in

- Feature List
  - Multiple levels, next level loads after you reach the tree
  
  - Square Cat that kills enimies, picked random png on my pc for the image

  - Scoring System that is color coded (uses Observer)
    - Specs asked for just rgb, I assumed that meant you can specify which color enimies grant points, as in red blue or green.
      I added purple and yellow aswell because the resources folder has options for them. The colour of the text indicates the
      levels colour. Black means all entities give points. Not sure if thats how you guys wanted it but I didnt really understand the spec.
      Displays the total score at the end of the game in the top lefthad corner
  
  - Save and Load (uses prototype and memento)
    - Worked with s to save and q to load, resets all entites to not allow explotation of save and load. Can be loaded multiple times. Was really easy aside from figuring out
      how to handle cloning the levels, which took forever.
    



- Design Patterns used
  - Factory Pattern
    - Used in SquareCatFactoryPattern.java
  - Stratergy Pattern
    - Used in SquareCatBehaviourStartergy.java
- Observer Pattern
  - Used in Observer.java
  - Memento Pattern
    - Used in Memento.java 
  - Prototype Pattern
    - Used in GameEngineImpl, GameWindow, LevelImpl, DynamicEntityImpl, ControllableDynamicEntity, StaticEntityImpl
    
    Enjoyed writing this assingment :)
    Didnt do the test cases, ran out of time and theyre only worth 5% and 75% coverage is marked the same as 0% 
  so if I get up to 75% before the due date it was basically worthless in the eyes of the marker




























# Overview

A basic ballboy game written in JavaFX. Try to reach the finish without colliding with the enemies!

# Getting Started 

#### Running 

`gradle run`

#### Building 

`gradle build`

#### Testing 

`gradle test`

# Game Controls

The ballboy is controlled through the left, right and up arrow keys.

Pressing up will boost the bounce height, with a maximum bounce height being the level height.

Pressing left or right will move the entity in the given direction.

Lifting the left or right key after pressing it, without having at least one of the two keys down, will result
in the bounce height being reduced.

# Configuration

The level configuration is to be located in the resources directory with the name `config.json`.

The root level configuration consists of `currentLevelIndex', specifying which level to load from the `levels` array. 

The following fields are necessary for the level configuration: `levelWidth`, `levelHeight`, `floor`, `levelGravity` and `maxHeroVelocityX`. An example is shown below:

``` json
  "_levelWidthComment": "The actual width of the level. This will be used to enforce game boundaries",
  "levelWidth": 2000.0,
  "_levelHeightComment": "The actual height of the level. This will also be used to enforce game boundaries",
  "levelHeight": 620.0,
  "_floorComment": "The configuration of the levels floor",
  "floor": {
    "_heightComment": "The floor height. Dynamic entities will not be allowed below this",
    "height": 600.0,
    "_colorComment": "Color must be a web color, e.g. #0033cc",
    "color": "#001100"
  },
  "_levelGravityComment": "The gravity applied to bouncing entities. This is in pixels per millisecond",
  "levelGravity": 700.0,
  "_maxHeroVelocityX": "The maximum horizontal velocity of the hero. This is used to provide usable game controls",
  "maxHeroVelocityX": 50,
```

The generic entities for the level are configured as shown below:
``` json
   "_genericEntitiesComment":"A list of all generic entities. They are generic in the sense that the level treats them as a batch, with no specific understanding or logic for each",
   "genericEntities":[
   {
      "_typeComment":"The key used to distinguish which concrete entity type is to be instantiated inside the factory method",
      "type":"cloud",
      "startX":234.0,
      "startY":0.1,
      "_horizontalVelocityComment":"This is the horizontal velocity of the cloud",
      "horizontalVelocity":-20.2,
      "_imageComment":"The image to be used for the entity view. The height and width are derived from this.",
      "image":"cloud_1.png"
   },
   {
      "type":"static",
      "posX":400,
      "posY":565,
      "_heightComment":"The width will be derived from the height and image dimensions, with the ratio being preserved.",
      "height":40,
      "image":"bolder.png"
   },
   {
      "type": "enemy",
      "startX": 10.0,
      "startY": 550.1,
      "startVelocityX": -10.0,
      "height": 20.0,
      "_behaviourComment": "The possible values are aggressive, passive and scared",
      "behaviour": "aggressive"
    },
   {
      "type":"background",
      "posX":0.0,
      "posY":400.0,
      "height":200,
      "image":"landscape_0000_1_trees.png"
   }
]
```

The hero is configured as shown below:
```json
  "hero": {
    "type": "hero",
    "startX": 150.0,
    "startY": 300.0,
    "_sizeComment": "options include small, medium, large. If not specified this field will default to normal",
    "size": "large",
    "image": "ch_stand2.png"
  }
```

The finish is configured as shown below:

```json
  "finish": {
    "type": "finish",
    "posX": 1200.0,
    "posY": 520.0,
    "height": 80.0,
    "width": 40
  }
```

The complete configuration shown above is sourced from `config.json` in the resources directory.

#### Example Configuration

The current levels have slimes as the enemies and a tree as the finish.

- The current default level is index 0, and is a demonstration of all the required features (including but not limited to those listed below):
  - This may take a few tries to finish, especially if youre not familiar with the inputs.
  - The three enemies have different behaviours
  - Colliding with an enemy will reset the hero to the starting position
  - Colliding with the finish (the tree) will bring end the game.
  - The camera will follow the hero both vertically and horizontally.
  - The floor is configurable in color, as outlined above.
  - Clouds move at a constant speed, and do not collide with foreground objects.
  - Static immobile objects are included in the form of rocks and two stone walls.
  - The ballboy can be loaded in three sizes (small medium and large)
- It is easy to reach the finish with the second configuration, which can be loaded by changing `  "currentLevelIndex": 0` to `  "currentLevelIndex": 1`. You can find the configuration file in the resources directory.

# Design Patterns 

### Factory Method 

###### Product

See Entity in Entity.java

###### Concrete Products

See StaticEntityImpl in StaticEntityImpl.java, and DynamicEntityImpl in DynamicEntityImpl.java.

###### Creator

See EntityFactory in EntityFactory.java

###### Concrete Creator

- StaticEntityFactory in StaticEntityFactory.java
- FinishFactory in FinishFactory.java
- CloudFactory in CloudFactory.java
- EnemyFactory in EnemyFactory.java
- BallboyFactory in BallboyFactory.java

These are registered in EntityFactoryRegistry in EntityFactoryRegistry.java, and injected in the root level App class in App.java


### Strategy Pattern

#### 1. Entity Behaviour 

###### Strategy

See BehaviourStrategy in BehaviourStrategy.java

###### ConcreteStrategy

- AggressiveEnemyBehaviourStrategy in AggressiveEnemyBehaviourStrategy.java
- PassiveBehaviourStrategy in PassiveBehaviourStrategy.java
- ScaredEnemyBehaviourStrategy in ScaredEnemeyBehaviourStrategy.java
- FloatingCloudBehaviourStrategy in FloatingCloudBehaviourStrategy.java

###### Context

See DynamicEntityImpl in DynamicEntityImpl.java

#### 2. CollisionResolution

###### Strategy

See CollisionStrategy in CollisionStrategy.java

###### ConcreteStrategy

- BallboyCollisionStrategy in BallboyCollisionStrategy.java
- EnemyCollisionStrategy in EnemyCollisionStrategy.java
- PassiveCollisionStrategy in PassiveCollisionStrategy.java

###### Context

See DynamicEntityImpl in DynamicEntityImpl.java

# Image Sources

- [Boulder](https://www.pngwing.com/en/free-png-zrzud/download)
- [Stone Block](https://www.deviantart.com/sarahstudiosart/art/Stone-Wall-1-458649249)
