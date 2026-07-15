package com.minimarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.support.SecurityTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReportePrivacidadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String gerenteToken;
    private String clienteToken;

    @BeforeEach
    void setUp() throws Exception {
        gerenteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "gerente", "Gerente123!");
        clienteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "cliente", "Cliente123!");
    }

    @Test
    void gerentePuedeConsultarRotacionDeProductos() throws Exception {
        mockMvc.perform(get("/api/reportes/rotacion")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk());
    }

    @Test
    void gerentePuedeConsultarMasYMenosVendidos() throws Exception {
        mockMvc.perform(get("/api/reportes/mas-vendidos?limite=3")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reportes/menos-vendidos?limite=3")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk());
    }

    @Test
    void clienteNoPuedeConsultarReportes() throws Exception {
        mockMvc.perform(get("/api/reportes/rotacion")
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void politicaDePrivacidadEsPublica() throws Exception {
        mockMvc.perform(get("/api/privacidad/politica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.normativa").isArray())
                .andExpect(jsonPath("$.derechos").isArray());
    }

    @Test
    void mapaDeMicroserviciosEsPublico() throws Exception {
        mockMvc.perform(get("/api/privacidad/microservicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].basePath").exists());
    }
}
