package com.bytes7.GameHub.exception;

import com.bytes7.GameHub.config.SecurityConfig;
import com.bytes7.GameHub.controller.TournamentController;
import com.bytes7.GameHub.security.JwtUtil;
import com.bytes7.GameHub.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TournamentController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "PLAYER")
    void shouldReturnNotFoundWhenTournamentDoesNotExist() throws Exception {

        UUID id = UUID.randomUUID();

        Mockito.when(tournamentService.getTournamentById(id))
               .thenThrow(new NoSuchElementException("Torneo no encontrado"));

        mockMvc.perform(get("/api/tournaments/" + id))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.status").value(404))
               .andExpect(jsonPath("$.message").value("Torneo no encontrado"))
               .andExpect(jsonPath("$.timestamp").exists());
    }
}
