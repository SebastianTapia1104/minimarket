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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoOnlineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String clienteToken;
    private String gerenteToken;
    private long clienteId;

    @BeforeEach
    void setUp() throws Exception {
        clienteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "cliente", "Cliente123!");
        gerenteToken = SecurityTestSupport.loginAndGetToken(mockMvc, objectMapper, "gerente", "Gerente123!");
        clienteId = resolveUsuarioId("cliente");
    }

    @Test
    void clientePuedeCrearPedidoRetiroEnTienda() throws Exception {
        String body = """
                {
                  "cliente": { "id": %d },
                  "sucursal": { "id": 2 },
                  "tipoEntrega": "RETIRO",
                  "detalles": [
                    { "producto": { "id": 2 }, "cantidad": 1 }
                  ]
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/api/pedidos")
                        .header("Authorization", "Bearer " + clienteToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoEntrega").value("RETIRO"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void clientePuedeCrearPedidoDespachoConDireccion() throws Exception {
        String body = """
                {
                  "cliente": { "id": %d },
                  "sucursal": { "id": 3 },
                  "tipoEntrega": "DESPACHO",
                  "direccionDespacho": "Av. Apoquindo 4500, Las Condes",
                  "detalles": [
                    { "producto": { "id": 3 }, "cantidad": 1 }
                  ]
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/api/pedidos")
                        .header("Authorization", "Bearer " + clienteToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoEntrega").value("DESPACHO"))
                .andExpect(jsonPath("$.direccionDespacho").value("Av. Apoquindo 4500, Las Condes"));
    }

    @Test
    void clientePuedeListarSusPedidos() throws Exception {
        mockMvc.perform(get("/api/pedidos/cliente/" + clienteId)
                        .header("Authorization", "Bearer " + clienteToken))
                .andExpect(status().isOk());
    }

    private long resolveUsuarioId(String username) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + gerenteToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode usuarios = root.path("_embedded").path("usuarios");
        if (!usuarios.isArray()) {
            usuarios = root;
        }
        for (JsonNode usuario : usuarios) {
            if (username.equalsIgnoreCase(usuario.path("username").asText())) {
                return usuario.path("id").asLong();
            }
        }
        throw new IllegalStateException("No se encontró el usuario: " + username);
    }
}
