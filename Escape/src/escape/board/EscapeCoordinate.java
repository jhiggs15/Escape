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

    @Override
    public int hashCode()
    {
        // taken from https://stackoverflow.com/questions/22826326/good-hashcode-function-for-2d-coordinates
        // creates a bijection between (x,y) and the hash
        int tmp = ( y +  ((x+1)/2));
        return x +  ( tmp * tmp);
    }
}
