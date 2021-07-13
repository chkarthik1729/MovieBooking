package com.moviebooking.config;

import com.moviebooking.commands.*;
import com.moviebooking.repositories.*;
import com.moviebooking.repositories.data.*;
import com.moviebooking.services.*;

public class ApplicationConfig {

    private final ICinemaRepository cinemaRepository = new CinemaRepository();
    private final ICustomerRepository customerRepository = new CustomerRepository();
    private final IMovieRepository movieRepository = new MovieRepository();
    private final IShowRepository showRepository = new ShowRepository();
    private final IShowSeatRepository showSeatRepository = new ShowSeatRepository();
    private final ITicketRepository ticketRepository = new TicketRepository();

    private final IMovieService movieService = new MovieService(movieRepository);
    private final IShowService showService = new ShowService(showRepository, showSeatRepository);
    private final ITicketService ticketService = new TicketService(customerRepository, showSeatRepository, showRepository, ticketRepository);

    private final ICommand bookTicketCommand = new BookTicketCommand(ticketService);
    private final ICommand cancelTicketCommand = new CancelTicketCommand(ticketService);
    private final ICommand displayMovieCommand = new DisplayMovieCommand(movieService);
    private final ICommand displayShowCommand = new DisplayShowCommand(showService);
    private final ICommand displayShowSeatCommand = new DisplayShowSeatCommand(showService);

    private final IData cinemaData = new CinemaData(cinemaRepository);
    private final IData customerData = new CustomerData(customerRepository);
    private final IData movieData = new MovieData(movieRepository);
    private final IData screenData = new ScreenData(cinemaRepository);
    private final IData seatData = new SeatData(cinemaRepository);
    private final IData showData = new ShowData(showRepository, cinemaRepository, movieRepository, showSeatRepository);

    private final CommandInvoker commandInvoker = new CommandInvoker();
    private final DataLoader dataLoader = new DataLoader();

    public CommandInvoker getCommandInvoker() {
        commandInvoker.registerCommand("BOOK-TICKET", bookTicketCommand);
        commandInvoker.registerCommand("CANCEL-TICKET", cancelTicketCommand);
        commandInvoker.registerCommand("DISPLAY-MOVIES", displayMovieCommand);
        commandInvoker.registerCommand("DISPLAY-SHOWS", displayShowCommand);
        commandInvoker.registerCommand("DISPLAY-SEATS", displayShowSeatCommand);

        return commandInvoker;
    }

    public DataLoader getDataLoader() {
        dataLoader.register("CINEMA-DATA", cinemaData);
        dataLoader.register("CUSTOMER-DATA", customerData);
        dataLoader.register("MOVIE-DATA", movieData);
        dataLoader.register("SCREEN-DATA", screenData);
        dataLoader.register("SEAT-DATA", seatData);
        dataLoader.register("SHOW-DATA", showData);

        return dataLoader;
    }
}
