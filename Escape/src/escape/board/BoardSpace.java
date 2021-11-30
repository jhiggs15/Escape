package escape.board;

import escape.piece.EscapeGamePiece;
import escape.required.LocationType;

import java.util.ArrayList;
import java.util.List;

public class BoardSpace
{
    private List<EscapeGamePiece> pieces = new ArrayList<>();
    private LocationType type = LocationType.CLEAR;

    public BoardSpace(EscapeGamePiece piece, LocationType type)
    {
        if(type != null) this.type = type;
        addPiece(piece);
    }


    public EscapeGamePiece removePiece(){ return null; }
    public EscapeGamePiece removePiece(EscapeGamePiece piece){ return null; }
    public void addPiece(EscapeGamePiece piece)
    {
        if(piece != null) pieces.add(piece);
    }
    public boolean isEmpty() {return false; }
    public List<EscapeGamePiece> getPieces(){return pieces; }
    public LocationType getType() { return type; }

}
