package com.cognizant.mockito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExternalApiTest {

    @Test
    void testMocking() {

        // Create a fake object
        ExternalApi api = mock(ExternalApi.class);

        // Tell the fake object what to return
        when(api.getData()).thenReturn("Mockito Working!");

        // Verify
        assertEquals("Mockito Working!", api.getData());

    }
}