package com.actors;

import com.enums.MovementSpeed;

/**
 * @author Leboc Philippe
 */
public class ChefNain extends GuerrierNain {

    public ChefNain() {
        super();
        setName("Chef Nain");
        setCost(4);
        setNumberOfDice(10);
        setSpeed(MovementSpeed.SLOW);
    }

    @Override
    public void handleReceiveDamage(int damage) {
        super.handleReceiveDamage(damage);
        setCurrentHealth(getCurrentHealth() - (damage / 2));
    }
}
