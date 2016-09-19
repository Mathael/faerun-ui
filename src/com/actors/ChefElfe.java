package com.actors;

import com.enums.MovementSpeed;

/**
 * @author Leboc Philippe
 */
public class ChefElfe extends GuerrierElfe {

    public ChefElfe() {
        super();
        setName("Chef Elfe");
        setNumberOfDice(40);
        setCost(5);
        setSpeed(MovementSpeed.SLOW);
    }
}
