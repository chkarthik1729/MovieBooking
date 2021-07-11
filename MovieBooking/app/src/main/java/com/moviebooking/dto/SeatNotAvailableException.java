package com.moviebooking.dto;

public class SeatNotAvailableException extends Exception{

    @Override
    public String toString() {
        return "Some Seats are already Booked! Please try booking available seats!";
    }
}
