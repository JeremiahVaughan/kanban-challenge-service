package com.kanbanchallenge.kanbanchallengeservice.controller;

import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;
import com.kanbanchallenge.kanbanchallengeservice.service.TicketService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/api/kanban")
public class TicketController {

    private final TicketService ticketService;
    private final Validator createTicketValidator;

    public TicketController(TicketService ticketService,
                            @Qualifier("createTicketValidator") Validator createTicketValidator) {
        this.ticketService = ticketService;
        this.createTicketValidator = createTicketValidator;
    }

    /**
     * @return Returns all tickets to the client.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * @return We are returning all tickets each time their are modifications to make managing state between both
     * applications simpler.
     *
     * This controller has a validator since it is being passing a new ticket, we want to protect our persisted data
     * should the front end be mis-configured and send invalid values.
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    List<Ticket> getCreateNewTicket(@RequestBody Ticket ticket, BindingResult bindingResult) {
        createTicketValidator.validate(ticket, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
        }
        return ticketService.createNewTicket(ticket);
    }

    /**
     * @param tickets each ticket stores which swimming lane it lives in. We are passing the whole object for
     *                the sake of simplicity.
     * @return the returned boolean value will let the front end know whether the save was successful or not.
     */
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    boolean updatePositionOfTickets(@RequestBody Ticket[] tickets) {
        return ticketService.updatePositionOfTickets(tickets);
    }
}
