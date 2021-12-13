package escape.movement.pathfinding;

import escape.board.EscapeCoordinate;
import escape.piece.EscapeGamePiece;

import java.util.List;

@FunctionalInterface
public interface LineChecker
{
    boolean isSpecifiedLineType(EscapeCoordinate from, EscapeCoordinate to);

}
