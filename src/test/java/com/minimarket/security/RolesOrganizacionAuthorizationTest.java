package com.minimarket.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.support.SecurityTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RolesOrganizacionAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String cajeroToken;
    private String reponedorToken;
    private String jefeToken;
    private String asistenteToken;
    private String clienteToken;

    @BeforeEach
    void setUp() throws Exception {
        cajeroToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "cajero", "Cajero123!");
        reponedorToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "reponedor", "Reponedor123!");
        jefeToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "jefe", "Jefe123!");
        asistenteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "asistente", "Asistente123!");
        clienteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "cliente", "Cliente123!");
    }

    @Test
    void reponedorPuedeAccederAInventarioYNoAReportes() throws Exception {
        mockMvc.perform(get("/api/inventario")
                        .header("Authorization", "Bearer " + reponedorToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reportes/rotacion")
                        .header("Authorization", "Bearer " + reponedorToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void jefeTurnoPuedeConsultarReportes() throws Exception {
        mockMvc.perform(get("/api/reportes/resumen-rotacion")
                        .header("Authorization", "Bearer " + jefeToken))
                .andExpect(status().isOk());
    }

    @Test
    void cajeroPuedeConsultarPedidos() throws Exception {
        mockMvc.perform(get("/api/pedidos")
                        .header("Authorization", "Bearer " + cajeroToken))
                .andExpect(status().isOk());
    }

    @Test
    void asistentePuedeConsultarPedidosYNoInventario() throws Exception {
        mockMvc.perform(get("/api/pedidos")
                        .header("Authorization", "Bearer " + asistenteToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/inventario")
                        .header("Authorization", "Bearer " + asistenteToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void clienteNoPuedeRegistrarVentas() throws Exception {
        mockMvc.perform(post("/api/ventas")
                        .header("Authorization", "Bearer " + clienteToken)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }
}
