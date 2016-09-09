package com.interfaces;

import com.actors.Character;

/**
 * @author Leboc Philippe
 */
public interface Soldier {
    void handleAttack(Character character);
    void handleReceiveDamage(int damage);
}
