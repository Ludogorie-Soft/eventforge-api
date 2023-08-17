package com.eventforge.service.exception;

import com.eventforge.exception.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomAuthenticationEntryPointTest {
    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCommence() throws IOException {
        // Arrange
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(mockResponse.getWriter()).thenReturn(writer);

        // Act
        entryPoint.commence(mockRequest, mockResponse, null);

        // Assert
        verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        verify(mockResponse).setContentType("text/plain");
        verify(mockResponse).setCharacterEncoding("UTF-8");
        writer.flush();
        String responseContent = stringWriter.toString();
        // Add assertions for the response content if necessary
    }
}
