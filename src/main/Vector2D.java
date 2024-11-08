package main;

public class Vector2D {
    // Fields
    private int x;
    private int y;

    // Constructor
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    // Methods
    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int newX){
        x = newX;
    }

    public void setY(int newY){
        y = newY;
    }

    public void adjustX(int adjustment){
        // Backward adjustments can be made by passing a negative number as an adjustment
        x += adjustment;
    }

    public void adjustY(int adjustment){
        // Backward adjustments can be made by passing a negative number as an adjustment
        y += adjustment;
    }
}
