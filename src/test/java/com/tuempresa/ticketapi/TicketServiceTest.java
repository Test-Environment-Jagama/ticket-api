package com.tuempresa.ticketapi;

import com.tuempresa.ticketapi.model.Ticket;
import com.tuempresa.ticketapi.service.TicketService;
import com.tuempresa.ticketapi.service.TicketServiceImpl;
import com.tuempresa.ticketapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}