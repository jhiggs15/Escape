package escape.board;

import escape.required.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EscapeCoordinate implements Coordinate
{
    private int x, y;

    public EscapeCoordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }



    @Override
    public boolean equals(Object otherCoordinate) {
        if(otherCoordinate.getClass() != this.getClass()) return false;
        EscapeCoordinate otherCoord = (EscapeCoordinate) otherCoordinate;
        return x == otherCoord.getX() && y == otherCoord.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return  x + ", " + y;
    }
}
