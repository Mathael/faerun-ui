package com.utils;

/**
 * @author Leboc Philippe
 * Classe utilitaire de l'application.
 */
public final class Util {

    public static int roll(int faces, int nbrOfDice) {
        int res = 0;
        for (int i = 0; i < nbrOfDice; i++) res += roll(faces);
        return res;
    }

    public static int roll(int faces) {
        return (int) ((faces-1)*Math.random()) + 1;
    }
}
