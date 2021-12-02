package escape.movement;

import escape.board.EscapeCoordinate;
import escape.piece.EscapeGamePiece;

import java.util.List;

@FunctionalInterface
public interface NeighborSearch {
    List<EscapeCoordinate> findNeighbors(EscapeCoordinate coordinate);
}
