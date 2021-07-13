package com.moviebooking.commands;

import com.moviebooking.entities.Seat;
import com.moviebooking.entities.Show;
import com.moviebooking.entities.ShowSeat;
import com.moviebooking.services.IShowService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DisplayShowSeatsCommandTest")
@ExtendWith(MockitoExtension.class)
public class DisplayShowSeatsCommandTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    Show show;

    @Mock
    IShowService iShowService;

    @InjectMocks
    DisplayShowSeatCommand displayShowSeatCommand;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @DisplayName("execute on DisplayShowSeats should print show seats to console")
    @Test
    public void execute_ShouldPrintShowSeats() {
        // Arrange
        String showId = "1";
        List<ShowSeat> showSeatList = new ArrayList<>();
        showSeatList.add(new ShowSeat("1#1#1", new Seat("1#1", 1, 1), show));
        showSeatList.add(new ShowSeat("1#1#2", new Seat("1#2", 1, 2), show));
        showSeatList.add(new ShowSeat("1#2#1", new Seat("2#1", 2, 1), show));
        showSeatList.add(new ShowSeat("1#2#2", new Seat("2#2", 2, 2), show));
        showSeatList.add(new ShowSeat("1#3#1", new Seat("3#1", 3, 1), show));
        showSeatList.add(new ShowSeat("1#3#2", new Seat("3#2", 3, 2), show));

        when(iShowService.getAllShowSeats(showId)).thenReturn(showSeatList);

        String expectedOutput = """
                SeatRow - 1
                SeatColumn - 1
                Status - UNRESERVED
                
                SeatRow - 1
                SeatColumn - 2
                Status - UNRESERVED
                
                SeatRow - 2
                SeatColumn - 1
                Status - UNRESERVED
                
                SeatRow - 2
                SeatColumn - 2
                Status - UNRESERVED
                
                SeatRow - 3
                SeatColumn - 1
                Status - UNRESERVED
                
                SeatRow - 3
                SeatColumn - 2
                Status - UNRESERVED""";

        // Act
        displayShowSeatCommand.execute(List.of("DISPLAY-SEATS", showId));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iShowService).getAllShowSeats(showId);
    }


    @DisplayName("execute on DisplayShowSeats should print empty string to console")
    @Test
    public void execute_ShouldPrintEmptyIfNoSeats() {
        // Arrange
        String showId = "1";
        List<ShowSeat> showSeatList = new ArrayList<>();

        when(iShowService.getAllShowSeats(showId)).thenReturn(showSeatList);
        String expectedOutput = "";

        // Act
        displayShowSeatCommand.execute(List.of("DISPLAY-SEATS", showId));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iShowService).getAllShowSeats(showId);
    }


    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
