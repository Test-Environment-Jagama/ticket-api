package com.tuempresa.ticketapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuempresa.ticketapi.model.Ticket;
import com.tuempresa.ticketapi.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void testCreateTicket() throws Exception {
        // Arrange
        Ticket ticket = Ticket.builder()
                .title("Integration Test Ticket")
                .description("Integration Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Ticket"))
                .andExpect(jsonPath("$.description").value("Integration Test Description"));
    }

    @Test
    void testGetAllTickets() throws Exception {
        // Arrange
        Ticket ticket1 = Ticket.builder()
                .title("Ticket 1")
                .description("Description 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket ticket2 = Ticket.builder()
                .title("Ticket 2")
                .description("Description 2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        // Act & Assert
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Ticket 1"))
                .andExpect(jsonPath("$[1].title").value("Ticket 2"));
    }

    @Test
    void testDeleteTicket() throws Exception {
        // Arrange
        Ticket ticket = Ticket.builder()
                .title("Ticket to Delete")
                .description("Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        // Act & Assert
        mockMvc.perform(delete("/api/tickets/{id}", savedTicket.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateTicket() throws Exception {
        // Arrange
        Ticket ticket = Ticket.builder()
                .title("Ticket to Update")
                .description("Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        Ticket updatedTicket = Ticket.builder()
                .title("Updated Title")
                .description("Updated Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/tickets/{id}", savedTicket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }
    @Test
    void testGetTicketById() throws Exception {
        // Arrange
        Ticket ticket = Ticket.builder()
                .title("Ticket to Get")
                .description("Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        // Act & Assert
        mockMvc.perform(get("/api/tickets/{id}", savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ticket to Get"))
                .andExpect(jsonPath("$.description").value("Description"));
    }
    @Test
    void testGetTicketByIdNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/tickets/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
