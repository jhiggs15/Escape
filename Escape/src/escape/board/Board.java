package escape.board;

import escape.Score;
import escape.exception.NoPathExists;
import escape.exception.OutOfBoundsException;
import escape.movement.BoundsChecker;
import escape.movement.MoveManager;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.required.Player;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {

    private HashMap<EscapeCoordinate, BoardSpace> board = new HashMap<>();
    private MoveManager moveManager;
    private List<EscapeGamePiece> piecesOnTheBoard = new ArrayList<>();
    private HashMap<EscapeCoordinate, BoardSpace> exitSpaces = new HashMap<>();


    public Board(EscapeGameInitializer gameInitializer)
    {
        this.moveManager = new MoveManager(gameInitializer.getCoordinateType(), this,
                gameInitializer.getxMax(), gameInitializer.getyMax());

        HashMap<EscapePiece.PieceName, EscapeGamePiece> piecesWithoutOwner = new HashMap();
        for(PieceTypeDescriptor pieceTypeDescriptor : gameInitializer.getPieceTypes())
            piecesWithoutOwner.put(pieceTypeDescriptor.getPieceName(), new EscapeGamePiece(pieceTypeDescriptor, this));

        // create pieces
        // create locations with pieces
        for(LocationInitializer location : gameInitializer.getLocationInitializers())
        {
            // gets the piece at the location and null otherwise
            EscapeGamePiece pieceAtCurrentLocation = piecesWithoutOwner.get(location.pieceName);
            // if a piece exists in the current location create a new piece with a player attached to it
            if(pieceAtCurrentLocation != null)
            {
                pieceAtCurrentLocation = pieceAtCurrentLocation.makeCopy(location.player);
                piecesOnTheBoard.add(pieceAtCurrentLocation);
            }
            // creates a board space on the board, or adds to it depending on the context
            createBoardSpace(makeCoordinate(location.x, location.y), location.locationType, pieceAtCurrentLocation);
        }
    }

    void createBoardSpace(EscapeCoordinate coordinate, LocationType locationType, EscapeGamePiece piece)
    {
        if(spaceExists(coordinate))
            board.get(coordinate).addPiece(piece);

        else
        {
            BoardSpace boardSpace = new BoardSpace(piece, locationType);
            if(locationType != null && locationType.equals(LocationType.EXIT)) exitSpaces.put(coordinate, boardSpace);
            board.put(
                    coordinate,
                    boardSpace
            );
        }



    }

    public EscapeGamePiece getPieceAt(EscapeCoordinate coordinate)
    {
        if(spaceExists(coordinate))
        {
            BoardSpace boardSpace = board.get(coordinate);
            return boardSpace.getPiece();
        }

        return null;
    }

    HashMap<EscapeCoordinate, BoardSpace> getBoard() {
        return board;
    }

    public List<EscapeGamePiece> getPiecesOnTheBoard()
    {
        return piecesOnTheBoard;
    }

    public Score move(Player player, EscapeCoordinate from, EscapeCoordinate to)
    {
        if(canMove(player, from, to))
        {
            // no path exists
            List<EscapeCoordinate> path = moveManager.findPath(from, to);
            int pathSize = path.size();
            if(pathSize == 0 && !from.equals(to)) throw new NoPathExists(from, to);

            // the shortest path is larger than the possible spaces the piece can move
            int pieceValue = getPieceAt(from).getMovementValue();
            if(pathSize > pieceValue) throw new NoPathExists(from, to, pathSize, pieceValue);

            EscapeCoordinate destination = executeMove(from, path);

            Score score = findScore(player, destination);
            return score;

        }

        return null;

    }

    private EscapeCoordinate executeMove(EscapeCoordinate from, List<EscapeCoordinate> path)
    {
        EscapeGamePiece pieceToMove = board.get(from).removePiece();
        EscapeCoordinate destination = path.get(path.size() - 1);
        List<EscapeCoordinate> possibleNewDestination =
                path.stream()
                        .filter(coordinate -> exitSpaces.containsKey(coordinate))
                        .collect(Collectors.toList());
        if(possibleNewDestination.size() >= 1)
            destination = possibleNewDestination.get(0);
        createBoardSpace(destination, LocationType.CLEAR, pieceToMove);

        return destination;
    }

    private Score findScore(Player player, EscapeCoordinate to)
    {
        Score score = new Score(player);

        if(exitSpaces.containsKey(to))
        {
            EscapeGamePiece removedPiece = board.get(to).removePiece();
            score.incrementPlayerScore(removedPiece);
            piecesOnTheBoard.remove(removedPiece);
        }

        return score;
    }

    public boolean canMove(Player player, EscapeCoordinate from, EscapeCoordinate to)
    {
        return moveManager.canMove(player, from, to);
    }


    public EscapeCoordinate makeCoordinate(int x, int y)
    {
        return new EscapeCoordinate(x, y);
    }

    private boolean spaceExists(EscapeCoordinate coordinate)
    {
        return board.get(coordinate) != null;
    }

    public boolean isAccessible(EscapeCoordinate coordinate)
    {
        return (spaceExists(coordinate) && board.get(coordinate).isAccessible()) || !spaceExists(coordinate) ;
    }


}
