package com.minimarket.controller;

import com.minimarket.entity.Proveedor;
import com.minimarket.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "Gestión de proveedores para órdenes de compra")
@SecurityRequirement(name = "bearerAuth")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    @Operation(summary = "Listar proveedores")
    public CollectionModel<EntityModel<Proveedor>> listar() {
        var models = proveedorService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(models, linkTo(methodOn(ProveedorController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    public ResponseEntity<EntityModel<Proveedor>> obtener(@PathVariable Long id) {
        Proveedor proveedor = proveedorService.findById(id);
        return proveedor != null ? ResponseEntity.ok(toModel(proveedor)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Registrar proveedor")
    public EntityModel<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return toModel(proveedorService.save(proveedor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<EntityModel<Proveedor>> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        if (proveedorService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        proveedor.setId(id);
        return ResponseEntity.ok(toModel(proveedorService.save(proveedor)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (proveedorService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        proveedorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Proveedor> toModel(Proveedor proveedor) {
        return EntityModel.of(proveedor,
                linkTo(methodOn(ProveedorController.class).obtener(proveedor.getId())).withSelfRel(),
                linkTo(methodOn(ProveedorController.class).listar()).withRel("proveedores")
        );
    }
}
