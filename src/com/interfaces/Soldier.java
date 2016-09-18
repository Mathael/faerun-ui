package com.interfaces;

import com.actors.Character;
import com.exceptions.CriticalHitException;

/**
 * @author Leboc Philippe
 */
public interface Soldier {

    /**
     * Gestion de l'action: Attaquer
     * @param character la cible
     */
    void handleAttack(Character character) throws CriticalHitException;

    /**
     * Gestion de la réception de dégats sur une créature
     * @param damage le nombre de points de dégats reçu
     */
    void handleReceiveDamage(int damage);
}
