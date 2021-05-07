package com.kanbanchallenge.kanbanchallengeservice.repository;

import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;

import java.util.List;

/**
 * Being able to swap out our repository is especially handy in our case since we are currently saving to a file and
 * will likely need to promote our storage method should our app begin to scale.
 */
public interface TicketRepository {
    List<Ticket> findAllTickets();

    List<Ticket> saveTicket(Ticket ticket);

    boolean saveAllTickets(Ticket[] tickets);
}
