package com.moviebooking.config;

import com.moviebooking.exceptions.NoSuchCommandException;
import com.moviebooking.exceptions.NoSuchDataException;
import com.moviebooking.repositories.data.DataLoader;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@DisplayName("IntegrationTest")
public class IntegrationTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCapture = new ByteArrayOutputStream();
    private ApplicationConfig appConfig;

    @BeforeEach
    public void setUp() throws NoSuchDataException {
        System.setOut(new PrintStream(outputStreamCapture));

        appConfig = new ApplicationConfig();
        DataLoader dataLoader = appConfig.getDataLoader();

        String dataFolder = "src/test/resources/data/";
        dataLoader.loadData("CINEMA-DATA", dataFolder + "cinemas.csv");
        dataLoader.loadData("CUSTOMER-DATA", dataFolder + "customers.csv");
        dataLoader.loadData("MOVIE-DATA", dataFolder + "movies.csv");
        dataLoader.loadData("SCREEN-DATA", dataFolder + "screens.csv");
        dataLoader.loadData("SEAT-DATA", dataFolder + "seats.csv");
        dataLoader.loadData("SHOW-DATA", dataFolder + "shows.csv");
    }



    @DisplayName("DISPLAY-MOVIES command should print all movies")
    @Test
    public void displayMovies_ShouldPrintAllMovies() throws NoSuchCommandException {
        // Arrange
        String expectedOutput = """
                Movie ID - 1
                Title - MovieA
                Duration - 120
                
                Movie ID - 2
                Title - MovieB
                Duration - 120
                
                Movie ID - 3
                Title - MovieC
                Duration - 120""";
        // Act
        appConfig.getCommandInvoker().executeCommand("DISPLAY-MOVIES", new ArrayList<>());

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }


    @DisplayName("DISPLAY-SHOWS command should print all shows")
    @Test
    public void displayShows_ShouldPrintAllShows() throws NoSuchCommandException {
        // Arrange
        String expectedOutput = """
                Show ID - 1
                Title - MovieA
                Start - Wed Oct 14 10:30:00 IST 2020
                End - Wed Oct 14 13:00:00 IST 2020
                Cinema - CinemaA
                Screen - ScreenA
                
                Show ID - 4
                Title - MovieA
                Start - Wed Oct 14 10:30:00 IST 2020
                End - Wed Oct 14 13:00:00 IST 2020
                Cinema - CinemaB
                Screen - ScreenA
                
                Show ID - 7
                Title - MovieA
                Start - Wed Oct 14 10:30:00 IST 2020
                End - Wed Oct 14 13:00:00 IST 2020
                Cinema - CinemaC
                Screen - ScreenA""";
        // Act
        appConfig.getCommandInvoker().executeCommand("DISPLAY-SHOWS", List.of("DISPLAY-SHOWS", "MovieA"));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }


    @DisplayName("DISPLAY-SEATS command should print all seats")
    @Test
    public void displaySeats_ShouldPrintAllSeats() throws NoSuchCommandException {
        // Arrange
        List<String> expectedOutput = List.of(
                """
                SeatRow - 1
                SeatColumn - 1
                Status - UNRESERVED""",
                """
                SeatRow - 1
                SeatColumn - 2
                Status - UNRESERVED""",
                """
                SeatRow - 1
                SeatColumn - 3
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 1
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 2
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 3
                Status - UNRESERVED""",
                """
                SeatRow - 3
                SeatColumn - 1
                Status - UNRESERVED""",
                """
                SeatRow - 3
                SeatColumn - 2
                Status - UNRESERVED""",
                """
                SeatRow - 3
                SeatColumn - 3
                Status - UNRESERVED"""
        );

        // Act
        appConfig.getCommandInvoker().executeCommand("DISPLAY-SEATS", List.of("DISPLAY-SEATS", "1"));

        // Assert
        String actualOutput = outputStreamCapture.toString().trim();
        for (String expectedSeat : expectedOutput) {
            Assertions.assertTrue(actualOutput.contains(expectedSeat));
        }
    }


    @DisplayName("DISPLAY-SEATS command should print all seats according to their status")
    @Test
    public void displaySeats_ShouldSeatsWithCorrectStatus() throws NoSuchCommandException {
        // Arrange
        appConfig.getCommandInvoker().executeCommand("BOOK-TICKET", List.of(
                "BOOK-TICKET", "1", "1", "1#1", "1#2", "2#3"
        ));

        List<String> expectedOutput = List.of(
                """
                SeatRow - 1
                SeatColumn - 1
                Status - RESERVED""",
                """
                SeatRow - 1
                SeatColumn - 2
                Status - RESERVED""",
                """
                SeatRow - 1
                SeatColumn - 3
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 1
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 2
                Status - UNRESERVED""",
                """
                SeatRow - 2
                SeatColumn - 3
                Status - RESERVED""",
                """
                SeatRow - 3
                SeatColumn - 1
                Status - UNRESERVED""",
                """
                SeatRow - 3
                SeatColumn - 2
                Status - UNRESERVED""",
                """
                SeatRow - 3
                SeatColumn - 3
                Status - UNRESERVED"""
        );

        // Act
        appConfig.getCommandInvoker().executeCommand("DISPLAY-SEATS", List.of(
                "DISPLAY-SEATS", "1"
        ));

        // Assert
        String actualOutput = outputStreamCapture.toString().trim();
        for (String seat : expectedOutput) {
            Assertions.assertTrue(actualOutput.contains(seat));
        }
    }


    @DisplayName("BOOK-TICKET command should book ticket and print ticket id")
    @Test
    public void bookTicket_ShouldBookTicketAndPrintTicketId() throws NoSuchCommandException {
        // Arrange
        String customerId = "1";
        String showId = "1";
        String seat1_1 = "1#1";
        String command = "BOOK-TICKET";
        String expectedOutput = "Ticket ID - 1";

        // Act
        appConfig.getCommandInvoker().executeCommand(
                command,
                List.of(command, customerId, showId, seat1_1)
        );

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }


    @DisplayName("BOOK-TICKET command should print error message when one or more seats are already reserved")
    @Test
    public void bookTicket_ShouldPrintErrorMessage() throws NoSuchCommandException {
        // Arrange
        String customerId = "1";
        String showId = "1";
        String seat1_1 = "1#1";
        String seat1_2 = "1#2";
        String seat1_3 = "1#3";
        String seat2_1 = "2#1";
        String command = "BOOK-TICKET";

        appConfig.getCommandInvoker().executeCommand(
                command,
                List.of(command, customerId, showId, seat1_1, seat1_2)
        );
        outputStreamCapture.reset();

        String expectedOutput = "Some Seats are already Booked! Please try booking available seats!";

        // Act
        appConfig.getCommandInvoker().executeCommand(
                command,
                List.of(command, customerId, showId, seat1_1, seat1_3, seat2_1)
        );

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }


    @DisplayName("CANCEL-TICKET command should cancel ticket and print success message")
    @Test
    public void cancelTicket_ShouldPrintCancellationSuccessMessage() throws NoSuchCommandException {
        // Arrange
        appConfig.getCommandInvoker().executeCommand("BOOK-TICKET", List.of(
                "BOOK-TICKET", "1", "1", "1#1"
        ));
        outputStreamCapture.reset();

        String expectedOutput = "Cancellation Successful!";

        // Act
        appConfig.getCommandInvoker().executeCommand("CANCEL-TICKET", List.of(
                "CANCEL-TICKET", "1"
        ));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }


    @DisplayName("CANCEL-TICKET command should print error message when ticket is not present")
    @Test
    public void cancelTicket_ShouldPrintErrorMessage() throws NoSuchCommandException {
        // Arrange
        String expectedOutput = "No Such Ticket Found!";

        // Act
        appConfig.getCommandInvoker().executeCommand("CANCEL-TICKET", List.of(
                "CANCEL-TICKET", "1"
        ));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCapture.toString().trim());
    }



    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
