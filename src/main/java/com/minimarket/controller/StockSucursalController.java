package com.minimarket.controller;

import com.minimarket.dto.DisponibilidadProductoDto;
import com.minimarket.entity.StockSucursal;
import com.minimarket.service.StockSucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/stock-sucursal")
@Tag(name = "Stock por sucursal", description = "Inventario centralizado multi-sucursal en tiempo real")
@SecurityRequirement(name = "bearerAuth")
public class StockSucursalController {

    private final StockSucursalService stockSucursalService;

    public StockSucursalController(StockSucursalService stockSucursalService) {
        this.stockSucursalService = stockSucursalService;
    }

    @Operation(summary = "Listar stock de todas las sucursales")
    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    public CollectionModel<EntityModel<StockSucursal>> listar() {
        List<EntityModel<StockSucursal>> models = stockSucursalService.findAll().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(models,
                linkTo(methodOn(StockSucursalController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Listar stock de una sucursal")
    @GetMapping("/sucursal/{sucursalId}")
    public CollectionModel<EntityModel<StockSucursal>> listarPorSucursal(@PathVariable Long sucursalId) {
        List<EntityModel<StockSucursal>> models = stockSucursalService.findBySucursalId(sucursalId).stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(models,
                linkTo(methodOn(StockSucursalController.class).listarPorSucursal(sucursalId)).withSelfRel());
    }

    @Operation(summary = "Consultar disponibilidad de un producto en todas las sucursales")
    @GetMapping("/disponibilidad/producto/{productoId}")
    public List<DisponibilidadProductoDto> disponibilidadProducto(@PathVariable Long productoId) {
        return stockSucursalService.consultarDisponibilidad(productoId);
    }

    @Operation(summary = "Consultar disponibilidad de un producto en una sucursal")
    @GetMapping("/disponibilidad/producto/{productoId}/sucursal/{sucursalId}")
    public DisponibilidadProductoDto disponibilidadEnSucursal(
            @PathVariable Long productoId,
            @PathVariable Long sucursalId
    ) {
        return stockSucursalService.consultarDisponibilidadEnSucursal(productoId, sucursalId);
    }

    @Operation(summary = "Registrar o actualizar stock de producto en sucursal")
    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO')")
    public EntityModel<StockSucursal> guardar(@RequestBody StockSucursal stock) {
        return toModel(stockSucursalService.save(stock));
    }

    @Operation(summary = "Obtener registro de stock por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO','REPONEDOR','EMPLEADO','CAJERO','ASISTENTE_CLIENTE')")
    public ResponseEntity<EntityModel<StockSucursal>> obtener(@PathVariable Long id) {
        StockSucursal stock = stockSucursalService.findById(id);
        return stock != null ? ResponseEntity.ok(toModel(stock)) : ResponseEntity.notFound().build();
    }

    private EntityModel<StockSucursal> toModel(StockSucursal stock) {
        return EntityModel.of(stock,
                linkTo(methodOn(StockSucursalController.class).obtener(stock.getId())).withSelfRel(),
                linkTo(methodOn(StockSucursalController.class).listar()).withRel("stocks"),
                linkTo(methodOn(StockSucursalController.class)
                        .listarPorSucursal(stock.getSucursal().getId())).withRel("sucursal-stock")
        );
    }
}
