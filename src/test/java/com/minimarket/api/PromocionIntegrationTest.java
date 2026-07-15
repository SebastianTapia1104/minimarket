package com.minimarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.support.SecurityTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PromocionIntegrationTest {

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
    void clientePuedeConsultarPromocionesVigentes() throws Exception {
        mockMvc.perform(get("/api/promociones/vigentes")
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isOk());
    }

    @Test
    void gerentePuedeCrearPromocionCentralizada() throws Exception {
        long now = System.currentTimeMillis();
        String body = """
                {
                  "nombre": "Promo Abarrotes Test",
                  "descripcion": "10%% en arroz",
                  "porcentajeDescuento": 10.0,
                  "producto": { "id": 1 },
                  "fechaInicio": "%s",
                  "fechaFin": "%s",
                  "activa": true
                }
                """.formatted(
                new java.util.Date(now - 1000).toInstant().toString(),
                new java.util.Date(now + 86_400_000).toInstant().toString()
        );

        mockMvc.perform(post("/api/promociones")
                        .header("Authorization", "Bearer " + gerenteToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Promo Abarrotes Test"));
    }
}
