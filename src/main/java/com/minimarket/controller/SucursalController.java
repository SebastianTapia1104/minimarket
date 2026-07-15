package com.minimarket.controller;

import com.minimarket.entity.Sucursal;
import com.minimarket.hateoas.SucursalModelAssembler;
import com.minimarket.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sucursales")
@Tag(name = "Sucursales", description = "Gestión de sucursales MiniMarket Plus (microservicio de catálogo/operaciones)")
@SecurityRequirement(name = "bearerAuth")
public class SucursalController {

    private final SucursalService sucursalService;
    private final SucursalModelAssembler assembler;

    public SucursalController(SucursalService sucursalService, SucursalModelAssembler assembler) {
        this.sucursalService = sucursalService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar sucursales")
    @GetMapping
    public CollectionModel<EntityModel<Sucursal>> listar() {
        return assembler.toCollectionModel(sucursalService.findAll());
    }

    @Operation(summary = "Obtener sucursal por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Sucursal>> obtenerPorId(@PathVariable Long id) {
        Sucursal sucursal = sucursalService.findById(id);
        return sucursal != null
                ? ResponseEntity.ok(assembler.toModel(sucursal))
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear sucursal")
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public EntityModel<Sucursal> crear(@RequestBody Sucursal sucursal) {
        return assembler.toModel(sucursalService.save(sucursal));
    }

    @Operation(summary = "Actualizar sucursal")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<EntityModel<Sucursal>> actualizar(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        if (sucursalService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        sucursal.setId(id);
        return ResponseEntity.ok(assembler.toModel(sucursalService.save(sucursal)));
    }

    @Operation(summary = "Eliminar sucursal")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (sucursalService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        sucursalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
