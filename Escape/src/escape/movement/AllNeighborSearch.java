package escape.movement;

import escape.board.EscapeCoordinate;

import java.util.List;

@FunctionalInterface
public interface AllNeighborSearch {
    List<EscapeCoordinate> findAllNeighbors(EscapeCoordinate coordinate);
}
