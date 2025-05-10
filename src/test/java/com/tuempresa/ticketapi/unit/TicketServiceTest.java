package com.tuempresa.ticketapi.unit;

import com.tuempresa.ticketapi.model.Ticket;
import com.tuempresa.ticketapi.service.TicketService;
import com.tuempresa.ticketapi.service.TicketServiceImpl;
import com.tuempresa.ticketapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;

class TicketServiceTest {

    private final TicketRepository ticketRepository = mock(TicketRepository.class);
    private final TicketService ticketService = new TicketServiceImpl(ticketRepository);

    @Test
    void testCreateTicket() {
        // Arrange
        Ticket input = Ticket.builder()
                .title("Test Ticket")
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(input);

        // Act
        Ticket result = ticketService.createTicket(input);

        // Assert
        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
    @Test
    void testGetAllTickets() {
        // Arrange
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

        // Act
        List<Ticket> result = ticketService.getAllTickets();

        // Assert
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
    void testUpdateTicket() {
        // Arrange
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

        when(ticketRepository.findById(ticketId)).thenReturn(java.util.Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        // Act
        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
    @Test
    void testDeleteTicket() {
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
        ticketService.deleteTicket(ticketId);

        // Assert
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }
    
}