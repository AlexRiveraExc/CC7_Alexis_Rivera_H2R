package com.example.H2R_Alexis_Rivera_CC7.service;


import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.ProductoResponseDTO;
import com.example.H2R_Alexis_Rivera_CC7.exception.ConflictoException;
import com.example.H2R_Alexis_Rivera_CC7.exception.RecursoNoEncontradoException;
import com.example.H2R_Alexis_Rivera_CC7.model.EstadoPedido;
import com.example.H2R_Alexis_Rivera_CC7.model.Producto;
import com.example.H2R_Alexis_Rivera_CC7.repository.PedidoRepository;
import com.example.H2R_Alexis_Rivera_CC7.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public ProductoService(ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ProductoResponseDTO desactivarProducto(Long id) {
        Producto producto = buscarProducto(id);

        if (pedidoRepository.existsByProductoIdAndEstado(id, EstadoPedido.PENDIENTE)) {
            throw new ConflictoException("No se puede desactivar el producto porque tiene pedidos PENDIENTE");
        }

        producto.setActivo(false);
        return ProductoResponseDTO.desde(productoRepository.save(producto));
    }

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(ProductoResponseDTO::desde)
                .toList();
    }

    public List<ProductoResponseDTO> listarActivos() {
        return productoRepository.findAllByActivoTrue().stream()
                .map(ProductoResponseDTO::desde)
                .toList();
    }

    public List<ProductoResponseDTO> listarPorCategoria(String categoria) {
        return productoRepository.findAllByCategoriaIgnoreCase(categoria).stream()
                .map(ProductoResponseDTO::desde)
                .toList();
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con id " + id + " no existe"));
    }
}