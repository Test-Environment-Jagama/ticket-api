package com.tuempresa.ticketapi.unit;

import com.tuempresa.ticketapi.model.Ticket;
import com.tuempresa.ticketapi.repository.TicketRepository;
import com.tuempresa.ticketapi.service.TicketServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private EntityManager entityManager;
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        entityManager = mock(EntityManager.class);
        ticketService = new TicketServiceImpl(ticketRepository);
        // Inyecta el EntityManager simulado usando reflexión
        try {
            var field = TicketServiceImpl.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(ticketService, entityManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateTicket() {
        Ticket input = Ticket.builder()
                .title("Test Ticket")
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(input);

        Ticket result = ticketService.createTicket(input);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testGetAllTickets() {
        Ticket ticket1 = Ticket.builder()
                .id(1L)
                .title("Ticket 1")
                .description("Description 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2L)
                .title("Ticket 2")
                .description("Description 2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2));

        List<Ticket> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetTicketById() {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .title("Ticket 1")
                .description("Description 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(java.util.Optional.of(ticket));

        // Act
        Ticket result = ticketService.getTicketById(ticketId);

        // Assert
        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetTicketByIdNotFound() {
        Long ticketId = 999L;
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString(), eq(Ticket.class))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            ticketService.getTicketById(ticketId);
        });

        assertTrue(thrown.getMessage().contains("Ticket not found"));
    }

    @Test
    void testUpdateTicket() {
        Long ticketId = 1L;
        Ticket existingTicket = Ticket.builder()
                .id(ticketId)
                .title("Old Title")
                .description("Old Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket updatedTicket = Ticket.builder()
                .id(ticketId)
                .title("Updated Title")
                .description("Updated Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testDeleteTicket() {
        Long ticketId = 1L;
        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .title("Ticket 1")
                .description("Description 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }
}