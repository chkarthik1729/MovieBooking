package com.moviebooking.commands;

import com.moviebooking.exceptions.SeatNotAvailableException;
import com.moviebooking.entities.Seat;
import com.moviebooking.entities.Ticket;
import com.moviebooking.services.ITicketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@DisplayName("BookTicketCommandTest")
@ExtendWith(MockitoExtension.class)
public class BookTicketCommandTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    ITicketService iTicketServiceMock;

    @Mock
    Ticket ticket;

    @InjectMocks
    BookTicketCommand bookTicketCommand;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("execute method should print Ticket Id to console")
    public void execute_ShouldPrintTicketId() throws SeatNotAvailableException {
        // Arrange
        String customerId = "1";
        String showId = "1";
        Seat seat1_2 = new Seat("1#2", 1, 2);
        Seat seat1_3 = new Seat("1#3", 1, 3);
        List<Seat> seatList = List.of(seat1_2, seat1_3);

        when(iTicketServiceMock.bookTicket(customerId, showId, seatList)).thenReturn(ticket);
        when(ticket.getId()).thenReturn(1);

        String expectedOutput = "Ticket ID - 1";

        // Act
        bookTicketCommand.execute(List.of("BOOK-TICKET", customerId, showId, seat1_2.getId(), seat1_3.getId()));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iTicketServiceMock).bookTicket(customerId, showId, seatList);
    }

    @Test
    @DisplayName("execute method should print error message to console")
    public void execute_ShouldPrintErrorMessage() throws SeatNotAvailableException {
        // Arrange
        String customerId = "1";
        String showId = "1";
        Seat seat1_2 = new Seat("1#2",1,2);
        Seat seat1_3 = new Seat("1#3",1,3);
        List<Seat> seatList = new ArrayList<>(List.of(seat1_2,seat1_3));

        when(iTicketServiceMock.bookTicket(customerId,showId,seatList)).thenThrow(new SeatNotAvailableException());

        String expectedOutput = "Some Seats are already Booked! Please try booking available seats!";

        //Act
        bookTicketCommand.execute(List.of("BOOK-TICKET", customerId, showId, seat1_2.getId(), seat1_3.getId()));

        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iTicketServiceMock).bookTicket(customerId, showId, seatList);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
