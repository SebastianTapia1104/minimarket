package com.minimarket.hateoas;

import com.minimarket.controller.CarritoController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.entity.Usuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito")
        );
    }

    @Override
    public CollectionModel<EntityModel<Usuario>> toCollectionModel(Iterable<? extends Usuario> entities) {
        CollectionModel<EntityModel<Usuario>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());
        return collectionModel;
    }
}
