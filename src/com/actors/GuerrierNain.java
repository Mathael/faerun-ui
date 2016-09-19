package com.actors;

/**
 * @author Leboc Philippe
 */
public class GuerrierNain extends Character {

    public GuerrierNain() {
        super();
        setName("Guerrier Nain");
        setCost(2);
        setNumberOfDice(10);
    }

    @Override
    public void handleReceiveDamage(int damage) {
        setCurrentHealth(getCurrentHealth() - (damage/2));
    }
}
