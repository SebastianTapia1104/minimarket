package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Relation(collectionRelation = "ordenes-compra", itemRelation = "orden-compra")
@Schema(description = "Orden de compra a proveedor")
public class OrdenCompra {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_ENVIADA = "ENVIADA";
    public static final String ESTADO_RECIBIDA = "RECIBIDA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private String estado = ESTADO_PENDIENTE;

    @Column(nullable = false)
    private boolean generadaAutomaticamente;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenCompraDetalle> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isGeneradaAutomaticamente() {
        return generadaAutomaticamente;
    }

    public void setGeneradaAutomaticamente(boolean generadaAutomaticamente) {
        this.generadaAutomaticamente = generadaAutomaticamente;
    }

    public List<OrdenCompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenCompraDetalle> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(OrdenCompraDetalle detalle) {
        detalle.setOrdenCompra(this);
        this.detalles.add(detalle);
    }
}
