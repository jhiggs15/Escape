package escape.gamemanagement;

import escape.required.Player;

public class TurnManager
{
    private Player currentPlayer = Player.PLAYER1;
    private int turnNumber = 1;

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public int getTurnNumber()
    {
        return turnNumber;
    }

    public void endTurn()
    {
        if(currentPlayer.equals(Player.PLAYER1))
            currentPlayer = Player.PLAYER2;
        else
        {
            currentPlayer = Player.PLAYER1;
            turnNumber++;
        }
    }
}
