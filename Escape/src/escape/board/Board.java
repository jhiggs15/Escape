package escape.board;

import escape.movement.BoundsChecker;
import escape.movement.MoveManager;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {

    private HashMap<EscapeCoordinate, BoardSpace> board = new HashMap<>();
    private MoveManager moveManager;
    private List<EscapeGamePiece> piecesOnTheBoard = new ArrayList<>();


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
            board.put(
                    coordinate,
                    new BoardSpace(piece, locationType)
            );


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

    public boolean move(EscapeCoordinate from, EscapeCoordinate to)
    {
        return false;
    }

    public boolean canMove(EscapeCoordinate from, EscapeCoordinate to)
    {
        return moveManager.canMove(from, to);
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
