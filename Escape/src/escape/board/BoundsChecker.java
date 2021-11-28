package escape.board;

public class BoundsChecker
{
    private int xMax;
    private int xMin = 1;
    private int yMax;
    private int yMin = 1;

    public BoundsChecker(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean checkBounds(EscapeCoordinate coordinate)
    {
        return withinBounds(coordinate.getX(), xMin, xMax) && withinBounds(coordinate.getY(), yMin, yMax);
    }

    public boolean withinBounds(int value, int valueMin, int valueMax)
    {
        if(valueMax == 0) return true;
        return value >= valueMin && value <= valueMax;
    }
}
