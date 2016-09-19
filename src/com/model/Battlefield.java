package com.model;

import com.GameUI;
import com.actors.*;
import com.actors.Character;
import com.enums.MovementSpeed;
import com.enums.TeamColor;
import com.exceptions.CriticalHitException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.enums.TeamColor.BLUE;
import static com.enums.TeamColor.RED;

/**
 * @author Leboc Philippe
 * Il s'agit du plateau du jeu.
 * Cette classe permet d'executer les différentes actions du jeu.
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
     *
     * Ce code doit être factorisé, mais j'ai la flemme...
     */
    public void movementPhase()
    {
        // Blue Team
        for (int i = getCases().length-1; i > 0; i--)
        {
            final Case currentCase = this.getCase(i); // La case la plus à droite
            final Case nextCase = getCase(i + 1); // La case suivante
            final Case next2ndCase = i + 2 > getCases().length ? getCase(i + 1) : getCase(i + 2); // Celle d'après encore...
            final List<Character> currentCaseCharacters = currentCase.getBlueCharacters();

            // Récupère les créatures en fonction de leur vitesse de mouvement
            final List<Character> normalSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.NORMAL).collect(Collectors.toList());
            final List<Character> highSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.SPEED).collect(Collectors.toList());
            final List<Character> slowSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.SLOW && k.canMove()).collect(Collectors.toList());

            // Vérifie que les unitées de la case en cours puissent bouger sur la case suivante
            if(!currentCase.hasRedCharacters())
            {
                // Les créature faisant un simple mouvement bouge sur la suivante
                if(!normalSpeedCharacters.isEmpty()) {
                    nextCase.getBlueCharacters().addAll(normalSpeedCharacters);
                    currentCaseCharacters.removeAll(normalSpeedCharacters);
                }

                // Les créatures faisant un double mouvement vérifie que la case suivante soit aussi libre pour éviter de sauter au dessus des unités ennemies
                if(!highSpeedCharacters.isEmpty()) {
                    if(!nextCase.hasRedCharacters()) {
                        next2ndCase.getBlueCharacters().addAll(highSpeedCharacters);
                    } else nextCase.getBlueCharacters().addAll(highSpeedCharacters);
                    currentCase.getBlueCharacters().removeAll(highSpeedCharacters);
                }

                // Les créatures aux mouvements réduits bougent aussi
                if(!slowSpeedCharacters.isEmpty()) {
                    nextCase.getBlueCharacters().addAll(slowSpeedCharacters);
                    currentCase.getBlueCharacters().removeAll(slowSpeedCharacters);
                    slowSpeedCharacters.forEach(k -> k.setCanMove(false));
                }

                currentCase.getBlueCharacters().stream().filter(k-> !k.canMove()).forEach(c -> c.setCanMove(true));
            }
        }

        // Red Team
        for (int i = 2; i <= getCases().length; i++)
        {
            final Case currentCase = this.getCase(i); // La case la plus à droite
            final Case nextCase = getCase(i - 1); // La case suivante
            final Case next2ndCase = i-2 <= 0 ? getCase(i - 1) : getCase(i - 2); // Celle d'après encore...
            final List<Character> currentCaseCharacters = currentCase.getRedCharacters();

            // Récupère les créatures en fonction de leur vitesse de mouvement
            final List<Character> normalSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.NORMAL).collect(Collectors.toList());
            final List<Character> highSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.SPEED).collect(Collectors.toList());
            final List<Character> slowSpeedCharacters = currentCaseCharacters.stream().filter(k -> k.getSpeed() == MovementSpeed.SLOW && k.canMove()).collect(Collectors.toList());

            // Vérifie que les unitées de la case en cours puissent bouger sur la case suivante
            if(!currentCase.hasBlueCharacters())
            {
                // Les créature faisant un simple mouvement bouge sur la suivante
                if(!normalSpeedCharacters.isEmpty()) {
                    nextCase.getRedCharacters().addAll(normalSpeedCharacters);
                    currentCaseCharacters.removeAll(normalSpeedCharacters);
                }

                // Les créatures faisant un double mouvement vérifie que la case suivante soit aussi libre pour éviter de sauter au dessus des unités ennemies
                if(!highSpeedCharacters.isEmpty()) {
                    if(!nextCase.hasBlueCharacters()) {
                        next2ndCase.getRedCharacters().addAll(highSpeedCharacters);
                    } else nextCase.getRedCharacters().addAll(highSpeedCharacters);
                    currentCase.getRedCharacters().removeAll(highSpeedCharacters);
                }

                // Les créatures aux mouvements réduits bougent aussi
                if(!slowSpeedCharacters.isEmpty()) {
                    nextCase.getRedCharacters().addAll(slowSpeedCharacters);
                    currentCase.getRedCharacters().removeAll(slowSpeedCharacters);
                    slowSpeedCharacters.forEach(k -> k.setCanMove(false));
                }

                currentCase.getRedCharacters().stream().filter(k-> !k.canMove()).forEach(c -> c.setCanMove(true));
            }
        }
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
                        case 5:
                            newCharCreation = new Gobelin();
                            break;
                    }

                    if (newCharCreation != null) {
                        final int cost = newCharCreation.getCost();
                        if (currentCastle.getResources() >= cost) {
                            currentCastle.buildUnit(newCharCreation);
                            newCharCreation = null;
                        } else {
                            // TODO: signaler qu'il n'a pas les ressources nécessaires pour acheter
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

    /**
     * Démarre la phase d'attaque:
     * - Parcours l'ensemble des cases et, pour chaque cases, définit les attaquants et les défenseurs et ordonne d'effectuer l'action.
     */
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

    /**
     * Cette fonction prends tous les attaquants et leur fais donner un coup à toutes les cibles
     * @param attackers l'ensemble des attaquants
     * @param targets l'ensenble des cibles
     */
    private void doAttack(ArrayList<Character> attackers, ArrayList<Character> targets) {
        try {
            for (Character k : attackers) {
                for (int i = 0; i < targets.size(); i++) {
                    final Character currentTarget = targets.get(i);
                    k.handleAttack(currentTarget);
                    if (currentTarget.isDead()) targets.remove(currentTarget);
                }
            }
        }catch(CriticalHitException e) {
            // TODO: afficher l'exception levée quelque part dans l'UI
            targets.forEach(Character::doDie);
            targets.clear();
            Util.println("Critical Hit !");
        }
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

    /**
     * Attribue le nombre de ressources gagnées par les chateaux à chaque tour
     */
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
