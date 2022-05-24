package org.apache.zookeeper;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void sum()
    {
        App app = new App();
        int c = app.sum(12, 4);
        assertEquals(16, c);

    }
}
