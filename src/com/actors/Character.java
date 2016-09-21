package com.actors;

import com.enums.MovementSpeed;
import com.exceptions.CriticalHitException;
import com.interfaces.Soldier;
import com.utils.Util;

/**
 * @author Leboc Philippe
 * Cette classe représente une creature du jeu (n'importe laquelle)
 */
public abstract class Character implements Soldier {

    private String name;
    private int currentHealth;
    private int numberOfDice;
    private int cost;
    private boolean canMove;
    private boolean isChief;
    private MovementSpeed speed;

    public Character() {
        setCurrentHealth(100);
        setCanMove(true);
        setChief(false);
        setSpeed(MovementSpeed.NORMAL);
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    protected void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    protected void setNumberOfDice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
    }

    public int getCost() {
        return cost;
    }

    protected void setCost(int cost) {
        this.cost = cost;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isChief() {
        return isChief;
    }

    public void setChief(boolean chief) {
        isChief = chief;
    }

    public MovementSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(MovementSpeed speed) {
        this.speed = speed;
    }

    /**
     * @return true si l'entité est considérée comme morte, false sinon
     */
    public boolean isDead() {
        return getCurrentHealth() <= 0;
    }

    public void doDie() {
        setCurrentHealth(0);
    }

    @Override
    public void handleAttack(Character character) throws CriticalHitException {
        int dmg = Util.roll(3, getNumberOfDice());
        if(!(character instanceof Gobelin) && dmg >= (getNumberOfDice() * 3) - 2) {
            throw new CriticalHitException("Critical hit !");
        }
        character.handleReceiveDamage(dmg);
    }

    @Override
    public void handleReceiveDamage(int damage) {
        setCurrentHealth(getCurrentHealth() - damage);
    }

    @Override
    public String toString() {
        return getName()+" ["+getCurrentHealth()+"]";
    }
}
