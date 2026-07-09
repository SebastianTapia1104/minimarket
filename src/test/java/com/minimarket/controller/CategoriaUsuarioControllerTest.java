package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void listarCategorias_retornaLista() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Lacteos");
        when(categoriaService.findAll()).thenReturn(List.of(categoria));
        assertEquals(1, categoriaController.listarCategorias().size());
    }

    @Test
    void obtenerCategoriaPorId_existente() {
        when(categoriaService.findById(1L)).thenReturn(new Categoria());
        assertEquals(HttpStatus.OK, categoriaController.obtenerCategoriaPorId(1L).getStatusCode());
    }

    @Test
    void obtenerCategoriaPorId_inexistente() {
        when(categoriaService.findById(99L)).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, categoriaController.obtenerCategoriaPorId(99L).getStatusCode());
    }

    @Test
    void guardarCategoria_delegaEnServicio() {
        Categoria categoria = new Categoria();
        when(categoriaService.save(categoria)).thenReturn(categoria);
        assertEquals(categoria, categoriaController.guardarCategoria(categoria));
    }

    @Test
    void actualizarCategoria_existente() {
        Categoria categoria = new Categoria();
        when(categoriaService.findById(1L)).thenReturn(categoria);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        assertEquals(HttpStatus.OK, categoriaController.actualizarCategoria(1L, categoria).getStatusCode());
    }

    @Test
    void eliminarCategoria_existente() {
        when(categoriaService.findById(1L)).thenReturn(new Categoria());
        assertEquals(HttpStatus.NO_CONTENT, categoriaController.eliminarCategoria(1L).getStatusCode());
        verify(categoriaService).deleteById(1L);
    }
}
