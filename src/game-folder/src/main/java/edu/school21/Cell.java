package edu.school21;

public class Cell {
    private Integer x;
    private Integer y;

    private char value;

    public Cell(Integer x, Integer y, char value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

}
