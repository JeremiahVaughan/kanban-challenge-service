package com.kanbanchallenge.kanbanchallengeservice.service;

import com.kanbanchallenge.kanbanchallengeservice.model.SwimmingLane;
import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;
import com.kanbanchallenge.kanbanchallengeservice.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    public DefaultTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllTickets();
    }

    /**
     * @param ticket new ticket to be created that was passed from the client. We are
     *               setting the date and initial swimming lane here as the user does not
     *               decide this.
     */
    @Override
    public List<Ticket> createNewTicket(Ticket ticket) {
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setAssignedSwimmingLane(SwimmingLane.BACKLOG);
        return ticketRepository.saveTicket(ticket);
    }

    @Override
    public boolean updatePositionOfTickets(Ticket[] tickets) {
        return ticketRepository.saveAllTickets(tickets);
    }
}
