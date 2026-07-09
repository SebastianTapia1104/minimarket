package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.hateoas.InventarioModelAssembler;
import com.minimarket.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Registro y consulta de movimientos de stock con enlaces HATEOAS")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler inventarioAssembler;

    public InventarioController(InventarioService inventarioService, InventarioModelAssembler inventarioAssembler) {
        this.inventarioService = inventarioService;
        this.inventarioAssembler = inventarioAssembler;
    }

    @Operation(
            summary = "Listar movimientos de inventario",
            description = "Obtiene todos los movimientos de stock con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos con enlaces _links"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {
        return inventarioAssembler.toCollectionModel(inventarioService.findAll());
    }

    @Operation(summary = "Obtener movimiento por ID", description = "Retorna un movimiento con enlaces self, inventario y producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(
            @Parameter(description = "Identificador del movimiento", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null)
                ? ResponseEntity.ok(inventarioAssembler.toModel(inventario))
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar movimiento de inventario", description = "Registra una entrada o salida de stock para un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento registrado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    @PostMapping
    public EntityModel<Inventario> registrarMovimiento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del movimiento de inventario",
                    required = true,
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/InventarioRequest"),
                            examples = @ExampleObject(
                                    name = "Entrada de stock",
                                    value = """
                                            {
                                              "producto": { "id": 1 },
                                              "cantidad": 10,
                                              "tipoMovimiento": "Entrada",
                                              "fechaMovimiento": "2026-07-08T18:00:00.000+00:00"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Inventario inventario) {
        return inventarioAssembler.toModel(inventarioService.save(inventario));
    }

    @Operation(summary = "Actualizar movimiento", description = "Modifica un movimiento de inventario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado con enlaces HATEOAS"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(
            @Parameter(description = "Identificador del movimiento", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioAssembler.toModel(inventarioService.save(inventario)));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento de inventario por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(description = "Identificador del movimiento", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
