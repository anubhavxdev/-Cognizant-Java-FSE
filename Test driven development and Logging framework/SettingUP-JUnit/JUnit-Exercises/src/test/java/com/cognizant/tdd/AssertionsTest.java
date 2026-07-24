package com.cognizant.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsTest {

    @Test
    void testAssertions() {

        // Assert Equals
        assertEquals(5, 2 + 3);

        // Assert True
        assertTrue(10 > 5);

        // Assert False
        assertFalse(10 < 5);

        // Assert Null
        assertNull(null);

        // Assert Not Null
        assertNotNull(new Object());

    }
}