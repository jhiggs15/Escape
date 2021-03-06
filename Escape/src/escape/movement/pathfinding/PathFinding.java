package escape.movement.pathfinding;

import escape.board.EscapeCoordinate;
import escape.board.EscapeGamePiece;

import java.util.List;

public interface PathFinding
{
    List<EscapeCoordinate> findPath(EscapeCoordinate from, EscapeCoordinate to, EscapeGamePiece piece);
}
