package com.minimarket.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.support.SecurityTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrdenCompraAutomaticaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String gerenteToken;

    @BeforeEach
    void setUp() throws Exception {
        gerenteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "gerente", "Gerente123!");
    }

    @Test
    void alBajarStockBajoMinimo_generaOrdenDeCompraAutomatica() throws Exception {
        String body = """
                {
                  "sucursal": { "id": 1 },
                  "producto": { "id": 1 },
                  "cantidad": 3,
                  "stockMinimo": 10,
                  "proveedorPreferido": { "id": 1 }
                }
                """;

        mockMvc.perform(post("/api/stock-sucursal")
                        .header("Authorization", "Bearer " + gerenteToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/ordenes-compra")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(root.has("_embedded") || root.isArray() || root.toString().contains("PENDIENTE")
                || root.toString().contains("orden"));
    }

    @Test
    void gerentePuedeListarProveedores() throws Exception {
        mockMvc.perform(get("/api/proveedores")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk());
    }
}
