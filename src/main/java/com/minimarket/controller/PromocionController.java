package com.minimarket.controller;

import com.minimarket.entity.Promocion;
import com.minimarket.service.PromocionService;
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
@RequestMapping("/api/promociones")
@Tag(name = "Promociones", description = "Gestión centralizada de ofertas y promociones")
@SecurityRequirement(name = "bearerAuth")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las promociones")
    public CollectionModel<EntityModel<Promocion>> listar() {
        var models = promocionService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(models, linkTo(methodOn(PromocionController.class).listar()).withSelfRel());
    }

    @GetMapping("/vigentes")
    @Operation(summary = "Listar promociones vigentes")
    public CollectionModel<EntityModel<Promocion>> vigentes() {
        var models = promocionService.findVigentes().stream().map(this::toModel).toList();
        return CollectionModel.of(models, linkTo(methodOn(PromocionController.class).vigentes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Promocion>> obtener(@PathVariable Long id) {
        Promocion promocion = promocionService.findById(id);
        return promocion != null ? ResponseEntity.ok(toModel(promocion)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    @Operation(summary = "Crear promoción")
    public EntityModel<Promocion> crear(@RequestBody Promocion promocion) {
        return toModel(promocionService.save(promocion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    public ResponseEntity<EntityModel<Promocion>> actualizar(@PathVariable Long id, @RequestBody Promocion promocion) {
        if (promocionService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        promocion.setId(id);
        return ResponseEntity.ok(toModel(promocionService.save(promocion)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (promocionService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        promocionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Promocion> toModel(Promocion promocion) {
        return EntityModel.of(promocion,
                linkTo(methodOn(PromocionController.class).obtener(promocion.getId())).withSelfRel(),
                linkTo(methodOn(PromocionController.class).listar()).withRel("promociones"),
                linkTo(methodOn(PromocionController.class).vigentes()).withRel("vigentes")
        );
    }
}
