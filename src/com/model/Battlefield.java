package com.model;

import com.GameUI;
import com.actors.*;
import com.actors.Character;
import com.enums.TeamColor;

import java.util.ArrayList;

import static com.enums.TeamColor.BLUE;
import static com.enums.TeamColor.RED;

/**
 * @author Leboc Philippe
 * Il s'agit du plateau du jeu.
 * Cette classe sert spécifiquement à faire en sorte que le jeu se déroule pas à pas en suivant les différentes phases.
 * Elle contient toutes les cases du plateau ainsi que les deux chateaux. Elle à donc accès à l'intégralité du jeu.
 */
public final class Battlefield {

    private Case[] cases;
    private Castle blue;
    private Castle red;

    public Battlefield(int numberOfCases) {
        cases = new Case[numberOfCases];
        setBlue(new Castle(BLUE));
        setRed(new Castle(RED));
        init(numberOfCases);
    }

    private void init(int numberOfCases){
        for (int i = 0; i < numberOfCases; i++) {
            cases[i] = new Case(i+1);
        }
    }

    public Castle getCastle(TeamColor color) {
        if(color == BLUE) return getBlue();
        return getRed();
    }

    public Case getCase(int number) {
        return cases[number-1];
    }

    public Case[] getCases() {
        return cases;
    }

    public Castle getBlue() {
        return blue;
    }

    private void setBlue(Castle blue) {
        this.blue = blue;
    }

    public Castle getRed() {
        return red;
    }

    private void setRed(Castle red) {
        this.red = red;
    }

    //////////////////////////////////   ENGINE   /////////////////////////////////

    /**
     * Les bleu commencent toujours leur mouvement (ce qui déséquilibre le jeu)
     * Les bleus étant à gauche j'éffectue un décalage du curseur de la droite vers la gauche
     * Chaque créature de l'équipe est donc déplacée une unique fois vers la droite.
     * Le même schema est reproduit en sens inverse pour l'équipe rouge.
     */
    public void movementPhase() {
        // Blue Team
        for (int i = this.getCases().length-1; i > 0; i--)
        {
            final Case currentCase = this.getCase(i);
            final Case nextCase = this.getCase(i+1); // suivante à gauche

            // Mouvement seulement si pas les deux couleurs sur la même case
            if (!currentCase.getBlueCharacters().isEmpty() && currentCase.getRedCharacters().isEmpty()) {
                doMoveFromCase(currentCase, nextCase, BLUE);
            }
        }

        // Red Team
        for(int i = 2; i <= this.getCases().length; i++)
        {
            final Case currentCase = this.getCase(i);
            final Case nextCase = this.getCase(i-1);
            if(currentCase.getBlueCharacters().isEmpty() && !currentCase.getRedCharacters().isEmpty()) {
                doMoveFromCase(currentCase, nextCase, RED);
            }
        }
    }

    /**
     * Déplace toutes les unité d'une couleur se trouvant sur la case courante vers la case suivante.
     * @param currentCase la case où se trouve le curseur
     * @param nextCase la case sur laquelle seront déplacé les éventuels créatures
     * @param color la couleur de l'équipe
     */
    private void doMoveFromCase(Case currentCase, Case nextCase, TeamColor color) {
        final ArrayList<Character> chars = color == BLUE ? currentCase.getBlueCharacters() : currentCase.getRedCharacters();
        final ArrayList<Character> nextCaseChars = color == BLUE ? nextCase.getBlueCharacters() : nextCase.getRedCharacters();
        nextCaseChars.addAll(chars);
        chars.clear();
    }

    /**
     * Phase d'achat des unités.
     * Le joueur peut acheter autant d'unités qu'il le souhaite jusqu'à ne plus avoir de ressources.
     * Le joueur peut décider de ne pas acheter d'unités.
     */
    public void buyPhase()
    {
        for (TeamColor color : TeamColor.values())
        {
            boolean exit = false;
            Character newCharCreation = null;

            while(!exit)
            {
                final Castle currentCastle = getCastle(color);

                if(currentCastle.getResources() >= 1)
                {
                    final int choice = GameUI.getInstance().chooseBuyOptionsDialog(color, currentCastle.getResources());
                    switch (choice) {
                        case 0:
                            exit = true;
                            break;
                        case 1:
                            newCharCreation = new GuerrierNain();
                            break;
                        case 2:
                            newCharCreation = new GuerrierElfe();
                            break;
                        case 3:
                            newCharCreation = new ChefNain();
                            break;
                        case 4:
                            newCharCreation = new ChefElfe();
                            break;
                    }

                    if (newCharCreation != null) {
                        final int cost = newCharCreation.getCost();
                        if (currentCastle.getResources() >= cost) {
                            currentCastle.buildUnit(newCharCreation);
                            newCharCreation = null;
                        } else {
                            // TODO: signaler qu'il n'a pas les ressources nécessaires pour acheter
                            GameUI.getInstance().sendMessage("Vous n'avez pas les ressources nécessaires !");
                        }
                    }
                }
                else
                {
                    exit = true;
                }
            }
        }
    }

    public void attackPhase() {
        for (Case current : getCases())
        {
            while(current.canLaunchBattle())
            {
                doAttack(current.getBlueCharacters(), current.getRedCharacters());
                doAttack(current.getRedCharacters(), current.getBlueCharacters());
            }
        }
    }

    private void doAttack(ArrayList<Character> attackers, ArrayList<Character> targets) {
        attackers.forEach(k -> {
            for (int i = 0; i < targets.size(); i++) {
                final Character currentTarget = targets.get(i);
                k.handleAttack(currentTarget);
                if(currentTarget.isDead()) targets.remove(currentTarget);
            }
        });
    }

    /**
     * Fait apparaitre les unités ayant été construites le tour précédant.
     * Chacune apparait sur la case la plus proche de son chateau.
     */
    public void spawnCharacters() {
        getBlue().getWasBought().forEach(entry -> getCase(1).getBlueCharacters().add(entry));
        getRed().getWasBought().forEach(entry -> getCase(getCases().length).getRedCharacters().add(entry));

        getBlue().getWasBought().clear();
        getRed().getWasBought().clear();
    }

    public void giveTurnRewards() {
        getBlue().addResources(1);
        getRed().addResources(1);
    }

    @Override
    public String toString() {
        String result = "========== Plateau ==========\r\n";
        for (Case aCase : cases) {
            result += aCase.toString();
        }
        return result;
    }
}
