package com.moviebooking.commands;

import com.moviebooking.exceptions.SeatNotAvailableException;
import com.moviebooking.entities.Seat;
import com.moviebooking.entities.Ticket;
import com.moviebooking.services.ITicketService;

import java.util.ArrayList;
import java.util.List;

public class BookTicketCommand implements ICommand {
    private final ITicketService iTicketService;

    public BookTicketCommand(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }

    @Override
    public void execute(List<String> tokens) {
        String customerId = tokens.get(1);
        String showId = tokens.get(2);
        List<Seat> seatsList = new ArrayList<>();

        for (int i = 3; i < tokens.size(); i++) {
            String[] word = tokens.get(i).split("#");
            Seat seat = new Seat(tokens.get(i), Integer.parseInt(word[0]), Integer.parseInt(word[1]));
            seatsList.add(seat);
        }

        try {
            Ticket ticket = iTicketService.bookTicket(customerId, showId, seatsList);
            System.out.println("Ticket ID - " + ticket.getId());
            System.out.println();
        } catch (SeatNotAvailableException e) {
            System.out.println(e);
        }
    }
}
