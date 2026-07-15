package com.minimarket.hateoas;

import com.minimarket.controller.SucursalController;
import com.minimarket.entity.Sucursal;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<Sucursal, EntityModel<Sucursal>> {

    @Override
    public EntityModel<Sucursal> toModel(Sucursal sucursal) {
        return EntityModel.of(sucursal,
                linkTo(methodOn(SucursalController.class).obtenerPorId(sucursal.getId())).withSelfRel(),
                linkTo(methodOn(SucursalController.class).listar()).withRel("sucursales"),
                linkTo(methodOn(com.minimarket.controller.StockSucursalController.class)
                        .listarPorSucursal(sucursal.getId())).withRel("stock")
        );
    }

    @Override
    public CollectionModel<EntityModel<Sucursal>> toCollectionModel(Iterable<? extends Sucursal> entities) {
        CollectionModel<EntityModel<Sucursal>> collectionModel =
                RepresentationModelAssembler.super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(SucursalController.class).listar()).withSelfRel());
        return collectionModel;
    }
}
