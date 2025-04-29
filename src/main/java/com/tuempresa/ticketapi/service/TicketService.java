package com.tuempresa.ticketapi.service;

import com.tuempresa.ticketapi.model.Ticket;
import java.util.List;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    Ticket updateTicket(Long id, Ticket ticket);
    void deleteTicket(Long id);
}