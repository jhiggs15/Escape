package escape.board;

import escape.piece.EscapeGamePiece;
import escape.required.LocationType;
import escape.required.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardSpace
{
    private List<EscapeGamePiece> pieces = new ArrayList<>();
    private LocationType type = LocationType.CLEAR;

    public BoardSpace(EscapeGamePiece piece, LocationType type)
    {
        if(type != null) this.type = type;
        addPiece(piece);
    }

    public boolean isAccessible()
    {
        return type == LocationType.BLOCK;
    }

    public EscapeGamePiece removePiece(){ return null; }
    public EscapeGamePiece removePiece(EscapeGamePiece piece){ return null; }
    public void addPiece(EscapeGamePiece piece)
    {
        if(piece != null) pieces.add(piece);
    }
    public boolean isEmpty() {return false; }
    public List<EscapeGamePiece> getPieces(){return pieces; }
    public EscapeGamePiece getPiece(Player player){
        if(!pieces.isEmpty())
        {
            Optional<EscapeGamePiece> potentialPiece = pieces.stream().filter(piece -> piece.getPlayer() == player).findFirst();
            if(!potentialPiece.isEmpty())
                return potentialPiece.get();
        }
        return null;
    }
    public LocationType getType() { return type; }


    @Override
    public boolean equals(Object otherBoardSpace) {
        if(otherBoardSpace.getClass() != this.getClass()) return false;
        BoardSpace otherBP = (BoardSpace) otherBoardSpace;
        return otherBP.getPieces().equals(pieces) && type == otherBP.getType();
    }
}
