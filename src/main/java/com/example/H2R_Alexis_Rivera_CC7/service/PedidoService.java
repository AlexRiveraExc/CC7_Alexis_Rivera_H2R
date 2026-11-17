package com.example.H2R_Alexis_Rivera_CC7.service;



import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.CrearPedidoDTO;
import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.PedidoResponseDTO;
import com.example.H2R_Alexis_Rivera_CC7.exception.ConflictoException;
import com.example.H2R_Alexis_Rivera_CC7.exception.OperacionInvalidaException;
import com.example.H2R_Alexis_Rivera_CC7.exception.RecursoNoEncontradoException;
import com.example.H2R_Alexis_Rivera_CC7.model.EstadoPedido;
import com.example.H2R_Alexis_Rivera_CC7.model.Pedido;
import com.example.H2R_Alexis_Rivera_CC7.model.Producto;
import com.example.H2R_Alexis_Rivera_CC7.repository.PedidoRepository;
import com.example.H2R_Alexis_Rivera_CC7.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public PedidoResponseDTO crearPedido(CrearPedidoDTO payload) {
        Producto producto = buscarProducto(payload.getIdProducto());

        if (!producto.isActivo()) {
            throw new OperacionInvalidaException("El producto no esta activo");
        }

        if (producto.getStock() < payload.getCantidad()) {
            throw new ConflictoException("No hay stock suficiente. Stock disponible: " + producto.getStock());
        }

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(payload.getNombreCliente());
        pedido.setCantidad(payload.getCantidad());
        pedido.setPrecioUnitario(producto.getPrecio());
        pedido.setTotal(producto.getPrecio() * payload.getCantidad());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setProducto(producto);

        producto.setStock(producto.getStock() - payload.getCantidad());
        productoRepository.save(producto);

        return PedidoResponseDTO.desde(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ConflictoException("Solo se pueden cancelar pedidos PENDIENTE");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        devolverStock(pedido.getProducto(), pedido.getCantidad());

        return PedidoResponseDTO.desde(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO procesarPedido(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ConflictoException("Solo se pueden procesar pedidos PENDIENTE");
        }

        pedido.setEstado(EstadoPedido.PROCESADO);
        return PedidoResponseDTO.desde(pedidoRepository.save(pedido));
    }

    @Transactional
    public List<PedidoResponseDTO> cancelarPendientesDeProducto(Long idProducto) {
        Producto producto = buscarProducto(idProducto);
        List<Pedido> pendientes = pedidoRepository.findAllByProductoIdAndEstado(idProducto, EstadoPedido.PENDIENTE);

        for (Pedido pedido : pendientes) {
            pedido.setEstado(EstadoPedido.CANCELADO);
            producto.setStock(producto.getStock() + pedido.getCantidad());
        }

        productoRepository.save(producto);
        return pedidoRepository.saveAll(pendientes).stream()
                .map(PedidoResponseDTO::desde)
                .toList();
    }

    public PedidoResponseDTO consultarDetalle(Long id) {
        return PedidoResponseDTO.desde(buscarPedido(id));
    }

    public List<PedidoResponseDTO> listarPorEstado(String estado) {
        EstadoPedido estadoPedido;
        try {
            estadoPedido = EstadoPedido.valueOf(estado);
        } catch (IllegalArgumentException ex) {
            throw new OperacionInvalidaException("Estado no valido: " + estado + ". Use PENDIENTE, PROCESADO o CANCELADO");
        }

        return pedidoRepository.findAllByEstado(estadoPedido).stream()
                .map(PedidoResponseDTO::desde)
                .toList();
    }

    private void devolverStock(Producto producto, Integer cantidad) {
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido con id " + id + " no existe"));
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con id " + id + " no existe"));
    }
}
