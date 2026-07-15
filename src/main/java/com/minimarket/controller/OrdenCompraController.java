package com.minimarket.controller;

import com.minimarket.entity.OrdenCompra;
import com.minimarket.service.OrdenCompraService;
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
@RequestMapping("/api/ordenes-compra")
@Tag(name = "Órdenes de compra", description = "Órdenes a proveedores, incluyendo generación automática por stock mínimo")
@SecurityRequirement(name = "bearerAuth")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    @Operation(summary = "Listar órdenes de compra")
    public CollectionModel<EntityModel<OrdenCompra>> listar() {
        var models = ordenCompraService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(models, linkTo(methodOn(OrdenCompraController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    public ResponseEntity<EntityModel<OrdenCompra>> obtener(@PathVariable Long id) {
        OrdenCompra orden = ordenCompraService.findById(id);
        return orden != null ? ResponseEntity.ok(toModel(orden)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR')")
    @Operation(summary = "Crear orden de compra manual")
    public EntityModel<OrdenCompra> crear(@RequestBody OrdenCompra orden) {
        return toModel(ordenCompraService.crearManual(orden));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR')")
    @Operation(summary = "Actualizar estado de la orden")
    public EntityModel<OrdenCompra> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return toModel(ordenCompraService.actualizarEstado(id, body.get("estado")));
    }

    @PostMapping("/{id}/recibir")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    @Operation(summary = "Marcar orden como recibida e incrementar stock de sucursal")
    public EntityModel<OrdenCompra> recibir(@PathVariable Long id) {
        return toModel(ordenCompraService.marcarRecibida(id));
    }

    private EntityModel<OrdenCompra> toModel(OrdenCompra orden) {
        return EntityModel.of(orden,
                linkTo(methodOn(OrdenCompraController.class).obtener(orden.getId())).withSelfRel(),
                linkTo(methodOn(OrdenCompraController.class).listar()).withRel("ordenes-compra"),
                linkTo(methodOn(OrdenCompraController.class).recibir(orden.getId())).withRel("recibir")
        );
    }
}
