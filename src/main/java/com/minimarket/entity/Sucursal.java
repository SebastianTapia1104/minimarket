package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Relation(collectionRelation = "sucursales", itemRelation = "sucursal")
@Schema(description = "Sucursal de MiniMarket Plus")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la sucursal", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Providencia")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Dirección", example = "Av. Providencia 1234")
    private String direccion;

    @Column(nullable = false)
    @Schema(description = "Comuna", example = "Providencia")
    private String comuna;

    @Column(nullable = false)
    @Schema(description = "Región", example = "Metropolitana")
    private String region;

    @Column(nullable = false)
    @Schema(description = "Indica si la sucursal está operativa")
    private boolean activa = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
