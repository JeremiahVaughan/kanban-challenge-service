package com.kanbanchallenge.kanbanchallengeservice.service;

import com.kanbanchallenge.kanbanchallengeservice.model.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class CreateTicketValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Ticket.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Ticket ticket = (Ticket) o;
        if (Objects.isNull(ticket.getDescription())) {
            errors.rejectValue("description", "empty");
        }
    }
}
