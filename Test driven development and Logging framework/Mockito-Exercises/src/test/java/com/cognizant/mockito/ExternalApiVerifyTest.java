package com.cognizant.mockito;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ExternalApiVerifyTest {

    @Test
    void testVerifyInteraction() {

        // Arrange
        ExternalApi api = mock(ExternalApi.class);

        // Act
        api.getData();

        // Assert
        verify(api).getData();

    }
}