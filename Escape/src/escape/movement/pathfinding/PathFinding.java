package escape.movement.pathfinding;

import escape.board.EscapeCoordinate;

import java.util.List;

public interface PathFinding
{
    List<EscapeCoordinate> findPath(EscapeCoordinate from, EscapeCoordinate to);
}
