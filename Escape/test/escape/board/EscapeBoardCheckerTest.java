package escape.board;

import escape.required.Coordinate;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class EscapeBoardCheckerTest {

    @Test
    void inBounds_XInfinite_YInfinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(0, 0);
        assertTrue(boundsChecker.checkBounds(new EscapeCoordinate(-1, 1000)));
    }

    @Test
    void inBounds_XInfinite_YFinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(0, 10);
        assertTrue(boundsChecker.checkBounds(new EscapeCoordinate(-12, 10)));
    }

    @Test
    void inBounds_XFinite_YInfinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(49, 0);
        assertTrue(boundsChecker.checkBounds(new EscapeCoordinate(2, 51)));
    }

    @Test
    void inBounds_XFinite_YFinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(12, 12);
        assertTrue(boundsChecker.checkBounds(new EscapeCoordinate(12, 1)));
    }

    @Test
    void outOfBounds_XInfinite_YFinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(0, 10);
        assertFalse(boundsChecker.checkBounds(new EscapeCoordinate(-2, 12)));
    }

    @Test
    void outOfBounds_XFinite_YInfinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(49, 0);
        assertFalse(boundsChecker.checkBounds(new EscapeCoordinate(-1, 20)));
    }

    @Test
    void outOfBounds_XFinite_YFinite()
    {
        BoundsChecker boundsChecker = new BoundsChecker(12, 12);
        assertFalse(boundsChecker.checkBounds(new EscapeCoordinate(13, 13)));
    }







}
