package com.moviebooking.commands;

import com.moviebooking.exceptions.NoSuchTicketFoundException;
import com.moviebooking.services.ITicketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("CancelTicketCommandTest")
@ExtendWith(MockitoExtension.class)
public class CancelTicketCommandTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Mock
    ITicketService iTicketServiceMock;

    @InjectMocks
    CancelTicketCommand cancelTicketCommand;

    @DisplayName("execute method should print cancellation success to console")
    @Test
    public void execute_ShouldPrintCancellationSuccessMessage() throws NoSuchTicketFoundException {
        // Arrange
        Integer ticketId = 1;
        String expectedResult = "Cancellation Successful!";

        // Act
        cancelTicketCommand.execute(List.of("CANCEL-TICKET", ticketId.toString()));

        // Assert
        Assertions.assertEquals(expectedResult, outputStreamCaptor.toString().trim());
        verify(iTicketServiceMock, atLeastOnce()).cancelTicket(ticketId);
    }


    @DisplayName("execute method should print error message to console")
    @Test
    public void execute_ShouldPrintErrorMessage() throws NoSuchTicketFoundException {
        // Arrange
        Integer ticketId = 1;
        String expectedResult = new NoSuchTicketFoundException().toString();
        doThrow(new NoSuchTicketFoundException()).when(iTicketServiceMock).cancelTicket(ticketId);

        // Act
        cancelTicketCommand.execute(List.of("CANCEL-TICKET", ticketId.toString()));

        // Assert
        Assertions.assertEquals(expectedResult, outputStreamCaptor.toString().trim());
    }


    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
