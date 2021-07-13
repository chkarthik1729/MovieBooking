package com.moviebooking.entities;

import java.util.Objects;

public class Seat {

    private final String id;
    private final int seatRow;
    private final int seatColumn;

    public Seat(String id, int seatRow, int seatColumn) {
        this.id = id;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }

    public String getId() {
        return id;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public int getSeatColumn() {
        return seatColumn;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Seat)) return false;
        Seat other = (Seat) obj;
        return this.id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Seat: { " +
                    "id = " + getId() + ", " +
                    "seatRow = " + getSeatRow() + ", " +
                    "seatColumn = " + getSeatColumn() +
                " }";
    }
}
