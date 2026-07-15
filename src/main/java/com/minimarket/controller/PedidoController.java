package com.minimarket.controller;

import com.minimarket.entity.Pedido;
import com.minimarket.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos en línea", description = "Pedidos para retiro en tienda o despacho a domicilio")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','CAJERO','ASISTENTE_CLIENTE','EMPLEADO')")
    @Operation(summary = "Listar pedidos")
    public CollectionModel<EntityModel<Pedido>> listar() {
        var models = pedidoService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(models, linkTo(methodOn(PedidoController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','CAJERO','ASISTENTE_CLIENTE','CLIENTE','EMPLEADO')")
    public ResponseEntity<EntityModel<Pedido>> obtener(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        return pedido != null ? ResponseEntity.ok(toModel(pedido)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','CAJERO','ASISTENTE_CLIENTE','CLIENTE','EMPLEADO')")
    public CollectionModel<EntityModel<Pedido>> porCliente(@PathVariable Long clienteId) {
        var models = pedidoService.findByClienteId(clienteId).stream().map(this::toModel).toList();
        return CollectionModel.of(models,
                linkTo(methodOn(PedidoController.class).porCliente(clienteId)).withSelfRel());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','CAJERO','ASISTENTE_CLIENTE','GERENTE')")
    @Operation(summary = "Crear pedido en línea (RETIRO o DESPACHO)")
    public EntityModel<Pedido> crear(@RequestBody Pedido pedido) {
        return toModel(pedidoService.crearPedido(pedido));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','CAJERO','ASISTENTE_CLIENTE','EMPLEADO')")
    @Operation(summary = "Actualizar estado del pedido")
    public EntityModel<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return toModel(pedidoService.actualizarEstado(id, body.get("estado")));
    }

    private EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtener(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listar()).withRel("pedidos")
        );
    }
}
