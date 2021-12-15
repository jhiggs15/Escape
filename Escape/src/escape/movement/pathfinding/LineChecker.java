package escape.movement.pathfinding;

import escape.board.EscapeCoordinate;

@FunctionalInterface
public interface LineChecker
{
    boolean isSpecifiedLineType(EscapeCoordinate from, EscapeCoordinate to);

}
