package escape.movement.pathfinding;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.movement.NeighborFinder;

import java.util.*;

public class AStar implements PathFinding
{
    private PriorityQueue<CostPair> coordinates = new PriorityQueue<>();

    private NeighborFinder neighborFinder;

    public AStar(NeighborFinder neighborFinder)
    {
        this.neighborFinder = neighborFinder;

    }

    void addToCoordinates(EscapeCoordinate coordinate, int cost)
    {
        CostPair pair = new CostPair(coordinate, cost);
        coordinates.add(pair);
    }

    EscapeCoordinate getNextCoordinate()
    {
        if (coordinates.isEmpty()) return null;
        return coordinates.poll().getItem();
    }

    public List<EscapeCoordinate> findPath(EscapeCoordinate from, EscapeCoordinate to)
    {
        coordinates.clear();

        final Map<EscapeCoordinate, EscapeCoordinate> path = new HashMap<>();
        final Map<EscapeCoordinate, Integer> costSoFar = new HashMap<>();

        addToCoordinates(from, 0);
        path.put(from, null);
        costSoFar.put(from, 0);

        while(!coordinates.isEmpty())
        {
            EscapeCoordinate current = getNextCoordinate();
            if(current.equals(to)) break;

            List<EscapeCoordinate> neighbors = neighborFinder.getNeighbors(current);

            for(EscapeCoordinate neighbor : neighbors)
            {
                int newCost = neighborFinder.minimumDistance(neighbor, to);
                if(!path.containsKey(neighbor) || newCost < costSoFar.get(neighbor))
                {
                    costSoFar.put(neighbor, newCost);
                    addToCoordinates(neighbor, newCost);
                    path.put(neighbor, current);

                }
            }

        }

        return buildPath(path, from, to);

    }



    private List<EscapeCoordinate> buildPath(Map<EscapeCoordinate, EscapeCoordinate> path, EscapeCoordinate from, EscapeCoordinate to)
    {
        LinkedList<EscapeCoordinate> listPath = new LinkedList<>();
        EscapeCoordinate next = to;

        while (path.get(next) != null) {
            listPath.push(next);
            next = path.get(next);
        }
        return listPath;

    }


    private class CostPair implements Comparable<CostPair>
    {
        private final EscapeCoordinate item;
        private final int cost;

        private CostPair(EscapeCoordinate item, int cost) {
            this.item = item;
            this.cost = cost;
        }

        public EscapeCoordinate getItem() {
            return item;
        }

        public int getCost() { return cost; }

        public int compareTo(CostPair o) {
            return Double.compare(cost, o.getCost());
        }

    }
}
