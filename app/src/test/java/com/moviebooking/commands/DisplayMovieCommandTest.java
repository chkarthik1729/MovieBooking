package com.moviebooking.commands;

import com.moviebooking.entities.Movie;
import com.moviebooking.services.IMovieService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("DisplayMovieCommandTest")
@ExtendWith(MockitoExtension.class)
public class DisplayMovieCommandTest {

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    IMovieService iMovieServiceMock;

    @InjectMocks
    DisplayMovieCommand displayMovieCommand;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @DisplayName("execute should print the list of all available movies")
    @Test
    public void execute_ShouldPrintListOfMovies() {
        // Arrange
        List<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("1", "MovieA", 120));
        movieList.add(new Movie("2", "MovieB", 110));
        movieList.add(new Movie("3", "MovieC", 150));

        when(iMovieServiceMock.getAllMovies()).thenReturn(movieList);
        String expectedResult = "" +
                "Movie ID - 1\n" +
                "Title - MovieA\n" +
                "Duration - 120\n" +
                "\n" +
                "Movie ID - 2\n" +
                "Title - MovieB\n" +
                "Duration - 110\n" +
                "\n" +
                "Movie ID - 3\n" +
                "Title - MovieC\n" +
                "Duration - 150";


        // Act
        displayMovieCommand.execute(List.of("DISPLAY-MOVIES"));

        // Assert
        Assertions.assertEquals(expectedResult, outputStreamCaptor.toString().trim());
        verify(iMovieServiceMock, atLeastOnce()).getAllMovies();
    }


    @DisplayName("execute should print empty string if no movies")
    @Test
    public void execute_ShouldPrintEmptyIfNoMovies() {
        // Arrange
        when(iMovieServiceMock.getAllMovies()).thenReturn(new ArrayList<>());
        String expectedOutput = "";

        // Act
        displayMovieCommand.execute(List.of("DISPLAY-MOVIES"));

        // Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(iMovieServiceMock, atLeastOnce()).getAllMovies();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(stdOut);
    }
}
