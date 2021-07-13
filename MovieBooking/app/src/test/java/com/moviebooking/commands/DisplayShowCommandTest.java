package com.moviebooking.commands;

import com.moviebooking.dto.ShowResponse;
import com.moviebooking.services.IShowService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DisplayShowCommandTest")
@ExtendWith(MockitoExtension.class)
public class DisplayShowCommandTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    IShowService iShowServiceMock;

    @InjectMocks
    DisplayShowCommand displayShowCommand;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @DisplayName("execute should print list of shows")
    @Test
    public void execute_ShouldPrintListOfShows() throws ParseException {
        // Arrange
        List<ShowResponse> showResponseList = new ArrayList<>();
        String movieTitle = "MovieA";

        Date start = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/07/2021 10:30");
        Date end = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/07/2021 13:00");

        showResponseList.add(new ShowResponse("1", movieTitle, "CinemaA", "ScreenA", start, end));
        showResponseList.add(new ShowResponse("2", movieTitle, "CinemaB", "ScreenA", start, end));
        showResponseList.add(new ShowResponse("3", movieTitle, "CinemaC", "ScreenA", start, end));

        String expectedOutput = """
                Show ID - 1
                Title - MovieA
                Start - Tue Jul 20 10:30:00 IST 2021
                End - Tue Jul 20 13:00:00 IST 2021
                Cinema - CinemaA
                Screen - ScreenA
                
                Show ID - 2
                Title - MovieA
                Start - Tue Jul 20 10:30:00 IST 2021
                End - Tue Jul 20 13:00:00 IST 2021
                Cinema - CinemaB
                Screen - ScreenA
                
                Show ID - 3
                Title - MovieA
                Start - Tue Jul 20 10:30:00 IST 2021
                End - Tue Jul 20 13:00:00 IST 2021
                Cinema - CinemaC
                Screen - ScreenA""";

        when(iShowServiceMock.getAllShowsByMovieTitle(movieTitle)).thenReturn(showResponseList);

        // Act
        displayShowCommand.execute(List.of("DISPLAY-SHOWS", movieTitle));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iShowServiceMock).getAllShowsByMovieTitle(movieTitle);
    }


    @DisplayName("execute should print empty string")
    @Test
    public void execute_ShouldPrintEmpty() {
        // Arrange
        String movieTitle = "MovieA";
        List<ShowResponse> showResponseList = new ArrayList<>();
        when(iShowServiceMock.getAllShowsByMovieTitle(movieTitle)).thenReturn(showResponseList);
        String expectedOutput = "";

        // Act
        displayShowCommand.execute(List.of("DISPLAY-SHOWS", movieTitle));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iShowServiceMock).getAllShowsByMovieTitle(movieTitle);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
