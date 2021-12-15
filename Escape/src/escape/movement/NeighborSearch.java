package escape.movement;

import escape.board.EscapeCoordinate;

import java.util.List;

@FunctionalInterface
public interface NeighborSearch {
    List<EscapeCoordinate> findNeighbors(EscapeCoordinate coordinate);
}
