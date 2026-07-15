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
class SucursalDisponibilidadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String clienteToken;

    @BeforeEach
    void setUp() throws Exception {
        clienteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "cliente", "Cliente123!");
    }

    @Test
    void clientePuedeListarSucursalesConHateoas() throws Exception {
        mockMvc.perform(get("/api/sucursales")
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.sucursales").isArray());
    }

    @Test
    void clientePuedeConsultarDisponibilidadPorProducto() throws Exception {
        mockMvc.perform(get("/api/stock-sucursal/disponibilidad/producto/1")
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isOk());
    }

    @Test
    void clientePuedeConsultarDisponibilidadPorProductoYSucursal() throws Exception {
        mockMvc.perform(get("/api/stock-sucursal/disponibilidad/producto/1/sucursal/1")
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(1))
                .andExpect(jsonPath("$.sucursalId").value(1));
    }
}
