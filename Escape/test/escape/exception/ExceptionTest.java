package escape.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest
{
    @Test
    void getPieceThatExists() throws Exception {
        assertThrows(EscapeException.class, () -> fun());
    }

    public void fun()
    {
        throw new EscapeException("Peace of Mind", new Throwable());
    }
}
