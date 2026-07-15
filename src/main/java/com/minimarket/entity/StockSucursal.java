package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sucursal_id", "producto_id"}))
@Relation(collectionRelation = "stocks", itemRelation = "stock")
@Schema(description = "Stock de un producto en una sucursal específica")
public class StockSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    @Schema(description = "Cantidad disponible en la sucursal", example = "40")
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Nivel mínimo para reposición automática", example = "10")
    private Integer stockMinimo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @Schema(description = "Proveedor preferido para reposición")
    private Proveedor proveedorPreferido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Proveedor getProveedorPreferido() {
        return proveedorPreferido;
    }

    public void setProveedorPreferido(Proveedor proveedorPreferido) {
        this.proveedorPreferido = proveedorPreferido;
    }

    public boolean estaBajoMinimo() {
        return cantidad != null && stockMinimo != null && cantidad <= stockMinimo;
    }

    public void validar() {
        if (sucursal == null || sucursal.getId() == null) {
            throw new IllegalArgumentException("La sucursal es obligatoria");
        }
        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (stockMinimo == null || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
    }
}
