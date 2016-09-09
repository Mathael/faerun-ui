package com.model;

import com.actors.Character;
import com.enums.TeamColor;

import java.util.ArrayList;

/**
 * @author Leboc Philippe
 */
public final class Castle {

    private int resources;
    private TeamColor color;
    private ArrayList<Character> wasBought;

    public Castle(TeamColor color) {
        setResources(2);
        setColor(color);
        setWasBought(new ArrayList<>());
    }

    public void addResources(int amount) {
        setResources(getResources() + amount);
    }

    public int getResources() {
        return resources;
    }

    private void setResources(int resources) {
        this.resources = resources;
    }

    public TeamColor getColor() {
        return color;
    }

    private void setColor(TeamColor color) {
        this.color = color;
    }

    /**
     * Ajoute une nouvelle unité dans la liste de construction et déduit le coût en points de ressources
     * @param character l'unité à créer
     */
    public void buildUnit(Character character) {
        addResources(-character.getCost());
        wasBought.add(character);
    }

    public ArrayList<Character> getWasBought() {
        return wasBought;
    }

    private void setWasBought(ArrayList<Character> wasBought) {
        this.wasBought = wasBought;
    }
}
