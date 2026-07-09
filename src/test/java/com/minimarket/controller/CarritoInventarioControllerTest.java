package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Usuario;
import com.minimarket.hateoas.CarritoModelAssembler;
import com.minimarket.hateoas.InventarioModelAssembler;
import com.minimarket.hateoas.UsuarioModelAssembler;
import com.minimarket.service.CarritoService;
import com.minimarket.service.InventarioService;
import com.minimarket.service.UsuarioService;
import com.minimarket.support.HateoasTestSupport;
import com.minimarket.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @Mock
    private CarritoModelAssembler carritoAssembler;

    @InjectMocks
    private CarritoController carritoController;

    @BeforeEach
    void setUp() {
        when(carritoAssembler.toModel(any(Carrito.class))).thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0)));
        when(carritoAssembler.toCollectionModel(any())).thenAnswer(invocation ->
                HateoasTestSupport.toCollectionModel((Iterable<Carrito>) invocation.getArgument(0)));
    }

    @Test
    void operacionesCarrito() {
        Carrito carrito = new Carrito();
        when(carritoService.findAll()).thenReturn(List.of(carrito));
        when(carritoService.findById(1L)).thenReturn(carrito);
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito);

        assertEquals(1, carritoController.listarCarrito().getContent().size());
        assertEquals(HttpStatus.OK, carritoController.obtenerCarritoPorId(1L).getStatusCode());
        assertEquals(carrito, carritoController.agregarProductoAlCarrito(carrito).getContent());
        assertEquals(HttpStatus.OK, carritoController.actualizarCarrito(1L, carrito).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, carritoController.eliminarProductoDelCarrito(1L).getStatusCode());
        verify(carritoService).deleteById(1L);
    }

    @Test
    void obtenerCarrito_inexistente() {
        when(carritoService.findById(99L)).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, carritoController.obtenerCarritoPorId(99L).getStatusCode());
    }
}

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @Mock
    private InventarioModelAssembler inventarioAssembler;

    @InjectMocks
    private InventarioController inventarioController;

    @BeforeEach
    void setUp() {
        when(inventarioAssembler.toModel(any(Inventario.class))).thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0)));
        when(inventarioAssembler.toCollectionModel(any())).thenAnswer(invocation ->
                HateoasTestSupport.toCollectionModel((Iterable<Inventario>) invocation.getArgument(0)));
    }

    @Test
    void operacionesInventario() {
        Inventario inventario = new Inventario();
        when(inventarioService.findAll()).thenReturn(List.of(inventario));
        when(inventarioService.findById(1L)).thenReturn(inventario);
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        assertEquals(1, inventarioController.listarMovimientosDeInventario().getContent().size());
        assertEquals(HttpStatus.OK, inventarioController.obtenerMovimientoPorId(1L).getStatusCode());
        assertEquals(inventario, inventarioController.registrarMovimiento(inventario).getContent());
        assertEquals(HttpStatus.OK, inventarioController.actualizarMovimiento(1L, inventario).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, inventarioController.eliminarMovimiento(1L).getStatusCode());
    }
}

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioModelAssembler usuarioAssembler;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        when(usuarioAssembler.toModel(any(Usuario.class))).thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0)));
        when(usuarioAssembler.toCollectionModel(any())).thenAnswer(invocation ->
                HateoasTestSupport.toCollectionModel((Iterable<Usuario>) invocation.getArgument(0)));
    }

    @Test
    void listarUsuarios_retornaLista() {
        when(usuarioService.findAll()).thenReturn(List.of(TestDataFactory.usuarioCompleto("cliente", "CLIENTE")));
        assertEquals(1, usuarioController.listarUsuarios().getContent().size());
    }

    @Test
    void obtenerUsuarioPorId_existente() {
        when(usuarioService.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.usuarioCompleto("cliente", "CLIENTE")));
        assertEquals(HttpStatus.OK, usuarioController.obtenerUsuarioPorId(1L).getStatusCode());
    }

    @Test
    void obtenerUsuarioPorId_inexistente() {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, usuarioController.obtenerUsuarioPorId(99L).getStatusCode());
    }

    @Test
    void guardarUsuario_delegaEnServicio() {
        Usuario usuario = TestDataFactory.usuarioCompleto("nuevo", "CLIENTE");
        when(usuarioService.save(usuario)).thenReturn(usuario);
        assertEquals(usuario, usuarioController.guardarUsuario(usuario).getContent());
    }

    @Test
    void actualizarUsuario_existente() {
        Usuario usuario = TestDataFactory.usuarioCompleto("nuevo", "CLIENTE");
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        assertEquals(HttpStatus.OK, usuarioController.actualizarUsuario(1L, usuario).getStatusCode());
    }

    @Test
    void eliminarUsuario_existente() {
        when(usuarioService.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.usuarioCompleto("cliente", "CLIENTE")));
        assertEquals(HttpStatus.NO_CONTENT, usuarioController.eliminarUsuario(1L).getStatusCode());
        verify(usuarioService).deleteById(1L);
    }
}
