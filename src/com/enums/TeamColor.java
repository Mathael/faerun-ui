package com.enums;

/**
 * @author Leboc Philippe
 */
public enum TeamColor {
    BLUE("Bleu"),
    RED("Rouge");

    private String name;

    TeamColor(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
