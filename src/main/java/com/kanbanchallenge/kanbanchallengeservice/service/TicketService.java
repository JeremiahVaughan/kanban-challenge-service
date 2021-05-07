package com.kanbanchallenge.kanbanchallengeservice.service;

import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;

import java.util.List;

/**
 * The use of interfaces promotes dependency inversion as we can now swap out this service for another should the need
 * arise.
 */
public interface TicketService {
    List<Ticket> getAllTickets();

    List<Ticket> createNewTicket(Ticket ticket);

    boolean updatePositionOfTickets(Ticket[] tickets);
}
