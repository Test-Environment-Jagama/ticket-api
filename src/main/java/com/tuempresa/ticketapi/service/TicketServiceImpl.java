package com.tuempresa.ticketapi.service;

import com.tuempresa.ticketapi.model.Ticket;
import com.tuempresa.ticketapi.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket ticket = getTicketById(id);
        ticket.setTitle(updatedTicket.getTitle());
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
        ticketRepository.deleteById(id);
    }

    /**
     * Método vulnerable solo para pruebas de análisis estático.
     * No debe usarse en producción.
     */
    public void vulnerableSql(String userInput) {
        // Vulnerabilidad: concatenación directa en consulta SQL
        String sql = "SELECT * FROM ticket WHERE description = '" + userInput + "'";
        entityManager.createNativeQuery(sql);
    }
}