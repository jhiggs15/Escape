package escape;

import escape.piece.EscapeGamePiece;
import escape.required.Player;

import java.util.ArrayList;
import java.util.List;

public class Score
{
    private Player player;
    private int playerScore = 0;
    private List<EscapeGamePiece> playerScoredGamePieces = new ArrayList<>();

    public Score(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) return false;
        Score score = (Score) obj;
        return score.player.equals(player)
                && score.playerScore == playerScore
                && playerScoredGamePieces.containsAll(score.playerScoredGamePieces)
                && score.playerScoredGamePieces.containsAll(playerScoredGamePieces);
    }

    public int getPlayerScore()
    {
        return playerScore;
    }

    public List<EscapeGamePiece> getPlayerScoredGamePieces()
    {
        return playerScoredGamePieces;
    }

    public void incrementPlayerScore(EscapeGamePiece piece)
    {
        playerScore += piece.getValue();
    }

}
