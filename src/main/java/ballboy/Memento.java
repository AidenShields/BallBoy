package ballboy;

import ballboy.model.GameEngine;
import ballboy.view.GameWindow;

public class Memento {
    public GameEngine state;

    public GameEngine getState(){
        return state;
    }
    public void setState(GameEngine memento){
        state = memento;
    }
}
