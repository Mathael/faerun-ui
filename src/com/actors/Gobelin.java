package com.actors;

import com.enums.MovementSpeed;

/**
 * @author Leboc Philippe.
 */
public class Gobelin extends Character {
    public Gobelin() {
        super();
        setName("Gobelin");
        setCost(1);
        setNumberOfDice(5);
        setSpeed(MovementSpeed.SPEED);
    }
}
