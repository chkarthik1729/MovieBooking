package com.moviebooking.repositories;

import com.moviebooking.entities.*;

import java.util.List;

public interface ITicketRepository {
    Ticket getTicketById(Integer id);
    Ticket saveTicket(Customer customer, Show show, List<Seat> seatList);
    void removeTicket(Integer id);
}
