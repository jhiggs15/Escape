package escape.gamemanagement;

import escape.required.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager
{
    private List<GameObserver> observers;


    public ObserverManager()
    {
        observers = new ArrayList<>();
    }


    public GameObserver addObserver(GameObserver observer)
    {
        observers.add(observer);
        return observer;
    }

    public GameObserver removeObserver(GameObserver observer)
    {
        observers.remove(observer);
        return observer;
    }

    public void notifyAll(String message)
    {
        for(GameObserver observer : observers)
        {
            observer.notify(message);
        }
    }

    public void notifyAll(String message, Throwable exception)
    {
        for(GameObserver observer : observers)
        {
            observer.notify(message, exception);
        }
    }



}
