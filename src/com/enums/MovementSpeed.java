package com.enums;

/**
 * @author Leboc Philippe
 */
public enum MovementSpeed {

    BLOCKED(0),
    NORMAL(1),
    SPEED(2),
    SLOW(3);

    private int value;

    MovementSpeed(int value) {
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
