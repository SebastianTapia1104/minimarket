package com.minimarket.controller;

import com.minimarket.dto.RotacionProductoDto;
import com.minimarket.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Reportes de rotación de productos (más y menos vendidos)")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/rotacion")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    @Operation(summary = "Rotación completa de productos ordenada por unidades vendidas")
    public List<RotacionProductoDto> rotacion() {
        return reporteService.rotacionProductos();
    }

    @GetMapping("/mas-vendidos")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    @Operation(summary = "Productos más vendidos")
    public List<RotacionProductoDto> masVendidos(@RequestParam(defaultValue = "5") int limite) {
        return reporteService.masVendidos(limite);
    }

    @GetMapping("/menos-vendidos")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    @Operation(summary = "Productos menos vendidos")
    public List<RotacionProductoDto> menosVendidos(@RequestParam(defaultValue = "5") int limite) {
        return reporteService.menosVendidos(limite);
    }

    @GetMapping("/resumen-rotacion")
    @PreAuthorize("hasAnyRole('GERENTE','JEFE_TURNO')")
    @Operation(summary = "Resumen con más y menos vendidos")
    public Map<String, List<RotacionProductoDto>> resumen(@RequestParam(defaultValue = "5") int limite) {
        return Map.of(
                "masVendidos", reporteService.masVendidos(limite),
                "menosVendidos", reporteService.menosVendidos(limite)
        );
    }
}
