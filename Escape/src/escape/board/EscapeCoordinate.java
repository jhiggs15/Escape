package escape.board;

import escape.required.Coordinate;

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
}
